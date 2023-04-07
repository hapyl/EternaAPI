package me.hapyl.spigotutils.module.util;

import com.google.common.collect.Maps;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * A HashMap that uses two keys.
 *
 * @param <K> - The first key.
 * @param <P> - The second key.
 * @param <V> - The value.
 */
public class KPVHashMap<K, P, V> {

    private final Map<KeyPair<K, P>, V> map;

    public KPVHashMap() {
        map = Maps.newConcurrentMap();
    }

    public KPVHashMap<K, P, V> insert(K k, P p, V v) {
        put(k, p, v);
        return this;
    }

    public boolean remove(K k, P p) {
        return map.remove(KeyPair.of(k, p)) != null;
    }

    public boolean contains(K k, P p) {
        return map.containsKey(KeyPair.of(k, p));
    }

    public boolean put(K k, P p, V v) {
        return map.put(KeyPair.of(k, p), v) != null;
    }

    @Nullable
    public V get(K k, P p) {
        return getOrDefault(k, p, null);
    }

    @Nullable
    public V getOrDefault(K k, P p, V def) {
        return map.getOrDefault(KeyPair.of(k, p), def);
    }

    public static <K, P, V> KPVHashMap<K, P, V> of() {
        return new KPVHashMap<>();
    }

    public static <K, P, V> KPVHashMap<K, P, V> of(K k, P p, V v) {
        final KPVHashMap<K, P, V> map = new KPVHashMap<>();
        map.put(k, p, v);
        return map;
    }

}
