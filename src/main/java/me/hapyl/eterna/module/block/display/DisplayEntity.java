package me.hapyl.eterna.module.block.display;

import com.google.common.collect.Lists;
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.entity.LookAnchor;
import io.papermc.paper.entity.TeleportFlag;
import io.papermc.paper.threadedregions.scheduler.EntityScheduler;
import me.hapyl.eterna.module.block.display.animation.DisplayEntityAnimation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.TriState;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Represents a display entity.
 * An entity consists of a head (always present) and children.
 */
@SuppressWarnings("UnstanbleApiUsage")
public class DisplayEntity implements Iterable<Display>, Display {

    private final BlockDisplay head;
    private final List<Display> children;

    public DisplayEntity(@Nonnull BlockDisplay head) {
        this.head = head;
        this.children = Lists.newArrayList();
        this.children.add(head);
    }

    /**
     * Creates a new {@link DisplayEntityAnimation} instance using the default {@link JavaPlugin}.
     *
     * @return A new {@link DisplayEntityAnimation} instance.
     */
    @Nonnull
    public DisplayEntityAnimation newAnimation() {
        return new DisplayEntityAnimation(this);
    }

    /**
     * Creates a new {@link DisplayEntityAnimation} instance with the specified {@link JavaPlugin}.
     *
     * @param plugin - The {@link JavaPlugin} to be used for the animation.
     * @return A new {@link DisplayEntityAnimation} instance.
     */
    @Nonnull
    public DisplayEntityAnimation newAnimation(@Nonnull JavaPlugin plugin) {
        return new DisplayEntityAnimation(this, plugin);
    }

