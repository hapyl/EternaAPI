package me.hapyl.eterna.module.command;

import com.google.common.collect.Maps;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.util.Compute;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Represents a {@link SimpleCommand} processor, which is used to register commands.
 */
public class CommandProcessor {
    
    private static final Map<Plugin, List<SimpleCommand>> COMMANDS_BY_PLUGIN = Maps.newHashMap();
    
    private final Plugin plugin;
    
    /**
     * Creates a new {@link CommandProcessor} with the given plugin as delegate.
     *
     * @param plugin - The plugin delegate.
     */
    public CommandProcessor(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Registers the given {@link SimpleCommand}.
     *
     * @param simpleCommand - The command to register.
     */
    public void registerCommand(@NotNull SimpleCommand simpleCommand) {
        this.registerCommand0(new SimpleCommand[] { simpleCommand });
    }
    
    /**
     * Registers the given {@link SimpleCommand} varargs, in the exact order passed.
     *
     * @param commands - The commands to register.
     * @throws IllegalArgumentException if no commands are passed.
     */
    public void registerCommands(@NotNull SimpleCommand... commands) {
        this.registerCommand0(commands);
    }
    
    @ApiStatus.Internal
    private void registerCommand0(@NotNull SimpleCommand[] commands) {
        try {
            if (commands.length == 0) {
                throw new IllegalArgumentException("There must be at least one command!");
            }
            
            final CommandMap commandMap = Bukkit.getCommandMap();
            
            for (final SimpleCommand simpleCommand : commands) {
                final Command command = simpleCommand.createCommand();
                
                // Register Command
                commandMap.register(plugin.getName(), command);
                COMMANDS_BY_PLUGIN.compute(plugin, Compute.listAdd(simpleCommand));
            }
            
        }
        catch (Exception ex) {
            throw EternaLogger.acknowledgeException(ex);
        }
    }
    
    /**
     * Gets an immutable view of all the {@link SimpleCommand} registered by the given {@link Plugin}.
     *
     * @param plugin - The plugin.
     * @return an immutable view of all commands registered by the plugin.
     */
    @NotNull
    public static List<SimpleCommand> getCommands(@NotNull Plugin plugin) {
        final List<SimpleCommand> commands = COMMANDS_BY_PLUGIN.get(plugin);
        
        return commands != null ? List.copyOf(commands) : List.of();
    }
    
}
