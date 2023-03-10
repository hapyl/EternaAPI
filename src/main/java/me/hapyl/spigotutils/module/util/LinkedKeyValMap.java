package me.hapyl.spigotutils.module.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LinkedKeyValMap<K, V> extends ConcurrentHashMap<K, V> {

    public static <K, V> LinkedKeyValMap<K, V> of(K k, V v) {
        return new LinkedKeyValMap<K, V>().insert(k, v);
    }

    public static <K, V> LinkedKeyValMap<K, V> of() {
        return new LinkedKeyValMap<>();
    }

    public static <K, V> LinkedKeyValMap<K, V> empty() {
        return of();
    }

    public LinkedKeyValMap<K, V> insert(K k, V v) {
        put(k, v);
        return this;
    }

    @Nullable
    public K getKey(V v) {
        for (Map.Entry<K, V> entry : this.entrySet()) {
            if (entry.getValue().equals(v)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Nullable
    public V getValue(K k) {
        return getOrDefault(k, null);
    }

    public void useValueAndRemove(@Nonnull K k, @Nonnull Action<V> action) {
        final V v = get(k);

        if (v != null) {
            action.use(v);
        }

        remove(k);
    }

    public void useKeyAndRemove(@Nonnull V v, @Nonnull Action<K> action) {
        final K k = getKey(v);

        if (k != null) {
            action.use(k);
            remove(k);
        }
    }

}
