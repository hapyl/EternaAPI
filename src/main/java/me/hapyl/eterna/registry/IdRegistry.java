package me.hapyl.eterna.registry;

import me.hapyl.eterna.module.entity.IdHolder;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IdRegistry<V extends IdHolder> extends Registry<Integer, V> {

    private int next;

    public IdRegistry(JavaPlugin plugin) {
        super(plugin);
    }

    public void registerValue(V v) {
        v.setId(next++);
        super.register(v.getId(), v);
    }

    public boolean unregisterValue(V v) {
        return super.unregister(v.getId());
    }

    @Override
    public void register(@Nonnull Integer integer, @Nullable V v) {
        if (v == null) {
            throw new IllegalArgumentException("v cannot be null");
        }

        registerValue(v);
    }

    @Override
    public boolean unregister(@Nonnull Integer integer, @Nonnull V v) {
        return unregisterValue(v);
    }

    @Deprecated
    @Override
    public void register(@Nonnull Integer integer) throws IllegalArgumentException {
        throw new IllegalArgumentException("Cannot register integer");
    }

    @Deprecated
    @Override
    public boolean unregister(@Nonnull Integer integer) {
        throw new IllegalArgumentException("Cannot unregister integer");
    }
}