package me.hapyl.eterna.module.player.quest;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.hapyl.eterna.EternaHandler;
import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.component.Components;
import me.hapyl.eterna.module.event.PlayerInteractNpcEvent;
import me.hapyl.eterna.module.location.LocationHelper;
import me.hapyl.eterna.module.location.Position;
import me.hapyl.eterna.module.npc.Npc;
import me.hapyl.eterna.module.player.dialog.Dialog;
import me.hapyl.eterna.module.player.dialog.DialogEndType;
import me.hapyl.eterna.module.player.dialog.DialogInstance;
import me.hapyl.eterna.module.player.quest.objective.*;
import me.hapyl.eterna.Runnables;
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
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents an internal {@link QuestHandler}.
 */
@ApiStatus.Internal
public final class QuestHandler extends EternaHandler<Plugin, QuestRegistry> implements Listener {
    
    static QuestHandler handler;
    
    public QuestHandler(@NotNull EternaKey key, @NotNull EternaPlugin eterna) {
        super(key, eterna);
        
        handler = this;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void handlePlayerJoinEvent(PlayerJoinEvent ev) {
        final Player player = ev.getPlayer();
        
        forEach(questRegistry -> {
            // Realistically this should never happen, since the one way that the data exists for a player
            // is if a quest has been started, which is impossible to do for an offline player, but we should
            // still handle that case, I guess.
            if (questRegistry.playerData.containsKey(player)) {
                return;
            }
            
            // Load quests
            final QuestDataMap questDataMap = QuestDataMap.createExisting(
                    questRegistry.load(player)
                                 .stream()
                                 .map(questData -> Map.entry(questData.getQuest(), questData))
                                 .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
            );
            
            questRegistry.playerData.put(player, questDataMap);
            
            // Handle `QuestStartBehaviour.OnJoin` quests
            questRegistry.getQuests().forEach(quest -> {
                if (quest.getStartBehaviour() instanceof QuestStartBehaviour.OnJoin(boolean silent)) {
                    questRegistry.startQuest(player, quest, silent);
                }
            });
        });
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void handlePlayerQuitEvent(PlayerQuitEvent ev) {
        final Player player = ev.getPlayer();
        
        forEach(questRegistry -> {
            final QuestDataMap questDataMap = questRegistry.playerData.remove(player);
            
            // Haven't done any quests
            if (questDataMap == null) {
                return;
            }
            
            // Save quests
            questRegistry.save(player, new HashSet<>(questDataMap.data.values()));
        });
    }
    
    // *-* Quest Handlers *-* //
    
    @EventHandler(ignoreCancelled = true)
    @QuestObjectiveHandler({ QuestObjectiveBreakBlock.class })
    public void handleBlockBreakEvent(BlockBreakEvent ev) {
        final Material material = ev.getBlock().getType();
        
        tryIncrementObjective(ev.getPlayer(), QuestObjectiveBreakBlock.class, QuestObjectArray.create(material));
    }
    
    @EventHandler(ignoreCancelled = true)
    @QuestObjectiveHandler({ QuestObjectiveBreakItem.class })
    public void handlePlayerItemBreakEvent(PlayerItemBreakEvent ev) {
        tryIncrementObjective(ev.getPlayer(), QuestObjectiveBreakItem.class, QuestObjectArray.create(ev.getBrokenItem().getType()));
    }
    
    @EventHandler(ignoreCancelled = true)
    @QuestObjectiveHandler({ QuestObjectiveBreedAnimal.class })
    public void handleAnimalBreedEvent(EntityBreedEvent ev) {
        final LivingEntity breeder = ev.getBreeder();
        
        if (!(breeder instanceof Player player)) {
            return;
        }
        
        tryIncrementObjective(player, QuestObjectiveBreedAnimal.class, QuestObjectArray.create(ev.getMother().getType()));
    }
    
    // https://github.com/ezeiger92/QuestWorld2/blob/master/src/main/java/com/questworld/extension/builtin/CraftMission.java
    @EventHandler(ignoreCancelled = true)
    @QuestObjectiveHandler({ QuestObjectiveCraftItem.class })
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
        
        tryIncrementObjective(player, QuestObjectiveCraftItem.class, QuestObjectArray.create(result.getType(), amount));
    }
    
    @EventHandler(ignoreCancelled = true)
    @QuestObjectiveHandler({ QuestObjectiveDealDamage.class })
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
        
        tryIncrementObjective(player, QuestObjectiveDealDamage.class, QuestObjectArray.create(entity.getType(), cause, damage));
    }
    
