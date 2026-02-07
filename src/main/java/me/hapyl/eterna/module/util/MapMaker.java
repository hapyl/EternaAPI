package me.hapyl.eterna.module.util;

import me.hapyl.eterna.module.annotate.SelfReturn;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * A builder-type {@link Map} maker.
 *
 * @param <K> - The key type.
 * @param <V> - The value type.
 * @param <M> - The map type.
 */
public final class MapMaker<K, V, M extends Map<K, V>> {
    
    private final M map;
    
    private MapMaker(@NotNull M map) {
        this.map = map;
    }
    
    /**
     * Associates the specified value with the specified key in this {@link Map}.
     *
     * @param k - The key with which the specified value is to be associated.
     * @param v - The value to be associated with the specified key.
     */
    @SelfReturn
    public MapMaker<K, V, M> put(@NotNull K k, @NotNull V v) {
        map.put(k, v);
        return this;
    }
    
    /**
     * Puts all the elements from the given {@link Map} into this {@link Map}.
     *
     * @param otherMap - The other map.
     */
    @SelfReturn
    public MapMaker<K, V, M> putAll(@NotNull Map<? extends K, ? extends V> otherMap) {
        map.putAll(otherMap);
        return this;
    }
    
    /**
     * Puts all the elements from the given {@code array} into this {@link Map}.
     *
     * @param keys          - The keys.
     * @param valueSupplier - The value supplier.
     */
    @SelfReturn
    public MapMaker<K, V, M> putAll(@NotNull K[] keys, @NotNull Function<K, V> valueSupplier) {
        return putAll(List.of(keys), valueSupplier);
    }
    
    /**
     * Puts all the elements from the given {@link Iterable} into this {@link Map}.
     *
     * @param keys          - The keys.
     * @param valueSupplier - The value supplier.
     */
    @SelfReturn
    public MapMaker<K, V, M> putAll(@NotNull Iterable<K> keys, @NotNull Function<K, V> valueSupplier) {
        for (K key : keys) {
            final V value = valueSupplier.apply(key);
            
            map.put(key, value);
        }
        
        return this;
    }
    
    /**
     * Makes the {@link Map}.
     *
     * @return the map.
     */
    @NotNull
    public Map<K, V> makeMap() {
        return map;
    }
    
    /**
     * Makes an immutable copy of the underlying {@link Map}.
     *
     * @return an immutable copy of the underlying map.
     */
    @NotNull
    public Map<K, V> makeImmutableMap() {
        return Map.copyOf(map);
    }
    
    /**
     * Makes the generic {@link M}.
     *
     * @return the generic {@link M}.
     */
    @NotNull
    public M makeGenericMap() {
        return map;
    }
    
    /**
     * A static factory method for creating {@link MapMaker} with an underlying {@link HashMap}.
     *
     * @return a mapmaker of hash map.
     */
    @NotNull
    public static <K, V> MapMaker<K, V, HashMap<K, V>> of() {
        return new MapMaker<>(new HashMap<>());
    }
    
    /**
     * A static factory method for creating {@link MapMaker} with an underlying {@link LinkedHashMap}.
     *
     * @return a mapmaker of linked hash map.
     */
    @NotNull
    public static <K, V> MapMaker<K, V, LinkedHashMap<K, V>> ofLinkedHashMap() {
        return new MapMaker<>(new LinkedHashMap<>());
    }
    
    /**
     * A static factory method for creating {@link MapMaker} with an underlying {@link TreeMap}.
     *
     * @return a mapmaker of tree map.
     */
    @NotNull
    public static <K extends Comparable<K>, V> MapMaker<K, V, TreeMap<K, V>> ofTreeMap() {
        return new MapMaker<>(new TreeMap<>());
    }
    
    /**
     * A static factory method for creating {@link MapMaker} with an underlying {@link ConcurrentHashMap}.
     *
     * @return a mapmaker of concurrent hash map.
     */
    @NotNull
    public static <K, V> MapMaker<K, V, ConcurrentHashMap<K, V>> ofConcurrentHashMap() {
        return new MapMaker<>(new ConcurrentHashMap<>());
    }
    
    /**
     * A static factory method for creating {@link MapMaker} with an underlying {@link WeakHashMap}.
     *
     * @return a mapmaker of weak hash map.
     */
    @NotNull
    public static <K, V> MapMaker<K, V, WeakHashMap<K, V>> ofWeakHashMap() {
        return new MapMaker<>(new WeakHashMap<>());
    }
    
    /**
     * A static factory method for creating {@link MapMaker} with an underlying {@link IdentityHashMap}.
     *
     * @return a mapmaker of identity hash map.
     */
    @NotNull
    public static <K, V> MapMaker<K, V, IdentityHashMap<K, V>> ofIdentityHashMap() {
        return new MapMaker<>(new IdentityHashMap<>());
    }
    
    /**
     * A static factory method for creating {@link MapMaker} with an underlying {@link EnumMap}.
     *
     * @return a mapmaker of enum map.
     */
    @NotNull
    public static <K extends Enum<K>, V> MapMaker<K, V, EnumMap<K, V>> ofEnumMap(@NotNull Class<K> enumClass) {
        return new MapMaker<>(new EnumMap<>(enumClass));
    }
    
}