    public boolean teleport(@Nonnull Location location) {
        return teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    @Override
    public boolean teleport(@Nonnull Location location, @Nonnull PlayerTeleportEvent.TeleportCause cause) {
        children.forEach(child -> child.teleport(location, cause));
        return true;
    }

    @Override
    public boolean teleport(@Nonnull Entity destination) {
        return teleport(destination.getLocation());
    }

    @Override
    public boolean teleport(@Nonnull Entity destination, @Nonnull PlayerTeleportEvent.TeleportCause cause) {
        return teleport(destination.getLocation(), cause);
    }

    @Nonnull
    @Override
    public CompletableFuture<Boolean> teleportAsync(@Nonnull Location loc, @Nonnull PlayerTeleportEvent.TeleportCause cause, @Nonnull TeleportFlag... teleportFlags) {
        children.forEach(child -> child.teleportAsync(loc, cause));

        return CompletableFuture.completedFuture(Boolean.TRUE);
    }

    @Nonnull
    @Override
    public List<Entity> getNearbyEntities(double x, double y, double z) {
        return head.getNearbyEntities(x, y, z);
    }

    /**
     * Gets the entity Id of the head of this entity.
     *
     * @return the entity Id of the head of this entity.
     * @deprecated {@link #getEntitiesId()}
     */
    @Deprecated
    @Override
    public int getEntityId() {
        return head.getEntityId();
    }

    @Nonnull
    public List<Integer> getEntitiesId() {
        return children.stream()
                .map(Display::getEntityId)
                .toList();
    }

    @Override
    public int getFireTicks() {
        return head.getFireTicks();
    }

    @Override
    public void setFireTicks(int ticks) {
        children.forEach(child -> child.setFireTicks(ticks));
    }

    @Override
    public int getMaxFireTicks() {
        return head.getMaxFireTicks();
    }

    @Override
    public boolean isVisualFire() {
        return head.isVisualFire();
    }
    
    @Override
    public @NotNull TriState getVisualFire() {
        return TriState.byBoolean(isVisualFire());
    }
    
    @Override
    public void setVisualFire(boolean fire) {
        children.forEach(child -> child.setVisualFire(fire));
    }
    
    @Override
    public void setVisualFire(@NotNull TriState triState) {
        setVisualFire(Boolean.TRUE.equals(triState.toBoolean()));
    }
    
    @Override
    public int getFreezeTicks() {
        return head.getFreezeTicks();
    }

    @Override
    public void setFreezeTicks(int ticks) {
        children.forEach(child -> child.setFreezeTicks(ticks));
    }

    @Override
    public int getMaxFreezeTicks() {
        return head.getMaxFreezeTicks();
    }

    @Override
    public boolean isFrozen() {
        return head.isFrozen();
    }

    @Override
    public boolean isInvisible() {
        return head.isInvisible();
    }

    @Override
    public void setInvisible(boolean invisible) {
        children.forEach(child -> child.setInvisible(invisible));
    }

    @Override
    public void setNoPhysics(boolean noPhysics) {
        children.forEach(child -> child.setNoPhysics(noPhysics));
    }

    @Override
    public boolean hasNoPhysics() {
        return head.hasNoPhysics();
    }

    @Override
    public boolean isFreezeTickingLocked() {
        return head.isFreezeTickingLocked();
    }

    @Override
    public void lockFreezeTicks(boolean locked) {
        children.forEach(child -> child.lockFreezeTicks(locked));
    }

    @Override
    public void remove() {
        children.forEach(Display::remove);
        children.clear();
    }

    @Override
    public boolean isDead() {
        return head.isDead();
    }

    @Override
    public boolean isValid() {
        return head.isValid();
    }

    @Deprecated
    @Override
    public void sendMessage(@Nonnull String message) {
        throw uoe("sendMessage");
    }

    @Deprecated
    @Override
    public void sendMessage(@Nonnull String... messages) {
        throw uoe("sendMessage");
    }

    @Deprecated
    @Override
    public void sendMessage(@Nullable UUID sender, @Nonnull String message) {
        throw uoe("sendMessage");
    }

    @Deprecated
    @Override
    public void sendMessage(@Nullable UUID sender, @Nonnull String... messages) {
        throw uoe("sendMessage");
    }

    @Nonnull
    @Override
    public Server getServer() {
        return head.getServer();
    }

    @Nonnull
    @Override
    public String getName() {
        return head.getName();
    }

    @Override
    public boolean isPersistent() {
        return head.isPersistent();
    }

    @Override
    public void setPersistent(boolean persistent) {
        children.forEach(child -> child.setPersistent(persistent));
    }

    @Deprecated
    @Nullable
    @Override
    public Entity getPassenger() {
        throw uoe("getPassenger");
    }

    @Deprecated
    @Override
    public boolean setPassenger(@Nonnull Entity passenger) {
        throw uoe("setPassenger");
    }

    @Nonnull
    @Override
    public List<Entity> getPassengers() {
        return head.getPassengers();
    }

    @Deprecated
    @Override
    public boolean addPassenger(@Nonnull Entity passenger) {
        throw uoe("addPassenger");
    }

    @Deprecated
    @Override
    public boolean removePassenger(@Nonnull Entity passenger) {
        throw uoe("removePassenger");
    }

    @Override
    public boolean isEmpty() {
        return children.size() == 1;
    }

    @Override
    public boolean eject() {
        return false;
    }

    @Override
    public float getFallDistance() {
        return head.getFallDistance();
    }

    @Override
    public void setFallDistance(float distance) {
        children.forEach(child -> child.setFallDistance(distance));
    }

    @Nullable
    @Override
    public EntityDamageEvent getLastDamageCause() {
        return null;
    }

    @Deprecated(forRemoval = true)
    @Override
    public void setLastDamageCause(@Nullable EntityDamageEvent event) {
    }

    @Deprecated
    @Nonnull
    @Override
    public UUID getUniqueId() {
        return head.getUniqueId();
    }

    @Override
    public int getTicksLived() {
        return head.getTicksLived();
    }

    @Override
    public void setTicksLived(int value) {
        head.setTicksLived(value);
    }

    @Override
    public void playEffect(@Nonnull EntityEffect type) {
    }

    @Nonnull
    @Override
    public EntityType getType() {
        return EntityType.BLOCK_DISPLAY;
    }

    @Nonnull
    @Override
    public Sound getSwimSound() {
        return Sound.ENTITY_GENERIC_SWIM;
    }

    @Nonnull
    @Override
    public Sound getSwimSplashSound() {
        return Sound.ENTITY_GENERIC_SPLASH;
    }

    @Nonnull
    @Override
    public Sound getSwimHighSpeedSplashSound() {
        return Sound.ENTITY_GENERIC_SPLASH;
    }

    @Override
    public boolean isInsideVehicle() {
        return false;
    }

    @Override
    public boolean leaveVehicle() {
        return false;
    }

    @Override
    @Nullable
    public Entity getVehicle() {
        return null; // we're the vehicle
    }

    @Deprecated
    @Override
    public boolean isCustomNameVisible() {
        return false;
    }

    @Deprecated
    @Override
    public void setCustomNameVisible(boolean flag) {
    }

    @Override
    public boolean isVisibleByDefault() {
        return head.isVisibleByDefault();
    }

    @Override
    public void setVisibleByDefault(boolean visible) {
        children.forEach(child -> child.setVisibleByDefault(visible));
    }

    @Nonnull
    @Override
    public Set<Player> getTrackedBy() {
        return head.getTrackedBy();
    }
    
    @Override
    public boolean isTrackedBy(@NotNull Player player) {
        return false;
    }
    
    @Override
    public boolean isGlowing() {
        return head.isGlowing();
    }

    @Override
    public void setGlowing(boolean flag) {
        children.forEach(child -> child.setGlowing(flag));
    }

    @Override
    public boolean isInvulnerable() {
        return head.isInvulnerable();
    }

    @Override
    public void setInvulnerable(boolean flag) {
        children.forEach(child -> child.setInvisible(flag));
    }

    @Override
    public boolean isSilent() {
        return true;
    }

    @Override
    public void setSilent(boolean flag) {
    }

    @Override
    public boolean hasGravity() {
        return false;
    }

    @Override
    public void setGravity(boolean gravity) {
    }

    @Override
    public int getPortalCooldown() {
        return head.getPortalCooldown();
    }

    @Override
    public void setPortalCooldown(int cooldown) {
        children.forEach(child -> child.setPortalCooldown(cooldown));
    }

    @Nonnull
    @Override
    public Set<String> getScoreboardTags() {
        return head.getScoreboardTags();
    }

    @Override
    public boolean addScoreboardTag(@Nonnull String tag) {
        boolean wasAdded = false;

        for (Display child : children) {
            if (child.addScoreboardTag(tag)) {
                wasAdded = true;
            }
        }

        return wasAdded;
    }

    @Override
    public boolean removeScoreboardTag(@Nonnull String tag) {
        boolean wasRemoved = false;

        for (Display child : children) {
            if (child.removeScoreboardTag(tag)) {
                wasRemoved = true;
            }
        }

        return wasRemoved;
    }

    @Nonnull
    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return PistonMoveReaction.IGNORE;
    }

