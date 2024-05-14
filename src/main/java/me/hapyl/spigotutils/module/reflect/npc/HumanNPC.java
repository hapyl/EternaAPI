package me.hapyl.spigotutils.module.reflect.npc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import me.hapyl.spigotutils.Eterna;
import me.hapyl.spigotutils.EternaLogger;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.annotate.Range;
import me.hapyl.spigotutils.module.annotate.TestedOn;
import me.hapyl.spigotutils.module.annotate.Version;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.entity.LimitedVisibility;
import me.hapyl.spigotutils.module.hologram.Hologram;
import me.hapyl.spigotutils.module.hologram.LineFit;
import me.hapyl.spigotutils.module.math.Numbers;
import me.hapyl.spigotutils.module.math.nn.IntInt;
import me.hapyl.spigotutils.module.player.PlayerLib;
import me.hapyl.spigotutils.module.quest.PlayerQuestObjective;
import me.hapyl.spigotutils.module.quest.QuestManager;
import me.hapyl.spigotutils.module.quest.QuestObjectiveType;
import me.hapyl.spigotutils.module.reflect.DataWatcherType;
import me.hapyl.spigotutils.module.reflect.JsonHelper;
import me.hapyl.spigotutils.module.reflect.Reflect;
import me.hapyl.spigotutils.module.reflect.metadata.MetadataIndex;
import me.hapyl.spigotutils.module.reflect.npc.entry.NPCEntry;
import me.hapyl.spigotutils.module.reflect.npc.entry.StringEntry;
import me.hapyl.spigotutils.module.util.BukkitUtils;
import me.hapyl.spigotutils.module.util.TeamHelper;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.EntityAreaEffectCloud;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.entity.EnumItemSlot;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.AssertNonNullIfNonNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Function;

/**
 * Allows to create <b>simple</b> player NPCs with support of clicks.
 * <p>
 * For complex NPCs use <a href="https://github.com/CitizensDev/CitizensAPI">CitizensAPI</a>!
 */
@SuppressWarnings("unused")
@TestedOn(version = Version.V1_20_4)
public class HumanNPC extends LimitedVisibility implements Human, NPCListener {

    public static final String CLICK_NAME = "&e&lCLICK";
    public static final String CHAT_FORMAT = "&e[NPC] &a{NAME}: " + ChatColor.WHITE + "{MESSAGE}";

    private static final String nameToUuidRequest = "https://api.mojang.com/users/profiles/minecraft/%s";
    private static final String uuidToProfileRequest = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";

    protected final Set<Player> showingTo = Sets.newHashSet();

    private final EternaPlayer human;
    private final Hologram aboveHead;
    private final String hexName;
    private final String teamName;
    private final UUID uuid;
    private final NPCResponse responses = new NPCResponse();
    private final NPCEquipment equipment;
    private final Map<Player, InteractionDelay> interactedAt = new HashMap<>();
    private final List<NPCEntry> entries = new ArrayList<>();
    private final int id;
    private String npcName;
    private Location location;
    private String chatPrefix;
    private boolean alive;
    private boolean collision = true;
    private boolean stopTalking = false;
    private long interactionDelay = 0L;
    private String cannotInteractMessage = "Not now.";
    private int lookAtCloseDist;
    private BukkitTask moveTask;
    private EntityAreaEffectCloud sitEntity;
    private RestPosition restPosition;

    private HumanNPC() {
        this(BukkitUtils.getSpawnLocation(), "", "");
    }

    public HumanNPC(Location location, @Nullable String npcName) {
        this(location, npcName, npcName);
    }

    /**
     * Create instance of HumanNPC class.
     *
     * @param location  - Location of NPC.
     * @param npcName   - Name of NPC.
     *                  Use null or blank string to remove name.
     * @param skinOwner - Skin owner.
     *                  Leave empty or null to apply skin later.
     *                  Only use online players names for this. See {@link HumanNPC#setSkin(String)} disclaimer.
     */
    public HumanNPC(@Nonnull Location location, @Range(max = 32, throwsError = true) @Nullable String npcName, @Nullable String skinOwner) {
        this(location, npcName, skinOwner, uuid -> ("§8[NPC] " + uuid.toString().replace("-", "")).substring(0, 16));
    }

