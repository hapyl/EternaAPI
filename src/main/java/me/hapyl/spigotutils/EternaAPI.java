package me.hapyl.spigotutils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.spigotutils.module.util.Runnables;
import me.hapyl.spigotutils.module.util.Validate;
import me.hapyl.spigotutils.registry.PluginLibrary;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class EternaAPI {

    private static final Map<String, EternaAPI> byName = Maps.newLinkedHashMap();

    private final JavaPlugin plugin;
    private final PluginLibrary library;
    private final boolean silent;

    public EternaAPI(@Nonnull JavaPlugin plugin) {
        this(plugin, false);
    }

    public EternaAPI(@Nonnull JavaPlugin plugin, boolean silent) {
        if (exists(plugin.getName())) {
            throw new IllegalArgumentException("Plugin named '%s' is already registered in EternaAPI!");
        }

        this.plugin = plugin;
        this.silent = silent;

        Validate.notNull(plugin, "Could not load %s because plugin is null!".formatted(getAPIName()));
        Validate.isTrue(
                isDepends(),
                "Could not load %s for %s since it doesn't depend nor soft-depend the API!".formatted(getAPIName(), plugin.getName())
        );

        this.library = new PluginLibrary(plugin);
        byName.put(plugin.getName().toLowerCase(Locale.ROOT), this);
    }

    /**
     * Returns owning plugin.
     *
     * @return owning plugin
     */
    public JavaPlugin getPlugin() {
        return plugin;
    }

    /**
     * Returns library for this API.
     * Library stores important data such as registries etc.
     *
     * @return library for this API.
     */
    public PluginLibrary getLibrary() {
        return library;
    }

    /**
     * Returns true if plugin depend on EternaAPI; If false, exception will be thrown.
     *
     * @return true if plugin depend on EternaAPI; If false, exception will be thrown.
     */
    public boolean isDepends() {
        final PluginDescriptionFile description = plugin.getDescription();
        final String pluginName = getAPIName();
        return description.getDepend().contains(pluginName) || description.getSoftDepend().contains(pluginName);
    }

    // static members

    /**
     * Returns current API version.
     *
     * @return current API version.
     */
    public static String getAPIVersion() {
        return EternaPlugin.getPlugin().getDescription().getVersion();
    }

    /**
     * Returns current API name.
     *
     * @return current API name.
     */
    public static String getAPIName() {
        return EternaPlugin.getPlugin().getName();
    }

    /**
     * Loads all APIs and their respected registries.
     */
    public static void loadAll() {
        if (byName.isEmpty()) {
            return;
        }

        Runnables.runLater(() -> {
            EternaLogger.broadcastMessageConsole("&eAPI version is %s. Loading plugins...", getAPIVersion());
            EternaLogger.broadcastMessageOP("&eAPI version is %s. Loading plugins...", getAPIVersion());
            byName.forEach((name, api) -> {
                // api.getLibrary().load();

                final JavaPlugin javaPlugin = api.getPlugin();
                final String string = "Loaded %s v%s.".formatted(javaPlugin.getName(), javaPlugin.getDescription().getVersion());
                if (!api.silent) {
                    EternaLogger.broadcastMessageOP(string);
                }
                EternaLogger.broadcastMessageConsole(string);
            });
        }, 20L);
    }

    public static List<EternaAPI> listModules() {
        return Lists.newArrayList(byName.values());
    }

    public static List<String> listNames() {
        final List<EternaAPI> apis = listModules();
        final List<String> names = Lists.newArrayList();

        for (EternaAPI api : apis) {
            names.add(api.getPlugin().getName());
        }

        return names;
    }

    @Nullable
    public static EternaAPI byName(String name) {
        return byName.get(name.toLowerCase(Locale.ROOT));
    }

    public static boolean exists(String name) {
        return byName(name) != null;
    }

}
