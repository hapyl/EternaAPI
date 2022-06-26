package me.hapyl.spigotutils.module.util;

import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class DependencyInjector<T extends JavaPlugin> {

    private final T plugin;

    public DependencyInjector(T plugin) {
        this.plugin = plugin;
    }

    @Nonnull
    public final T getPlugin() {
        return plugin;
    }
}