    protected HumanNPC(Location location, String npcName, String skinOwner, Function<UUID, String> hexNameFn) {
        if (npcName == null) {
            npcName = "";
        }

        if (npcName.length() > 32) {
            throw new IndexOutOfBoundsException("NPC name cannot be longer than 32.");
        }

        if (!npcName.isEmpty()) {
            this.chatPrefix = npcName;
        }

        this.uuid = UUID.randomUUID();
        this.location = location.clone();
        this.npcName = npcName;
        this.hexName = hexNameFn.apply(uuid);
        this.aboveHead = new Hologram().addLine(this.npcName).create(getLocation().subtract(0.0d, 1.75d, 0.0d));
        this.equipment = new NPCEquipment();
        this.teamName = TeamHelper.PARENT + "npc." + uuid;

        this.human = new EternaPlayer(location, hexName);
        this.human.setLocation(location);
        this.id = human.getEntityId();

        if (skinOwner != null && !skinOwner.isEmpty()) {
            setSkin(skinOwner);
        }

        setVisibility(40);
        Eterna.getRegistry().npcRegistry.register(this);
        this.alive = true;
    }

    @Override
    public boolean isShaking() {
        return Reflect.getDataWatcherValue(human.getHuman(), DataWatcherType.INT, 7) != 0;
    }

    @Override
    public void setShaking(boolean shaking) {
        showingTo.forEach(player -> Reflect.setDataWatcherValue(
                human.getHuman(),
                DataWatcherType.INT,
                7,
                shaking ? Integer.MAX_VALUE : 0
        ));

        updateDataWatcher();
    }

    @Override
    public void setShoulderEntity(Shoulder shoulder, boolean status) throws NotImplementedException {
        throw new NotImplementedException("setShoulderEntity not yet implemented");
    }

    @Nonnull
    public Hologram getAboveHead() {
        return aboveHead;
    }

