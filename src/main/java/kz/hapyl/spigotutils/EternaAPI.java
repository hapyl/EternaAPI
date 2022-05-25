package kz.hapyl.spigotutils;

import com.google.common.collect.Lists;
import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.util.Runnables;
import kz.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.List;

public final class EternaAPI {

    private static final List<EternaAPI> plugins = Lists.newArrayList();
    private static final String PREFIX = "&b&lEternaAPI&b> &a";

    private final JavaPlugin plugin;
    private final EternaLibrary library;
    private final boolean silent;

    public EternaAPI(@Nonnull JavaPlugin plugin) {
        this(plugin, false);
    }

    public EternaAPI(@Nonnull JavaPlugin plugin, boolean silent) {
        this.plugin = plugin;
        this.silent = silent;

        Validate.notNull(plugin, "Could not load %s because plugin is null!".formatted(getAPIName()));
        Validate.isTrue(
                isDepends(),
                "Could not load %s for %s since it doesn't depend nor soft-depend the API!".formatted(getAPIName(), plugin.getName())
        );

        this.library = new EternaLibrary(this);

        plugins.add(this);
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public EternaLibrary getLibrary() {
        return library;
    }

    public boolean isDepends() {
        final PluginDescriptionFile description = plugin.getDescription();
        final String pluginName = getAPIName();
        return description.getDepend().contains(pluginName) || description.getSoftDepend().contains(pluginName);
    }

    public static String getAPIVersion() {
        return EternaPlugin.getPlugin().getDescription().getVersion();
    }

    public static String getAPIName() {
        return EternaPlugin.getPlugin().getName();
    }

    public static void loadAll() {
        Runnables.runLater(() -> {
            if (plugins.isEmpty()) {
                return;
            }
            broadcastAPIMessageConsole("&eAPI version is %s. Loading plugins...", getAPIVersion());
            broadcastAPIMessageOP("&eAPI version is %s. Loading plugins...", getAPIVersion());
            plugins.forEach(plugin -> {
                final JavaPlugin javaPlugin = plugin.getPlugin();
                final String string = "Loaded %s v%s.".formatted(javaPlugin.getName(), javaPlugin.getDescription().getVersion());
                if (!plugin.silent) {
                    broadcastAPIMessageOP(string);
                }
                broadcastAPIMessageConsole(string);
            });
        }, 20L);
    }

    public static List<EternaAPI> getPlugins() {
        return plugins;
    }

    public static void broadcastAPIMessageOP(String string, Object... objects) {
        Chat.broadcastOp(PREFIX + string, objects);
    }

    public static void broadcastAPIMessageConsole(String string, Object... replacements) {
        Chat.sendMessage(Bukkit.getConsoleSender(), PREFIX + string, replacements);
    }

}
