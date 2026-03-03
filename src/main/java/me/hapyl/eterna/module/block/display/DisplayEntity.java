package me.hapyl.eterna.module.block.display;

import io.papermc.paper.entity.TeleportFlag;
import me.hapyl.eterna.module.annotate.CaseSensitive;
import me.hapyl.eterna.module.annotate.Immutable;
import me.hapyl.eterna.module.location.Coordinates;
import me.hapyl.eterna.module.location.Located;
import me.hapyl.eterna.module.location.Rotation;
import me.hapyl.eterna.module.util.Removable;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Represents a spawned {@link BDEngine} {@link Display} entity.
 *
 * <p>
 * The entity always consists of a head {@link BlockDisplay} which is purely used as a pivot point, and children {@link Display}
 * which are used as display objects and are passengers of the {@code head}.
 * </p>
 *
 * <p>
 * The {@link DisplayEntity} provides methods for {@code scale} and {@code rotation} around the pivot:
 * </p>
 *
 * @see #getScale()
 * @see #setScale(Vector3f)
 * @see #editScale(Consumer)
 * @see #getRotation()
 * @see #setRotation(Quaternionf)
 * @see #editRotation(Consumer)
 */
public class DisplayEntity implements Iterable<Display>, Removable, Located, Coordinates, Rotation {
    
    private final BlockDisplay head;
    private final List<Display> children;
    
    @NotNull private Vector3f scale;
    @NotNull private Quaternionf rotation;
    
    DisplayEntity(@NotNull BlockDisplay head, @NotNull @Immutable List<Display> children) {
        this.head = head;
        this.children = children;
        this.scale = new Vector3f();
        this.rotation = new Quaternionf();
        
        // Add children to the head to match the vanilla behaviour
        this.children.forEach(head::addPassenger);
    }
    
    /**
     * Gets a copy of the current {@code scale} of this {@link DisplayEntity} as {@link Vector3f}.
     *
     * @return a copy of the current {@code scale} of this display entity as a vector.
     */
    @NotNull
    public Vector3f getScale() {
        return new Vector3f(this.scale);
    }
    
    /**
     * Sets the new {@code scale} of this {@link DisplayEntity}.
     *
     * @param scale - The new scale to set.
     */
    public void setScale(@NotNull Vector3f scale) {
        this.scale = scale;
        
        // Scale head
        final Transformation headTransformation = head.getTransformation();
        final Vector3f newHeadScale = new Vector3f(headTransformation.getScale().mul(scale));
        
        this.head.setTransformation(new Transformation(
                headTransformation.getTranslation(),
                headTransformation.getLeftRotation(),
                newHeadScale,
                headTransformation.getRightRotation()
        ));
        
        // Scale children
        for (Display child : this.children) {
            final Transformation transformation = child.getTransformation();
            
            final Vector3f newTranslation = new Vector3f(transformation.getTranslation().mul(scale));
            final Vector3f newScale = new Vector3f(transformation.getScale().mul(scale));
            
            child.setTransformation(new Transformation(
                    newTranslation,
                    transformation.getLeftRotation(),
                    newScale,
                    transformation.getRightRotation()
            ));
        }
    }
    
    /**
     * Edits the {@code scale} of this {@link DisplayEntity} by applying the given {@link Consumer} to it and updates it.
     *
     * @param edit - The edit to perform.
     */
    public void editScale(@NotNull Consumer<Vector3f> edit) {
        edit.accept(this.scale);
        
        this.setScale(this.scale);
    }
    
    /**
     * Gets a copy of the current {@code rotation} of this {@link DisplayEntity} as a {@link Quaternionf}.
     *
     * @return a copy of the current {@code rotation} of this {@link DisplayEntity} as {@link Quaternionf}.
     */
    @NotNull
    public Quaternionf getRotation() {
        return new Quaternionf(this.rotation);
    }
    
    /**
     * Sets the new {@code rotation} of this {@link DisplayEntity}.
     *
     * @param rotation - The new rotation to set.
     */
    public void setRotation(@NotNull Quaternionf rotation) {
        this.rotation = rotation;
        
        // Rotate head
        final Transformation headTransformation = this.head.getTransformation();
        final Quaternionf newHeadRotation = new Quaternionf(rotation)
                .mul(headTransformation.getLeftRotation())
                .normalize();
        
        this.head.setTransformation(new Transformation(
                headTransformation.getTranslation(),
                newHeadRotation,
                headTransformation.getScale(),
                headTransformation.getRightRotation()
        ));
        
        // Rotate children
        for (Display child : this.children) {
            final Transformation transformation = child.getTransformation();
            
            final Vector3f newTranslation = rotation.transform(new Vector3f(transformation.getTranslation()));
            final Quaternionf newLeftRotation = new Quaternionf(rotation)
                    .mul(transformation.getLeftRotation())
                    .normalize();
            
            child.setTransformation(new Transformation(
                    newTranslation,
                    newLeftRotation,
                    transformation.getScale(),
                    transformation.getRightRotation()
            ));
        }
    }
    
