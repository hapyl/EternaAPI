package me.hapyl.eterna;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.eterna.builtin.updater.Updater;
import me.hapyl.eterna.module.util.Runnables;
import me.hapyl.eterna.module.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.NumberConversions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Represents the API.
 *
 * <div>
 *     Plugin must register itself to use some feature.
 *     (Not yet implemented)
 * </div>
 *
 * <pre>
 *     new EternaAPI(this);
 * </pre>
 */
public final class EternaAPI {

    private static final Map<String, EternaAPI> byName = Maps.newLinkedHashMap();

    private final JavaPlugin plugin;
    private final String minVersion;

    /**
     * Instantiates the API for the given {@link JavaPlugin}.
     *
     * @param plugin - Plugin.
     * @throws IllegalArgumentException if the given plugin is already registered.
     * @throws IllegalArgumentException if the given plugin doesn't depend nor soft-depend EternaAPI.
     */
    public EternaAPI(@Nonnull JavaPlugin plugin) {
        this(plugin, null);
    }

    /**
     * Instantiates the API for the given {@link JavaPlugin}.
     *
     * @param plugin     - Plugin.
     * @param minVersion - Minimum required version of the EternaAPI plugin.
     * @throws IllegalArgumentException if the given plugin is already registered.
     *                                  if the given plugin doesn't depend nor soft-depend EternaAPI.
     *                                  if the current version is lower than the min version.
     */
    public EternaAPI(@Nonnull JavaPlugin plugin, @Nullable String minVersion) {
        if (exists(plugin.getName())) {
            throw new IllegalArgumentException("Plugin named '%s' is already registered in EternaAPI!".formatted(plugin.getName()));
        }

        this.plugin = plugin;
        this.minVersion = minVersion;

        Validate.isTrue(
                isDepends(),
                "Could not load %s for %s since it doesn't depend on nor soft-depend the API!".formatted(getAPIName(), plugin.getName())
        );

        if (minVersion != null) {
            checkVersionAndDisable(minVersion);
        }

        byName.put(plugin.getName().toLowerCase(Locale.ROOT), this);
    }

    @Nullable
    public String getMinVersion() {
        return minVersion;
    }

    /**
     * Returns owning plugin.
     *
     * @return owning plugin.
     */
    @Nonnull
    public JavaPlugin getPlugin() {
        return plugin;
    }

    /**
     * Returns true if plugin depends on EternaAPI. If false, exception will be thrown.
     *
     * @return true if plugin depends on EternaAPI. If false, exception will be thrown.
     */
    public boolean isDepends() {
        final PluginDescriptionFile description = plugin.getDescription();
        final String pluginName = getAPIName();
        return description.getDepend().contains(pluginName) || description.getSoftDepend().contains(pluginName);
    }

    /**
     * Loads all APIs and their respective registries.
     * This is not yet implemented and only displays the modules.
     */
    static void loadAll() {
        if (byName.isEmpty()) {
            return;
        }

        Runnables.runLater(() -> {
            EternaLogger.broadcastMessageConsole("&eAPI version is %s. Loading plugins...".formatted(getAPIVersion()));
            EternaLogger.broadcastMessageOP("&eAPI version is %s. Loading plugins...".formatted(getAPIVersion()));
            byName.forEach((name, api) -> {
                // api.getLibrary().load();

                final JavaPlugin javaPlugin = api.getPlugin();
                final String string = "Loaded %s v%s.".formatted(javaPlugin.getName(), javaPlugin.getDescription().getVersion());

                EternaLogger.broadcastMessageOP(string);
                EternaLogger.broadcastMessageConsole(string);
            });
        }, 20L);
    }

    private void throwAndDisableAPI(String message) {
        final EternaPlugin plugin = EternaPlugin.getPlugin();
        final PluginManager pluginManager = Bukkit.getPluginManager();

        EternaLogger.broadcastMessageConsole(message);
        pluginManager.disablePlugin(plugin);

        throw new IllegalArgumentException(message);
    }

    private void checkVersionAndDisable(String minVersion) {
        final String current = getAPIVersion().replaceFirst(Updater.VERSION_REGEX, "");
        minVersion = minVersion.replaceFirst(Updater.VERSION_REGEX, "");

        final String[] currentParts = current.split("\\.");
        final String[] remoteParts = minVersion.split("\\.");

        int length = Math.max(currentParts.length, remoteParts.length);
        for (int i = 0; i < length; i++) {
            final int thisPart = i < currentParts.length ? NumberConversions.toInt(currentParts[i]) : 0;
            final int thatPart = i < remoteParts.length ? NumberConversions.toInt(remoteParts[i]) : 0;

            if (thisPart >= thatPart) {
                break;
            }

            final EternaPlugin eternaPlugin = EternaPlugin.getPlugin();
            final PluginManager pluginManager = Bukkit.getPluginManager();

            EternaLogger.severe("εεε ");
            EternaLogger.severe("εεε Could not load %s!".formatted(plugin.getName()));
            EternaLogger.severe("εεε It requires EternaAPI version %s!".formatted(minVersion));
            EternaLogger.severe("εεε You are on %s! Please update EternaAPI! ".formatted(current));
            EternaLogger.severe("εεε ");

            pluginManager.disablePlugin(eternaPlugin);
        }
    }

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
     * Return a new list of EternaAPI modules.
     *
     * @return a new list of EternaAPI modules.
     */
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
