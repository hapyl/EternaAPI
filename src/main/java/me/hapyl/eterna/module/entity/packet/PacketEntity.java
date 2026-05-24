package me.hapyl.eterna.module.entity.packet;

import me.hapyl.eterna.module.entity.Showable;
import me.hapyl.eterna.module.util.BukkitWrapper;
import me.hapyl.eterna.module.util.Disposable;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.Entity;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Represents an entity that are spawned via packets.
 */
public interface PacketEntity extends Showable, Disposable, BukkitWrapper<org.bukkit.entity.Entity> {
    
    /**
     * Gets the minecraft {@link Entity}.
     *
     * @return the minecraft entity.
     */
    @NotNull
    Entity getEntity();
    
    /**
     * Gets the bukkit entity.
     *
     * @return the bukkit entity.
     */
    @NotNull
    @Override
    org.bukkit.entity.Entity bukkit();
    
    /**
     * Gets the {@link PacketEntity} id.
     *
     * @return the entity id.
     */
    default int getId() {
        return getEntity().getId();
    }
    
    /**
     * Shows this {@link PacketEntity} to the given {@link Player}.
     *
     * @param player - The player for whom this entity should be shown.
     */
    @Override
    void show(@NotNull Player player);
    
    /**
     * Hides this {@link PacketEntity} for the given {@link Player}.
     *
     * @param player - The player for whom this entity should be hidden.
     */
    @Override
    void hide(@NotNull Player player);
    
    /**
     * Gets whether this {@link PacketEntity} is visible to the given {@link Player}.
     *
     * @param player - The player to check.
     * @return {@code true} if this entity is visible, {@code false} otherwise.
     */
    @Override
    boolean isShowingTo(@NotNull Player player);
    
    /**
     * Destroys this {@link PacketEntity}, hiding it from all players who can see it.
     */
    @Override
    void dispose();
    
    /**
     * Sets whether this {@link PacketEntity} is visible.
     * <p>Visibility refers to a physical visibility, just like an invisibility potion.</p>
     *
     * @param visibility - {@code true} if this entity should be visible; {@code false} otherwise.
     */
    void setVisible(boolean visibility);
    
    /**
     * Sets whether this {@link PacketEntity} has collisions.
     *
     * @param collision - {@code true} if this entity should have collisions; {@code false} otherwise.
     */
    void setCollision(boolean collision);
    
    /**
     * Sets whether this {@link PacketEntity} is silent.
     *
     * @param silent - {@code true} if this entity should be silent; {@code false} otherwise.
     */
    void setSilent(boolean silent);
    
    /**
     * Sets whether this {@link PacketEntity} has gravity.
     *
     * @param gravity - {@code true} if this entity should have gravity; {@code false} otherwise.
     */
    void setGravity(boolean gravity);
    
    /**
     * Teleports this {@link PacketEntity} to the given {@link Location}.
     *
     * @param location - The designated location.
     */
    void teleport(@NotNull Location location);
    
    /**
     * Gets the copy of this entity location.
     *
     * @return the copy of this entity location.
     */
    @NotNull
    Location getLocation();
    
    /**
     * Gets the entity data value for the given {@link EntityDataSerializer} and {@code id}.
     *
     * @param type - The serializer type.
     * @param id   - The data id.
     * @param <D>  - The data type.
     * @return the entity data wrapped in an optional.
     * @see EntityDataSerializers
     */
    @NotNull
    <D> Optional<D> getEntityDataValue(@NotNull EntityDataSerializer<D> type, int id);
    
    /**
     * Sets the entity data value for given {@link EntityDataSerializer} and {@code id}.
     *
     * <p>
     * Note that mutating entity data after showing the entity requires manual update via {@link #updateEntityData(Player)} or {@link #updateEntityData()}.
     * </p>
     *
     * @param type  - The serializer type.
     * @param id    - The data id.
     * @param value - The value to set.
     * @param <D>   - The data type.
     * @see EntityDataSerializers
     */
    <D> void setEntityDataValue(@NotNull EntityDataSerializer<D> type, int id, @NotNull D value);
    
    /**
     * Updates this entity data for the given {@link Player}.
     *
     * @param player - The player for whom to update the data.
     */
    void updateEntityData(@NotNull Player player);
    
    /**
     * Updates this entity data for all players who this entity {@link #isShowingTo(Player)}.
     */
    void updateEntityData();
}