    @Override
    public Player bukkitEntity() {
        return this.human.getBukkitEntity();
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    @Override
    public Set<Player> getViewers() {
        return showingTo;
    }

    @Nonnull
    @Override
    public Location getLocation() {
        return BukkitUtils.newLocation(location);
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
        this.human.setLocation(location);
        this.setHeadRotation(this.location.getYaw());

        sendPacket(human.packetFactory.getPacketTeleport());
        syncText();

        // Call "event"
        showingTo.forEach(player -> onTeleport(player, location));
    }

    @Override
    public boolean isSitting() {
        return sitEntity != null;
    }

    @Override
    public void setSitting(boolean sitting) {
        if (sitEntity != null) {
            showingTo.forEach(player -> Reflect.destroyEntity(sitEntity, player));
            sitEntity = null;
        }

        if (sitting) {
            final Location location = getLocation();
            final World world = getWorld();

            sitEntity = new EntityAreaEffectCloud(Reflect.getMinecraftWorld(world), location.getX(), location.getY(), location.getZ());
            sitEntity.a(0.0f);            // setRadius(float arg0)
            sitEntity.a(Integer.MAX_VALUE); // setDuration(int arg0)

            ((net.minecraft.world.entity.Entity) sitEntity).p = ImmutableList.of(human.getHuman());

            showingTo.forEach(player -> {
                Reflect.createEntity(sitEntity, player);
                Reflect.updateMetadata(sitEntity, player);
            });

            updateSitting();
        }
    }

    @Override
    public void setRestPosition(float yaw, float pitch) {
        restPosition = new RestPosition(yaw, pitch);
    }

    @Override
    @Nullable
    public RestPosition getRestPosition() {
        return restPosition;
    }

    @Override
    public void resetRestPotion() {
        restPosition = null;
    }

    @Override
    public void hideVisibility(@Nonnull Player player) {
        human.hide(player);
    }

    @Override
    public void showVisibility(@Nonnull Player player) {
        hideDisplayName();

        human.show(player);

        setLocation(location);

        updateEquipment();
        updateCollision();
        updateSkin();

        updateDataWatcher();
        hideTabListName();
        updateSitting();
    }

    @Override
    public boolean isShowingTo(Player player) {
        return this.showingTo.contains(player);
    }

    public String getNpcName() {
        return npcName;
    }

    public void rename(@Nullable String newName) {
        npcName = newName == null ? "" : newName;
        aboveHead.setLine(0, npcName);
        aboveHead.updateLines();
    }

    public String getHexName() {
        return hexName;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getChatPrefix() {
        return chatPrefix;
    }

    public void setChatPrefix(String chatPrefix) {
        this.chatPrefix = chatPrefix;
    }

    public void sendNpcMessage(Player player, String msg) {
        // Placeholders
        if (msg.contains("{") && msg.contains("}")) {
            msg = placeHold(msg, player);
        }

        final String finalMessage = CHAT_FORMAT.replace("{NAME}", this.getPrefix()).replace("{MESSAGE}", msg);
        Chat.sendMessage(player, finalMessage);

    }

    @Override
    public HumanNPC addEntry(NPCEntry entry) {
        this.entries.add(entry);
        return this;
    }

    @Override
    public HumanNPC addDialogLine(String string, int delayNext) {
        entries.add(new StringEntry(string, delayNext));
        return this;
    }

    @Override
    public HumanNPC addDialogLine(String string) {
        return this.addDialogLine(string, Numbers.clamp(string.length(), 20, 100));
    }

    @Override
    public void onClick(@Nonnull Player player, @Nonnull ClickType type) {
    }

    public final boolean onClickAuto(Player player, ClickType clickType) {
        // Can interact?
        if (!this.canInteract(player)) {
            final String cannotInteract = this.getNPCResponses().getCannotInteract();
            if (cannotInteract.isEmpty()) {
                return false;
            }
            sendNpcMessage(player, cannotInteract);
            return false;
        }

        onClick(player, clickType);

        // Do the entry magic
        final QuestManager quest = QuestManager.current();
        if (!this.entries.isEmpty()) {
            final IntInt nextDelay = new IntInt();
            final IntInt i = new IntInt();

            for (final NPCEntry entry : this.entries) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!exists() || !player.isOnline() || stopTalking) {
                            stopTalking = false;
                            cancel();
                            return;
                        }

                        // Progress FINISH_DIALOG
                        if (i.get() == (entries.size() - 1)) {
                            quest.checkActiveQuests(player, QuestObjectiveType.FINISH_DIALOGUE, this);
                        }

                        i.increment();
                        entry.invokeEntry(HumanNPC.this, player);
                    }
                }.runTaskLater(EternaPlugin.getPlugin(), nextDelay.get());
                nextDelay.addAndGet(entry.getDelay());
            }

