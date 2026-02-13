package me.hapyl.eterna.module.command;

import me.hapyl.eterna.module.annotate.CaughtExceptions;
import me.hapyl.eterna.module.annotate.MethodDelegate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Creates a new {@link SimpleCommand} that is only available for {@link Player}.
 */
public abstract class SimplePlayerCommand extends SimpleCommand {
    
    /**
     * Creates a new {@link SimplePlayerCommand}.
     *
     * @param name - The name of the command.
     */
    public SimplePlayerCommand(@NotNull String name) {
        super(name);
        
        this.setAllowOnlyPlayer(true);
    }
    
    /**
     * Attempts to execute the command.
     *
     * @param player - The command sender.
     * @param args   - The command arguments.
     */
    @CaughtExceptions
    protected abstract void execute(@NotNull Player player, @NotNull ArgumentList args);
    
    @Override
    @MethodDelegate
    protected final void execute(@NotNull CommandSender sender, @NotNull ArgumentList args) {
        if (sender instanceof Player player) {
            this.execute(player, args);
        }
    }
}
