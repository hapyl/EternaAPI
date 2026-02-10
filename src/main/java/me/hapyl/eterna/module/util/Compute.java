package me.hapyl.eterna.module.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import me.hapyl.eterna.module.annotate.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Represents a helper utility class to be used for computation in {@link Map}.
 *
 * <pre>{@code
 * Map<String, Integer> namesToScore = new HashMap<>();
 *
 * namesToScore.compute("hapyl", (name, score) -> {
 *     return score != null ? score + 1 : 1;
 * });
 *
 * // Can be replaced with:
 * namesToScore.compute("hapyl", Compute.intAdd());
 * }</pre>
 *
 * <pre>{@code
 * Map<String, List<Material>> materialsByType = new HashMap<>();
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
 * // Can be replaced with:
 * materialsByType.compute("stone", Compute.listAdd(Material.STONE, Material.COBBLESTONE));
 * }</pre>
 */
@UtilityClass
public final class Compute {
    
    private Compute() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Gets a computation function that computes an {@link Integer} {@link Map} value by incrementing it by {@code 1}.
     *
     * @param <K> - The key type.
     * @return a computation function that computes an integer hash map value by incrementing it by {@code 1}.
     */
    @NotNull
    public static <K> BiFunction<K, Integer, Integer> intAdd() {
        return intAdd(1);
    }
    
    /**
     * Gets a computation function that computes an {@link Integer} {@link Map} value by incrementing it by the given value.
     *
     * @param addition - The integer to increment by.
     * @param <K>      - The key type.
     * @return a computation function that computes an integer hash map value by incrementing it by the given value.
     */
    @NotNull
    public static <K> BiFunction<K, Integer, Integer> intAdd(int addition) {
        return (k, integer) -> integer != null ? integer + addition : addition;
    }
    
    /**
     * Gets a computation function that computes an {@link Integer} {@link Map} value by decrementing it by {@code 1}.
     *
     * @param <K> - The key type.
     * @return a computation function that computes an integer hash map value by decrementing it by {@code 1}.
     */
    @NotNull
    public static <K> BiFunction<K, Integer, Integer> intSubtract() {
        return intSubtract(1);
    }
    
    /**
     * Gets a computation function that computes an {@link Integer} {@link Map} value by decrementing it by the given value.
     *
     * @param subtraction - The integer to subtract by.
     * @param <K>         - The key type.
     * @return a computation function that computes an integer hash map value by decrementing it by the given value.
     */
    @NotNull
    public static <K> BiFunction<K, Integer, Integer> intSubtract(int subtraction) {
        return (k, integer) -> integer != null ? integer - subtraction : -subtraction;
    }
    
    /**
     * Gets a computation function that computes a {@link Long} {@link Map} value by incrementing it by {@code 1}.
     *
     * @param <K> - The key type.
     * @return a computation function that computes a long hash map value by incrementing it by {@code 1}.
     */
    @NotNull
    public static <K> BiFunction<K, Long, Long> longAdd() {
        return longAdd(1);
    }
    
    /**
     * Gets a computation function that computes a {@link Long} {@link Map} value by incrementing it by the given value.
     *
     * @param addition - The long to increment by.
     * @param <K>      - The key type.
     * @return a computation function that computes a long hash map value by incrementing it by the given value.
     */
    @NotNull
    public static <K> BiFunction<K, Long, Long> longAdd(long addition) {
        return (k, aLong) -> aLong == null ? addition : aLong + addition;
    }
    
    /**
     * Gets a computation function that computes a {@link Long} {@link Map} value by decrementing it by {@code 1}.
     *
     * @param <K> - The key type.
     * @return a computation function that computes a long hash map value by decrementing it by {@code 1}.
     */
    @NotNull
    public static <K> BiFunction<K, Long, Long> longSubtract() {
        return longSubtract(1);
    }
    
    /**
     * Gets a computation function that computes a {@link Long} {@link Map} value by decrementing it by the given value.
     *
     * @param subtraction - The long to subtract by.
     * @param <K>         - The key type.
     * @return a computation function that computes a long hash map value by decrementing it by the given value.
     */
    @NotNull
    public static <K> BiFunction<K, Long, Long> longSubtract(long subtraction) {
        return (k, aLong) -> aLong == null ? -subtraction : aLong - subtraction;
    }
    
    /**
     * Gets a computation function that computes a {@link Double} {@link Map} value by incrementing it by {@code 1}.
     *
     * @param <K> - The key type.
     * @return a computation function that computes a double hash map value by incrementing it by {@code 1}.
     */
    @NotNull
    public static <K> BiFunction<K, Double, Double> doubleAdd() {
        return doubleAdd(1);
    }
    
    /**
     * Gets a computation function that computes a {@link Double} {@link Map} value by incrementing it by the given value.
     *
     * @param addition - The double to increment by.
     * @param <K>      - The key type.
     * @return a computation function that computes a double hash map value by incrementing it by the given value.
     */
    @NotNull
    public static <K> BiFunction<K, Double, Double> doubleAdd(double addition) {
        return (k, aDouble) -> aDouble == null ? addition : aDouble + addition;
    }
    
    /**
     * Gets a computation function that computes a {@link Double} {@link Map} value by decrementing it by {@code 1}.
     *
     * @param <K> - The key type.
     * @return a computation function that computes a double hash map value by decrementing it by {@code 1}.
     */
    @NotNull
    public static <K> BiFunction<K, Double, Double> doubleSubtract() {
        return doubleSubtract(1);
    }
    