    /**
     * Edits the {@code rotation} of this {@link DisplayEntity} by applying the given {@link Consumer} to it and updates it.
     *
     * @param edit - The edit to perform.
     */
    public void editRotation(@NotNull Consumer<Quaternionf> edit) {
        edit.accept(this.rotation);
        
        this.setRotation(this.rotation);
    }
    
    /**
     * Teleports this {@link DisplayEntity} to the given {@link Location}.
     *
     * <p>
     * This will call teleport on the {@code head} of the {@link DisplayEntity}, which will result in teleportation of the children.
     * </p>
     *
     * @param location - The new location to teleport to.
     * @return {@code true} if the teleportation was successful; {@code false} otherwise.
     */
    public boolean teleport(@NotNull Location location) {
        return this.head.teleport(location);
    }
    
    /**
     * Teleports this {@link DisplayEntity} to the given {@link Location}.
     *
     * <p>
     * This will call teleport on the {@code head} of the {@link DisplayEntity}, which will result in teleportation of the children.
     * </p>
     *
     * @param location - The new location to teleport to.
     * @param cause    - The teleportation cause.
     * @return {@code true} if the teleportation was successful; {@code false} otherwise.
     */
    public boolean teleport(@NotNull Location location, @NotNull PlayerTeleportEvent.TeleportCause cause) {
        return this.head.teleport(location, cause);
    }
    
    /**
     * Teleports this {@link DisplayEntity} to the given {@link Entity}.
     *
     * <p>
     * This will call teleport on the {@code head} of the {@link DisplayEntity}, which will result in teleportation of the children.
     * </p>
     *
     * @param entity - The entity to teleport to.
     * @return {@code true} if the teleportation was successful; {@code false} otherwise.
     */
    public boolean teleport(@NotNull Entity entity) {
        return this.head.teleport(entity);
    }
    
    /**
     * Teleports this {@link DisplayEntity} to the given {@link Entity}.
     *
     * <p>
     * This will call teleport on the {@code head} of the {@link DisplayEntity}, which will result in teleportation of the children.
     * </p>
     *
     * @param entity - The entity to teleport to.
     * @param cause  - The teleportation cause.
     * @return {@code true} if the teleportation was successful; {@code false} otherwise.
     */
    public boolean teleport(@NotNull Entity entity, @NotNull PlayerTeleportEvent.TeleportCause cause) {
        return this.head.teleport(entity, cause);
    }
    
    /**
     * Teleports this {@link DisplayEntity} to the given {@link Entity}.
     *
     * <p>
     * This will call teleport on the {@code head} of the {@link DisplayEntity}, which will result in teleportation of the children.
     * </p>
     *
     * @param location      - The location to teleport to.
     * @param cause         - The teleportation cause.
     * @param teleportFlags - The teleportation flags.
     * @return {@code true} if the teleportation was successful; {@code false} otherwise.
     */
    public boolean teleport(@NotNull Location location, @NotNull PlayerTeleportEvent.TeleportCause cause, @NotNull TeleportFlag... teleportFlags) {
        return this.head.teleport(location, cause, teleportFlags);
    }
    
    /**
     * Teleports this {@link DisplayEntity} to the given {@link Entity} asynchronously, which will result in chunk loading.
     *
     * <p>
     * This will call teleport on the {@code head} of the {@link DisplayEntity}, which will result in teleportation of the children.
     * </p>
     *
     * @param location - The location to teleport to.
     * @return A completable future that will be completed with the result of the teleport.
     */
    @NotNull
    public CompletableFuture<Boolean> teleportAsync(@NotNull Location location, @NotNull PlayerTeleportEvent.TeleportCause cause, @NotNull TeleportFlag @NotNull ... teleportFlags) {
        return this.head.teleportAsync(location, cause, teleportFlags);
    }
    
    /**
     * Gets a {@link List} of nearby {@link Entity} around the {@code head} of this {@link DisplayEntity}.
     *
     * @param x - The {@code 1/2} the size of the box along {@code x} axis.
     * @param y - The {@code 1/2} the size of the box along {@code y} axis.
     * @param z - The {@code 1/2} the size of the box along {@code z} axis.
     * @return a list of nearby entities.
     */
    @NotNull
    public List<Entity> getNearbyEntities(double x, double y, double z) {
        return this.head.getNearbyEntities(x, y, z);
    }
    
