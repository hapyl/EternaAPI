package me.hapyl.spigotutils;

public abstract class HashRegistry<K, V> extends Registry<V> {
    public HashRegistry(EternaPlugin plugin) {
        super(plugin);
    }

    public abstract void register(K k, V v);

    public abstract void unregister(K k, V v);

    @Override
    public void register(V v) {
        register(null, v);
    }

    @Override
    public void unregister(V v) {
        unregister(null, v);
    }
}