    @Nonnull
    @Override
    public BlockFace getFacing() {
        return head.getFacing();
    }

    @Nonnull
    @Override
    public Pose getPose() {
        return head.getPose();
    }

    @Override
    public boolean isSneaking() {
        return false;
    }

    @Override
    public void setSneaking(boolean sneak) {
    }

    @Override
    public void setPose(@Nonnull Pose pose, boolean fixed) {
    }

    @Override
    public boolean hasFixedPose() {
        return true;
    }

    @Nonnull
    @Override
    public SpawnCategory getSpawnCategory() {
        return SpawnCategory.MISC;
    }

    @Override
    public boolean isInWorld() {
        return true;
    }

    @Deprecated
    @Nullable
    @Override
    public String getAsString() {
        return null;
    }

    @Deprecated
    @Nullable
    @Override
    public EntitySnapshot createSnapshot() {
        return null;
    }

    @Deprecated
    @Nonnull
    @Override
    public Entity copy() {
        throw uoe("copy");
    }

    @Nonnull
    @Override
    public Entity copy(@Nonnull Location to) {
        throw uoe("copy");
    }

    @Nonnull
    @Override
    public Spigot spigot() {
        throw uoe("spigot");
    }

    @Nonnull
    @Override
    public Component name() {
        return head.name();
    }

