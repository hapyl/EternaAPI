package me.hapyl.spigotutils.module.util;


import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * @deprecated Use {@link LinkedKeyValMap} instead.
 */
@Deprecated
public class Gap<K, V> {

    private final Map<K, V> hashMap;
    private final Map<V, K> hashBack;

    public Gap() {
        this.hashMap = new HashMap<>();
        this.hashBack = new HashMap<>();
    }

    public Gap<K, V> put(K key, V value) {
        this.putBoth(key, value);
        return this;
    }

    @Nullable
    public V getValue(K key) {
        return this.hashMap.getOrDefault(key, null);
    }

    @Nullable
    public K getKey(V value) {
        return this.hashBack.getOrDefault(value, null);
    }

    public void clear() {
        this.hashMap.clear();
        this.hashBack.clear();
    }

    public boolean hasKey(K key) {
        return this.hashMap.containsKey(key);
    }

    public boolean hasValue(V value) {
        return this.hashMap.containsValue(value);
    }

    public boolean hasBoth(K key, V value) {
        return this.hashMap.containsKey(key) && this.hashMap.containsValue(value);
    }

    private void putBoth(K key, V var) {
        this.hashMap.put(key, var);
        this.hashBack.put(var, key);
    }

}
