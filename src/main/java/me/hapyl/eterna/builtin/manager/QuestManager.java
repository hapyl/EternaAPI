package me.hapyl.eterna.builtin.manager;

import com.google.common.collect.Maps;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.event.PlayerClickAtNpcEvent;
import me.hapyl.eterna.module.event.PlayerMoveOneBlockEvent;
import me.hapyl.eterna.module.locaiton.Position;
import me.hapyl.eterna.module.player.dialog.Dialog;
import me.hapyl.eterna.module.player.quest.*;
import me.hapyl.eterna.module.player.quest.objective.*;
import me.hapyl.eterna.module.reflect.npc.HumanNPC;
import me.hapyl.eterna.module.util.Consumers;
import me.hapyl.eterna.module.util.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class QuestManager extends EternaManager<Player, QuestDataList> implements Listener {

    private final Map<JavaPlugin, QuestHandler> pluginHandlers;

    QuestManager(@Nonnull EternaPlugin eterna) {
        super(eterna);

        this.pluginHandlers = Maps.newHashMap();
    }

    /**
     * Simulates the actions performed as the player would have joined the server.
     * <p>Needed in case of reload.</p>
     *
     * @param player - The player who has joined.
     * @deprecated <h2>It's a bad practice to reload servers, only use this for testing/development.</h2>
     */
    @Deprecated
    public void simulateOnJoin(@Nonnull Player player) {
        // Already has data, don't care
        if (managing.containsKey(player)) {
            return;
        }

        workHandlers(handler -> {
            // Load quests
            final Set<QuestData> questDataSet = handler.loadQuests(player);
            final QuestDataList questData = get(player);

            for (QuestData data : questDataSet) {
                questData.data.put(data.getQuest(), data);
            }

            // Start onJoin quests
            handler.getQuests().forEach(quest -> {
                for (QuestStartBehaviour behaviour : quest.getStartBehaviours(player)) {
                    if (!(behaviour instanceof QuestStartBehaviour.OnJoin onJoin)) {
                        continue;
                    }

                    startQuest(player, quest, false, onJoin.sendNotification());
                }
            });
        });
    }

    // Being monitor is very important, since we want to allow others to create
    // all the data necessary before loading quests
    @EventHandler(priority = EventPriority.MONITOR)
    public void handlePlayerJoinEvent(PlayerJoinEvent ev) {
        simulateOnJoin(ev.getPlayer());
    }

    // It is very important that this even has the lowest priority to allow
    // handlers to save the data BEFORE they removed any database/other storing instances
    @EventHandler(priority = EventPriority.LOWEST)
    public void handlePlayerQuitEvent(PlayerQuitEvent ev) {
        final Player player = ev.getPlayer();
        final QuestDataList questData = get(player);

        workHandlers(handler -> {
            final Set<QuestData> questDataSet = new HashSet<>();

            questData.data.values().removeIf(data -> {
                if (data.getQuest().getPlugin() == handler.getPlugin()) {
                    questDataSet.add(data);
                    return true;
                }

                return false;
            });

            handler.saveQuests(player, questDataSet);
        });
    }

    @EventHandler(ignoreCancelled = true)
    @ObjectiveHandler({ BreakBlockQuestObjective.class })
    public void handleBlockBreakEvent(BlockBreakEvent ev) {
        final Material material = ev.getBlock().getType();

        tryIncrementObjective(ev.getPlayer(), BreakBlockQuestObjective.class, material);
    }

    @EventHandler(ignoreCancelled = true)
    @ObjectiveHandler({ BreakItemQuestObjective.class })
    public void handlePlayerItemBreakEvent(PlayerItemBreakEvent ev) {
        tryIncrementObjective(ev.getPlayer(), BreakItemQuestObjective.class, ev.getBrokenItem().getType());
    }

    @EventHandler(ignoreCancelled = true)
    @ObjectiveHandler({ BreedAnimalsQuestObjective.class })
    public void handleAnimalBreedEvent(EntityBreedEvent ev) {
        final LivingEntity breeder = ev.getBreeder();

        if (!(breeder instanceof Player player)) {
            return;
        }

        tryIncrementObjective(player, BreedAnimalsQuestObjective.class, ev.getMother().getType());
    }

    // https://github.com/ezeiger92/QuestWorld2/blob/master/src/main/java/com/questworld/extension/builtin/CraftMission.java
    @EventHandler(ignoreCancelled = true)
    @ObjectiveHandler({ CraftItemQuestObjective.class })
    public void handleCraftItemEvent(CraftItemEvent ev) {
        final Player player = (Player) ev.getWhoClicked();
        final PlayerInventory inventory = player.getInventory();
        final ClickType clickType = ev.getClick();

        ItemStack result = ev.getRecipe().getResult();
        int amount = result.getAmount();

        switch (clickType) {
            case NUMBER_KEY -> {
                final ItemStack clickedItem = inventory.getItem(ev.getHotbarButton());

                // If the item is not null, we clicked at slot that
                // has an item, resulting in recipe failing
                if (clickedItem != null) {
                    amount = 0;
                }
            }
            case DROP, CONTROL_DROP -> {
                final ItemStack cursorItem = ev.getCursor();

                // If the cursor item is not null, we clicked while holding
                // an item, resulting in recipe failing
                if (!cursorItem.isEmpty()) {
                    amount = 0;
                }
            }
            // Shift clicking is a little tricky...
            case SHIFT_LEFT, SHIFT_RIGHT -> {
                // Find the maximum number of items that can be crafted
                int count = result.getAmount();
                int maxCount = 99;

                for (@Nullable ItemStack matrix : ev.getInventory().getMatrix()) {
                    if (matrix != null && matrix.getAmount() < maxCount) {
                        maxCount = matrix.getAmount();
                    }
                }

                int craftAmount = count * maxCount;

                // Find how many items can fit in the inventory
                final Inventory bottomInventory = ev.getView().getBottomInventory();

                int canFit = 0;

                for (@Nullable ItemStack content : bottomInventory.getStorageContents()) {
                    // If the slot is empty, assume we can fit the whole stack
                    if (content == null) {
                        canFit += result.getType().getMaxStackSize();
                    }
                    // Check if the item can be fit in the slot
                    // and increment by the amount
                    else if (content.isSimilar(result)) {
                        canFit += Math.max(result.getMaxStackSize() - content.getAmount(), 0);
                    }
                }

                // If we can't fit everything, some items will be dropped on
                // the ground; we need to compensate for them
                if (canFit < craftAmount) {
                    craftAmount = ((canFit + amount - 1) / amount) * amount;
                }

                amount = craftAmount;
            }
        }

        tryIncrementObjective(player, CraftItemQuestObjective.class, result.getType(), amount);
    }

    @EventHandler(ignoreCancelled = true)
    @ObjectiveHandler({ DealDamageQuestObjective.class })
    public void handleEntityDamageEvent(EntityDamageByEntityEvent ev) {
        final Entity entity = ev.getEntity();
        Entity damager = ev.getDamager();

        if (damager instanceof Projectile projectile && projectile.getShooter() instanceof Player player) {
            damager = player;
        }

        if (!(damager instanceof Player player)) {
            return;
        }

        final EntityDamageEvent.DamageCause cause = ev.getCause();
        final double damage = ev.getFinalDamage();

        tryIncrementObjective(player, DealDamageQuestObjective.class, entity.getType(), cause, damage);
    }

    @EventHandler(ignoreCancelled = true)
    @ObjectiveHandler({ // jesus
            FinishDialogQuestObjective.class,
            GiveItemToNpcQuestObjective.class,
            GiveKeyedItemToNpcQuestObjective.class,
            TalkToMultipleNpcQuestObjective.class,
            TalkToNpcQuestObjective.class
    })
    public void handlePlayerClickAtNpcEvent(PlayerClickAtNpcEvent ev) {
        final Player player = ev.getPlayer();
        final HumanNPC npc = ev.getNpc();

        // Quest start
        tryStartQuest(player, quest -> {
            for (QuestStartBehaviour behaviour : quest.getStartBehaviours(player)) {
                if (!(behaviour instanceof QuestStartBehaviour.TalkToNpc talkToNpc)) {
                    continue;
                }

                if (!talkToNpc.npc().equals(npc)) {
                    continue;
                }

                talkToNpc.dialog().start(player);
                return false;
            }

            return false;
        });

        // Dialog start
        for (QuestData data : getActiveQuests(player)) {
            final QuestObjective objective = data.getCurrentObjective();

            Dialog dialog = null;

            if (objective instanceof TalkToNpcQuestObjective talkToNpcQuestObjective && npc.equals(talkToNpcQuestObjective.npc)) {
                dialog = talkToNpcQuestObjective.dialog;
            }
            else if (objective instanceof TalkToMultipleNpcQuestObjective talkToMultipleNpcQuestObjective) {
                dialog = talkToMultipleNpcQuestObjective.getDialog(npc, player);
            }

            if (dialog == null) {
                continue;
            }

            dialog.start(player);

            // Do start the cooldown, but don't run onClick
            ev.setResponse(PlayerClickAtNpcEvent.ClickResponse.HOLD);

            // We stop here, either the dialog was started or already in a dialog
            return;
        }

        // Give items
        final ItemStack handItem = player.getInventory().getItemInMainHand();

        tryIncrementObjective(player, GiveItemToNpcQuestObjective.class, npc, handItem);
        tryIncrementObjective(player, GiveKeyedItemToNpcQuestObjective.class, npc, handItem);
    }

    @EventHandler(ignoreCancelled = true)
    @ObjectiveHandler({ JumpQuestObjective.class })
    public void handlePlayerStatisticIncrementEvent(PlayerStatisticIncrementEvent ev) {
        final Player player = ev.getPlayer();
        final Statistic statistic = ev.getStatistic();

        if (statistic == Statistic.JUMP) {
            tryIncrementObjective(player, JumpQuestObjective.class);
        }
    }

    @EventHandler(ignoreCancelled = true)
    @ObjectiveHandler({ PlaceBlockQuestObjective.class })
    public void handleBlockPlaceEvent(BlockPlaceEvent ev) {
        final Player player = ev.getPlayer();
        final Block block = ev.getBlock();

        tryIncrementObjective(player, PlaceBlockQuestObjective.class, block.getType());
    }

    @EventHandler(ignoreCancelled = true)
    @ObjectiveHandler({ PlayNoteQuestObjective.class })
    public void handlePlayerInteractEvent(PlayerInteractEvent ev) {
        final Player player = ev.getPlayer();
        final Action action = ev.getAction();
        final Block clickedBlock = ev.getClickedBlock();

        if (ev.getHand() == EquipmentSlot.OFF_HAND || action == Action.PHYSICAL || clickedBlock == null) {
            return;
        }

        if (!(clickedBlock.getBlockData() instanceof NoteBlock noteBlock)) {
            return;
        }

        // Don't count creative players left clicks
        if (player.getGameMode() == GameMode.CREATIVE && action == Action.LEFT_CLICK_BLOCK) {
            return;
        }

        final Instrument instrument = noteBlock.getInstrument();
        final Note note = noteBlock.getNote();

        tryIncrementObjective(player, PlayNoteQuestObjective.class, instrument, note);
    }

    @EventHandler(ignoreCancelled = true)
    @ObjectiveHandler({ SlayEntityQuestObjective.class })
    public void handleEntityDeathEvent(EntityDeathEvent ev) {
        final LivingEntity entity = ev.getEntity();
        final Player killer = entity.getKiller();

        if (killer == null) {
            return;
        }

        tryIncrementObjective(killer, SlayEntityQuestObjective.class, entity.getType());
    }

    @EventHandler(ignoreCancelled = true)
    @ObjectiveHandler({ TakeDamageQuestObjective.class })
    public void handleEntityDamageEvent(EntityDamageEvent ev) {
        final Entity entity = ev.getEntity();
        final double damage = ev.getFinalDamage();
        final EntityDamageEvent.DamageCause cause = ev.getCause();

        if (!(entity instanceof Player player)) {
            return;
        }

        Entity damager = null;

        if (ev instanceof EntityDamageByEntityEvent entityDamageByEntityEvent) {
            damager = entityDamageByEntityEvent.getDamager();

            // In case of projectiles, we have to get the projectile shooter
            if (damager instanceof Projectile projectile && projectile.getShooter() instanceof Entity shooterEntity) {
                damager = shooterEntity;
            }
        }

        tryIncrementObjective(player, TakeDamageQuestObjective.class, damager != null ? damager.getType() : null, cause, damage);
    }

    @EventHandler(ignoreCancelled = true)
    @ObjectiveHandler({ TalkInChatQuestObjective.class })
    public void handleAsyncPlayerChatEvent(AsyncChatEvent ev) {
        final Component message = ev.message();

        if (!(message instanceof TextComponent textComponent)) {
            return;
        }

        tryIncrementObjective(ev.getPlayer(), TalkInChatQuestObjective.class, (objective, response) -> {
            if (objective.cancelMessage) {
                ev.setCancelled(true);
            }
        }, textComponent.content());
    }

    @EventHandler(ignoreCancelled = true)
    @ObjectiveHandler({ TravelDistanceQuestObjective.class, TravelToQuestObjective.class })
    public void handleVehicleMoveEvent(PlayerMoveOneBlockEvent ev) {
        final Player player = ev.getPlayer();

        TravelDistanceQuestObjective.TravelType travelType = TravelDistanceQuestObjective.TravelType.ON_FOOT;

        if (player.isGliding()) {
            travelType = TravelDistanceQuestObjective.TravelType.ON_ELYTRA;
        }
        else {
            final Entity vehicle = player.getVehicle();

            if (vehicle instanceof Minecart) {
                travelType = TravelDistanceQuestObjective.TravelType.IN_MINECART;
            }
            else if (vehicle instanceof Boat) {
                travelType = TravelDistanceQuestObjective.TravelType.IN_BOAT;
            }
            else if (vehicle != null) {
                travelType = TravelDistanceQuestObjective.TravelType.ON_MOUNT;
            }
        }

        // Start quest
        tryStartQuest(player, quest -> {
            for (QuestStartBehaviour behaviour : quest.getStartBehaviours(player)) {
                if (!(behaviour instanceof QuestStartBehaviour.GoTo goTo)) {
                    continue;
                }

                final Position position = goTo.position();

                if (!position.contains(player.getLocation())) {
                    continue;
                }

                final Dialog dialog = goTo.dialog();

                if (dialog != null) {
                    dialog.start(player);
                    return false; // The dialog starts the quest
                }

                return true; // Otherwise do start the quest
            }

            return false;
        });

        // Increment objective
        tryIncrementObjective(player, TravelDistanceQuestObjective.class, travelType);
        tryIncrementObjective(player, TravelToQuestObjective.class, player.getLocation());
    }

    public <T extends QuestObjective> void tryIncrementObjective(@Nonnull Player player, @Nonnull Class<T> clazz, @Nullable Object... objects) {
        tryIncrementObjective(player, clazz, Consumers.emptyBi(), objects);
    }

    public <T extends QuestObjective> void tryIncrementObjective(@Nonnull Player player, @Nonnull Class<T> clazz, @Nonnull BiConsumer<T, QuestObjective.Response> consumer, @Nullable Object... objects) {
        // Not calling get() because it computes the data
        final QuestDataList questData = managing.get(player);

        // nullable because the quest should create data, not us
        if (questData != null) {
            questData.forEach(data -> data.tryIncrementProgress(clazz, consumer, objects));
        }
    }

    @Nonnull
    public <T extends QuestObjective> Set<Tuple<QuestData, T>> getCurrentObjectivesOf(@Nonnull Player player, @Nonnull Class<T> ofClass) {
        final QuestDataList questData = managing.get(player);
        final Set<Tuple<QuestData, T>> hashSet = new HashSet<>();

        if (questData == null) {
            return hashSet;
        }

        for (QuestData data : questData.data.values()) {
            final QuestObjective currentObjective = data.getCurrentObjective();

            if (ofClass.isInstance(currentObjective)) {
                hashSet.add(Tuple.of(data, ofClass.cast(currentObjective)));
            }
        }

        return hashSet;
    }

    public void registerHandler(@Nonnull QuestHandler handler) {
        pluginHandlers.put(handler.getPlugin(), handler);
    }

    @Nonnull
    public QuestHandler getHandler(@Nonnull JavaPlugin plugin) {
        return Objects.requireNonNull(pluginHandlers.get(plugin), "Handler for plugin '%s' does not exist!".formatted(plugin.getName()));
    }

    @Nonnull
    @Override
    public QuestDataList get(@Nonnull Player player) {
        return managing.computeIfAbsent(player, QuestDataList::new);
    }

    /**
     * Attempts to start the given {@link Quest} for the given {@link Player}.
     * <br>
     * This method silently ignores starting already started quests.
     *
     * @param player      - Player to start the quest for.
     * @param quest       - Quest to start.
     * @param notifyError - Whether to send error messages if the quest was not started.
     * @param notifyStart - Whether to send the 'quest started' message.
     * @return The outcome of the start attempt.
     * @throws IllegalStateException If the plugin attempts to start unregistered quest
     */
    @Nonnull
    public StartResponse startQuest(@Nonnull Player player, @Nonnull Quest quest, boolean notifyError, boolean notifyStart) {
        final QuestHandler handler = getHandler(quest.getPlugin());
        final QuestDataList questData = get(player);
        final QuestFormatter formatter = quest.getFormatter();

        // Make sure the quest has at least one objective
        quest.getFirstObjective();

        if (questData.hasData(quest)) {
            if (notifyError) {
                formatter.sendCannotStartQuestAlreadyStarted(player, quest);
            }

            return StartResponse.ALREADY_STARTED;
        }

        // Make sure the quest is registered
        if (!handler.isRegistered(quest.getKey())) {
            throw new IllegalStateException("Quest '%s' is not registered in '%s'!".formatted(quest.getKeyAsString(), handler.getName()));
        }

        // Pre requirements
        final QuestPreRequirement preRequirements = quest.getPreRequirement();
        if (preRequirements != null && !preRequirements.isMet(player)) {
            if (notifyError) {
                formatter.sendPreRequirementNotMet(player, preRequirements);
            }

            return StartResponse.PRE_REQUIREMENTS_NOT_MET;
        }

        // Already completed
        if (handler.hasCompleted(player, quest)) {
            if (notifyError) {
                formatter.sendCannotStartQuestAlreadyCompleted(player, quest);
            }

            return StartResponse.ALREADY_COMPLETED;
        }

        questData.getDataOrCompute(quest);

        if (notifyStart) {
            formatter.sendQuestStartedFormat(player, quest);
        }

        return StartResponse.OK;
    }

    /**
     * Gets a copy of all active {@link QuestData} for the given {@link Player}, or an empty {@link Set} if none.
     *
     * @param player - Player to get the data for.
     * @return A set of all active quests, or an empty set if none.
     */
    @Nonnull
    public Set<QuestData> getActiveQuests(@Nonnull Player player) {
        final QuestDataList questData = managing.get(player);
        final Set<QuestData> questDataSet = new HashSet<>();

        if (questData == null) {
            return questDataSet;
        }

        questDataSet.addAll(questData.data.values());
        return questDataSet;
    }

    @Nonnull
    public Map<QuestHandler, List<Quest>> getQuests() {
        final Map<QuestHandler, List<Quest>> quests = new HashMap<>();
        workHandlers(handler -> quests.put(handler, handler.getQuests()));

        return quests;
    }

    @ApiStatus.Internal
    public void forEachData(@Nonnull Consumer<QuestData> consumer) {
        managing.forEach((player, data) -> data.forEach(consumer));
    }

    @ApiStatus.Internal
    public void workHandlers(@Nonnull Consumer<QuestHandler> consumer) {
        pluginHandlers.values().forEach(consumer);
    }

    @ApiStatus.Internal
    public void tryStartQuest(@Nonnull Player player, @Nonnull Predicate<Quest> predicate) {
        workHandlers(handler -> handler.getQuests().forEach(quest -> {
            if (!predicate.test(quest)) {
                return;
            }

            startQuest(player, quest, false, true);
        }));
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.METHOD })
    public @interface ObjectiveHandler {
        @Nonnull Class<? extends QuestObjective>[] value();
    }

    public enum StartResponse {
        OK,
        ALREADY_STARTED,
        PRE_REQUIREMENTS_NOT_MET,
        ALREADY_COMPLETED
    }
}
