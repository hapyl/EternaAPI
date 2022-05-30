package me.hapyl.spigotutils.module.util;

import com.google.common.collect.Maps;

import javax.annotation.Nullable;
import java.util.Map;

public class KPVHashMap<K, P, V> {

    private final Map<KeyPair<K, P>, V> map;

    public KPVHashMap() {
        map = Maps.newConcurrentMap();
    }

    @Nullable
    public V get(K k, P p) {
        return getOrDefault(k, p, null);
    }

    @Nullable
    public V getOrDefault(K k, P p, V def) {
        return map.getOrDefault(KeyPair.of(k, p), def);
    }

}
