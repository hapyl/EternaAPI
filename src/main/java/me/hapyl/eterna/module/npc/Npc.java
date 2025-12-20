package me.hapyl.eterna.module.npc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.module.annotate.DefensiveCopy;
import me.hapyl.eterna.module.annotate.EventLike;
import me.hapyl.eterna.module.annotate.Immutable;
import me.hapyl.eterna.module.component.ComponentList;
import me.hapyl.eterna.module.entity.Showable;
import me.hapyl.eterna.module.event.PlayerClickAtNpcEvent;
import me.hapyl.eterna.module.hologram.Hologram;
import me.hapyl.eterna.module.locaiton.Located;
import me.hapyl.eterna.module.math.Numbers;
import me.hapyl.eterna.module.npc.appearance.Appearance;
import me.hapyl.eterna.module.npc.appearance.AppearanceBuilder;
import me.hapyl.eterna.module.npc.tag.TagLayout;
import me.hapyl.eterna.module.npc.tag.TagPart;
import me.hapyl.eterna.module.reflect.PacketFactory;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.reflect.team.PacketTeam;
import me.hapyl.eterna.module.util.BukkitUtils;
import me.hapyl.eterna.module.util.Destroyable;
import me.hapyl.eterna.module.util.NpcPlaceholder;
import me.hapyl.eterna.module.util.Ticking;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.*;

/**
 * Represents a very customizable npc implementation.
 */
public class Npc implements Located, Showable, Destroyable, Ticking {
    
    public static final double CHAIR_Y_OFFSET = 0.4;
    
    // Field instantiation because we must have the `playerData` before appearance build()
    protected final Map<Player, NpcPlayerData> playerData = Maps.newHashMap();
    
    private final Appearance appearance;
    private final Hologram hologram;
    private final NpcProperties properties;
    private final PacketTeam packetTeam;
    
    @Nonnull private final Component defaultName;
    
    private final int entityId;
    
    @Nonnull private Location location;
    @Nonnull private TagLayout tagLayout;
    
    @Nullable private AreaEffectCloud chairEntity;
    
    public Npc(@Nonnull @DefensiveCopy Location location, @Nonnull Component defaultName, @Nonnull AppearanceBuilder<? extends Appearance> builder) {
        // Build appearance
        final Appearance appearance = builder.build(this);
        
        this.appearance = appearance;
        this.entityId = appearance.getHandle().getId();
        
        this.defaultName = defaultName;
        this.location = BukkitUtils.newLocation(location);
        this.tagLayout = TagLayout.DEFAULT;
        this.properties = new NpcProperties();
        
        // Create hologram
        this.hologram = Hologram.ofArmorStand(this.getHologramLocation());
        this.updateHologram();
        
        // Create team for collisions
        this.packetTeam = new PacketTeam("npc_" + UUID.randomUUID());
        
        // Register npc by the entity Id, because mojang still uses this system
        Eterna.getManagers().npc.register(entityId, this);
    }
    
    @Nonnull
    public Hologram getHologram() {
        return hologram;
    }
    
    @Nonnull
    public PacketTeam getPacketTeam() {
        return packetTeam;
    }
    
    @Nonnull
    public NpcPose getPose() {
        return this.appearance.getPose();
    }
    
    public void setPose(@Nonnull NpcPose pose) {
        this.appearance.setPose(pose);
        this.syncHologram();
    }
    
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
    
    public void syncHologram() {
        this.hologram.teleport(getHologramLocation());
    }
    
    @Nonnull
    public Location getHologramLocation() {
        final Location location = BukkitUtils.newLocation(this.location);
        
        // Offset the location by height
        location.add(0, appearance.getHeight(), 0);
        
        // If the npc is sitting, offset by chairYOffset
        if (isSitting()) {
            location.subtract(0, appearance.chairYOffset(), 0);
        }
        
        // Offset by pose
        final Double poseYOffset = this.appearance.poseYOffset().get(getPose());
        
        if (poseYOffset != null) {
            location.subtract(0, poseYOffset, 0);
        }
        
        return location;
    }
    
    @Nonnull
    public NpcProperties getProperties() {
        return properties;
    }
    
    @Nonnull
    public String getDefaultName() {
        if (defaultName instanceof TextComponent textComponent) {
            return textComponent.content();
        }
        
        return "Invalid Name";
    }
    
    @Nonnull
    public Component getName(@Nonnull Player player) {
        return defaultName;
    }
    
    @Nonnull
    public TagLayout getTagLayout() {
        return tagLayout;
    }
    
    public void setTagLayout(@Nonnull TagLayout tagLayout) {
        this.tagLayout = tagLayout;
        this.updateHologram();
    }
    
