package me.hapyl.eterna.module.reflect.npc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.annotate.EventLike;
import me.hapyl.eterna.module.annotate.TestedOn;
import me.hapyl.eterna.module.annotate.Version;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.component.Components;
import me.hapyl.eterna.module.entity.ViewDistance;
import me.hapyl.eterna.module.event.PlayerClickAtNpcEvent;
import me.hapyl.eterna.module.hologram.ComponentSupplier;
import me.hapyl.eterna.module.hologram.Hologram;
import me.hapyl.eterna.module.player.PlayerSkin;
import me.hapyl.eterna.module.player.dialog.Dialog;
import me.hapyl.eterna.module.player.dialog.DialogEntry;
import me.hapyl.eterna.module.reflect.DataWatcherType;
import me.hapyl.eterna.module.reflect.JsonHelper;
import me.hapyl.eterna.module.reflect.PacketFactory;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.reflect.team.PacketTeam;
import me.hapyl.eterna.module.util.BukkitUtils;
import me.hapyl.eterna.module.util.Placeholder;
import me.hapyl.eterna.module.util.Runnables;
import me.hapyl.eterna.module.util.Ticking;
import me.hapyl.eterna.module.component.ComponentList;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundHurtAnimationPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AreaEffectCloud;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Range;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

/**
 * Allows to create <b>simple</b> player NPCs with support of clicks.
 * <p>
 * For complex NPCs use <a href="https://github.com/CitizensDev/CitizensAPI">CitizensAPI</a>!
 */
@SuppressWarnings("unused")
@TestedOn(version = Version.V1_21_8)
public class HumanNPC implements Human, NPCListener, ViewDistance, Ticking {
    
    public static final double CHAIR_LOCATION_Y_OFFSET = 0.39d;
    public static final double CROUCH_LOCATION_Y_OFFSET = 0.3d;
    public static final double LAY_LOCATION_Y_OFFSET = 1.25d;
    
    public static final double HOLOGRAM_Y_OFFSET = 1.75d;
    
    public static final int defaultViewDistance = 48;
    
    private static final String nameToUuidRequest = "https://api.mojang.com/users/profiles/minecraft/%s";
    private static final String uuidToProfileRequest = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";
    
    protected final Map<Player, Visibility> showingTo = Maps.newHashMap();
    protected final Map<Player, InteractionDelay> interactedAt = Maps.newHashMap();
    
    protected final PacketTeam team;
    
    private final UUID uuid;
    private final String hexName;
    private final EternaPlayer human;
    private final Hologram hologram;
    
    private final NPCEquipment equipment;
    private final int id;
    
    private String defaultName;
    
    @Nonnull private NPCFormat format;
    
    private int interactionDelay;
    private double lookAtCloseDist;
    private double viewDistance;
    
    private boolean collision;
    
