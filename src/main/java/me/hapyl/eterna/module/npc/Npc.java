package me.hapyl.eterna.module.npc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import me.hapyl.eterna.module.annotate.DefensiveCopy;
import me.hapyl.eterna.module.annotate.EventLike;
import me.hapyl.eterna.module.component.ComponentList;
import me.hapyl.eterna.module.component.builder.ComponentBuilder;
import me.hapyl.eterna.module.component.builder.ComponentResolver;
import me.hapyl.eterna.module.component.builder.ComponentSupplier;
import me.hapyl.eterna.module.entity.Showable;
import me.hapyl.eterna.module.event.PlayerInteractNpcEvent;
import me.hapyl.eterna.module.hologram.Hologram;
import me.hapyl.eterna.module.location.Located;
import me.hapyl.eterna.module.location.LocationHelper;
import me.hapyl.eterna.module.math.Numbers;
import me.hapyl.eterna.module.npc.appearance.Appearance;
import me.hapyl.eterna.module.npc.appearance.AppearanceBuilder;
import me.hapyl.eterna.module.npc.tag.TagLayout;
import me.hapyl.eterna.module.npc.tag.TagPart;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.reflect.packet.PacketFactory;
import me.hapyl.eterna.module.reflect.team.PacketTeam;
import me.hapyl.eterna.module.reflect.team.PacketTeamColor;
import me.hapyl.eterna.module.util.Disposable;
import me.hapyl.eterna.module.util.Ticking;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.*;

/**
 * Represents a very customizable npc implementation.
 */
public class Npc implements Located, Showable, Disposable, Ticking {
    
    public static final double CHAIR_Y_OFFSET;
    
    private static final ComponentBuilder DEFAULT_MESSAGE_FORMAT;
    
    static {
        CHAIR_Y_OFFSET = 0.4;
        DEFAULT_MESSAGE_FORMAT = new ComponentBuilder()
                .append(ComponentSupplier.ofLiteral(Component.text("[NPC] ", NamedTextColor.YELLOW)))
                .append(ComponentSupplier.ofPlaceholder("name"))
                .append(ComponentSupplier.ofLiteral(Component.text(": ", NamedTextColor.YELLOW)))
                .append(ComponentSupplier.ofLiteral(Component.text("", NamedTextColor.WHITE)))
                .append(ComponentSupplier.ofPlaceholder("message"));
    }
    
    // Field instantiation because we must have the `playerData` before appearance build()
    protected final Map<Player, NpcPlayerData> playerData = Maps.newHashMap();
    
    private final Appearance appearance;
    private final Hologram hologram;
    private final NpcProperties properties;
    private final PacketTeam packetTeam;
    
    @NotNull private final Component defaultName;
    
    private final int entityId;
    
    @NotNull private Location location;
    @NotNull private TagLayout tagLayout;
    
    @Nullable private AreaEffectCloud chairEntity;
    
    public Npc(@NotNull @DefensiveCopy Location location, @NotNull Component defaultName, @NotNull AppearanceBuilder<? extends Appearance> builder) {
        // Build appearance
        final Appearance appearance = builder.build(this);
        
        this.appearance = appearance;
        this.entityId = appearance.getHandle().getId();
        
        this.defaultName = defaultName;
        this.location = LocationHelper.copyOf(location);
        this.tagLayout = TagLayout.DEFAULT;
        this.properties = new NpcProperties();
        
        // Create hologram
        this.hologram = Hologram.ofArmorStand(this.getHologramLocation());
        this.updateHologram();
        
        // Create team for collisions
        this.packetTeam = new PacketTeam("npc_" + UUID.randomUUID());
        
        // Register npc by the entity Id, because mojang still uses this system
        NpcHandler.handler.register(entityId, this);
    }
    
    /**
     * Gets the {@link Hologram} used by this {@link Npc}.
     *
     * @return the hologram used by this npc.
     */
    @NotNull
    public Hologram getHologram() {
        return hologram;
    }
    
    /**
     * Gets the {@link PacketTeam} used by this {@link Npc}.
     *
     * @return the packet team used by this npc.
     */
    @NotNull
    public PacketTeam getPacketTeam() {
        return packetTeam;
    }
    
