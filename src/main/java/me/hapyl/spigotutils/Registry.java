package me.hapyl.spigotutils;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class Registry<T> {

    protected final JavaPlugin plugin;

    public Registry(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public abstract void register(T t);

    public abstract void unregister(T t);

    public final JavaPlugin getPlugin() {
        return plugin;
    }
}
