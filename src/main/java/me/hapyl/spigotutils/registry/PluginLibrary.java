package me.hapyl.spigotutils.registry;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class is used to store per-plugin registries.
 */
public class PluginLibrary {

    private final JavaPlugin plugin;

    public PluginLibrary(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
    }
}
