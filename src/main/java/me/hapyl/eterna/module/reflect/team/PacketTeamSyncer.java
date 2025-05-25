package me.hapyl.eterna.module.reflect.team;

import javax.annotation.Nonnull;

/**
 * Represents a set of boolean flags that determine which team options should be synchronized in a packet.
 */
public interface PacketTeamSyncer {
    
    /**
     * Default syncer that enables all options.
     */
    @Nonnull
    PacketTeamSyncer DEFAULT = new PacketTeamSyncer() {};
    
    /**
     * Syncer that disables color syncing but includes all other options.
     */
    @Nonnull
    PacketTeamSyncer NO_COLOR = new PacketTeamSyncer() {
        @Override
        public boolean color() {
            return false;
        }
    };
    
    /**
     * Whether the "allow friendly fire" option should be synchronized.
     */
    default boolean friendlyFire() {
        return true;
    }
    
    /**
     * Whether the "see friendly invisibles" option should be synchronized.
     */
    default boolean friendlyInvisibles() {
        return true;
    }
    
    /**
     * Whether the collision rule should be synchronized.
     */
    default boolean collisionRule() {
        return true;
    }
    
    /**
     * Whether the death message visibility should be synchronized.
     */
    default boolean deathMessageVisibility() {
        return true;
    }
    
    /**
     * Whether the name tag visibility should be synchronized.
     */
    default boolean nameTagVisibility() {
        return true;
    }
    
    /**
     * Whether the player prefix should be synchronized.
     */
    default boolean prefix() {
        return true;
    }
    
    /**
     * Whether the player suffix should be synchronized.
     */
    default boolean suffix() {
        return true;
    }
    
    
    /**
     * Whether the team color should be synchronized.
     */
    default boolean color() {
        return true;
    }
    
}
