package me.hapyl.eterna.registry;

import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class SimplePluginRegistry<T extends Keyed> extends SimpleRegistry<T> {

    private final JavaPlugin plugin;

    public SimplePluginRegistry(@Nonnull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Nonnull
    public JavaPlugin getPlugin() {
        return plugin;
    }
}