    /**
     * Gets the {@link Appearance} of this {@link Npc} as a base class.
     *
     * @return the {@link Appearance} of this {@link Npc} as a base class.
     */
    @Nonnull
    public Appearance getAppearance() {
        return appearance;
    }
    
    /**
     * Gets the {@link Appearance} of this {@link Npc} cast to a given {@code class}.
     *
     * @param appearanceClass - The class to cast to.
     * @param <T>             - The appearance type.
     * @return the {@link Appearance} of this {@link Npc} cast to a given {@code class}.
     * @throws InputMismatchException if the given class isn't the same as the npc appearance.
     */
    @Nonnull
    public <T extends Appearance> T getAppearance(@Nonnull Class<T> appearanceClass) {
        if (!appearanceClass.isInstance(appearance)) {
            throw new InputMismatchException("Appearance mismatch! Expected \"%s\", got \"%s\".".formatted(appearance.getClass().getSimpleName(), appearanceClass.getSimpleName()));
        }
        
        return appearanceClass.cast(appearance);
    }
    
    /**
     * Gets the entity Id associated with this {@link Npc}.
     * <p>
     * The Id belongs to the {@link Appearance} entity and manager by the server.
     * </p>
     *
     * @return the entity Id associated with this {@link Npc}.
     */
    public int getEntityId() {
        return entityId;
    }
    
