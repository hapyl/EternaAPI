package me.hapyl.spigotutils.module.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Allows static computation for hash maps.
 * <pre>
 * Map<\String, Integer> namesToScore = new HashMap<>();
 *
 * namesToScore.compute("hapyl", (name, score) -> {
 *     return score != null ? score + 1 : 1;
 * });
 *
 * <i>Can be replaced with:</i>
 *
 * namesToScore.compute("hapyl", Compute.intAdd());
 * </pre>
 *
 * <pre>
 * Map<\String, List<\Material>> materialsByType = new HashMap<>();
 *
 * materialsByType.compute("stone", (str, list) -> {
 *      if (list == null) {
 *          list = Lists.newArrayList();
 *      }
 *
 *      list.add(Material.STONE);
 *      list.add(Material.COBBLESTONE);
 *      return list;
 *  });
 *
 * <i>Can be replaced with:</i>
 *
 * materialsByType.compute("stone", Compute.listAdd(Material.STONE, Material.COBBLESTONE));
 * </pre>
 */
public final class Compute {

    /**
     * Performs an {@link Integer} computation in a {@link java.util.Map} by 1.
     *
     * @param <K> - Key.
     * @return a computation {@link BiFunction} which increments an {@link Integer} by 1.
     */
    @Nonnull
    public static <K> BiFunction<K, Integer, Integer> intAdd() {
        return intAdd(1);
    }

    /**
     * Performs an {@link Integer} computation in a {@link java.util.Map} by a given value.
     *
     * @param addition - Value.
     *                 <p>
     *                 If the computable value is null, it defaults to 0, meaning:
     *                 <pre>0 + addition = addition</pre>
     * @param <K>      - Key.
     * @return a computation {@link BiFunction} which increments an {@link Integer} by a given value.
     */
    @Nonnull
    public static <K> BiFunction<K, Integer, Integer> intAdd(int addition) {
        return (k, integer) -> integer != null ? integer + addition : addition;
    }

    /**
     * Performs an {@link Integer} computation in a {@link java.util.Map} by <b>-1</b>.
     *
     * @param <K> - Key.
     * @return a computation {@link BiFunction} which decrements an {@link Integer} by 1.
     */
    @Nonnull
    public static <K> BiFunction<K, Integer, Integer> intSubtract() {
        return intSubtract(1);
    }

    /**
     * Performs an {@link Integer} computation in a {@link java.util.Map} by a given value.
     *
     * @param subtraction - Value.
     *                    <p>
     *                    If the computable value is null, it defaults to 0, meaning:
     *                    <pre>0 - subtraction = -subtraction.</pre>
     * @param <K>         - Key.
     * @return a computation {@link BiFunction} which decrements an {@link Integer} by a given value.
     */
    @Nonnull
    public static <K> BiFunction<K, Integer, Integer> intSubtract(int subtraction) {
        return (k, integer) -> integer != null ? integer - subtraction : -subtraction;
    }

    /**
     * Performs a {@link Long} computation in a {@link java.util.Map} by 1.
     *
     * @param <K> - Key.
     * @return a computation {@link BiFunction} which increments a {@link Long} by 1.
     */
    @Nonnull
    public static <K> BiFunction<K, Long, Long> longAdd() {
        return longAdd(1);
    }

    /**
     * Performs a {@link Long} computation in a {@link java.util.Map} by a given value.
     *
     * @param <K> - Key.
     * @return a computation {@link BiFunction} which increments a {@link Long} by a given value.
     */
    @Nonnull
    public static <K> BiFunction<K, Long, Long> longAdd(long addition) {
        return (k, aLong) -> aLong == null ? addition : aLong + addition;
    }

    /**
     * Performs a {@link Long} computation in a {@link java.util.Map} by -1.
     *
     * @param <K> - Key.
     * @return a computation {@link BiFunction} which decrements a {@link Long} by 1.
     */
    @Nonnull
    public static <K> BiFunction<K, Long, Long> longSubtract() {
        return longSubtract(1);
    }

    /**
     * Performs a {@link Long} computation in a {@link java.util.Map} by a given value.
     *
     * @param <K> - Key.
     * @return a computation {@link BiFunction} which decrements a {@link Long} value by a given value.
     */
    @Nonnull
    public static <K> BiFunction<K, Long, Long> longSubtract(long subtraction) {
        return (k, aLong) -> aLong == null ? -subtraction : aLong - subtraction;
    }

