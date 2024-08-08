package me.hapyl.eterna.module.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.annotate.TestedOn;
import me.hapyl.eterna.module.annotate.Version;
import me.hapyl.eterna.module.util.Compute;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CommandProcessor {

    private static final Map<Plugin, List<SimpleCommand>> COMMANDS_BY_PLUGIN = Maps.newHashMap();

    private final JavaPlugin plugin;

    /**
     * Creates a CommandProcessor with plugin owner of EternaAPI.
     */
    @Deprecated(forRemoval = true, since = "4.2.1")
    public CommandProcessor() {
        throw new IllegalArgumentException("Don't delegate commands to Eterna!");
    }

    /**
     * Creates a CommandProcessor with provided plugin owner.
     * Plugins will have provided plugins' signature. Ex: /plugin_name:command_name
     *
     * @param plugin - Plugin to register command for.
     */
    public CommandProcessor(@Nonnull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Registers a command.
     *
     * @param simpleCommand - Command to register.
     * @throws IllegalArgumentException - If command is already registered for this plugin.
     */
    public void registerCommand(@Nonnull SimpleCommand simpleCommand) {
        this.registerCommand0(new SimpleCommand[] { simpleCommand });
    }

    /**
     * Registers multiple commands.
     *
     * @param commands - Array of commands to register.
     * @throws IllegalArgumentException - If command is already registered for this plugin.
     */
    public void registerCommands(@Nonnull SimpleCommand... commands) {
        this.registerCommand0(commands);
    }

    private void registerCommand0(@Nonnull SimpleCommand[] commands) {
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

        } catch (Exception exception) {
            EternaLogger.exception(exception);
        }
    }

    /**
     * Gets the command map for this server.
     *
     * @return command map for this server.
     * @deprecated use {@link Bukkit#getCommandMap()}
     */
    @Nonnull
    @Deprecated
    public static CommandMap getCommandMap() {
        return Bukkit.getCommandMap();
    }

    /**
     * Gets an immutable list of commands registered by a plugin.
     *
     * @param plugin - Plugin.
     * @return an immutable list of commands.
     */
    public static List<SimpleCommand> getCommands(@Nonnull Plugin plugin) {
        return Collections.unmodifiableList(COMMANDS_BY_PLUGIN.computeIfAbsent(plugin, fn -> Lists.newArrayList()));
    }

}
