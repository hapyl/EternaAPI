package kz.hapyl.spigotutils;

import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.error.EternaException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class EternaAPI {

    private static final String PREFIX = "&b&lEternaAPI&b> &a";
    private final JavaPlugin plugin;

    public EternaAPI(JavaPlugin init) {
        this(init, false);
    }

    public EternaAPI(JavaPlugin init, boolean broadcastMessageOnlyToConsole) {

        if (init == null) {
            throw new EternaException("Could not load EternaAPI since provided plugin is null!");
        }

        if (!isDepends(init)) {
            throw new EternaException(String.format(
                    "Could not load EternaAPI for %s since it's doesn't depend nor soft-depends the API!",
                    init.getName()
            ));
        }

        this.plugin = init;
        final String formattedMessage = String.format(
                "%s implements EternaAPI v%s.",
                plugin.getName(),
                getPluginVersion()
        );

        new BukkitRunnable() {
            @Override
            public void run() {

                // broadcast to console
                Bukkit.getConsoleSender().sendMessage(formattedMessage);

                if (broadcastMessageOnlyToConsole) {
                    return;
                }

                // broadcast to admins
                broadcastAPIMessage(formattedMessage);
            }
        }.runTaskLater(plugin, 20);
    }

    private boolean isDepends(JavaPlugin plugin) {
        final PluginDescriptionFile description = plugin.getDescription();
        final String pluginName = EternaPlugin.getPlugin().getName();
        return description.getDepend().contains(pluginName) || description.getSoftDepend().contains(pluginName);
    }

    public String getPluginVersion() {
        return EternaPlugin.getPlugin().getDescription().getVersion();
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public static void broadcastAPIMessage(String string) {
        Chat.broadcastOp(PREFIX + string);
    }

    public static void sendAPIMessage(CommandSender receiver, String string, Object... replacements) {
        Chat.sendMessage(receiver, PREFIX + string, replacements);
    }

}
