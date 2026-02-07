package me.hapyl.eterna.module.command.completer;

import me.hapyl.eterna.module.command.ArgumentList;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Represents a tab completer for {@link CompleterHandler}.
 */
public interface Completer {
    
    /**
     * Gets a list of available tab-completions.
     *
     * @param sender - The command sender.
     * @param args   - The command arguments.
     * @param index  - The argument index for the current argument.
     * @return a list of available tab-completions.
     */
    @NotNull
    List<String> complete(@NotNull CommandSender sender, @NotNull ArgumentList args, int index);
    
}
