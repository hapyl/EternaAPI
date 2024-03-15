package me.hapyl.spigotutils.registry;

import me.hapyl.spigotutils.module.entity.IdHolder;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

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
    public void register(@Nonnull Integer integer, @Nonnull V v) {
        registerValue(v);
    }

    @Override
    public boolean unregister(@Nonnull Integer integer, @Nonnull V v) {
        unregisterValue(v);
    }

    @Deprecated
    @Override
    public void register(@Nonnull Integer integer) throws IllegalArgumentException {
        throw new IllegalArgumentException("Cannot register integer");
    }

    @Deprecated
    @Override
    public void unregister(@Nonnull Integer integer) {
        throw new IllegalArgumentException("Cannot unregister integer");
    }
}