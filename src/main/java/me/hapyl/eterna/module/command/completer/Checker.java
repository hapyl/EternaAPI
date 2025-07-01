package me.hapyl.eterna.module.command.completer;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Provides a way to check for a valid string input based on the current argument and arguments.
 */
public interface Checker {
    
    /**
     * Returns a string to append at the end of the tab completion list.
     *
     * @param player - The player to check.
     * @param arg    - The current argument.
     * @param args   - The arguments array.
     * @return a string to append at the end of the tab completion list.
     */
    @Nullable
    String check(@Nonnull Player player, @Nonnull String arg, @Nonnull String[] args);
    
}
