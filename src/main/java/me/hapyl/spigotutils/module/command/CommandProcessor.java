package me.hapyl.spigotutils.module.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.annotate.TestedOn;
import me.hapyl.spigotutils.module.annotate.Version;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
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
     * This is not recommended to avoid IllegalArgumentException from {@link CommandProcessor#registerCommand0(SimpleCommand[])}
     */
    public CommandProcessor() {
        this(EternaPlugin.getPlugin());
    }

    /**
     * Creates a CommandProcessor with provided plugin owner.
     * Plugins will have provided plugins' signature. Ex: /plugin_name:command_name
     *
     * @param plugin - Plugin to register command for.
     */
    public CommandProcessor(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Registers a command.
     *
     * @param simpleCommand - Command to register.
     * @throws IllegalArgumentException - If command is already registered for this plugin.
     */
    public void registerCommand(SimpleCommand simpleCommand) {
        this.registerCommand0(new SimpleCommand[] { simpleCommand });
    }

    /**
     * Registers multiple commands.
     *
     * @param commands - Array of commands to register.
     * @throws IllegalArgumentException - If command is already registered for this plugin.
     */
    public void registerCommands(SimpleCommand... commands) {
        this.registerCommand0(commands);
    }

    private void registerCommand0(@Nonnull SimpleCommand[] commands) {
        try {
            if (commands.length == 0) {
                throw new IllegalArgumentException("There must be at least one command!");
            }

            final SimpleCommandMap simpleMap = getCommandMap();

            for (final SimpleCommand simpleCommand : commands) {
                final Command command = simpleCommand.createCommand();

                // Register Command
                simpleMap.register(plugin.getName(), command);
                COMMANDS_BY_PLUGIN.compute(plugin, (pl, list) -> {
                    if (list == null) {
                        list = Lists.newArrayList();
                    }

                    list.add(simpleCommand);
                    return list;
                });
            }

        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    /**
     * Gets the command map for this server.
     *
     * @return command map for this server.
     * @throws IllegalAccessException if failed to retrieve the command map.
     */
    @TestedOn(version = Version.V1_21)
    public static SimpleCommandMap getCommandMap() throws IllegalAccessException {
        final PluginManager manager = Bukkit.getServer().getPluginManager();
        return (SimpleCommandMap) FieldUtils.getDeclaredField(manager.getClass(), "commandMap", true).get(manager);
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
