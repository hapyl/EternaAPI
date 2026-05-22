package me.hapyl.eterna.module.reflect.team;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a team syncer that determines which fields of a scoreboard are synced with a packet team.
 */
public interface PacketTeamSyncer {
    
    /**
     * Defines a default {@link PacketTeamSyncer}, which includes all fields.
     */
    @NotNull
    PacketTeamSyncer DEFAULT = new PacketTeamSyncer() {};
    
    /**
     * Defines a default {@link PacketTeamSyncer}, which includes all fields except a color.
     */
    @NotNull
    PacketTeamSyncer NO_COLOR = new PacketTeamSyncer() {
        @Override
        public boolean color() {
            return false;
        }
    };
    
    /**
     * Gets whether the friendly fire option should be synced.
     *
     * @return {@code true} if the friendly fire option should be synced.
     */
    default boolean friendlyFire() {
        return true;
    }
    
    /**
     * Gets whether the friendly invisibilities option should be synced.
     *
     * @return {@code true} if the friendly invisibilities option should be synced.
     */
    default boolean friendlyInvisibles() {
        return true;
    }
    
    /**
     * Gets whether the collision rule should be synced.
     *
     * @return {@code true} if the collision rule should be synced.
     */
    default boolean collisionRule() {
        return true;
    }
    
    /**
     * Gets whether the death message option should be synced.
     *
     * @return {@code true} if the death message option should be synced.
     */
    default boolean deathMessageVisibility() {
        return true;
    }
    
    /**
     * Gets whether the name tag visibility option should be synced.
     *
     * @return {@code true} if the name tag visibility option should be synced.
     */
    default boolean nameTagVisibility() {
        return true;
    }
    
    /**
     * Gets whether the prefix should be synced.
     *
     * @return {@code true} if the prefix should be synced.
     */
    default boolean prefix() {
        return true;
    }
    
    /**
     * Gets whether the suffix should be synced.
     *
     * @return {@code true} if the suffix should be synced.
     */
    default boolean suffix() {
        return true;
    }
    
    /**
     * Gets whether the color should be synced.
     *
     * @return {@code true} if the color should be synced.
     */
    default boolean color() {
        return true;
    }
    
}
