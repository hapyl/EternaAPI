package me.hapyl.eterna.module.player.dialog;

import org.bukkit.entity.Player;

/**
 * Represents a {@link DialogEndType} for {@link Dialog#onDialogEnd(Player, DialogEndType)}.
 */
public enum DialogEndType {
    
    /**
     * The {@link Dialog} has ended because it was successfully completed.
     */
    COMPLETED,
    
    /**
     * The {@link Dialog} has ended because it was skipped.
     */
    SKIPPED,
    
    /**
     * The {@link Dialog} has ended because the {@link Player} left the game.
     */
    PLAYER_LEFT
    
}