    @Nonnull
    @Override
    public Component teamDisplayName() {
        return head.teamDisplayName();
    }

    @Nullable
    @Override
    public Location getOrigin() {
        return head.getOrigin();
    }

    @Override
    public boolean fromMobSpawner() {
        return false;
    }

    @Nonnull
    @Override
    public CreatureSpawnEvent.SpawnReason getEntitySpawnReason() {
        return CreatureSpawnEvent.SpawnReason.CUSTOM;
    }

    @Override
    public boolean isUnderWater() {
        return head.isUnderWater();
    }

    @Override
    public boolean isInRain() {
        return head.isInRain();
    }

    @Override
    public boolean isInBubbleColumn() {
        return head.isInBubbleColumn();
    }

    @Override
    public boolean isInWaterOrRain() {
        return head.isInWaterOrRain();
    }

    @Override
    public boolean isInWaterOrBubbleColumn() {
        return head.isInWaterOrBubbleColumn();
    }

    @Override
    public boolean isInWaterOrRainOrBubbleColumn() {
        return head.isInWaterOrRainOrBubbleColumn();
    }

    @Override
    public boolean isInLava() {
        return head.isInLava();
    }

    @Deprecated
    @Override
    public boolean isTicking() {
        return head.isTicking();
    }

    @Deprecated
    @Nonnull
    @Override
    public Set<Player> getTrackedPlayers() {
        return head.getTrackedPlayers();
    }

    @Deprecated
    @Override
    public boolean spawnAt(@Nonnull Location location, @Nonnull CreatureSpawnEvent.SpawnReason reason) {
        throw uoe("spawnAt");
    }

    @Override
    public boolean isInPowderedSnow() {
        return head.isInPowderedSnow();
    }

    @Override
    public double getX() {
        return head.getX();
    }

    @Override
    public double getY() {
        return head.getY();
    }

    @Override
    public double getZ() {
        return head.getZ();
    }

    @Override
    public float getPitch() {
        return head.getPitch();
    }

    @Override
    public float getYaw() {
        return head.getYaw();
    }

    @Override
    public boolean collidesAt(@Nonnull Location location) {
        return false;
    }

    @Override
    public boolean wouldCollideUsing(@Nonnull BoundingBox boundingBox) {
        return false;
    }

    @Deprecated
    @Nonnull
    @Override
    public EntityScheduler getScheduler() {
        return head.getScheduler();
    }

    @Nonnull
    @Override
    public String getScoreboardEntryName() {
        return head.getScoreboardEntryName();
    }

    @Deprecated
    @Override
    public void broadcastHurtAnimation(@NotNull Collection<Player> collection) {
    }

    /**
     * Gets an immutable {@link List} of all the entities in this model, including the head and the children.
     *
     * @return an immutable list of all the entities in this model, including the head and the children.
     */
    @Nonnull
    public List<Display> getEntities() {
        return Collections.unmodifiableList(children);
    }

    /**
     * Gets the head entity of this model.
     *
     * @return the head entity of this model.
     */
    @Nonnull
    public BlockDisplay getHead() {
        return head;
    }

    /**
     * Gets an {@link Iterator} of all the entities of this model.
     * <br>
     * The iterator cannot remove children.
     *
     * @return an iterator of all the entities of this model.
     */
    @Nonnull
    @Override
    public Iterator<Display> iterator() {
        return getEntities().iterator();
    }

    /**
     * Gets the {@link Location} of the head of this model.
     *
     * @return the location of the head of this model.
     */
    @Nonnull
    @Override
    public Location getLocation() {
        return head.getLocation();
    }

    @Nullable
    @Override
    public Location getLocation(@Nullable Location loc) {
        return head.getLocation(loc);
    }

    @Nonnull
    @Override
    public Vector getVelocity() {
        return head.getVelocity();
    }

