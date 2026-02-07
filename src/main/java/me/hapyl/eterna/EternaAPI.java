package me.hapyl.eterna;

import io.papermc.paper.plugin.configuration.PluginMeta;
import me.hapyl.eterna.builtin.updater.Updater;
import me.hapyl.eterna.module.math.Numbers;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a utility validator for implementations.
 */
public final class EternaAPI {
    
    private final Plugin plugin;
    private final String requiredVersion;
    
    private EternaAPI(@NotNull Plugin plugin, @Nullable String requiredVersion) {
        this.plugin = plugin;
        this.requiredVersion = requiredVersion;
    }
    
    private void validateDependOrSoftDepend() {
        final PluginMeta pluginMeta = plugin.getPluginMeta();
        final String eternaPluginName = Eterna.getPlugin().getName();
        
        if (pluginMeta.getPluginDependencies().contains(eternaPluginName) || pluginMeta.getPluginSoftDependencies().contains(eternaPluginName)) {
            return;
        }
        
        severeShutdownServer("%s doesn't `depend` nor `soft-depend` on %s!".formatted(plugin.getName(), eternaPluginName));
    }
    
    private void validateRequiredVersion() {
        if (this.requiredVersion == null) {
            return;
        }
        
        final String pluginVersion = Eterna.getPlugin().getPluginMeta().getVersion().replaceFirst(Updater.VERSION_REGEX, "");
        final String requiredVersion = this.requiredVersion.replaceFirst(Updater.VERSION_REGEX, "");
        
        final String[] pluginVersionParts = pluginVersion.split("\\.");
        final String[] requiredVersionParts = requiredVersion.split("\\.");
        
        final int length = Math.max(pluginVersionParts.length, requiredVersionParts.length);
        
        for (int i = 0; i < length; i++) {
            final int pluginVersionPart = i < pluginVersionParts.length ? Numbers.toInt(pluginVersionParts[i]) : 0;
            final int requiredVersionPart = i < requiredVersionParts.length ? Numbers.toInt(requiredVersionParts[i]) : 0;
            
            // If any plugin part is lower than the required version, kill server
            if (pluginVersionPart < requiredVersionPart) {
                severeShutdownServer(
                        "`%s` requires version %s, you're on `%s`!".formatted(plugin.getName(), requiredVersion, pluginVersion),
                        "Please download the latest version from github!",
                        "https://github.com/hapyl/EternaAPI/releases"
                );
            }
        }
    }
    
    /**
     * A static utility method to validate the implementation {@link Plugin}.
     *
     * <p>
     * Plugins should generally call this method on their {@code onEnable()} method to validate important stuff.
     * </p>
     *
     * @param plugin          - The implementing plugin.
     * @param requiredVersion - The minimum required version of the api.
     */
    public static void instantiate(@NotNull Plugin plugin, @Nullable String requiredVersion) {
        final EternaAPI eternaApi = new EternaAPI(plugin, requiredVersion);
        
        eternaApi.validateDependOrSoftDepend();
        eternaApi.validateRequiredVersion();
    }
    
    /**
     * A static utility method to validate the implementation {@link Plugin}.
     * <p>Plugins should generally call this method on their {@code onEnable()} method to validate important stuff.</p>
     *
     * @param plugin - The implementing plugin.
     */
    public static void instantiate(@NotNull Plugin plugin) {
        instantiate(plugin, null);
    }
    
    private static void severeShutdownServer(@NotNull String... reasons) {
        final EternaPlugin eternaPlugin = Eterna.getPlugin();
        final String pluginName = eternaPlugin.getName();
        
        EternaLogger.severe("εεε | ");
        EternaLogger.severe("εεε | Failed to load %s, server shutting down...".formatted(pluginName));
        
        for (String reason : reasons) {
            EternaLogger.severe("εεε | " + reason);
        }
        
        EternaLogger.severe("εεε | ");
        
        Bukkit.getServer().shutdown();
    }
    
}
