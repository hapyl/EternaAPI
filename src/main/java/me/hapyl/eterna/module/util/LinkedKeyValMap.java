package me.hapyl.eterna.module.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Concurrent map that allows to get key by its value.
 *
 * @param <K> - The key.
 * @param <V> - The value.
 */
public class LinkedKeyValMap<K, V> extends ConcurrentHashMap<K, V> {

    public LinkedKeyValMap<K, V> insert(K k, V v) {
        put(k, v);
        return this;
    }

    /**
     * Returns the key of the value, or null if not found.
     *
     * @param v - The value.
     * @return the key of the value, or null if not found.
     */
    @Nullable
    public K getKey(V v) {
        for (Map.Entry<K, V> entry : this.entrySet()) {
            if (entry.getValue().equals(v)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Returns the key of the value, or the default value if not found.
     *
     * @param v   - The value.
     * @param def - The default value.
     * @return the key of the value, or the default value if not found.
     */
    @Nonnull
    public K getKey(V v, @Nonnull K def) {
        final K key = getKey(v);
        return key == null ? def : key;
    }

    /**
     * Returns the value of the key, or null if not found.
     *
     * @param k - The key.
     * @return the value of the key, or null if not found.
     */
    @Nullable
    public V getValue(K k) {
        return getOrDefault(k, null);
    }

    /**
     * Returns the value of the key, or the default value if not found.
     *
     * @param k   - The key.
     * @param def - The default value.
     * @return the value of the key, or the default value if not found.
     */
    public V getValue(K k, V def) {
        return getOrDefault(k, def);
    }

    /**
     * Uses action for the value of the key, then removes the key.
     *
     * @param k      - The key.
     * @param action - The action.
     */
    public void useValueAndRemove(@Nonnull K k, @Nonnull Consumer<V> action) {
        final V v = get(k);

        if (v != null) {
            action.accept(v);
        }

        remove(k);
    }

    /**
     * Uses action for the key of the value, then removes the key.
     *
     * @param v      - The value.
     * @param action - The action.
     */
    public void useKeyAndRemove(@Nonnull V v, @Nonnull Consumer<K> action) {
        final K k = getKey(v);

        if (k != null) {
            action.accept(k);
            remove(k);
        }
    }

    public static <K, V> LinkedKeyValMap<K, V> of(K k, V v) {
        return new LinkedKeyValMap<K, V>().insert(k, v);
    }

    public static <K, V> LinkedKeyValMap<K, V> of() {
        return new LinkedKeyValMap<>();
    }

    public static <K, V> LinkedKeyValMap<K, V> empty() {
        return of();
    }

}
