package me.hapyl.eterna.module.command;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Registers {@link SimpleCommand} instances using Paper's lifecycle command API.
 *
 * <p>Commands must be registered during the plugin's {@code onEnable()} method. Paper's
 * lifecycle command registrar is only available during plugin enable.</p>
 */
public class CommandProcessor {
    
    private static final Map<Plugin, List<SimpleCommand>> COMMANDS_BY_PLUGIN = new HashMap<>();
    
    private final Plugin plugin;
    
    /**
     * Creates a new command processor for the given plugin.
     *
     * @param plugin - The plugin registering commands.
     */
    public CommandProcessor(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Registers the given command.
     *
     * @param simpleCommand - The command to register.
     */
    public void registerCommand(@NotNull SimpleCommand simpleCommand) {
        registerCommands(simpleCommand);
    }
    
    /**
     * Registers commands in the exact order supplied.
     *
     * @param commands - The commands to register.
     * @throws IllegalArgumentException if no commands are supplied.
     * @throws IllegalStateException if called outside the plugin enable lifecycle phase.
     */
    public void registerCommands(@NotNull SimpleCommand... commands) {
        if (commands.length == 0) {
            throw new IllegalArgumentException("There must be at least one command!");
        }
        
        final List<SimpleCommand> commandsToRegister = List.of(commands);
        
        for (final SimpleCommand command : commandsToRegister) {
            if (command.isRegistered()) {
                throw new IllegalStateException("Command already registered! (%s)".formatted(command.getName()));
            }
        }
        
        try {
            plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
                final var registrar = event.registrar();
                
                for (final SimpleCommand command : commandsToRegister) {
                    registrar.register(
                            command.getName(),
                            command.getDescription(),
                            List.of(command.getAliases()),
                            new SimpleCommandAdapter(command)
                    );
                }
            });
        }
        catch (IllegalStateException ex) {
            throw new IllegalStateException(
                    "Commands must be registered during the plugin's onEnable() method!",
                    ex
            );
        }
        
        commandsToRegister.forEach(SimpleCommand::markRegistered);
        
        COMMANDS_BY_PLUGIN.compute(plugin, (ignored, existing) -> {
            final List<SimpleCommand> updated = existing == null ? new ArrayList<>() : existing;
            updated.addAll(commandsToRegister);
            return updated;
        });
    }
    
    /**
     * Gets an immutable view of the commands registered by the given plugin.
     *
     * @param plugin - The plugin.
     * @return an immutable command list.
     */
    @NotNull
    public static List<SimpleCommand> getCommands(@NotNull Plugin plugin) {
        final List<SimpleCommand> commands = COMMANDS_BY_PLUGIN.get(plugin);
        
        return commands != null ? List.copyOf(commands) : List.of();
    }
    
    private record SimpleCommandAdapter(SimpleCommand command) implements BasicCommand {
        
        @Override
        public boolean canUse(@NotNull CommandSender sender) {
            return command instanceof DisabledCommand || command.canExecute(sender);
        }
        
        @Override
        public void execute(@NotNull CommandSourceStack source, @NotNull String[] args) {
            command.executeSafely(source.getSender(), ArgumentList.create(args));
        }
        
        @Override
        @NotNull
        public List<String> suggest(@NotNull CommandSourceStack source, @NotNull String[] args) {
            final String[] normalizedArgs = args.length == 0 ? new String[] { "" } : args;
            return command.tabCompleteSafely(source.getSender(), ArgumentList.create(normalizedArgs));
        }
    }
}