    /**
     * Gets the {@link NpcPose} of this {@link Npc}.
     *
     * @return the npc pose of this npc.
     */
    @NotNull
    public NpcPose getPose() {
        return this.appearance.getPose();
    }
    
    /**
     * Sets the {@link NpcPose} for this {@link Npc}.
     *
     * @param pose - The new npc pose.
     */
    public void setPose(@NotNull NpcPose pose) {
        if (this.appearance.setPose(pose)) {
            this.syncHologram();
        }
    }
    
    /**
     * Gets whether this {@link Npc} is currently sitting.
     *
     * @return {@code true} if this npc is currently sitting; {@code false} otherwise.
     */
    public boolean isSitting() {
        return this.chairEntity != null;
    }
    
    /**
     * Sets whether this {@link Npc} should be sitting.
     * <p>Note that not all {@link Appearance} support sitting, this will do nothing if appearance cannot sit.</p>
     *
     * @param toSit - Whether the npc should sit.
     */
    public void setSitting(boolean toSit) {
        if (toSit && chairEntity == null) {
            final Location location = getLocation();
            location.subtract(0, CHAIR_Y_OFFSET, 0);
            
            this.chairEntity = new AreaEffectCloud(Reflect.getHandle(location.getWorld()), location.getX(), location.getY(), location.getZ());
            this.chairEntity.setRadius(0.0f);
            this.chairEntity.setDuration(Integer.MAX_VALUE);
            this.chairEntity.passengers = ImmutableList.of(appearance.getHandle());
            
            // Update sitting for all
            this.playerData.keySet().forEach(this::updateSitting);
        }
        else if (!toSit && chairEntity != null) {
            final ClientboundRemoveEntitiesPacket packetRemoveEntity = PacketFactory.makePacketRemoveEntity(chairEntity);
            
            // Nullate the chair, please...
            this.chairEntity = null;
            
            this.playerData.keySet().forEach(player -> Reflect.sendPacket(player, packetRemoveEntity));
            
            // Fix chair offset by forcing the location update
            this.appearance.setLocation(location);
        }
        
        this.syncHologram();
    }
    
    /**
     * Forcefully syncs the {@link Hologram} location.
     */
    public void syncHologram() {
        this.hologram.teleport(getHologramLocation());
    }
    
    /**
     * Gets the {@link Hologram} location of this {@link Npc}.
     *
     * <p>
     * The default implementation dynamically offsets the {@link Location} based on the {@link Appearance} height and whether the {@link Npc} is sitting.
     * </p>
     *
     * @return the hologram location of this npc.
     */
    @NotNull
    public Location getHologramLocation() {
        final Location location = LocationHelper.copyOf(this.location);
        
        // Offset the location by height
        location.add(0, appearance.getHeight(), 0);
        
        // If the npc is sitting, offset by chairYOffset
        if (isSitting()) {
            location.subtract(0, appearance.chairYOffset(), 0);
        }
        
        return location;
    }
    
    /**
     * Gets the {@link NpcProperties}.
     * <p><b>
     * Note that properties must be set before {@link #show(Player)} the {@link Npc}!
     * </b></p>
     *
     * @return the npc properties.
     */
    @NotNull
    public NpcProperties getProperties() {
        return properties;
    }
    
    /**
     * Gets the <i>default name</i> of this {@link Npc}.
     *
     * <p>
     * Since {@link Npc} names are per-player ({@link #getName(Player)}), we might need to have a default name for an {@link Npc} where {@link Player} isn't available.
     * </p>
     *
     * @return the default name of this npc.
     */
    @NotNull
    public String getDefaultName() {
        if (defaultName instanceof TextComponent textComponent) {
            return textComponent.content();
        }
        
        return "Invalid Name";
    }
    
    /**
     * Gets the name of this {@link Npc} for the given {@link Player}.
     *
     * <p>
     * This method is meant to be overridden for retrieve a different name for a different player.
     * </p>
     *
     * @param player - The player for whom to retrieve the name.
     * @return the name of this npc for the given player.
     */
    @NotNull
    public Component getName(@NotNull Player player) {
        return defaultName;
    }
    
