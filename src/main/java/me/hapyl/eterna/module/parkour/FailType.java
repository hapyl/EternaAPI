package me.hapyl.eterna.module.parkour;

import javax.annotation.Nonnull;

public enum FailType {
    
    TELEPORT("do not teleport"),
    FLIGHT("do no fly"),
    GAME_MODE_CHANGE("do not change your gamemode"),
    EFFECT_CHANGE("do not use potions");
    
    private final String reason;
    
    FailType(@Nonnull String reason) {
        this.reason = reason;
    }
    
    @Nonnull
    public String reason() {
        return this.reason;
    }
}