    @Override
    public void setVelocity(@Nonnull Vector velocity) {
        head.setVelocity(velocity);
    }

    @Override
    public double getHeight() {
        return 0;
    }

    @Override
    public double getWidth() {
        return 0;
    }

    @Nonnull
    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox();
    }

    @Override
    public boolean isOnGround() {
        return head.isOnGround();
    }

    @Override
    public boolean isInWater() {
        return head.isInWater();
    }

    @Nonnull
    @Override
    public World getWorld() {
        return head.getWorld();
    }

    @Override
    public void setRotation(float yaw, float pitch) {
        children.forEach(child -> child.setRotation(yaw, pitch));
    }

    @Override
    public boolean teleport(@Nonnull Location location, @Nonnull PlayerTeleportEvent.TeleportCause cause, @Nonnull TeleportFlag... teleportFlags) {
        children.forEach(child -> child.teleport(location, cause, teleportFlags));
        return true;
    }

    @Override
    public void lookAt(double v, double v1, double v2, @NotNull LookAnchor lookAnchor) {
    }

    @Nonnull
    @Override
    public Transformation getTransformation() {
        return head.getTransformation();
    }

    @Override
    public void setTransformation(@Nonnull Transformation transformation) {
        head.setTransformation(transformation);
    }

    @Override
    public void setTransformationMatrix(@Nonnull Matrix4f transformationMatrix) {
        head.setTransformationMatrix(transformationMatrix);
    }

    @Override
    public int getInterpolationDuration() {
        return head.getInterpolationDuration();
    }

    @Override
    public void setInterpolationDuration(int duration) {
        head.setInterpolationDuration(duration);
    }

    @Override
    public int getTeleportDuration() {
        return head.getTeleportDuration();
    }

    @Override
    public void setTeleportDuration(int duration) {
        forEach(child -> child.setTeleportDuration(duration));
    }

    @Override
    public float getViewRange() {
        return head.getViewRange();
    }

    @Override
    public void setViewRange(float range) {
        forEach(child -> child.setViewRange(range));
    }

    @Override
    public float getShadowRadius() {
        return head.getShadowRadius();
    }

    @Override
    public void setShadowRadius(float radius) {
        forEach(child -> child.setShadowRadius(radius));
    }

    @Override
    public float getShadowStrength() {
        return head.getShadowStrength();
    }

    @Override
    public void setShadowStrength(float strength) {
        forEach(child -> child.setShadowStrength(strength));
    }

    @Override
    public float getDisplayWidth() {
        return head.getDisplayWidth();
    }

    @Override
    public void setDisplayWidth(float width) {
        forEach(child -> child.setDisplayWidth(width));
    }

    @Override
    public float getDisplayHeight() {
        return head.getDisplayHeight();
    }

    @Override
    public void setDisplayHeight(float height) {
        forEach(child -> child.setDisplayHeight(height));
    }

    @Override
    public int getInterpolationDelay() {
        return head.getInterpolationDelay();
    }

    @Override
    public void setInterpolationDelay(int ticks) {
        forEach(child -> child.setInterpolationDelay(ticks));
    }

    @Nonnull
    @Override
    public Display.Billboard getBillboard() {
        return head.getBillboard();
    }

    @Override
    public void setBillboard(@Nonnull Display.Billboard billboard) {
        forEach(child -> child.setBillboard(billboard));
    }

    @Nullable
    @Override
    public Color getGlowColorOverride() {
        return head.getGlowColorOverride();
    }

    @Override
    public void setGlowColorOverride(@Nullable Color color) {
        forEach(child -> child.setGlowColorOverride(color));
    }

    @Nullable
    @Override
    public Display.Brightness getBrightness() {
        return head.getBrightness();
    }

    @Override
    public void setBrightness(@Nullable Display.Brightness brightness) {
        forEach(child -> child.setBrightness(brightness));
    }

    @Nullable
    @Override
    public Component customName() {
        return head.customName();
    }

    @Override
    public void customName(@Nullable Component customName) {
        head.customName(customName);
    }

    @Nullable
    @Override
    public String getCustomName() {
        return head.getCustomName();
    }

    @Override
    public void setCustomName(@Nullable String name) {
        head.setCustomName(name);
    }

    @Override
    public void setMetadata(@Nonnull String metadataKey, @Nonnull MetadataValue newMetadataValue) {
        children.forEach(child -> child.setMetadata(metadataKey, newMetadataValue));
    }

    @Nonnull
    @Override
    public List<MetadataValue> getMetadata(@Nonnull String metadataKey) {
        // Unless illegally set metadata, this will work
        return head.getMetadata(metadataKey);
    }

    @Override
    public boolean hasMetadata(@Nonnull String metadataKey) {
        // Unless illegally set metadata, this will work
        return head.hasMetadata(metadataKey);
    }

    @Override
    public void removeMetadata(@Nonnull String metadataKey, @Nonnull Plugin owningPlugin) {
        children.forEach(child -> child.removeMetadata(metadataKey, owningPlugin));
    }

    @Override
    public boolean isPermissionSet(@Nonnull String name) {
        return false;
    }

    @Override
    public boolean isPermissionSet(@Nonnull Permission perm) {
        return false;
    }

    @Override
    public boolean hasPermission(@Nonnull String name) {
        return false;
    }

    @Override
    public boolean hasPermission(@Nonnull Permission perm) {
        return false;
    }

    @Nonnull
    @Override
    public PermissionAttachment addAttachment(@Nonnull Plugin plugin, @Nonnull String name, boolean value) {
        return head.addAttachment(plugin, name, value);
    }

    @Nonnull
    @Override
    public PermissionAttachment addAttachment(@Nonnull Plugin plugin) {
        return head.addAttachment(plugin);
    }

    @Nullable
    @Override
    public PermissionAttachment addAttachment(@Nonnull Plugin plugin, @Nonnull String name, boolean value, int ticks) {
        return null;
    }

    @Nullable
    @Override
    public PermissionAttachment addAttachment(@Nonnull Plugin plugin, int ticks) {
        return null;
    }

    @Override
    public void removeAttachment(@Nonnull PermissionAttachment attachment) {
        head.removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        head.recalculatePermissions();
    }

    @Nonnull
    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return head.getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return false;
    }

    @Override
    public void setOp(boolean value) {
    }

    /**
     * Iterates over all children and applies the given {@link Consumer} if the child has a given tag as their scoreboard tag.
     *
     * @param tag      - The tag to check.
     * @param consumer - The consumer to apply.
     */
    public void asTagged(@Nonnull String tag, @Nonnull Consumer<Display> consumer) {
        children.forEach(child -> {
            if (child.getScoreboardTags().contains(tag)) {
                consumer.accept(child);
            }
        });
    }

    @Nonnull
    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return head.getPersistentDataContainer();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        final DisplayEntity other = (DisplayEntity) object;

        if (children.size() != other.children.size()) {
            return false;
        }

        for (int i = 0; i < children.size(); i++) {
            final Display thisChild = children.get(i);
            final Display otherChild = other.children.get(i);

            if (!Objects.equals(thisChild, otherChild)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 1;

        for (Display child : children) {
            hash = 31 * hash + (child != null ? child.hashCode() : 0);
        }

        return hash;
    }
    
    @Override
    public <T> @org.jetbrains.annotations.Nullable T getData(DataComponentType.@NotNull Valued<T> valued) {
        throw uoe("getData");
    }
    
    @Override
    public <T> @org.jetbrains.annotations.Nullable T getDataOrDefault(DataComponentType.@NotNull Valued<? extends T> valued, @org.jetbrains.annotations.Nullable T t) {
        throw uoe("getDataOrDefault");
    }
    
    @Override
    public boolean hasData(@NotNull DataComponentType dataComponentType) {
        throw uoe("hasData");
    }
    
    protected void append(@Nonnull Display display) {
        head.addPassenger(display);
        children.add(display);
    }

    private UnsupportedOperationException uoe(String name) {
        throw new UnsupportedOperationException(name);
    }
}