    @EventHandler(ignoreCancelled = true)
    @QuestObjectiveHandler({ // jesus
                             QuestObjectiveCompleteDialog.class,
                             QuestObjectiveGiveItemToNpc.class,
                             QuestObjectiveGiveKeyedItemToNpc.class,
                             QuestObjectiveTalkToNpc.class
    })
    public void handlePlayerInteractNpcEvent(PlayerInteractNpcEvent ev) {
        final Player player = ev.getPlayer();
        final Npc npc = ev.getNpc();
        
        for (QuestRegistry questRegistry : registry.values()) {
            // Handler `QuestStartBehaviour.TalkToNpc`
            questRegistry.tryStartQuest(
                    player, quest -> {
                        if (!(quest.getStartBehaviour() instanceof QuestStartBehaviour.TalkToNpc(@NotNull Npc targetNpc, @NotNull Dialog dialog))) {
                            return false;
                        }
                        
                        // If npc matches, start the dialog
                        if (targetNpc.equals(npc)) {
                            dialog.start(player);
                        }
                        
                        // Do not automatically start the quest, because we need to finish the dialog, not start it
                        return false;
                    }
            );
            
            final QuestDataMap questDataMap = questRegistry.playerData.get(player);
            
            if (questDataMap == null) {
                continue;
            }
            
            for (QuestData data : questDataMap) {
                final QuestObjectiveTalkToNpc objective = data.getCurrentObjective(QuestObjectiveTalkToNpc.class).orElse(null);
                
                // If wrong objective or npc don't match, continue
                if (objective == null || !objective.getNpc().equals(npc)) {
                    continue;
                }
                
                // Start the dialog
                objective.getDialog().start(player);
                
                // Deny the event with cooldown
                ev.setResponse(PlayerInteractNpcEvent.ClickResponse.DENY_WITH_COOLDOWN);
                
                // We stop the code here because the dialog has priority, so we don't trigger given item objectives at the same tick
                return;
            }
        }
        
        // Handle `QuestObjectiveGivenItemsToNpc` & `QuestObjectiveGivenKeyedItemToNpc`
        final ItemStack handItem = player.getInventory().getItemInMainHand();
        
        tryIncrementObjective(player, QuestObjectiveGiveItemToNpc.class, QuestObjectArray.create(npc, handItem));
    }
    
    @EventHandler(ignoreCancelled = true)
    @QuestObjectiveHandler({ QuestObjectiveJump.class })
    public void handlePlayerStatisticIncrementEvent(PlayerJumpEvent ev) {
        tryIncrementObjective(ev.getPlayer(), QuestObjectiveJump.class, QuestObjectArray.createEmpty());
    }
    
    @EventHandler(ignoreCancelled = true)
    @QuestObjectiveHandler({ QuestObjectivePlaceBlock.class })
    public void handleBlockPlaceEvent(BlockPlaceEvent ev) {
        final Player player = ev.getPlayer();
        final Block block = ev.getBlock();
        
        tryIncrementObjective(player, QuestObjectivePlaceBlock.class, QuestObjectArray.create(block.getType()));
    }
    
    @EventHandler(ignoreCancelled = true)
    @QuestObjectiveHandler({ QuestObjectivePlayNote.class })
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
        
