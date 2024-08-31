package me.hapyl.eterna.module.util;

import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class DependencyInjector<T extends JavaPlugin> {

    private final T plugin;

    public DependencyInjector(@Nonnull T plugin) {
        this.plugin = plugin;
    }

    @Nonnull
    public final T getPlugin() {
        return plugin;
    }
}
