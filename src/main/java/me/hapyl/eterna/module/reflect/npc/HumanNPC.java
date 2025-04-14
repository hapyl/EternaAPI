package me.hapyl.eterna.module.reflect.npc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.annotate.EventLike;
import me.hapyl.eterna.module.annotate.TestedOn;
import me.hapyl.eterna.module.annotate.Version;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.entity.LimitedVisibility;
import me.hapyl.eterna.module.event.PlayerClickAtNpcEvent;
import me.hapyl.eterna.module.hologram.HologramFunction;
import me.hapyl.eterna.module.hologram.PlayerHologram;
import me.hapyl.eterna.module.hologram.StringArray;
import me.hapyl.eterna.module.player.PlayerSkin;
import me.hapyl.eterna.module.player.dialog.Dialog;
import me.hapyl.eterna.module.player.dialog.DialogEntry;
import me.hapyl.eterna.module.reflect.DataWatcherType;
import me.hapyl.eterna.module.reflect.JsonHelper;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.util.BukkitUtils;
import me.hapyl.eterna.module.util.Placeholder;
import me.hapyl.eterna.module.util.TeamHelper;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EquipmentSlot;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Range;

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
@TestedOn(version = Version.V1_21_5)
public class HumanNPC extends LimitedVisibility implements Human, NPCListener {

    public static final double CHAIR_LOCATION_Y_OFFSET = 0.39d;
    public static final double CROUCH_LOCATION_Y_OFFSET = 0.3d;
    public static final double LAY_LOCATION_Y_OFFSET = 1.25d;

    public static final double HOLOGRAM_Y_OFFSET = 1.75d;

    private static final String nameToUuidRequest = "https://api.mojang.com/users/profiles/minecraft/%s";
    private static final String uuidToProfileRequest = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";

    protected final Set<Player> showingTo = Sets.newHashSet();
    protected final Map<Player, InteractionDelay> interactedAt = Maps.newHashMap();

    private final UUID uuid;
    private final String hexName;
    private final EternaPlayer human;
    private final PlayerHologram hologram;

    private final String teamName;
    private final NPCEquipment equipment;
    private final int id;

    private String defaultName;
    private Location location;

    @Nonnull private NPCFormat format;

    private int interactionDelay;
    private int lookAtCloseDist;

    private boolean collision;
    private boolean alive;

    @Nullable private Dialog dialog;
    @Nullable private HologramFunction aboveHead;
    @Nullable private HologramFunction belowHead;

    private BukkitTask moveTask;
    private AreaEffectCloud chair;
    private RestPosition restPosition;

    /**
     * Creates a new {@link HumanNPC} at the given {@link Location} with the given default name.
     *
     * @param location    - Location to create the {@link HumanNPC} at.
     * @param defaultName - The default name of the npc.
     *                    NPCs support per-player names: {@link #getName(Player)}
     */
    public HumanNPC(@Nonnull Location location, @Nullable String defaultName) {
        this(location, defaultName, defaultName);
    }

    /**
     * Creates a new {@link HumanNPC} at the given {@link Location} with the given default name
     * and applies a skin from the given skin owner name.
     *
     * @param location    - {@link Location} to create the {@link HumanNPC} at.
     * @param defaultName - The default name of the npc.
     *                    NPCs support per-player names: {@link #getName(Player)}
     * @param skinOwner   - The skin owner.
     *                    I strongly recommend against setting a skin using the owner, unless you
     *                    specifically want to set a skin for a given player, otherwise, prefer {@link #setSkin(String, String)}
     */
    public HumanNPC(@Nonnull Location location, @Range(from = 0, to = 32) @Nullable String defaultName, @Nullable String skinOwner) {
        this(location, defaultName, skinOwner, uuid -> ("§8[NPC] " + uuid.toString().replace("-", "")).substring(0, 16));
    }

