package me.hapyl.eterna.module.entity;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Range;

import javax.annotation.Nonnull;

/**
 * Represents an entity that has a certain distance that it is visible at.
 */
public interface ViewDistance {
    
    /**
     * Gets the distance within which this entity is visible.
     *
     * @return the distance within which this entity is visible.
     */
    double viewDistance();
    
    /**
     * Sets the distance within which this entity is visible.
     *
     * @param viewDistance - The new distance, in blocks.
     */
    void viewDistance(@Range(from = 0, to = Long.MAX_VALUE) double viewDistance);
    
    /**
     * Gets whether this entity has a view distance.
     *
     * @return {@code true} if this entity has view distance; {@code false} otherwise.
     */
    default boolean hasViewDistance() {
        return viewDistance() > 0;
    }
    
    /**
     * Gets whether this entity can be seen by the given player.
     *
     * @param player - The player to check.
     * @return {@code true} if the player can see the entity; {@code false} otherwise.
     */
    boolean canBeSeenBy(@Nonnull Player player);
    
}
