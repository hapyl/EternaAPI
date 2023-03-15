package me.hapyl.spigotutils.module.command;

import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.annotate.TestedReflection;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class CommandProcessor {

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

    // 1.1 - reduced reflection call for array commands registration
    // Pretty sure you still have to use reflection to do so
    // if not, feel free to modify this code without ooflection
    private void registerCommand0(@Nonnull SimpleCommand[] array) {
        try {
            if (array.length == 0) {
                throw new IllegalArgumentException("There must be at least one command!");
            }

            final SimpleCommandMap simpleMap = getCommandMap();

            for (final SimpleCommand cmd : array) {
                final Command command = cmd.createCommand();

                // Register Command
                simpleMap.register(this.plugin.getName(), command);
                cmd.tryArgumentProcessor();
            }

        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    /**
     * Gets the command map for this server.
     *
     * @return command map for this server.
     * @throws IllegalAccessException if failed to retrieve command map.
     */
    @TestedReflection(version = "1.19.4")
    public static SimpleCommandMap getCommandMap() throws IllegalAccessException {
        final PluginManager manager = Bukkit.getServer().getPluginManager();
        return (SimpleCommandMap) FieldUtils.getDeclaredField(manager.getClass(), "commandMap", true).get(manager);
    }

}
