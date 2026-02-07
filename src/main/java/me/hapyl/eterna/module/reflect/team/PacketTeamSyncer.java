package me.hapyl.eterna.module.reflect.team;

import org.jetbrains.annotations.NotNull;

public interface PacketTeamSyncer {
    
    @NotNull
    PacketTeamSyncer DEFAULT = new PacketTeamSyncer() {};
    
    @NotNull
    PacketTeamSyncer NO_COLOR = new PacketTeamSyncer() {
        @Override
        public boolean color() {
            return false;
        }
    };
    
    default boolean friendlyFire() {
        return true;
    }
    
    default boolean friendlyInvisibles() {
        return true;
    }
    
    default boolean collisionRule() {
        return true;
    }
    
    default boolean deathMessageVisibility() {
        return true;
    }
    
    default boolean nameTagVisibility() {
        return true;
    }
    
    default boolean prefix() {
        return true;
    }
    
    default boolean suffix() {
        return true;
    }
    
    default boolean color() {
        return true;
    }
    
}