            setInteractDelay(player, nextDelay.get() + 20L);
        }
        // Only start interact delay if there are no entries, entries override the delay because yes
        else {
            setInteractDelay(player, interactionDelay);
        }

        // Progress TALK_TO_NPC
        quest.checkActiveQuests(player, QuestObjectiveType.TALK_TO_NPC, this);

        // Progress GIVE_ITEM_STACK_TO_NPC
        final ItemStack item = player.getInventory().getItemInMainHand();
        if (!item.getType().isAir()) {

            // Item Stack test
            quest.checkActiveQuests(player, QuestObjectiveType.GIVE_ITEM_STACK_TO_NPC, item, this);

            // Material test
            if (quest.hasQuestsOfType(player, QuestObjectiveType.GIVE_ITEMS_TO_NPC)) {
                for (final PlayerQuestObjective obj : quest.getActiveObjectivesOfType(player, QuestObjectiveType.GIVE_ITEMS_TO_NPC)) {
                    final int amount = item.getAmount();
                    // Check for the correct item
                    if (obj.testQuestCompletion(item.getType(), amount, this) >= 0.0d) {

                        final int needMore = (int) (obj.getCompletionGoal() - obj.getGoalCurrent());
                        final int canGive = Numbers.clamp(amount - needMore, needMore, amount);

                        obj.incrementGoal(canGive, true);

                        if (obj.isFinished()) {
                            this.sendNpcMessage(player, this.getNPCResponses().getQuestGiveItemsFinish());
                            PlayerLib.villagerYes(player);
                        }
                        else {
                            this.sendNpcMessage(player, this.getNPCResponses().getQuestGiveItemsNeedMore());
                            PlayerLib.playSound(player, Sound.ENTITY_VILLAGER_TRADE, 1.0f);
                        }
                    }
                    // Not correct item
                    else {
                        this.sendNpcMessage(player, this.getNPCResponses().getQuestGiveItemsInvalidItem());
                        PlayerLib.villagerNo(player);
                    }
                }
            }

        }
        return true;
    }

    public NPCResponse getNPCResponses() {
        return responses;
    }

    public void setInteractDelay(@Nonnull Player player, long delayMillis) {
        this.interactedAt.put(player, new InteractionDelay(player, delayMillis));
    }

    @Override
    public boolean exists() {
        return Eterna.getRegistry().npcRegistry.isRegistered(getId());
    }

    @Override
    public String getName() {
        return npcName;
    }

    @Override
    public void stopTalking() {
        this.stopTalking = true;
    }

    public String getCannotInteractMessage() {
        return cannotInteractMessage;
    }

    public HumanNPC setCannotInteractMessage(String cannotInteractMessage) {
        this.cannotInteractMessage = cannotInteractMessage;
        return this;
    }

    public long getInteractionDelay() {
        return interactionDelay;
    }

    @Override
    public HumanNPC setInteractionDelay(long interactionDelayMillis) {
        this.interactionDelay = interactionDelayMillis;
        return this;
    }

    @Override
    public int getLookAtCloseDist() {
        return lookAtCloseDist;
    }

    @Override
    public HumanNPC setLookAtCloseDist(int lookAtCloseDist) {
        this.lookAtCloseDist = lookAtCloseDist;
        return this;
    }

    @Override
    public HumanNPC addTextAboveHead(String text) {
        this.aboveHead.addLine(text);
        this.aboveHead.updateLines(LineFit.BACKWARDS);
        return this;
    }

    @Override
    public HumanNPC removeTextAboveHead(int index) {
        this.aboveHead.removeLine(index);
        this.aboveHead.updateLines(LineFit.BACKWARDS);
        return this;
    }

    @Override
    public NPCPose getPose() {
        return human.getPose();
    }

    @Override
    public HumanNPC setPose(@Nonnull NPCPose pose) {
        if (!isShowing()) { // don't care, plugins should not call this unless NPC is showing to someone
            return this;
        }

        // STANDING pose handled differently because fuck me
        if (pose == NPCPose.STANDING) {
            human.setPose(NPCPose.fakeStandingPoseForNPCBecauseActualStandingPoseDoesNotWorkForSomeReason());
            updateDataWatcher();

            // Actually set the pose to STANDING for getPose()
            human.setPose(NPCPose.STANDING);
        }
        else {
            human.setPose(pose);
            updateDataWatcher();
        }

        return this;
    }

    @Override
    public boolean isShowing() {
        showingTo.removeIf(player -> !player.isOnline());

        return !showingTo.isEmpty();
    }

    @Override
    public void lookAt(Entity entity) {
        lookAt(entity.getLocation());
    }

    @Override
    public void lookAt(Location location) {
        this.location.setDirection(location.clone().subtract(this.location).toVector());
        this.setHeadRotation(this.location.getYaw());

        sendPacket(new PacketPlayOutEntity.PacketPlayOutEntityLook(
                this.getId(),
                (byte) (this.location.getYaw() * 256 / 360),
                (byte) (this.location.getPitch() * 256 / 360),
                true
        ));
    }

    @Override
    public void teleport(double x, double y, double z, float yaw, float pitch) {
        teleport(new Location(this.location.getWorld(), x, y, z, yaw, pitch));
    }

    @Override
    public void teleport(Location location) {
        this.location.setX(location.getX());
        this.location.setY(location.getY());
        this.location.setZ(location.getZ());
        this.location.setYaw(location.getYaw());
        this.location.setPitch(location.getPitch());
        this.setLocation(this.location);
        syncText();
    }

    @Override
    public void move(Location location, float speed) {
        stopMoving();
        lookAt(location);

        location.setYaw(this.location.getYaw());
        location.setPitch(this.location.getPitch());

        moveTask = new BukkitRunnable() {

            private Location npcLocation = getLocation();
            private final double distanceToMove = npcLocation.distance(location);
            private final double speedScaled = distanceToMove / speed;
            private double deltaX = (location.getX() - npcLocation.getX()) / speedScaled;
            private double deltaY = (location.getY() - npcLocation.getY()) / speedScaled;
            private double deltaZ = (location.getZ() - npcLocation.getZ()) / speedScaled;
            private int distance = 0;

            @Override
            public void run() {
                if (distance >= speedScaled) {
                    teleport(location);
                    stopMoving();
                    cancel();
                    return;
                }

                double newX = npcLocation.getX() + deltaX;
                double newY = npcLocation.getY() + deltaY;
                double newZ = npcLocation.getZ() + deltaZ;

                // Handle Gravity
                boolean onGround = isOnGround();

                // Handle climbing on stairs and slabs
                final World world = getWorld();
                final Block block = world.getBlockAt((int) newX, (int) newY, (int) newZ);

                if (!onGround) {
                    newY -= 0.08 * distance;
                }

                // Finally, simulate movement.
                teleport(newX, newY, newZ, location.getYaw(), location.getPitch());

                npcLocation = getLocation();
                deltaX = (location.getX() - npcLocation.getX()) / (speedScaled - distance);
                deltaY = (location.getY() - npcLocation.getY()) / (speedScaled - distance);
                deltaZ = (location.getZ() - npcLocation.getZ()) / (speedScaled - distance);

                distance++;
            }
        }.runTaskTimer(EternaPlugin.getPlugin(), 0L, 1L);
    }

    @Nonnull
    public World getWorld() {
        final World world = location.getWorld();

        if (world == null) {
            throw new IllegalStateException("Cannot move NPC in unloaded world.");
        }

        return world;
    }

    @Override
    public boolean isOnGround() {
        final Location loc = getLocation();
        final Block feetBlock = loc.subtract(0, 0.1, 0).getBlock();
        final Block headBlock = loc.add(0, bukkitEntity().getHeight(), 0).getBlock();

        final boolean isFeetSolid = feetBlock.getType().isSolid();
        final boolean isHeadSolid = headBlock.getType().isSolid();

        if (isFeetSolid || isHeadSolid) {
            return true;
        }

        // Check for carpets
        if (feetBlock.getType().name().contains("CARPET")) {
            double carpetHeight = feetBlock.getLocation().getY() + 0.0625; // Carpet is 1/16th of a block thick
            return loc.getY() <= carpetHeight;
        }

        return false;
    }

    @Override
    public void move(double x, double y, double z, float speed) {
        move(new Location(getLocation().getWorld(), x, y, z), speed);
    }

    @Override
    public boolean stopMoving() {
        if (moveTask == null || moveTask.isCancelled()) {
            return false;
        }

        moveTask.cancel();
        return true;
    }

    @Override
    public void jump(double height) {
        throw new NotImplementedException();
    }

    @Override
    public void push(Vector vector) {
        throw new NotImplementedException();
        //human.f(vector.getX(), vector.getY(), vector.getZ());
    }

    @Override
    public void setHeadRotation(float yaw, float pitch) {
        location.setYaw(yaw);
        location.setPitch(pitch);
        human.setYawPitch(yaw, pitch);

        setHeadRotation(yaw);
        sendPacket(human.packetFactory.getPacketTeleport());
    }

    @Override
    public void setHeadRotation(float yaw) {
        sendPacket(human.packetFactory.getPacketEntityHeadRotation(yaw));
    }

    @Override
    public void swingMainHand() {
        swingArm(true);
    }

    @Override
    public void swingOffHand() {
        swingArm(false);
    }

    @Override
    public HumanNPC setSkin(@Nonnull String texture, @Nonnull String signature) {
        final GameProfile profile = human.getProfile();

        profile.getProperties().removeAll("textures");
        profile.getProperties().put("textures", new Property("textures", texture, signature));
        return this;
    }

    public boolean hasSkin() {
        final Collection<Property> skin = getSkin();
        return skin != null && !skin.isEmpty();
    }

    public Collection<Property> getSkin() {
        return this.human.getProfile().getProperties().get("textures");
    }

    /**
     * <b>Warning, please read before using.</b>
     * This method grabs texture from Mojang API asynchronously,
     * which will result in delay. After textures will be applied,
     * NPC will reload, resetting all per-player packet changes
     * applied before this.
     * <p>
     * I recommend use {@link HumanNPC#setSkin(String, String)}
     * to apply textures and signature manually, instead of grabbing
     * it from API.
     * <p>
     * You can use <a href="https://mineskin.org/gallery">https://mineskin.org/gallery</a>
     * to grab textures and signature for skins that are permanent.
     * <p>
     * Grabbing skin from players name also means that skin is not
     * permanent and will change upon players changing it.
     *
     * @param username - Name (nick) of a minecraft account.
     */
    public void setSkin(@Nonnull String username) {
        final Player player = Bukkit.getPlayer(username);

        if (player != null) {
            try {
                final GameProfile profile = Reflect.getGameProfile(player);

                final Property textures = profile.getProperties()
                        .get("textures")
                        .stream()
                        .findFirst()
                        .orElse(new Property("null", "null"));

                final String signature = textures.signature();

                setSkin(textures.value(), signature != null ? signature : "");
                refresh();

            } catch (Exception e) {
                EternaLogger.exception(e);
            }

            return;
        }

        // Fetch from API if the player is not online
        new BukkitRunnable() {
            @Override
            public void run() {
                final JsonObject uuidJson = JsonHelper.getJson(nameToUuidRequest.formatted(username));

                if (uuidJson == null) {
                    return;
                }

                final JsonElement uuid = uuidJson.get("id");

                if (uuid == null) {
                    return;
                }

                final JsonObject profileObject = JsonHelper.getJson(uuidToProfileRequest.formatted(uuid.getAsString()));

                if (profileObject == null) {
                    return;
                }

                final JsonArray jsonArray = profileObject.get("properties").getAsJsonArray();

                if (jsonArray.isEmpty()) {
                    return;
                }

                final JsonObject textures = jsonArray.get(0).getAsJsonObject();

                final String value = textures.get("value").getAsString();
                final String signature = textures.get("signature").getAsString();

                setSkin(value, signature);
                refresh();
            }
        }.runTaskAsynchronously(EternaPlugin.getPlugin());
    }

    @Override
    public void setGhostItem(ItemSlot slot, ItemStack item, Player player) {
        sendEquipmentChange(slot, item, player);
    }

    @Override
    public void setItem(ItemSlot slot, ItemStack item) {
        equipment.setItem(slot, item);
        updateEquipment();
    }

    @Override
    public void updateEquipment() {
        if (this.equipment != null) {
            showingTo.forEach(player -> {
                sendEquipmentChange(ItemSlot.HEAD, equipment.getHelmet(), player);
                sendEquipmentChange(ItemSlot.CHEST, equipment.getChestplate(), player);
                sendEquipmentChange(ItemSlot.LEGS, equipment.getLeggings(), player);
                sendEquipmentChange(ItemSlot.FEET, equipment.getBoots(), player);
                sendEquipmentChange(ItemSlot.MAINHAND, equipment.getHand(), player);
                sendEquipmentChange(ItemSlot.OFFHAND, equipment.getOffhand(), player);
            });
        }
    }

    public NPCEquipment getEquipment() {
        return equipment;
    }

    @Override
    public void setEquipment(EntityEquipment equipment) {
        this.equipment.setEquipment(equipment);
        updateEquipment();
    }

    @Override
    public void showAll() {
        for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            this.show(onlinePlayer);
        }
    }

    @Override
    public void show(@Nonnull Player player) {
        showingTo.add(player);
        aboveHead.show(player);

        onSpawn(player);
        showVisibility(player);
    }

    @Override
    public void reloadNpcData() {
        showingTo.forEach(player -> {
            hideVisibility(player);
            showVisibility(player);
        });
    }

    @Override
    public void setDataWatcherByteValue(int key, byte value) {
        human.setDataWatcherByteValue(key, value);

        updateDataWatcher();
    }

    public <D> void setDataWatcherValue(@Nonnull DataWatcherType<D> type, int key, @Nonnull D value) {
        human.setDataWatcherValue(type, key, value);

        updateDataWatcher();
    }

    @Override
    public byte getDataWatcherByteValue(int key) {
        return human.getDataWatcherByteValue(key);
    }

    @Override
    public DataWatcher getDataWatcher() {
        return human.getDataWatcher();
    }

    @Override
    public void hideTabListName() {
        Bukkit.getScheduler().runTaskLater(EternaPlugin.getPlugin(), () -> {
            showingTo.forEach(human::hideTabName);
        }, 20L);
    }

    @Override
    public void updateSkin() {
        setDataWatcherByteValue(17, (byte) (0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40));
    }

    @Override
    public void updateSkin(SkinPart... parts) {
        setDataWatcherByteValue(17, SkinPart.mask(parts));
    }

    @Override
    public void playAnimation(NPCAnimation animation) {
        if (animation == NPCAnimation.TAKE_DAMAGE) {
            sendPacket(new ClientboundHurtAnimationPacket(human.getHuman()));
            return;
        }

        sendPacket(new PacketPlayOutAnimation(this.getHuman(), animation.getPos()));
    }

    @Override
    public void updateDataWatcher() {
        human.updateMetadata(showingTo);
    }

    @Override
    public void remove() {
        remove0();
        Eterna.getRegistry().npcRegistry.unregister(getId());
    }

    /**
     * Removes the NPC without unregistering it.
     */
    public final void remove0() {
        this.alive = false;

        if (this.sitEntity != null) {
            showingTo.forEach(player -> Reflect.destroyEntity(this.sitEntity, player));
        }

        this.hide();
        this.deleteTeam();
        this.showingTo.clear();
    }

    @Override
    public void hide() {
        showingTo.forEach(this::hide0);
        showingTo.clear();
    }

    @Override
    public void hide(@Nonnull Player player) {
        hide0(player);

        showingTo.remove(player);
    }

    public void hide0(@Nonnull Player player) {
        aboveHead.hide(player);
        human.hide(player);

        onDespawn(player);
    }

    @Override
    @InvokePolicy(Policy.ANYTIME)
    public HumanNPC setCollision(boolean flag) {
        collision = flag;
        updateCollision();
        return this;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    @Nonnull
    public EntityPlayer getHuman() {
        return human.getHuman();
    }

    @Nonnull
    public EternaPlayer getEternaPlayer() {
        return human;
    }

    @Override
    public void hideDisplayName() {
        for (Player player : getPlayers()) {
            final Team team = getTeamOrCreate(player.getScoreboard());
            team.addEntry(this.hexName);
        }
    }

    public Team getTeamOrCreate(Scoreboard scoreboard) {
        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }
        return team;
    }

    /**
     * Syncs the text above the NPC's head.
     */
    public void syncText() {
        if (this.aboveHead != null) {
            this.aboveHead.teleport(this.location.clone().add(0.0d, 1.75d, 0.0d));
        }
    }

    @Override
    public Player[] getPlayers() {
        return showingTo.toArray(new Player[0]);
    }

    protected void sendPacket(@Nonnull Packet<?> packet) {
        if (showingTo.isEmpty()) {
            return;
        }

        showingTo.forEach(player -> Reflect.sendPacket(player, packet));
    }

    @SuppressWarnings("all")
    @Nonnull
    protected Int2ObjectMap<DataWatcher.Item<?>> getInt2ObjectMap() {
        try {
            return (Int2ObjectMap<DataWatcher.Item<?>>) FieldUtils.readField(getDataWatcher(), "e", true);
        } catch (Exception e) {
            e.printStackTrace();
            return new Int2ObjectOpenHashMap();
        }
    }

    protected boolean canInteract(@Nonnull Player player) {
        final InteractionDelay delay = interactedAt.get(player);

        if (delay == null) {
            return true;
        }

        return delay.isOver();
    }

    private void refresh() {
        showingTo.forEach(player -> {
            hideVisibility(player);
            showVisibility(player);
        });
    }

    private String urlStringToString(String url) {
        StringBuilder text = new StringBuilder();
        try {
            Scanner scanner = new Scanner(new URL(url).openStream());
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                while (line.startsWith(" ")) {
                    line = line.substring(1);
                }
                text.append(line);
            }
            scanner.close();
        } catch (IOException exception) {
            EternaLogger.exception(exception);
        }
        return text.toString();
    }

    private void updateSitting() {
        if (sitEntity == null) {
            return;
        }

        sendPacket(new PacketPlayOutMount(sitEntity));
    }

    private String getPrefix() {
        return this.chatPrefix;
    }

    private String placeHold(String entry, Player player) {
        final String[] splits = entry.split(" ");
        final StringBuilder builder = new StringBuilder();
        for (String split : splits) {
            split = split.replace(Placeholders.PLAYER.get(), player.getName())
                    .replace(Placeholders.NAME.get(), this.getName())
                    .replace(Placeholders.LOCATION.get(), BukkitUtils.locationToString(this.getLocation()));
            builder.append(split);
            builder.append(" ");
        }
        return builder.toString().trim();
    }

    private void move0(double x, double y, double z) {
    }

    private void teleportY(double y) {
        final Location loc = getLocation();
        loc.setY(y);
        teleport(loc);
    }

    private void swingArm(boolean b) {
        sendPacket(new PacketPlayOutAnimation(this.human.getHuman(), b ? 0 : 3));
    }

    private void sendEquipmentChange(ItemSlot slot, ItemStack stack, Player player) {
        final List<Pair<EnumItemSlot, net.minecraft.world.item.ItemStack>> list = new ArrayList<>();
        list.add(new Pair<>(slot.getSlot(), toNMSItemStack(stack)));

        final PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(this.getId(), list);

        Reflect.sendPacket(player, packet);
    }

    private void updateCollision() {
        for (Player player : getPlayers()) {
            final Team team = getTeamOrCreate(player.getScoreboard());
            team.setOption(Team.Option.COLLISION_RULE, collision ? Team.OptionStatus.ALWAYS : Team.OptionStatus.NEVER);
            team.addEntry(this.hexName);
        }
    }

    private void deleteTeam() {
        for (Player viewer : getViewers()) {
            deleteTeam(viewer.getScoreboard());
        }
    }

    private void deleteTeam(Scoreboard scoreboard) {
        final Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            return;
        }

        team.unregister();
    }

    private net.minecraft.world.item.ItemStack toNMSItemStack(ItemStack stack) {
        return Reflect.bukkitItemToNMS(stack);
    }

    public static Human create(Location location) {
        return create(location, "", "");
    }

    public static Human create(Location location, String name) {
        return create(location, name, "");
    }

    public static Human create(Location location, String name, String skin) {
        return new HumanNPC(location, name, skin);
    }

    public static boolean isNPC(int entityId) {
        return Eterna.getRegistry().npcRegistry.isRegistered(entityId);
    }

    @Nullable
    public static HumanNPC getById(int id) {
        return Eterna.getRegistry().npcRegistry.byKey(id);
    }

    public static void hideAllNames(Scoreboard score) {
        for (final HumanNPC value : Eterna.getRegistry().npcRegistry.getRegistered().values()) {
            value.hideTabListName();
        }
    }

}