    @Nullable private Dialog dialog;
    @Nullable private ComponentSupplier aboveHead;
    @Nullable private ComponentSupplier belowHead;
    
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
     *                    specifically want to set a skin for a given player, otherwise, prefer {@link #setSkin(PlayerSkin)}
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
     *                    specifically want to set a skin for a given player, otherwise, prefer {@link #setSkin(PlayerSkin)}
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
        
        this.hologram = Hologram.ofArmorStand(BukkitUtils.newLocation(location).subtract(0, HOLOGRAM_Y_OFFSET, 0));
        updateHologram();
        
        this.equipment = new NPCEquipment();
        this.team = new PacketTeam("npc_" + uuid) {
            @Override
            protected void onCreate() {
                team.setNameTagVisibility(net.minecraft.world.scores.Team.Visibility.NEVER);
            }
        };
        this.id = human.getEntityId();
        
        this.defaultName = defaultName;
        this.format = NPCFormat.DEFAULT;
        
        this.interactionDelay = 10;
        this.lookAtCloseDist = 0;
        
        // Apply skin if the skinOwner provided
        if (skinOwner != null && !skinOwner.isEmpty()) {
            setSkin(skinOwner);
        }
        
        // Default visibility to 40 blocks
        viewDistance = defaultViewDistance;
        
        // Register NPC
        Eterna.getManagers().npc.register(this);
        
        // Set collision
        this.collision = true;
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
    public void setInteractionDelay(int interactionDelay) {
        this.interactionDelay = interactionDelay;
    }
    
    @Override
    public boolean isShaking() {
        return Reflect.getDataWatcherValue(human, DataWatcherType.INT, 7) != 0;
    }
    
    @Override
    public void setShaking(boolean shaking) {
        showingTo.keySet()
                 .forEach(player -> Reflect.setDataWatcherValue(
                         human,
                         DataWatcherType.INT,
                         7,
                         shaking ? Integer.MAX_VALUE : -100
                 ));
        
        updateDataWatcher();
    }
    
    @Override
    public final void updateHologram() {
        this.hologram.setLines(player -> {
            final ComponentList list = ComponentList.empty();
            
            if (aboveHead != null) {
                list.append(aboveHead.supply(player));
            }
            
            if (hasName()) {
                list.append(Components.ofLegacy(format.formatName(player, this)));
            }
            
            if (belowHead != null) {
                list.append(belowHead.supply(player));
            }
            
            return list;
        });
    }
    
    @Nonnull
    @Override
    public Player bukkitEntity() {
        return this.human.getBukkitEntity();
    }
    
    @Nonnull
    @Override
    public Hologram getHologram() {
        return hologram;
    }
    
    @Override
    public void setAboveHead(@Nullable ComponentSupplier aboveHead) {
        this.aboveHead = aboveHead;
    }
    
    @Override
    public void setBelowHead(@Nullable ComponentSupplier belowHead) {
        this.belowHead = belowHead;
    }
    
    @Nonnull
    @Override
    public Location getLocation() {
        return human.getLocation();
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
            teleport(getLocation());
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
    
    
    @Nonnull
    @Override
    public Set<Player> showingTo() {
        return Set.copyOf(showingTo.keySet());
    }
    
    @Override
    public boolean isShowingTo(@Nonnull Player player) {
        return this.showingTo.containsKey(player);
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
    public double getLookAtCloseDist() {
        return lookAtCloseDist;
    }
    
    @Override
    public void setLookAtCloseDist(double lookAtCloseDist) {
        this.lookAtCloseDist = lookAtCloseDist;
    }
    
    @Nonnull
    @Override
    public NPCPose getPose() {
        return human.getNPCPose();
    }
    
    @Override
    public void setPose(@Nonnull NPCPose pose) {
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
    }
    
    @Override
    public void lookAt(@Nonnull Location lookAt) {
        final Location location = getLocation();
        location.setDirection(lookAt.clone().subtract(location).toVector());
        
        this.setHeadRotation(location.getYaw());
        
        sendPacketToAll(PacketFactory.makePacketMoveEntityRot(human, location.getYaw(), location.getPitch()));
    }
    
    @Override
    public void teleport(double x, double y, double z, float yaw, float pitch) {
        teleport(new Location(this.human.getLocation().getWorld(), x, y, z, yaw, pitch));
    }
    
    @Override
    public void teleport(@Nonnull Location location) {
        this.human.setLocation(location);
        
        this.setHeadRotation(location.getYaw());
        
        sendPacketToAll(human.packetFactory.getPacketTeleport());
        syncText();
        
        showingTo.keySet().forEach(player -> onTeleport(player, location));
    }
    
    @Nonnull
    public World getWorld() {
        return getLocation().getWorld();
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
    public void setHeadRotation(float yaw, float pitch) {
        final Location location = getLocation();
        location.setYaw(yaw);
        location.setPitch(pitch);
        
        human.setLocation(location);
        human.setYawPitch(yaw, pitch);
        
        setHeadRotation(yaw);
        sendPacketToAll(human.packetFactory.getPacketTeleport());
    }
    
    @Override
    public void setHeadRotation(float yaw) {
        sendPacketToAll(human.packetFactory.getPacketEntityHeadRotation(yaw));
    }
    
    @Override
    public void swingMainHand() {
        swingArm(true);
    }
    
    @Override
    public void swingOffHand() {
        swingArm(false);
    }
    
    public boolean hasSkin() {
        final Collection<Property> skin = getSkin();
        return skin != null && !skin.isEmpty();
    }
    
    public Collection<Property> getSkin() {
        return this.human.getProfile().getProperties().get("textures");
    }
    
    @Override
    public void setSkin(@Nonnull PlayerSkin skin) {
        final GameProfile profile = human.getProfile();
        
        profile.getProperties().removeAll("textures");
        profile.getProperties().put("textures", new Property("textures", skin.getTexture(), skin.getSignature()));
    }
    
    /**
     * <b>Warning, please read before using.</b>
     * This method grabs texture from Mojang API asynchronously,
     * which will result in delay. After textures will be applied,
     * NPC will reload, resetting all per-player packet changes
     * applied before this.
     * <p>
     * I recommend use {@link HumanNPC#setSkin(PlayerSkin)}
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
                
                setSkin(PlayerSkin.of(textures.value(), signature != null ? signature : ""));
                reloadNpcData();
                
            }
            catch (Exception e) {
                throw EternaLogger.exception(e);
            }
            
            return;
        }
        
        // Fetch from API if the player is not online
        Runnables.runAsync(() -> {
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
            
            setSkin(PlayerSkin.of(value, signature));
            reloadNpcData();
        });
    }
    
    @Override
    public void sendItemChange(@Nonnull Player player, @Nonnull ItemSlot slot, @Nullable ItemStack item) {
        Reflect.sendPacket(player, PacketFactory.makePacketSetEquipment(human, List.of(new Pair<>(slot.getSlot(), Reflect.bukkitItemToNMS(item)))));
    }
    
    @Override
    public void setItem(@Nonnull ItemSlot slot, @Nullable ItemStack item) {
        equipment.setItem(slot, item);
        updateEquipment();
    }
    
    @Override
    public void updateEquipment() {
        if (this.equipment != null) {
            final ClientboundSetEquipmentPacket packet = PacketFactory.makePacketSetEquipment(
                    human, List.of(
                            new Pair<>(ItemSlot.HEAD.getSlot(), Reflect.bukkitItemToNMS(equipment.getHelmet())),
                            new Pair<>(ItemSlot.CHEST.getSlot(), Reflect.bukkitItemToNMS(equipment.getChestplate())),
                            new Pair<>(ItemSlot.LEGS.getSlot(), Reflect.bukkitItemToNMS(equipment.getLeggings())),
                            new Pair<>(ItemSlot.FEET.getSlot(), Reflect.bukkitItemToNMS(equipment.getBoots())),
                            new Pair<>(ItemSlot.MAINHAND.getSlot(), Reflect.bukkitItemToNMS(equipment.getHand())),
                            new Pair<>(ItemSlot.OFFHAND.getSlot(), Reflect.bukkitItemToNMS(equipment.getOffhand()))
                    )
            );
            
            showingTo.keySet().forEach(player -> Reflect.sendPacket(player, packet));
        }
    }
    
    public NPCEquipment getEquipment() {
        return equipment;
    }
    
    @Override
    public void setEquipment(@Nonnull EntityEquipment equipment) {
        this.equipment.setEquipment(equipment);
        updateEquipment();
    }
    
    @Override
    public void reloadNpcData() {
        showingTo.keySet()
                 .forEach(player -> {
                     hide0(player);
                     show0(player);
                 });
    }
    
    @Override
    public void setDataWatcherByteValue(int index, byte value) {
        human.setDataWatcherByteValue(index, value);
        
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
    
    @Nonnull
    @Override
    public SynchedEntityData getDataWatcher() {
        return human.getDataWatcher();
    }
    
    @Override
    public void updateSkin() {
        setDataWatcherByteValue(17, (byte) (0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40));
    }
    
    @Override
    public void updateSkin(@Nonnull SkinPart... parts) {
        setDataWatcherByteValue(17, SkinPart.mask(parts));
    }
    
    @Override
    public void playAnimation(@Nonnull NPCAnimation animation) {
        if (animation == NPCAnimation.TAKE_DAMAGE) {
            sendPacketToAll(new ClientboundHurtAnimationPacket(human));
            return;
        }
        
        sendPacketToAll(new ClientboundAnimatePacket(this.human, animation.getPos()));
    }
    
    @Override
    public void updateDataWatcher() {
        human.updateMetadata(showingTo.keySet());
    }
    
    @Override
    public void remove() {
        remove0();
        Eterna.getManagers().npc.unregister(getId());
    }
    
    @ApiStatus.Internal
    public final void remove0() {
        if (this.chair != null) {
            showingTo.keySet().forEach(player -> Reflect.destroyEntity(this.chair, player));
        }
        
        this.hideAll();
        this.deleteTeam();
        this.showingTo.clear();
    }
    
    @Override
    @OverridingMethodsMustInvokeSuper
    public void show(@Nonnull Player player) {
        show0(player);
        showingTo.put(player, Visibility.VISIBLE);
    }
    
    @ApiStatus.Internal
    public final void show0(@Nonnull Player player) {
        // Make holograms
        hologram.show(player);
        
        onSpawn(player);
        
        // Prepare teams
        team.create(player);
        team.entry(player, hexName);
        
        // Show the actual entity
        human.show(player);
        
        // Sync location & text
        Reflect.sendPacket(player, human.packetFactory.getPacketTeleport());
        syncText();
        
        updateEquipment();
        updateCollision();
        updateSkin();
        
        updateDataWatcher();
        updateSitting();
        
        // Hide tab name
        Runnables.runLater(() -> human.hideTabName(player), 5);
    }
    
    @Override
    public void hide(@Nonnull Player player) {
        hide0(player);
        showingTo.remove(player);
    }
    
    @ApiStatus.Internal
    public final void hide0(@Nonnull Player player) {
        human.hide(player);
        hologram.hide(player);
        
        if (chair != null) {
            Reflect.destroyEntity(chair, player);
        }
        
        onDespawn(player);
    }
    
    @Nullable
    @Override
    public Visibility visibility(@Nonnull Player player) {
        return showingTo.get(player);
    }
    
    @Override
    public void hideAll() {
        showingTo.keySet().forEach(this::hide0);
        showingTo.clear();
    }
    
    @Override
    public void setCollision(boolean flag) {
        collision = flag;
        updateCollision();
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
    
    /**
     * Syncs the text above the NPCs head.
     */
    public void syncText() {
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
        
        this.hologram.teleport(location);
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
    
    @Override
    public double viewDistance() {
        return viewDistance;
    }
    
    @Override
    public void viewDistance(@Range(from = 0, to = Long.MAX_VALUE) double viewDistance) {
        this.viewDistance = viewDistance;
    }
    
    @Override
    public boolean canBeSeenBy(@Nonnull Player player) {
        if (viewDistance() > 0) {
            final double distance = player.getLocation().distance(getLocation());
            
            return distance <= viewDistance;
        }
        
        return false;
    }
    
    @Override
    @OverridingMethodsMustInvokeSuper
    public void tick() {
        // Check for view distance
        if (hasViewDistance()) {
            showingTo.entrySet().forEach(entry -> {
                final Player player = entry.getKey();
                final Visibility visibility = entry.getValue();
                
                final boolean canBeSeen = canBeSeenBy(player);
                
                if (canBeSeen && visibility == Visibility.NOT_VISIBLE) {
                    show0(player);
                    entry.setValue(Visibility.VISIBLE);
                }
                else if (!canBeSeen && visibility == Visibility.VISIBLE) {
                    hide0(player);
                    entry.setValue(Visibility.NOT_VISIBLE);
                }
            });
        }
        
        // Look at the closest player
        if (lookAtCloseDist > 0) {
            final Location location = getLocation();
            final Player nearest = BukkitUtils.getNearestEntity(location, lookAtCloseDist, lookAtCloseDist, lookAtCloseDist, Player.class);
            
            if (nearest != null && nearest.isOnline()) {
                this.lookAt(nearest);
            }
            else {
                if (restPosition != null) {
                    final float yaw = location.getYaw();
                    final float pitch = location.getPitch();
                    
                    if (yaw == restPosition.yaw() && pitch == restPosition.pitch()) {
                        return;
                    }
                    
                    this.setHeadRotation(restPosition.yaw(), restPosition.pitch());
                }
            }
        }
    }
    
    protected void sendPacketToAll(@Nonnull Packet<?> packet) {
        if (showingTo.isEmpty()) {
            return;
        }
        
        showingTo.keySet().forEach(player -> Reflect.sendPacket(player, packet));
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
        
        showingTo.keySet().forEach(player -> Reflect.destroyEntity(chair, player));
        chair = null;
    }
    
    private String urlStringToString(String url) {
        StringBuilder text = new StringBuilder();
        try {
            Scanner scanner = new Scanner(BukkitUtils.url(url).openStream());
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                while (line.startsWith(" ")) {
                    line = line.substring(1);
                }
                text.append(line);
            }
            scanner.close();
        }
        catch (IOException exception) {
            throw EternaLogger.exception(exception);
        }
        return text.toString();
    }
    
    private void updateSitting() {
        if (chair == null) {
            return;
        }
        
        final ClientboundSetPassengersPacket packet = new ClientboundSetPassengersPacket(chair);
        
        showingTo.keySet().forEach(player -> {
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
        sendPacketToAll(new ClientboundAnimatePacket(this.human, b ? 0 : 3));
    }
    
    private void updateCollision() {
        showingTo.keySet().forEach(player -> team.option(player, Team.Option.COLLISION_RULE, collision ? Team.OptionStatus.ALWAYS : Team.OptionStatus.NEVER));
    }
    
    private void deleteTeam() {
        showingTo.keySet().forEach(team::destroy);
    }
    
    public static boolean isNPC(int entityId) {
        return Eterna.getManagers().npc.isManaging(entityId);
    }
    
    @Nullable
    public static HumanNPC getById(int id) {
        return Eterna.getManagers().npc.get(id);
    }
    
}