    /**
     * Performs a {@link List} computation in a {@link java.util.Map} list value.
     *
     * @param v   - Values to add to the list.
     * @param <K> - Key.
     * @param <V> - Value.
     * @return a computation {@link BiFunction} which adds the given values to the list.
     */
    @SafeVarargs
    @Nonnull
    public static <K, V> BiFunction<K, List<V>, List<V>> listAdd(@Nonnull V... v) {
        return collectionAdd(fn -> Lists.newArrayList(), v);
    }

    /**
     * Performs a {@link List} computation in a {@link java.util.Map} list value.
     *
     * @param v   - Values to remove from the list.
     * @param <K> - Key.
     * @param <V> - Value.
     * @return a computation {@link BiFunction} which removes the given values from the list.
     */
    @SafeVarargs
    @Nonnull
    public static <K, V> BiFunction<K, List<V>, List<V>> listRemove(@Nonnull V... v) {
        return collectionRemove(fn -> Lists.newArrayList(), v);
    }

    /**
     * Performs a {@link Set} computation in a {@link java.util.Map} set value.
     *
     * @param v   - Values to add to the set.
     * @param <K> - Key.
     * @param <V> - Value.
     * @return a computation {@link BiFunction} which adds the given values to the set.
     */
    @SafeVarargs
    @Nonnull
    public static <K, V> BiFunction<K, Set<V>, Set<V>> setAdd(@Nonnull V... v) {
        return collectionAdd(fn -> Sets.newHashSet(), v);
    }

    /**
     * Performs a {@link Set} computation in a {@link java.util.Map} set value.
     *
     * @param v   - Values to remove from the set.
     * @param <K> - Key.
     * @param <V> - Value.
     * @return a computation {@link BiFunction} which removes the given values from the set.
     */
    @SafeVarargs
    @Nonnull
    public static <K, V> BiFunction<K, Set<V>, Set<V>> setRemove(@Nonnull V... v) {
        return collectionRemove(fn -> Sets.newHashSet(), v);
    }

    /**
     * Performs a {@link Collection} computation in a {@link java.util.Map} collection value.
     *
     * @param newFn - New function on how to create a new collection, in case the map value is null.
     * @param v     - Values to add to the collection.
     * @param <K>   - Key.
     * @param <V>   - Value.
     * @param <C>   - Collection Type.
     * @return a computation {@link BiFunction} which adds the given values to the collection.
     */
    @SafeVarargs
    @Nonnull
    public static <K, V, C extends Collection<V>> BiFunction<K, C, C> collectionAdd(@Nonnull Function<K, C> newFn, @Nonnull V... v) {
        return (k, c) -> {
            (c = c != null ? c : newFn.apply(k)).addAll(List.of(v));

            return c;
        };
    }

    /**
     * Performs a {@link Collection} computation in a {@link java.util.Map} collection value.
     *
     * @param newFn - New function on how to create a new collection, in case the map value is null.
     * @param v     - Values to remove from the collection.
     * @param <K>   - Key.
     * @param <V>   - Value.
     * @param <C>   - Collection Type.
     * @return a computation {@link BiFunction} which removes the given values to the collection.
     */
    @SafeVarargs
    @Nonnull
    public static <K, V, C extends Collection<V>> BiFunction<K, C, C> collectionRemove(@Nonnull Function<K, C> newFn, @Nonnull V... v) {
        return (k, c) -> {
            (c = c != null ? c : newFn.apply(k)).removeAll(List.of(v));

            return c;
        };
    }

    /**
     * Performs a null check in a {@link java.util.Map}.
     *
     * @param ifNull    - Function to use if the map value is null.
     * @param ifNotNull - Function to use if the map value is not null.
     * @param <K>       - Key.
     * @param <V>       - Value.
     * @return a computation {@link BiFunction} based on the map value.
     */
    @Nonnull
    public static <K, V> BiFunction<K, V, V> nullable(@Nonnull Function<K, V> ifNull, @Nonnull Function<V, V> ifNotNull) {
        return (k, v) -> v == null ? ifNull.apply(k) : ifNotNull.apply(v);
    }


}