    /**
     * Gets the {@link TagLayout} of this {@link Npc}.
     *
     * @return the tag layout of this npc.
     */
    @NotNull
    public TagLayout getTagLayout() {
        return tagLayout;
    }
    
    /**
     * Sets the {@link TagLayout} of this {@link Npc}.
     *
     * @param tagLayout - The new layout of this npc.
     */
    public void setTagLayout(@NotNull TagLayout tagLayout) {
        this.tagLayout = tagLayout;
        this.updateHologram();
    }
    
    /**
     * Gets the {@link Appearance} of this {@link Npc} as a base class.
     *
     * @return the {@code appearance} of this {@code npc} as a base class.
     */
    @NotNull
    public Appearance getAppearance() {
        return appearance;
    }
    
    /**
     * Gets the {@link Appearance} of this {@link Npc} cast to a given {@link Class}.
     *
     * @param appearanceClass - The class to cast to.
     * @param <T>             - The appearance type.
     * @return the {@code appearance} of this {@code npc} cast to a given {@code class}.
     * @throws IllegalArgumentException if the given class isn't the same as the npc appearance.
     */
    @NotNull
    public <T extends Appearance> T getAppearance(@NotNull Class<T> appearanceClass) {
        if (!appearanceClass.isInstance(appearance)) {
            throw new IllegalArgumentException("Appearance mismatch! Expected `%s`, got `%s`.".formatted(appearance.getClass().getSimpleName(), appearanceClass.getSimpleName()));
        }
        
        return appearanceClass.cast(appearance);
    }
    
    /**
     * Gets the {@code entityId} associated with this {@link Npc}.
     *
     * <p>The id belongs to the {@link Appearance} entity and is managed by the server.</p>
     *
     * @return the entity Id associated with this {@code npc}.
     */
    public int getEntityId() {
        return entityId;
    }
    
    /**
     * Gets a copy of the current {@link Location} of this {@link Npc}.
     *
     * @return a copy of the current {@code location} of this {@code npc}.
     */
    @NotNull
    @DefensiveCopy
    @Override
    public Location getLocation() {
        return LocationHelper.copyOf(this.location);
    }
    
    /**
     * Sets the {@link Location} of this {@link Npc}.
     * <p>
     * The location will be updated for each player for whom this {@link Npc} {@link #isShowingTo(Player)}.
     * </p>
     *
     * @param location - The new location to set.
     */
    @Override
    public void setLocation(@NotNull @DefensiveCopy Location location) {
        this.location = LocationHelper.copyOf(location);
        
        // Synchronize appearance & hologram
        this.appearance.setLocation(location);
        this.syncHologram();
        
        // Call eventlike
        final Location copyOf = LocationHelper.copyOf(location);
        this.playerData.keySet().forEach(player -> this.onTeleport(player, copyOf));
    }
    
    /**
     * Shows this {@link Npc} to the given {@link Player}.
     *
     * @param player - The player for whom this entity should be shown.
     */
    @Override
    public void show(@NotNull Player player) {
        this.show0(player);
        
        // Visibility defaults to VISIBLE so just compute it
        this.playerData(player);
        
        // Call eventlike
        this.onSpawn(player);
    }
    
    /**
     * Hides this {@link Npc} for the given player.
     *
     * @param player - The player for whom this entity should be hidden.
     */
    @Override
    public void hide(@NotNull Player player) {
        this.hide0(player);
        
        this.playerData.remove(player);
        
        // Call eventlike
        this.onDespawn(player);
    }
    
    /**
     * Gets an immutable view of all players who can see this {@link Npc}.
     *
     * @return an immutable view of all players who can see this {@link Npc}.
     */
    @NotNull
    @Override
    public Set<Player> showingTo() {
        return Set.copyOf(playerData.keySet());
    }
    
