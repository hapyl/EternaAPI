package kz.hapyl.spigotutils;

public abstract class Registry<T> {

    protected final EternaPlugin plugin;

    public Registry(EternaPlugin plugin) {
        this.plugin = plugin;
    }

    public abstract void register(T t);

    public abstract void unregister(T t);

    public final EternaPlugin getPlugin() {
        return plugin;
    }
}
