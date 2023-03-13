package me.hapyl.spigotutils.module.util;


import javax.annotation.Nullable;

/**
 * @deprecated Use {@link LinkedKeyValMap} instead.
 */
@Deprecated()
public class Gap<K, V> extends LinkedKeyValMap<K, V> {

    @Nullable
    public V getValue(K key) {
        return super.getValue(key);
    }

    @Nullable
    public K getKey(V value) {
        return super.getKey(value);
    }

    public void clear() {
        super.clear();
    }

    public boolean hasKey(K key) {
        return super.containsKey(key);
    }

    public boolean hasValue(V value) {
        return super.containsValue(value);
    }

    public boolean hasBoth(K key, V value) {
        return hasKey(key) && hasValue(value);
    }

    private void putBoth(K key, V var) {
        put(key, var);
    }

}
