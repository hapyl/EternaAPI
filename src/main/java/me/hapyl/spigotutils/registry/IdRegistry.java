package me.hapyl.spigotutils.registry;

import me.hapyl.spigotutils.module.entity.IdHolder;
import org.bukkit.plugin.java.JavaPlugin;

public class IdRegistry<V extends IdHolder> extends Registry<Integer, V> {

    private int next;

    public IdRegistry(JavaPlugin plugin) {
        super(plugin);
    }

    public void registerValue(V v) {
        v.setId(next++);
        super.register(v.getId(), v);
    }

    public void unregisterValue(V v) {
        super.unregister(v.getId());
    }

    @Override
    public void register(Integer integer, V v) {
        registerValue(v);
    }

    @Override
    public void unregister(Integer integer, V v) {
        unregisterValue(v);
    }

    @Deprecated
    @Override
    public void register(Integer integer) throws IllegalArgumentException {
        throw new IllegalArgumentException("Cannot register integer");
    }

    @Deprecated
    @Override
    public void unregister(Integer integer) {
        throw new IllegalArgumentException("Cannot unregister integer");
    }
}