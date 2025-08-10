package me.hapyl.eterna.module.util;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A builder-type {@link Map} maker.
 *
 * @param <K> - Key.
 * @param <V> - Value.
 * @param <M> - Map.
 */
public final class MapMaker<K, V, M extends Map<K, V>> {

    private final M map;

    private MapMaker(@Nonnull M map) {
        this.map = map;
    }

    /**
     * Associates the specified value with the specified key in this map.
     *
     * @param k - Key.
     * @param v - Value.
     */
    public MapMaker<K, V, M> put(@Nonnull K k, @Nonnull V v) {
        map.put(k, v);
        return this;
    }

    /**
     * Gets the {@link Map}.
     *
     * @return the map.
     */
    @Nonnull
    public Map<K, V> makeMap() {
        return map;
    }

    /**
     * Gets an immutable {@link Map}.
     *
     * @return an immutable map.
     */
    @Nonnull
    public Map<K, V> makeImmutableMap() {
        return Map.copyOf(map);
    }

    /**
     * Gets the generic {@link M}.
     *
     * @return the generic map.
     */
    @Nonnull
    public M makeGenericMap() {
        return map;
    }

    /**
     * Creates a {@link MapMaker} of {@link HashMap}.
     *
     * @return a mapmaker of hash map.
     */
    @Nonnull
    public static <K, V> MapMaker<K, V, HashMap<K, V>> of() {
        return new MapMaker<>(new HashMap<>());
    }

    /**
     * Creates a {@link MapMaker} of {@link LinkedHashMap}.
     *
     * @return a mapmaker of linked hash map.
     */
    @Nonnull
    public static <K, V> MapMaker<K, V, LinkedHashMap<K, V>> ofLinkedHashMap() {
        return new MapMaker<>(new LinkedHashMap<>());
    }

    /**
     * Creates a {@link MapMaker} of {@link TreeMap}.
     *
     * @return a mapmaker of tree map.
     */
    @Nonnull
    public static <K extends Comparable<K>, V> MapMaker<K, V, TreeMap<K, V>> ofTreeMap() {
        return new MapMaker<>(new TreeMap<>());
    }

    /**
     * Creates a {@link MapMaker} of {@link ConcurrentHashMap}.
     *
     * @return a mapmaker of concurrent hash map.
     */
    @Nonnull
    public static <K, V> MapMaker<K, V, ConcurrentHashMap<K, V>> ofConcurrentHashMap() {
        return new MapMaker<>(new ConcurrentHashMap<>());
    }

    /**
     * Creates a {@link MapMaker} of {@link WeakHashMap}.
     *
     * @return a mapmaker of weak hash map.
     */
    @Nonnull
    public static <K, V> MapMaker<K, V, WeakHashMap<K, V>> ofWeakHashMap() {
        return new MapMaker<>(new WeakHashMap<>());
    }

    /**
     * Creates a {@link MapMaker} of {@link IdentityHashMap}.
     *
     * @return a mapmaker of identity hash map.
     */
    @Nonnull
    public static <K, V> MapMaker<K, V, IdentityHashMap<K, V>> ofIdentityHashMap() {
        return new MapMaker<>(new IdentityHashMap<>());
    }


}