    /**
     * Creates a new {@link HumanNPC} at the given {@link Location} with the given default name
     * and applies a skin from the given skin owner name.
     *
     * @param location    - {@link Location} to create the {@link HumanNPC} at.
     * @param defaultName - The default name of the npc.
     *                    NPCs support per-player names: {@link #getName(Player)}
     * @param skinOwner   - The skin owner.
     *                    I strongly recommend against setting a skin using the owner, unless you
     *                    specifically want to set a skin for a given player, otherwise, prefer {@link #setSkin(String, String)}
     * @param hexNameFn   - A {@link Function} on how to create a hex name for the {@link HumanNPC}.
     *                    Hex name is the actual entity name, used for team collision and name tag visibility.
     */
    protected HumanNPC(@Nonnull Location location, @Nullable String defaultName, @Nullable String skinOwner, @Nonnull Function<UUID, String> hexNameFn) {
        if (defaultName == null) {
            defaultName = "";
        }

        this.uuid = UUID.randomUUID();
        this.hexName = hexNameFn.apply(uuid);

        this.human = new EternaPlayer(location, hexName);
        this.human.setLocation(location);

        this.hologram = new PlayerHologram(BukkitUtils.newLocation(location).subtract(0, HOLOGRAM_Y_OFFSET, 0));
        updateHologram();

        this.equipment = new NPCEquipment();
        this.teamName = "%s.npc.%s".formatted(TeamHelper.PARENT, uuid.toString());
        this.id = human.getEntityId();

        this.defaultName = defaultName;
        this.location = BukkitUtils.newLocation(location);

        this.format = NPCFormat.DEFAULT;

        this.interactionDelay = 10;
        this.lookAtCloseDist = 0;

        // Apply skin if the skinOwner provided
        if (skinOwner != null && !skinOwner.isEmpty()) {
            setSkin(skinOwner);
        }

        // Default visibility to 40 blocks
        setVisibility(40);

        // Register NPC
        Eterna.getManagers().npc.register(this);

        // Set collision and mark as 'alive'
        this.collision = true;
        this.alive = true;
    }

    @Nonnull
    @Override
    public NPCFormat getFormat() {
        return format;
    }

    @Override
    public void setFormat(@Nonnull NPCFormat format) {
        this.format = format;
    }

    @Override
    public int getInteractionDelay() {
        return interactionDelay;
    }

    @Override
    public HumanNPC setInteractionDelay(int interactionDelay) {
        this.interactionDelay = interactionDelay;
        return this;
    }

    @Override
    public boolean isShaking() {
        return Reflect.getDataWatcherValue(human, DataWatcherType.INT, 7) != 0;
    }

    @Override
    public void setShaking(boolean shaking) {
        showingTo.forEach(player -> Reflect.setDataWatcherValue(
                human,
                DataWatcherType.INT,
                7,
                shaking ? Integer.MAX_VALUE : -100
        ));

        updateDataWatcher();
    }

    @Override
    public void setShoulderEntity(Shoulder shoulder, boolean status) throws NotImplementedException {
        throw new NotImplementedException("setShoulderEntity not yet implemented");
    }

    @Nonnull
    @Override
    public PlayerHologram getHologram() {
        return hologram;
    }

    @Override
    public void setAboveHead(@Nullable HologramFunction function) {
        this.aboveHead = function;
        updateHologram();
    }

    @Override
    public void setBelowHead(@Nullable HologramFunction function) {
        this.belowHead = function;
        updateHologram();
    }

    @Override
    public final void updateHologram() {
        this.hologram.setLines(player -> {
            final StringArray array = StringArray.empty();

            if (aboveHead != null) {
                array.append(aboveHead.apply(player));
            }

            if (hasName()) {
                array.append(format.formatName(player, this));
            }

            if (belowHead != null) {
                array.append(belowHead.apply(player));
            }

            return array;
        });
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
        return chair != null;
    }