        tryIncrementObjective(player, QuestObjectivePlayNote.class, QuestObjectArray.create(instrument, note));
    }
    
    @EventHandler(ignoreCancelled = true)
    @QuestObjectiveHandler({ QuestObjectiveKillEntity.class })
    public void handleEntityDeathEvent(EntityDeathEvent ev) {
        final LivingEntity entity = ev.getEntity();
        final Player killer = entity.getKiller();
        
        if (killer == null) {
            return;
        }
        
        tryIncrementObjective(killer, QuestObjectiveKillEntity.class, QuestObjectArray.create(entity.getType()));
    }
    
    @EventHandler(ignoreCancelled = true)
    @QuestObjectiveHandler({ QuestObjectiveTakeDamage.class })
    public void handleEntityDamageEvent(EntityDamageEvent ev) {
        final Entity entity = ev.getEntity();
        final double damage = ev.getFinalDamage();
        final EntityDamageEvent.DamageCause cause = ev.getCause();
        
        if (!(entity instanceof Player player)) {
            return;
        }
        
        final Entity damager = ev instanceof EntityDamageByEntityEvent entityDamageByEntityEvent
                               ? entityDamageByEntityEvent.getDamageSource().getCausingEntity()
                               : null;
        
        tryIncrementObjective(
                player,
                QuestObjectiveTakeDamage.class,
                QuestObjectArray.create(damager != null ? damager.getType() : null, cause, damage)
        );
    }
    
    @EventHandler(ignoreCancelled = true)
    @QuestObjectiveHandler({ QuestObjectiveTalkInChat.class })
    public void handleAsyncPlayerChatEvent(AsyncChatEvent ev) {
        final Player player = ev.getPlayer();
        final String message = Components.toString(ev.message());
        
        // Chat being async is annoying, so we have to handle the test manually in order to properly cancel the message
        for (QuestRegistry questRegistry : registry.values()) {
            final QuestDataMap questDataMap = questRegistry.playerData.get(player);
            
            if (questDataMap != null) {
                questDataMap.forEach(data -> {
                    final QuestObjectiveTalkInChat objective = data.getCurrentObjective(QuestObjectiveTalkInChat.class).orElse(null);
                    
                    if (objective != null && objective.test(message).isTestSucceeded() && objective.isCancelMessage()) {
                        ev.setCancelled(true);
                    }
                });
            }
        }
        
        // Yeah, this is ugly, but it's the only way I could think of
        final Runnable tryIncrementObjective = () -> tryIncrementObjective(
                player,
                QuestObjectiveTalkInChat.class,
                QuestObjectArray.create(message)
        );
        
        if (ev.isAsynchronous()) {
            Runnables.sync(tryIncrementObjective);
        }
        else {
            tryIncrementObjective.run();
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    @QuestObjectiveHandler({ QuestObjectiveTravelDistance.class, QuestObjectiveTravelTo.class })
    public void handlePlayerMoveEvent(PlayerMoveEvent ev) {
        class Holder {
            private static final double MAGIC_ESTIMATED_DISTANCE_FACTOR = 4.64;
        }
        
        final Player player = ev.getPlayer();
        
        final Location locationFrom = ev.getFrom();
        final Location locationTo = ev.getTo();
        
        // Do not fire for small or mouse movement
        if (!locationFrom.getWorld().equals(locationTo.getWorld()) || !LocationHelper.hasMovedBlock(locationFrom, locationTo)) {
            return;
        }
        
        // Because we only pass the event for when a player moves a full block, we have to estimate the distance,
        // which is just the distance multiplied by a magic value that approximately evaluates to `~1.0` for walking
        // and `~0.3` for sneaking.
        final double estimatedDistance = locationFrom.distance(locationTo) * Holder.MAGIC_ESTIMATED_DISTANCE_FACTOR;
        
        QuestObjectiveTravelDistance.TravelType travelType = QuestObjectiveTravelDistance.TravelType.ON_FOOT;
        
        if (player.isGliding()) {
            travelType = QuestObjectiveTravelDistance.TravelType.ON_ELYTRA;
        }
        else {
            final Entity vehicle = player.getVehicle();
            
            if (vehicle instanceof Minecart) {
                travelType = QuestObjectiveTravelDistance.TravelType.IN_MINECART;
            }
            else if (vehicle instanceof Boat) {
                travelType = QuestObjectiveTravelDistance.TravelType.IN_BOAT;
            }
            else if (vehicle != null) {
                travelType = QuestObjectiveTravelDistance.TravelType.ON_MOUNT;
            }
        }
        
        // Handle `QuestStartBehaviour.GoTo`
        forEach(questRegistry -> questRegistry.tryStartQuest(
                player, quest -> {
                    if (quest.getStartBehaviour() instanceof QuestStartBehaviour.GoTo(@NotNull Position position, @Nullable Dialog dialog)) {
                        // If not inside the cuboid, return
                        if (!position.contains(player.getLocation())) {
                            return false;
                        }
                        
                        // If the dialog exists, start the dialog instead of starting the quest itself
                        if (dialog != null) {
                            dialog.start(player);
                            return false;
                        }
                        
                        return true;
                    }
                    
                    return false;
                }
        ));
        
        // Increment objective
        tryIncrementObjective(player, QuestObjectiveTravelDistance.class, QuestObjectArray.create(travelType, estimatedDistance));
        tryIncrementObjective(player, QuestObjectiveTravelTo.class, QuestObjectArray.create(player.getLocation()));
    }
    
    @EventHandler
    @QuestObjectiveHandler({ QuestObjectiveTravelDistance.class })
    public void handleVehicleMoveEvent(VehicleMoveEvent ev) {
        final Vehicle vehicle = ev.getVehicle();
        
        // We only care about minecarts because for some reason they don't count for PlayerMoveEvent
        if (!(vehicle instanceof Minecart)) {
            return;
        }
        
        final List<Entity> passengers = vehicle.getPassengers();
        
        if (passengers.isEmpty()) {
            return;
        }
        
        final Location locationFrom = ev.getFrom();
        final Location locationTo = ev.getTo();
        
        if (!LocationHelper.hasMovedBlock(locationFrom, locationTo)) {
            return;
        }
        
        passengers.forEach(entity -> {
            if (!(entity instanceof Player player)) {
                return;
            }
            
            tryIncrementObjective(
                    player,
                    QuestObjectiveTravelDistance.class,
                    QuestObjectArray.create(QuestObjectiveTravelDistance.TravelType.IN_MINECART)
            );
        });
    }
    
    @ApiStatus.Internal
    <O extends QuestObjective> void tryIncrementObjective(
            @NotNull Player player,
            @NotNull Class<O> clazz,
            @NotNull QuestObjectArray array
    ) {
        forEach(questRegistry -> {
            final QuestDataMap questDataMap = questRegistry.playerData.get(player);
            
            // nullable because the quest should create data, not us
            if (questDataMap == null) {
                return;
            }
            
            final Iterator<QuestData> iterator = questDataMap.iterator();
            
            while (iterator.hasNext()) {
                final QuestData questData = iterator.next();
                
                // Ignore completed quests
                if (questData.isCompleted()) {
                    continue;
                }
                
                final Quest quest = questData.getQuest();
                final QuestFormatter formatter = quest.getFormatter();
                
                final O currentObjective = questData.getCurrentObjective(clazz).orElse(null);
                
                if (currentObjective == null) {
                    continue;
                }
                
                final QuestObjective.Response response = currentObjective.test(questData, array);
                
                // If response has failed, just stop here
                if (response.isTestFailed()) {
                    continue;
                }
                
                // Handle objective fail
                if (response.isObjectiveFailed()) {
                    final QuestObjective.FailType failType = currentObjective.getFailType();
                    
                    switch (failType) {
                        // Set progress to 0 and notify player
                        case RESTART_OBJECTIVE -> {
                            questData.setCurrentStageProgress(0.0);
                        }
                        
                        // Fail the quest entirely
                        case FAIL_QUEST -> {
                            quest.onFail(player, questData);
                            QuestFormatter.Timings.QUEST_FAILED.schedule(() -> formatter.questFailed(player, quest));
                            
                            iterator.remove();
                        }
                    }
                    
                    QuestFormatter.Timings.OBJECTIVE_FAILED.schedule(() -> formatter.objectiveFailed(player, currentObjective, failType));
                    currentObjective.onFail(player, failType);
                    return;
                }
                // Otherwise, the response is the value by which to increment the progress
                else {
                    final double increment = response.getValue();
                    
                    questData.setCurrentStageProgress(questData.getCurrentStageProgress() + increment);
                    currentObjective.onProgress(player, increment);
                }
                
                // Check for whether the object is now complete
                if (questData.getCurrentStageProgress() >= currentObjective.getGoal()) {
                    // Process current objective
                    currentObjective.onComplete(player);
                    QuestFormatter.Timings.OBJECTIVE_COMPLETE.schedule(() -> formatter.objectiveComplete(player, currentObjective));
                    
                    // Process next objective
                    final QuestObjective nextObjective = questData.getNextObjective().orElse(null);
                    
                    // Increment stage AFTER calling `getNextObjective()` to no jump over the objective
                    questData.setCurrentStage(questData.getCurrentStage() + 1);
                    
                    // If next objective is `null`, means the quest is complete
                    if (nextObjective == null) {
                        quest.onComplete(player, questData);
                        
                        // We always keep the data now, so mark it as complete instead of removing it
                        questData.markComplete();
                        
                        // Delay the quest complete message a little bit
                        QuestFormatter.Timings.QUEST_COMPLETE.schedule(() -> formatter.questComplete(player, quest));
                    }
                    // Otherwise, start the next objective
                    else {
                        questData.setCurrentStageProgress(questData.getCurrentStageProgress() + 1);
                        questData.setCurrentStageProgress(0.0);
                        
                        nextObjective.onStart(player);
                        
                        // Display the next objective with a little delay
                        QuestFormatter.Timings.OBJECTIVE_STARTED.schedule(() -> formatter.objectiveStarted(player, nextObjective));
                    }
                }
            }
        });
        
    }
    
    @ApiStatus.Internal
    @QuestObjectiveHandler({ QuestObjectiveCompleteDialog.class })
    public static void dialogFinished(@NotNull DialogInstance dialogInstance, @NotNull DialogEndType dialogEndType) {
        final Player player = dialogInstance.getPlayer();
        final Dialog dialog = dialogInstance.getDialog();
        
        // Handle `QuestStartBehaviour.DialogStartBehaviour`
        handler.forEach(questRegistry -> questRegistry.tryStartQuest(player, quest -> {
            if (quest.getStartBehaviour() instanceof QuestStartBehaviour.DialogStartBehaviour dialogStartBehaviour) {
                // If a quest has a `QuestStartBehaviour.DialogStartBehaviour` and the dialog in question
                // is the same as the finished one then start the quest.
                return dialog.equals(dialogStartBehaviour.dialog());
            }
            
            return false;
        }));
        
        // Increment `QuestObjectiveCompleteDialog`
        handler.tryIncrementObjective(player, QuestObjectiveCompleteDialog.class, QuestObjectArray.create(dialog, dialogEndType));
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.METHOD })
    public @interface QuestObjectiveHandler {
        @NotNull Class<? extends QuestObjective>[] value();
    }
}
