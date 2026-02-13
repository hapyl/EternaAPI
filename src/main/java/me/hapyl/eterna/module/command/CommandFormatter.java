package me.hapyl.eterna.module.command;

import me.hapyl.eterna.module.component.Formatter;
import me.hapyl.eterna.module.math.Tick;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a customizable formatter for {@link SimpleCommand}.
 */
public interface CommandFormatter extends Formatter {
    
    /**
     * Defines the default formatter used.
     */
    @NotNull
    CommandFormatter DEFAULT = new CommandFormatter() {
        @Override
        public void disabledCommand(@NotNull CommandSender sender) {
            sender.sendMessage(Component.text("This command is currently disabled!", NamedTextColor.DARK_RED));
        }
        
        @Override
        public void playerOnlyCommand(@NotNull CommandSender sender) {
            sender.sendMessage(Component.text("You must be a player to execute this command!", NamedTextColor.DARK_RED));
        }
        
        @Override
        public void noPermissions(@NotNull CommandSender sender) {
            sender.sendMessage(Component.text("You don't have permissions to execute this command!", NamedTextColor.DARK_RED));
        }
        
        @Override
        public void onCooldown(@NotNull CommandSender sender, int cooldown) {
            sender.sendMessage(
                    Component.text()
                             .append(Component.text("This command is on cooldown for ", NamedTextColor.DARK_RED))
                             .append(Component.text(Tick.format(cooldown), NamedTextColor.RED))
                             .append(Component.text("!", NamedTextColor.DARK_RED))
                             .build()
            );
        }
        
        @Override
        public void executionError(@NotNull CommandSender sender, @NotNull Exception ex) {
            sender.sendMessage(
                    Component.text()
                             .append(Component.text("An error has occurred whilst executing the command! ", NamedTextColor.DARK_RED))
                             .append(Component.text(ex.getMessage(), NamedTextColor.RED))
            );
        }
        
        @Override
        public void invalidUsage(@NotNull CommandSender sender, @NotNull String usage) {
            sender.sendMessage(
                    Component.text()
                             .append(Component.text("Invalid usage! ", NamedTextColor.DARK_RED))
                             .append(Component.text(usage, NamedTextColor.RED))
                             .build()
            );
        }
    };
    
    @NotNull
    @Override
    default Component defineFormatter() {
        return Component.text("command");
    }
    
    /**
     * Sends a message for when the command is annotated with {@link DisabledCommand}.
     *
     * @param sender - The sender for whom to send the message.
     */
    void disabledCommand(@NotNull CommandSender sender);
    
    /**
     * Sends a message for when a non-player attempts to execute player-only command.
     *
     * @param sender - The console sender.
     */
    void playerOnlyCommand(@NotNull CommandSender sender);
    
    /**
     * Sends a message for when a {@link CommandSender} attempts to execute the command, but doesn't have the {@link Permission}.
     *
     * @param sender - The sender for whom to send the message.
     */
    void noPermissions(@NotNull CommandSender sender);
    
    /**
     * Sends a message for when a {@link CommandSender} attempts to execute the command while its on cooldown.
     *
     * @param sender   - The sender for whom to send the message.
     * @param cooldown - The cooldown left in ticks.
     */
    void onCooldown(@NotNull CommandSender sender, int cooldown);
    
    /**
     * Sends a message for when a command execution resulted in an {@link Exception}.
     *
     * @param sender - The sender for whom to send the message.
     * @param ex     - The exception caused by executing the command.
     */
    void executionError(@NotNull CommandSender sender, @NotNull Exception ex);
    
    /**
     * Sends a message for {@link SimpleCommand#sendCommandUsage}.
     *
     * @param sender - The sender for whom to send the message.
     * @param usage  - The correct command usage.
     */
    void invalidUsage(@NotNull CommandSender sender, @NotNull String usage);
    
}
