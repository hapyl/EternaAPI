package me.hapyl.spigotutils.registry;

import com.google.common.collect.Maps;
import me.hapyl.spigotutils.EternaPlugin;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class Registry<K, V> {

    private final EternaPlugin plugin;
    protected final Map<K, V> registry = Maps.newLinkedHashMap();

    /**
     * Creates instance of registry.
     *
     * @param plugin - Eterna Main.
     */
    public Registry(EternaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Returns Eterna Main.
     *
     * @return Eterna Main.
     */
    public final EternaPlugin getPlugin() {
        return plugin;
    }

    /**
     * Returns original hash map of this registry.
     *
     * @return original hash map of this registry.
     */
    protected final Map<K, V> getRegistry() {
        return registry;
    }

    /**
     * Returns a <b>copy</b> hash map of this registry.
     * Original hash map is only accessible to self.
     *
     * @return a <b>copy</b> hash map of this registry.
     */
    public final Map<K, V> getRegistryCopy() {
        return Maps.newHashMap(this.registry);
    }

    /**
     * Registers key to a value.
     * Ignored if key already has any non-null value assigned.
     *
     * @param k - Key.
     * @param v - Value.
     */
    public void register(K k, V v) {
        if (registry.containsKey(k)) {
            return;
        }
        registry.put(k, v);
    }

    /**
     * Registers key.
     * Sometimes registries only need to
     * register just keys.
     *
     * @param k - Key.
     */
    public void register(K k) {
        if (registry.containsKey(k)) {
            return;
        }

        registry.put(k, null);
    }

    /**
     * Unregisters value from a key if value matches 'v'.
     *
     * @param k - Key.
     * @param v - Value.
     */
    public void unregister(K k, V v) {
        final V v1 = registry.get(k);
        if (v1 == null || !v1.equals(v)) {
            return;
        }

        unregister(k);
    }

    /**
     * Unregisters values from a key.
     *
     * @param k - Key.
     */
    public void unregister(K k) {
        registry.remove(k);
    }

    /**
     * Accepts consumer on all non-null key and values.
     *
     * @param consumer - Consumer.
     */
    public final void forEach(BiConsumer<K, V> consumer) {
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
     * Returns keys of the original hash map.
     *
     * @return keys of the original hash map.
     */
    public final Set<K> getKeys() {
        return registry.keySet();
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

    /**
     * Returns values of the original hash map.
     *
     * @return values of the original hash map.
     */
    public final Collection<V> getValues() {
        return registry.values();
    }

    @CheckForNull
    public final V byKey(K k, V def) {
        if (k == null) {
            return def;
        }

        return registry.getOrDefault(k, def);
    }

    @Nullable
    public final V byKey(K k) {
        return byKey(k, null);
    }

    @CheckForNull
    public final K byValue(V v, K def) {
        for (K k : registry.keySet()) {
            final V v1 = registry.get(k);
            if (v1 == null) {
                continue;
            }

            if (v1.equals(v)) {
                return k;
            }
        }

        return def;
    }

    @Nullable
    public final K byValue(V v) {
        return byValue(v, null);
    }

}