    /**
     * Gets the current {@link Location} of this {@link Npc}.
     *
     * @return the current {@link Location} of this {@link Npc}.
     */
    @Nonnull
    @DefensiveCopy
    @Override
    public Location getLocation() {
        return BukkitUtils.newLocation(location);
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
    public void setLocation(@Nonnull @DefensiveCopy Location location) {
        this.location = BukkitUtils.newLocation(location);
        
        // Synchronize appearance & hologram
        this.appearance.setLocation(location);
        this.syncHologram();
        
        // Call eventlike
        this.playerData.keySet().forEach(player -> this.onTeleport(player, location));
    }
    
    /**
     * Shows this {@link Npc} to the given {@link Player}.
     *
     * @param player - The player for whom this entity should be shown.
     */
    @Override
    public void show(@Nonnull Player player) {
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
    public void hide(@Nonnull Player player) {
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
    @Nonnull
    @Override
    public Set<Player> showingTo() {
        return Set.copyOf(playerData.keySet());
    }
    
    /**
     * Gets whether this {@link Npc} is showing for a given player.
     *
     * @param player - The player to check.
     * @return {@code true} if this {@link Npc} is showing for the given player, {@code false} otherwise.
     */
    @Override
    public boolean isShowingTo(@Nonnull Player player) {
        return playerData.containsKey(player);
    }
    
    /**
     * Completely destroys this {@link Npc}, removing the {@link Appearance} entity, clearing {@link Hologram}, etc.
     * <p>
     * Calling any methods after destroying the {@link Npc} is undefined and subject to not work.
     * </p>
     */
    @Override
    public void destroy() {
        this.playerData.keySet().forEach(player -> {
            this.appearance.hide(player);
            this.packetTeam.destroy(player);
        });
        this.playerData.clear();
        
        this.hologram.destroy();
        
        // Unregister
        Eterna.getManagers().npc.unregister(appearance.getHandle().getId());
    }
    
    @Override
    public final boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        
        final Npc that = (Npc) object;
        return this.entityId == that.entityId;
    }
    
    @Override
    public final int hashCode() {
        return Objects.hashCode(this.entityId);
    }
    
    @EventLike
    public void onClick(@Nonnull Player player, @Nonnull ClickType clickType) {
    }
    
    @EventLike
    public void onSpawn(@Nonnull Player player) {
    }
    
    @EventLike
    public void onDespawn(@Nonnull Player player) {
    }
    
    @EventLike
    public void onTeleport(@Nonnull Player player, @Nonnull @Immutable Location location) {
    }
    
    public void updateHologram() {
        this.hologram.setLines(player -> {
            final ComponentList lines = ComponentList.empty();
            
            for (@Nonnull TagPart part : tagLayout.parts()) {
                // We skip null components, use Component.empty() for linebreak
                final Component component = part.component(this, player);
                
                if (component != null) {
                    lines.append(component);
                }
            }
            
            return lines;
        });
    }
    
    @Override
    @OverridingMethodsMustInvokeSuper
    public void tick() {
        // Check for view distance
        final double viewDistance = properties.getViewDistance();
        
        if (viewDistance > 0) {
            this.playerData.values().forEach(playerData -> {
                final double distanceToNpc = playerData.player.getLocation().distance(location);
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
            final Player closestPlayer = BukkitUtils.getNearestEntity(location, lookAtClosePlayerDistance, lookAtClosePlayerDistance, lookAtClosePlayerDistance, Player.class);
            
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
    
    public void lookAt(@Nonnull @DefensiveCopy Location lookAt) {
        // Calculate yaw & pitch
        final Location location = getLocation();
        location.setDirection(lookAt.clone().subtract(location).toVector());
        
        // Update position via packets
        setHeadRotation(location.getYaw(), location.getPitch());
    }
    
    public void lookAt(@Nonnull LivingEntity entity, @Nonnull LookAnchor anchor) {
        this.lookAt(anchor.apply(entity));
    }
    
    public void setHeadRotation(float yaw, float pitch) {
        final Entity handle = appearance.getHandle();
        
        // If yaw and pitch already set, we simply ignore it, which technically can be considered
        // a bug, but optimization is better
        if (Numbers.floatEquals(location.getYaw(), yaw) && Numbers.floatEquals(location.getPitch(), pitch)) {
            return;
        }
        
        this.location.setYaw(yaw);
        this.location.setPitch(pitch);
        
        sendPackets(
                PacketFactory.makePacketRotateHead(handle, yaw),
                PacketFactory.makePacketMoveEntityRot(handle, yaw, pitch)
        );
    }
    
    public void sendMessage(@Nonnull Player player, @Nonnull Component message) {
        // Apply placeholders
        message = NpcPlaceholder.placehold(message, this, player);
        
        player.sendMessage(
                Component.text("[NPC] ", NamedTextColor.YELLOW)
                         .append(getName(player))
                         .append(Component.text(": ", NamedTextColor.YELLOW))
                         .append(message)
        );
    }
    
    public void playAnimation(@Nonnull NpcAnimation animation) {
        this.sendPackets(animation.makePacket(appearance.getHandle()));
    }
    
    @Nonnull
    protected NpcPlayerData playerData(@Nonnull Player player) {
        return playerData.computeIfAbsent(player, NpcPlayerData::new);
    }
    
    @OverridingMethodsMustInvokeSuper
    protected void show0(@Nonnull Player player) {
        // Prepare team
        this.packetTeam.create(player);
        this.packetTeam.entry(player, appearance.getScoreboardEntry());
        this.packetTeam.color(player, ChatColor.DARK_PURPLE);
        
        this.packetTeam.option(player, Team.Option.COLLISION_RULE, properties.isCollidable() ? Team.OptionStatus.ALWAYS : Team.OptionStatus.NEVER);
        
        this.appearance.show(player, location);
        
        // Create chair entity
        if (chairEntity != null) {
            updateSitting(player);
        }
        
        // Create hologram
        this.hologram.show(player);
    }
    
    @OverridingMethodsMustInvokeSuper
    protected void hide0(@Nonnull Player player) {
        this.appearance.hide(player);
        this.hologram.hide(player);
        
        // Delete chair
        if (this.chairEntity != null) {
            Reflect.sendPacket(player, PacketFactory.makePacketRemoveEntity(chairEntity));
        }
    }
    
    @ApiStatus.Internal
    private void updateSitting(@Nonnull Player player) {
        // Explicit chair check
        if (chairEntity == null) {
            return;
        }
        
        // Create the chair entity to the player
        Reflect.sendPacket(
                player,
                PacketFactory.makePacketAddEntity(chairEntity),
                PacketFactory.makePacketSetEntityData(chairEntity),
                PacketFactory.makePacketTeleportEntity(chairEntity),
                PacketFactory.makePacketSetPassengers(chairEntity)
        );
    }
    
    final void sendPackets(Packet<?>... packets) {
        playerData.values().forEach(playerData -> {
            if (playerData.visibility == Visibility.VISIBLE) {
                Reflect.sendPacket(playerData.player, packets);
            }
        });
    }
    
    final void onClick0(@Nonnull Player player, @Nonnull ClickType clickType) {
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
        final PlayerClickAtNpcEvent event = new PlayerClickAtNpcEvent(player, this, clickType);
        event.callEvent();
        
        final PlayerClickAtNpcEvent.ClickResponse response = event.getResponse();
        
        if (response == PlayerClickAtNpcEvent.ClickResponse.CANCEL) {
            return;
        }
        
        // Start the cooldown either way, can only be HOLD or OK which both
        // start the cooldown
        playerData.lastInteraction = System.currentTimeMillis();
        
        // Only pass the onClick() is response is OK
        if (response == PlayerClickAtNpcEvent.ClickResponse.OK) {
            onClick(player, clickType);
        }
    }
    
}
