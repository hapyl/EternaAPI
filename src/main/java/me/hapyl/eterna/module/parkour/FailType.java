package me.hapyl.eterna.module.parkour;

import me.hapyl.eterna.module.component.Named;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Defines a {@link FailType} for {@link Parkour}.
 */
public enum FailType implements Named {
    
    /**
     * Defines a {@link FailType} for when a {@link Player} teleports.
     *
     * <p>Excluding teleporting to a checkpoint.</p>
     */
    TELEPORT(Component.text("Teleporting is not allowed!")),
    
    /**
     * Defines a {@link FailType} for when a {@link Player} toggles flight mode.
     */
    FLIGHT(Component.text("Flying is not allowed!")),
    
    /**
     * Defines a {@link FailType} for when a {@link Player} changes their {@link GameMode}.
     */
    GAME_MODE_CHANGE(Component.text("Changing game mode is not allowed!")),
    
    /**
     * Defies a {@link FailType} for when a {@link Player} potion effects changes.
     */
    EFFECT_CHANGE(Component.text("Potions are not allowed!"));
    
    private final Component reason;
    
    FailType(@NotNull Component reason) {
        this.reason = reason;
    }
    
    @Override
    @NotNull
    public Component getName() {
        return reason;
    }
}