    @Override
    public void setSitting(boolean toSit) {
        final boolean isNpcSitting = isSitting();

        // Sit
        if (toSit && !isNpcSitting) {
            if (chair == null) {
                final World world = getWorld();
                final Location location = getLocation();

                // Offset location to make the NPC sit on the block it would stand on
                location.subtract(0.0d, CHAIR_LOCATION_Y_OFFSET, 0.0d);

                chair = new AreaEffectCloud(Reflect.getMinecraftWorld(world), location.getX(), location.getY(), location.getZ());
                chair.absSnapTo(location.getX(), location.getY(), location.getZ());
                chair.setRadius(0.0f);
                chair.setDuration(Integer.MAX_VALUE);

                chair.passengers = ImmutableList.of(human);
            }

            updateSitting();
        }
        // Stand up
        else if (!toSit && isNpcSitting) {
            removeChair();

            // Fix chair offset
            setLocation(getLocation());
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
        updateSitting();
    }

    @Override
    public boolean isShowingTo(Player player) {
        return this.showingTo.contains(player);
    }

    @Nonnull
    public String getHexName() {
        return hexName;
    }

    @Nonnull
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Sends NPC message to the given {@link Player}.
     * <p>The message is sent as the NPC would talk to the player.</p>
     * <p>The message is formatted using {@link #getFormat()} and {@link Placeholder#format(String, Object...)}</p>
     *
     * @param player  - Player to send the message to.
     * @param message - Message to send.
     */
    public void sendNpcMessage(@Nonnull Player player, @Nonnull String message) {
        Chat.sendMessage(player, Placeholder.format(format.formatText(player, this, message), player, this));
    }

    @EventLike
    @Override
    public void onClick(@Nonnull Player player, @Nonnull ClickType type) {
    }

    public final void onClick0(@Nonnull Player player, @Nonnull ClickType clickType) {
        // Check if the player can interact
        if (!canInteract(player)) {
            return;
        }

        // Call event
        final PlayerClickAtNpcEvent event = new PlayerClickAtNpcEvent(player, this, clickType);
        event.callEvent();

        final PlayerClickAtNpcEvent.ClickResponse response = event.getResponse();

        if (response == PlayerClickAtNpcEvent.ClickResponse.CANCEL) {
            return;
        }
        else if (response == PlayerClickAtNpcEvent.ClickResponse.HOLD) {
            startInteractionCooldown(player);
            return;
        }

        if (dialog != null) {
            dialog.start(player);
        }

        onClick(player, clickType);
        startInteractionCooldown(player);
    }

    @Nullable
    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(@Nullable Dialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public boolean exists() {
        return Eterna.getManagers().npc.isManaging(getId());
    }

    @Override
    @Nonnull
    @Deprecated
    public String getName() {
        return defaultName;
    }

    @Override
    @Deprecated
    public void setName(@Nullable String newName) {
        defaultName = newName == null ? "" : newName;
        updateHologram();
    }

    @Nonnull
    public String getName(@Nonnull Player player) {
        return this.defaultName;
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
    public NPCPose getPose() {
        return human.getNPCPose();
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

        syncText();
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

        sendPacket(new ClientboundMoveEntityPacket.Rot(
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

    public void setSkin(@Nonnull PlayerSkin skin) {
        setSkin(skin.getTexture(), skin.getSignature());
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
        hologram.create(player);

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
    public SynchedEntityData getDataWatcher() {
        return human.getDataWatcher();
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
            sendPacket(new ClientboundHurtAnimationPacket(human));
            return;
        }

        sendPacket(new ClientboundAnimatePacket(this.human, animation.getPos()));
    }

    @Override
    public void updateDataWatcher() {
        human.updateMetadata(showingTo);
    }

    @Override
    public void remove() {
        remove0();
        Eterna.getManagers().npc.unregister(getId());
    }

    /**
     * Removes the NPC without unregistering it.
     */
    public final void remove0() {
        this.alive = false;

        if (this.chair != null) {
            showingTo.forEach(player -> Reflect.destroyEntity(this.chair, player));
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
        human.hide(player);
        hologram.destroy(player);

        if (chair != null) {
            Reflect.destroyEntity(chair, player);
        }

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
    public EternaPlayer getHuman() {
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
     * Syncs the text above the NPCs head.
     */
    public void syncText() {
        if (this.hologram == null) {
            return;
        }

        final Location location = getLocation();

        location.add(0.0d, hologramYOffset(), 0.0d);

        if (isDynamicNameTag()) {
            if (isSitting()) {
                location.subtract(0.0d, CHAIR_LOCATION_Y_OFFSET, 0.0d);
            }
            else {
                switch (getPose()) {
                    case CROUCHING -> location.subtract(0, CROUCH_LOCATION_Y_OFFSET, 0);
                    case SWIMMING, SLEEPING, FALL_FLYING -> location.subtract(0, LAY_LOCATION_Y_OFFSET, 0);
                }
            }
        }

        this.hologram.move(location);
    }

    /**
     * Get the {@code Y} offset of the hologram above NPCs head.
     *
     * @return the {@code Y} offset of the hologram above NPCs head.
     */
    public double hologramYOffset() {
        return HOLOGRAM_Y_OFFSET;
    }

    @Override
    public boolean isDynamicNameTag() {
        return true;
    }

    @Override
    @Nonnull
    public Player[] getPlayers() {
        return showingTo.toArray(new Player[0]);
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        final HumanNPC other = (HumanNPC) object;
        return Objects.equals(uuid, other.uuid);
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(uuid);
    }

    @Nonnull
    public final DialogEntry[] dialogEntry(@Nonnull String... entries) {
        return DialogEntry.of(this, entries);
    }

    protected void sendPacket(@Nonnull Packet<?> packet) {
        if (showingTo.isEmpty()) {
            return;
        }

        showingTo.forEach(player -> Reflect.sendPacket(player, packet));
    }

    protected boolean canInteract(@Nonnull Player player) {
        final InteractionDelay delay = interactedAt.get(player);

        if (delay == null) {
            return true;
        }

        return delay.isOver();
    }

    private void startInteractionCooldown(Player player) {
        interactedAt.put(player, new InteractionDelay(player, interactionDelay * 50L));
    }

    private void removeChair() {
        if (chair == null) {
            return;
        }

        showingTo.forEach(player -> Reflect.destroyEntity(chair, player));
        chair = null;
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
        if (chair == null) {
            return;
        }

        final ClientboundSetPassengersPacket packet = new ClientboundSetPassengersPacket(chair);

        showingTo.forEach(player -> {
            // Remove old chair even if it didn't exist
            Reflect.destroyEntity(chair, player);

            // Create chair entity
            Reflect.createEntity(chair, player);
            Reflect.updateMetadata(chair, player);
            Reflect.updateEntityLocation(chair, player);

            // Send passengers packet
            Reflect.sendPacket(player, packet);
        });

        // Force update name tag
        syncText();
    }

    private void move0(double x, double y, double z) {
    }

    private void teleportY(double y) {
        final Location loc = getLocation();
        loc.setY(y);
        teleport(loc);
    }

    private void swingArm(boolean b) {
        sendPacket(new ClientboundAnimatePacket(this.human, b ? 0 : 3));
    }

    private void sendEquipmentChange(ItemSlot slot, ItemStack stack, Player player) {
        final List<Pair<EquipmentSlot, net.minecraft.world.item.ItemStack>> list = new ArrayList<>();
        list.add(new Pair<>(slot.getSlot(), toNMSItemStack(stack)));

        final ClientboundSetEquipmentPacket packet = new ClientboundSetEquipmentPacket(this.getId(), list);

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
        return Eterna.getManagers().npc.isManaging(entityId);
    }

    @Nullable
    public static HumanNPC getById(int id) {
        return Eterna.getManagers().npc.get(id);
    }

}
