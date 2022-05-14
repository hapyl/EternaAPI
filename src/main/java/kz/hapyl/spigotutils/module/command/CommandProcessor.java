package kz.hapyl.spigotutils.module.command;

import kz.hapyl.spigotutils.EternaPlugin;
import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.player.PlayerLib;
import kz.hapyl.spigotutils.module.util.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

            final PluginManager manager = this.plugin.getServer().getPluginManager();
            final Class<? extends PluginManager> clazz = manager.getClass();
            final Field field = clazz.getDeclaredField("commandMap");

            field.setAccessible(true);
            final SimpleCommandMap simpleMap = (SimpleCommandMap) field.get(manager);

            for (final SimpleCommand cmd : array) {

                if (simpleMap.getCommand(cmd.getName()) != null) {
                    throw new IllegalArgumentException(String.format(
                            "Command %s already registered in %s!",
                            cmd.getName(),
                            this.plugin.getName()
                    ));
                }

                simpleMap.register(
                        this.plugin.getName(),
                        new Command(cmd.getName(), cmd.getDescription(), cmd.getUsage(), Arrays.asList(cmd.getAliases())) {

                            // Register Command
                            @Override
                            public boolean execute(CommandSender sender, String label, String[] args) {
                                if (cmd.isOnlyForPlayers() && !(sender instanceof Player)) {
                                    sender.sendMessage(ChatColor.RED + "You must be a player to use perform this command!");
                                    return true;
                                }

                                // permission check
                                if ((cmd.isAllowOnlyOp() && !sender.isOp()) || !sender.hasPermission(cmd.getPermission())) {
                                    sender.sendMessage(ChatColor.RED + "No permissions.");
                                    return true;
                                }

                                // cooldown check
                                if (cmd.hasCooldown() && sender instanceof final Player playerSender) {
                                    final CommandCooldown cooldown = cmd.getCooldown();
                                    if (cooldown.hasCooldown(playerSender)) {
                                        Chat.sendMessage(
                                                playerSender,
                                                "&cThis command is on cooldown for %ss!",
                                                BukkitUtils.roundTick((int) (cooldown.getTimeLeft(playerSender) / 50L))
                                        );
                                        PlayerLib.playSound(playerSender, Sound.ENTITY_ENDERMAN_TELEPORT, 0.0f);
                                        return true;
                                    }
                                    if (!cooldown.canIgnoreCooldown(playerSender)) {
                                        cooldown.startCooldown(playerSender);
                                    }
                                }

                                cmd.execute(sender, args);

                                // check post processor
                                if (cmd.getPost() != null && args != null) {
                                    cmd.getPost().compareAndInvoke(sender, args);
                                }

                                return true;
                            }

                            // Register Tab Completer
                            @Override
                            public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
                                if (cmd.isOnlyForPlayers() && !(sender instanceof Player)) {
                                    return Collections.emptyList();
                                }
                                final List<String> strings = cmd.tabComplete(sender, args);
                                if (cmd.isTabCompleteArgumentProcessor()) {
                                    cmd.getPost().tabComplete(strings, args);
                                }
                                return strings == null ? defaultCompleter() : strings;
                            }
                        }
                );

            }

            field.setAccessible(false);
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    private static List<String> defaultCompleter() {
        final List<String> list = new ArrayList<>();
        for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            list.add(onlinePlayer.getName());
        }
        return list;
    }

    @Deprecated
    public static String getProcessorVersion() {
        return "getProcessorVersion() is deprecated";
    }

    public static void checkForUpdate() {
        throw new UnsupportedOperationException("not implemented yet");
    }

}
