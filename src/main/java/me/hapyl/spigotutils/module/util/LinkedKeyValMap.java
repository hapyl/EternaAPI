package me.hapyl.spigotutils.module.util;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class LinkedKeyValMap<K, V> extends HashMap<K, V> {

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

}