    /**
     * Gets whether this {@link Npc} is showing for a given {@link Player}.
     *
     * @param player - The player to check.
     * @return {@code true} if this npc is showing for the given player, {@code false} otherwise.
     */
    @Override
    public boolean isShowingTo(@NotNull Player player) {
        return playerData.containsKey(player);
    }
    
    /**
     * Completely destroys this {@link Npc}, removing the {@link Appearance} entity, clearing {@link Hologram}, etc.
     *
     * <p>Calling any methods after destroying the {@link Npc} is undefined and subject to not work.</p>
     */
    @Override
    public void dispose() {
        this.playerData.keySet().forEach(player -> {
            this.appearance.hide(player);
            this.packetTeam.destroy(player);
        });
        
        this.playerData.clear();
        this.hologram.dispose();
        
        // Unregister
        NpcHandler.handler.unregister(entityId);
    }
    
    /**
     * Gets the hashcode of this {@link Npc}.
     *
     * @return the hashcode of this npc.
     */
    @Override
    public final int hashCode() {
        return Objects.hashCode(this.entityId);
    }
    
    /**
     * Compares the given {@link Object} to this {@link Npc}.
     *
     * @param object - The reference object with which to compare.
     * @return {@code true} if the given object if a npc and has the same id as this npc.
     */
    @Override
    public final boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        
        final Npc that = (Npc) object;
        return this.entityId == that.entityId;
    }
    
    /**
     * An event-like method that is called whenever a {@link Player} clicks on this {@link Npc}.
     *
     * @param player    - The player who clicked at the npc.
     * @param clickType - The click type used.
     */
    @EventLike
    public void onClick(@NotNull Player player, @NotNull ClickType clickType) {
    }
    
    /**
     * An event-like method that is called whenever this {@link Npc} is shown for a {@link Player}.
     *
     * <p>This method is <b>not</b> called for when the npc is shown due to being out of visible distance.</p>
     *
     * @param player - The player for whom this npc is being shown.
     */
    @EventLike
    public void onSpawn(@NotNull Player player) {
    }
    
    /**
     * An event-like method that is called whenever this {@link Npc} is hidden for a {@link Player}.
     *
     * <p>This method is <b>not</b> called for when the npc is shown due to being out of visible distance.</p>
     *
     * @param player - The player for whom this npc is being hidden.
     */
    @EventLike
    public void onDespawn(@NotNull Player player) {
    }
    
    /**
     * An event-like method that is called whenever this {@link Npc} location is changed.
     *
     * <p>This method is called for each {@link Player} who can see the {@link Npc} upon teleportation.</p>
     *
     * @param player   - The player for whom the location has changed.
     * @param location - The new location.
     *                 <p>The location passed is a defensive copy of the actual location, but it should <b>not</b> be mutated regardless.</p>
     */
    @EventLike
    public void onTeleport(@NotNull Player player, @NotNull @DefensiveCopy Location location) {
    }
    
    /**
     * Updates the {@link Hologram} of this {@link Npc}.
     *
     * <p>This method should generally not be called by programmers, since it's automatically called on {@link #setTagLayout(TagLayout)}.</p>
     */
    public void updateHologram() {
        this.hologram.setLines(player -> {
            final ComponentList lines = ComponentList.empty();
            
            for (@NotNull TagPart part : tagLayout.parts()) {
                // We skip null components, use Component.empty() for linebreak
                final Component component = part.component(this, player);
                
                if (component != null) {
                    lines.append(component);
                }
            }
            
            return lines;
        });
    }
    
    /**
     * Ticks this {@link Npc}.
     */
    @Override
    @OverridingMethodsMustInvokeSuper
    public void tick() {
        // Check for view distance
        final double viewDistance = properties.getViewDistance();
        
        if (viewDistance > 0) {
            this.playerData.values().forEach(playerData -> {
                final double distanceToNpc = LocationHelper.distance(playerData.player.getLocation(), location);
                final boolean canSeeNpc = distanceToNpc <= viewDistance;
                
                if (canSeeNpc && playerData.visibility == Visibility.NOT_VISIBLE) {
                    show0(playerData.player);
                    playerData.visibility = Visibility.VISIBLE;
                }
                else if (!canSeeNpc && playerData.visibility == Visibility.VISIBLE) {
                    hide0(playerData.player);
                    playerData.visibility = Visibility.NOT_VISIBLE;
                }
            });
        }
        
        // Look at close player
        final double lookAtClosePlayerDistance = properties.getLookAtClosePlayerDistance();
        
        if (lookAtClosePlayerDistance > 0) {
            final Player closestPlayer = playerData.values()
                                                   .stream()
                                                   .filter(data -> data.visibility == Visibility.VISIBLE)
                                                   .map(data -> data.player)
                                                   .min(Comparator.comparingDouble(entity -> entity.getLocation().distanceSquared(location)))
                                                   .orElse(null);
            
            // Look at the closest player
            if (closestPlayer != null) {
                lookAt(closestPlayer, LookAnchor.FEET);
            }
            else {
                // Check for rest position
                final RestHeadPosition restPosition = properties.getRestPosition();
                
                if (restPosition != null) {
                    this.setHeadRotation(restPosition.yaw(), restPosition.pitch());
                }
            }
        }
    }
    
    /**
     * Makes this {@link Npc} look at the given {@link Location}.
     *
     * @param lookAt - The desired location for npc to look at.
     */
    public void lookAt(@NotNull @DefensiveCopy Location lookAt) {
        // Calculate yaw & pitch
        final Location location = getLocation();
        location.setDirection(LocationHelper.copyOf(lookAt).subtract(location).toVector());
        
        // Update position via packets
        setHeadRotation(location.getYaw(), location.getPitch());
    }
    
    /**
     * Makes this {@link Npc} look at the given {@link LivingEntity}.
     *
     * @param entity - The entity at which to look.
     * @param anchor - The look anchor.
     */
    public void lookAt(@NotNull LivingEntity entity, @NotNull LookAnchor anchor) {
        this.lookAt(anchor.apply(entity));
    }
    
    /**
     * Sets the head rotation of this {@link Npc}.
     *
     * <p>This is a convenience method that does not trigger any location updates other than the head.</p>
     *
     * @param yaw   - The desired yaw.
     * @param pitch - The desired pitch.
     */
    public void setHeadRotation(float yaw, float pitch) {
        final Entity handle = appearance.getHandle();
        
        // If yaw and pitch already set, we simply ignore it, which technically can be considered
        // a bug, but optimization is better
        if (Numbers.floatEquals(location.getYaw(), yaw) && Numbers.floatEquals(location.getPitch(), pitch)) {
            return;
        }
        
        this.location.setYaw(yaw);
        this.location.setPitch(pitch);
        
        sendPacketToAll(PacketFactory.makePacketRotateHead(handle, yaw));
        sendPacketToAll(PacketFactory.makePacketMoveEntityRot(handle, yaw, pitch));
    }
    
    /**
     * Sends the given {@link Component} message to the given {@link Player}, formatted according to {@link #getNpcMessageFormat()}.
     *
     * <p>The message component supports custom {@link NpcPlaceholder}.</p>
     *
     * @param player  - The player for whom to send the message.
     * @param message - The message to send.
     * @see NpcPlaceholder
     */
    public void sendMessage(@NotNull Player player, @NotNull Component message) {
        // Apply static placeholders
        message = NpcPlaceholder.doPlacehold(message, this, player);
        
        final Component component = getNpcMessageFormat().build(
                ComponentResolver.builder()
                                 .resolve("name", getName(player))
                                 .resolve("message", message)
        );
        
        player.sendMessage(component);
    }
    
    /**
     * Gets the {@link ComponentBuilder} for this {@link Npc}, which defines how the npc messages should be formatted.
     *
     * @return the npc message format.
     */
    @NotNull
    public ComponentBuilder getNpcMessageFormat() {
        return DEFAULT_MESSAGE_FORMAT;
    }
    
    /**
     * Plays the given {@link NpcAnimation}.
     *
     * @param animation - The animation to play.
     */
    public void playAnimation(@NotNull NpcAnimation animation) {
        this.sendPacketToAll(animation.makePacket(appearance.getHandle()));
    }
    
    /**
     * Sets whether this {@link Npc} is shaking.
     *
     * @param shaking - {@code true} to make the npc shake; {@code false} otherwise.
     */
    public void setShaking(boolean shaking) {
        this.appearance.setShaking(shaking);
    }
    
    /**
     * Gets the {@link NpcPlayerData} for the given {@link Player}.
     *
     * @param player - The player for whom to retrieve the data.
     * @return the existing or newly computed data.
     */
    @NotNull
    protected NpcPlayerData playerData(@NotNull Player player) {
        return playerData.computeIfAbsent(player, NpcPlayerData::new);
    }
    
    /**
     * Shows this {@link Npc} for the given {@link Player} <b>without</b> calling any event-like methods or computing data.
     *
     * @param player - The player for whom to show the npc.
     */
    @OverridingMethodsMustInvokeSuper
    protected void show0(@NotNull Player player) {
        // Prepare team
        this.packetTeam.create(player);
        this.packetTeam.entry(player, appearance.getScoreboardEntry());
        this.packetTeam.color(player, PacketTeamColor.DARK_PURPLE);
        
        this.packetTeam.option(player, Team.Option.COLLISION_RULE, properties.isCollidable() ? Team.OptionStatus.ALWAYS : Team.OptionStatus.NEVER);
        
        this.appearance.show(player, location);
        
        // Create chair entity
        if (chairEntity != null) {
            updateSitting(player);
        }
        
        // Create hologram
        this.hologram.show(player);
    }
    
    /**
     * Hides this {@link Npc} for the given {@link Player} <b>without</b> calling any event-like methods.
     *
     * @param player - The player for whom to hide the npc.
     */
    @OverridingMethodsMustInvokeSuper
    protected void hide0(@NotNull Player player) {
        this.appearance.hide(player);
        this.hologram.hide(player);
        
        // Delete chair
        if (this.chairEntity != null) {
            Reflect.sendPacket(player, PacketFactory.makePacketRemoveEntity(chairEntity));
        }
    }
    
    @ApiStatus.Internal
    private void updateSitting(@NotNull Player player) {
        // Explicit chair check
        if (chairEntity == null) {
            return;
        }
        
        // Create the chair entity to the player
        Reflect.sendPacket(player, PacketFactory.makePacketAddEntity(chairEntity));
        Reflect.sendPacket(player, PacketFactory.makePacketSetEntityData(chairEntity));
        Reflect.sendPacket(player, PacketFactory.makePacketTeleportEntity(chairEntity));
        Reflect.sendPacket(player, PacketFactory.makePacketSetPassengers(chairEntity));
    }
    
    @ApiStatus.Internal
    final void sendPacketToAll(@NotNull Packet<?> packet) {
        playerData.values().forEach(playerData -> {
            if (playerData.visibility == Visibility.VISIBLE) {
                Reflect.sendPacket(playerData.player, packet);
            }
        });
    }
    
    @ApiStatus.Internal
    final void onClick0(@NotNull Player player, @NotNull ClickType clickType) {
        // Check for interaction cooldown
        final NpcPlayerData playerData = playerData(player);
        final long interactionDelayMillis = properties.getInteractionDelay() * 50L;
        
        if (interactionDelayMillis > 0L) {
            final long timePassedSinceLastInteraction = System.currentTimeMillis() - playerData.lastInteraction;
            
            if (timePassedSinceLastInteraction < interactionDelayMillis) {
                return;
            }
        }
        
        // Call event
        final PlayerInteractNpcEvent event = new PlayerInteractNpcEvent(player, this, clickType);
        event.callEvent();
        
        final PlayerInteractNpcEvent.ClickResponse response = event.getResponse();
        
        if (response == PlayerInteractNpcEvent.ClickResponse.DENY) {
            return;
        }
        
        // Start the cooldown either way, can only be `HOLD` or `OK` which both start the cooldown
        playerData.lastInteraction = System.currentTimeMillis();
        
        // Only pass the onClick() is response is OK
        if (response == PlayerInteractNpcEvent.ClickResponse.OK) {
            onClick(player, clickType);
        }
    }
    
}