    /**
     * Gets a computation function that computes a {@link Double} {@link Map} value by decrementing it by the given value.
     *
     * @param subtraction - The double to subtract by.
     * @param <K>         - The key type.
     * @return a computation function that computes a double hash map value by decrementing it by the given value.
     */
    @NotNull
    public static <K> BiFunction<K, Double, Double> doubleSubtract(double subtraction) {
        return (k, aDouble) -> aDouble == null ? -subtraction : aDouble - subtraction;
    }
    
    /**
     * Gets a computation function that computes a {@link List} {@link Map} value by adding the given {@link V} to the list.
     *
     * @param v   - The value to add to the list.
     * @param <K> - The key type.
     * @param <V> - The value type.
     * @return a computation function that computes a list map value by adding the given {@link V} to the list.
     */
    @SafeVarargs
    @NotNull
    public static <K, V> BiFunction<K, List<V>, List<V>> listAdd(@NotNull V... v) {
        return collectionAdd(fn -> Lists.newArrayList(), v);
    }
    
    /**
     * Gets a computation function that computes a {@link List} {@link Map} value by removing the given {@link V} from the list.
     *
     * @param v   - The value to remove from the list.
     * @param <K> - The key type.
     * @param <V> - The value type.
     * @return a computation function that computes a list map value by removing the given {@link V} from the list.
     */
    @SafeVarargs
    @NotNull
    public static <K, V> BiFunction<K, List<V>, List<V>> listRemove(@NotNull V... v) {
        return collectionRemove(fn -> Lists.newArrayList(), v);
    }
    
    /**
     * Gets a computation function that computes a {@link Set} {@link Map} value by adding the given {@link V} to the set.
     *
     * @param v   - The value to add to the set.
     * @param <K> - The key type.
     * @param <V> - The value type.
     * @return a computation function that computes a set map value by adding the given {@link V} to the set.
     */
    @SafeVarargs
    @NotNull
    public static <K, V> BiFunction<K, Set<V>, Set<V>> setAdd(@NotNull V... v) {
        return collectionAdd(fn -> Sets.newHashSet(), v);
    }
    
    /**
     * Gets a computation function that computes a {@link Set} {@link Map} value by removing the given {@link V} from the set.
     *
     * @param v   - The value to remove from the set.
     * @param <K> - The key type.
     * @param <V> - The value type.
     * @return a computation function that computes a set map value by removing the given {@link V} from the set.
     */
    @SafeVarargs
    @NotNull
    public static <K, V> BiFunction<K, Set<V>, Set<V>> setRemove(@NotNull V... v) {
        return collectionRemove(fn -> Sets.newHashSet(), v);
    }
    
    /**
     * Gets a computation function that computes a {@link Collection} {@link Map} value by adding the given {@link V} to the collection.
     *
     * @param v   - The value to add to the collection.
     * @param <K> - The key type.
     * @param <V> - The value type.
     * @return a computation function that computes a collection map value by adding the given {@link V} to the collection.
     */
    @SafeVarargs
    @NotNull
    public static <K, V, C extends Collection<V>> BiFunction<K, C, C> collectionAdd(@NotNull Function<K, C> newFn, @NotNull V... v) {
        return (k, c) -> {
            (c = c != null ? c : newFn.apply(k)).addAll(List.of(v));
            
            return c;
        };
    }
    
    /**
     * Gets a computation function that computes a {@link Collection} {@link Map} value by removing the given {@link V} from the collection.
     *
     * @param v   - The value to remove from the collection.
     * @param <K> - The key type.
     * @param <V> - The value type.
     * @return a computation function that computes a collection map value by removing the given {@link V} from the collection.
     */
    @SafeVarargs
    @NotNull
    public static <K, V, C extends Collection<V>> BiFunction<K, C, C> collectionRemove(@NotNull Function<K, C> newFn, @NotNull V... v) {
        return (k, c) -> {
            (c = c != null ? c : newFn.apply(k)).removeAll(List.of(v));
            
            return c;
        };
    }
    
    /**
     * Gets a computation function that computes a {@link Map} value by applying the given {@link Function} based on if the {@link V} value is {@code null}.
     *
     * @param ifNull    - The function to apply if the {@link V} is null.
     * @param ifNotNull - The function to apply if the {@link V} is not-null.
     * @param <K>       - The key type.
     * @param <V>       - The value type.
     * @return a computation function that computes a map value by applying the given function based on if the value is {@code null}.
     */
    @NotNull
    public static <K, V> BiFunction<K, V, V> nullable(@NotNull Function<K, V> ifNull, @NotNull Function<V, V> ifNotNull) {
        return (k, v) -> v == null ? ifNull.apply(k) : ifNotNull.apply(v);
    }
    
    /**
     * Gets a computation function that computes an {@link Integer} {@link Map} value by incrementing the value while keeping it within the given {@code limit}.
     *
     * @param limit – The upper bound the value cannot exceed.
     * @return a computation function that computes an integer map value by increment the value while keeping it within the given {@code limit}.
     */
    @NotNull
    public static <K> BiFunction<K, Integer, Integer> increment(int limit) {
        return (k, integer) -> Math.min(limit, Objects.requireNonNull(integer) + 1);
    }
    
    /**
     * Gets a computation function that computes an {@link Integer} {@link Map} value by decrementing the value while keeping it within the given {@code limit}.
     *
     * @param limit – The upper bound the value cannot exceed.
     * @return a computation function that computes an integer map value by decrementing the value while keeping it within the given {@code limit}.
     */
    @NotNull
    public static <K> BiFunction<K, Integer, Integer> decrement(int limit) {
        return (k, integer) -> Math.max(limit, Objects.requireNonNull(integer) - 1);
    }
    
}