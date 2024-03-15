package me.hapyl.spigotutils.registry;

import com.google.common.collect.Maps;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Logger;

public abstract class Registry<K, V> implements IRegistry<K, V> {

    protected final Logger logger;
    protected final Map<K, V> registry = Maps.newLinkedHashMap();

    private final JavaPlugin owningPlugin;

    /**
     * Creates instance of registry.
     *
     * @param plugin - Eterna Main.
     */
    public Registry(@Nonnull JavaPlugin plugin) {
        this.owningPlugin = plugin;
        this.logger = plugin.getLogger();
    }

    @Override
    @Nonnull
    public final JavaPlugin getPlugin() {
        return owningPlugin;
    }

    @Override
    public void register(@Nonnull K k, @Nullable V v) {
        if (registry.containsKey(k)) {
            return;
        }

        registry.put(k, v);
    }

    @Override
    public boolean unregister(@Nonnull K k, @Nonnull V v) {
        final V v1 = registry.get(k);

        if (v1 == null || !v1.equals(v)) {
            return false;
        }

        return unregister(k);
    }

    @Override
    public boolean unregister(@Nonnull K k) {
        return registry.remove(k) != null;
    }

    @Override
    @Nonnull
    public final Map<K, V> getRegistryCopy() {
        return Maps.newHashMap(this.registry);
    }

    @Override
    public void forEach(@Nonnull BiConsumer<K, V> consumer) {
        registry.forEach((k, v) -> {
            if (k == null || v == null) {
                return;
            }

            consumer.accept(k, v);
        });
    }

    /**
     * Accepts consumer on all non-null keys.
     *
     * @param consumer - Consumer.
     */
    public final void forEachKeys(Consumer<K> consumer) {
        registry.keySet().forEach(k -> {
            if (k == null) {
                return;
            }

            consumer.accept(k);
        });
    }

    /**
     * Accepts consumer on all non-null values.
     *
     * @param consumer - Consumer.
     */
    public final void forEachValues(Consumer<V> consumer) {
        registry.values().forEach(v -> {
            if (v == null) {
                return;
            }

            consumer.accept(v);
        });
    }

    @Override
    public boolean isRegistered(@Nonnull K k) {
        return registry.containsKey(k);
    }

    @Override
    public boolean isRegistered(@Nonnull K k, @Nonnull V v) {
        return v.equals(registry.get(k));
    }

    @Override
    @Nullable
    public final V byKey(@Nonnull K k) {
        return registry.get(k);
    }

    @Override
    @Nullable
    public final K byValue(@Nonnull V v) {
        for (K k : registry.keySet()) {
            final V v1 = registry.get(k);
            if (v1 == null) {
                continue;
            }

            if (v1.equals(v)) {
                return k;
            }
        }

        return null;
    }

    @Override
    public int size() {
        return registry.size();
    }

    /**
     * Returns keys of the original hash map.
     *
     * @return keys of the original hash map.
     */
    protected final Set<K> getKeys() {
        return registry.keySet();
    }

    /**
     * Returns values of the original hash map.
     *
     * @return values of the original hash map.
     */
    protected final Collection<V> getValues() {
        return registry.values();
    }

    /**
     * Returns an original hash map of this registry.
     *
     * @return original hash map of this registry.
     */
    @Nonnull
    protected final Map<K, V> getRegistry() {
        return registry;
    }
}
