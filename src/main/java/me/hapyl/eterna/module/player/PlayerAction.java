package me.hapyl.eterna.module.player;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an action that may be performed by a {@link Player}.
 */
@FunctionalInterface
public interface PlayerAction {
    
    /**
     * Performs this action for the given {@link Player}.
     *
     * @param player - The player for whom to perform the action.
     */
    void use(@NotNull Player player);
    
}