    /**
     * Gets whether this {@link DisplayEntity} is invisible.
     *
     * @return {@code true} if this display entity is invisible; {@code false otherwise}.
     */
    public boolean isInvisible() {
        for (Display child : this.children) {
            if (!child.isInvisible()) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Sets whether this {@link DisplayEntity} should be invisible.
     *
     * @param invisible - {@code true} to set invisibility; {@code false} to unset.
     */
    public void setInvisible(boolean invisible) {
        for (Display child : this.children) {
            child.setInvisible(invisible);
        }
    }
    
    /**
     * Removes this {@link DisplayEntity} completely, including the {@code head} and {@code children}.
     */
    @Override
    public void remove() {
        this.head.remove();
        this.children.forEach(Display::remove);
    }
    
    /**
     * Gets whether this {@link DisplayEntity} is removed.
     *
     * @return {@code true} if this display entity is removed; {@code false} otherwise.
     */
    public boolean isRemoved() {
        return this.head.isDead();
    }
    
    /**
     * Gets an <b>immutable</b> view of the {@code children} of this {@link DisplayEntity}.
     *
     * @return an immutable view of the children of this display entity.
     */
    @NotNull
    public List<Display> getChildren() {
        // No need to copy since children are already immutable
        return this.children;
    }
    
    /**
     * Gets whether this {@link DisplayEntity} is glowing.
     *
     * @return {@code true} if this display entity is glowing; {@code false} otherwise.
     */
    public boolean isGlowing() {
        for (Display child : this.children) {
            if (!child.isGlowing()) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Sets whether this {@link DisplayEntity} is glowing.
     *
     * @param glowing - {@code true} to set glowing; {@code false} to unset.
     */
    public void setGlowing(boolean glowing) {
        for (Display child : this.children) {
            child.setGlowing(glowing);
        }
    }
    
    /**
     * Gets the {@code head} of this {@link DisplayEntity}.
     *
     * @return the {@code head} of this display entity.
     */
    @NotNull
    public BlockDisplay getHead() {
        return head;
    }
    
    /**
     * Gets an {@link Iterable} over all the {@code children} of this {@link DisplayEntity}.
     *
     * @return an iterator of all the children of this display entity.
     */
    @NotNull
    @Override
    public Iterator<Display> iterator() {
        return this.children.iterator();
    }
    
    /**
     * Gets the {@link Location} of the {@code head} of this {@link DisplayEntity}.
     *
     * @return the location of the {@code head} of this {@link DisplayEntity}.
     */
    @NotNull
    @Override
    public Location getLocation() {
        return this.head.getLocation();
    }
    
    /**
     * Gets the {@link World} the {@code head} of this {@link DisplayEntity} is located.
     *
     * @return the world the {@code head} of this display entity is located.
     */
    @NotNull
    @Override
    public World getWorld() {
        return this.head.getWorld();
    }
    
    /**
     * Applies the given {@link Consumer} to all {@link Display} that are tagged with the given {@code tag}.
     *
     * @param tag      - The tag to check.
     * @param consumer - The consumer to apply.
     */
    public void asTagged(@NotNull @CaseSensitive String tag, @NotNull Consumer<Display> consumer) {
        for (Display child : this.children) {
            if (child.getScoreboardTags().contains(tag)) {
                consumer.accept(child);
            }
        }
    }
    
    /**
     * Compares the given {@link Object} to this {@link DisplayEntity}.
     *
     * @param object - The object to compare.
     * @return {@code true} if the given object is display entity, and it is identical to this display entity; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        
        final DisplayEntity that = (DisplayEntity) object;
        
        if (this.children.size() != that.children.size()) {
            return false;
        }
        
        for (int i = 0; i < this.children.size(); i++) {
            final Display thisChild = this.children.get(i);
            final Display thatChild = that.children.get(i);
            
            if (!Objects.equals(thisChild, thatChild)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Gets the hash code of this {@link DisplayEntity}.
     *
     * @return the hash code of this display entity.
     */
    @Override
    public int hashCode() {
        return Objects.hash(children);
    }
    
    /**
     * Gets the {@code X} coordinate of this {@link DisplayEntity}.
     *
     * @return the {@code X} coordinate of this display entity.
     */
    @Override
    public double x() {
        return this.head.getX();
    }
    
    /**
     * Gets the {@code Y} coordinate of this {@link DisplayEntity}.
     *
     * @return the {@code Y} coordinate of this display entity.
     */
    @Override
    public double y() {
        return this.head.getY();
    }
    
    /**
     * Gets the {@code Z} coordinate of this {@link DisplayEntity}.
     *
     * @return the {@code Z} coordinate of this display entity.
     */
    @Override
    public double z() {
        return this.head.getZ();
    }
    
    /**
     * Gets the {@code yaw} of this {@link DisplayEntity}.
     *
     * @return the {@code yaw} of this display entity.
     */
    @Override
    public float yaw() {
        return this.head.getYaw();
    }
    
    /**
     * Gets the {@code pitch} of this {@link DisplayEntity}.
     *
     * @return the {@code pitch} of this display entity.
     */
    @Override
    public float pitch() {
        return this.head.getPitch();
    }
    
}