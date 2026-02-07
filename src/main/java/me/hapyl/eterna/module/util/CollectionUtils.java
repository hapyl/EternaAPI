package me.hapyl.eterna.module.util;

import me.hapyl.eterna.module.annotate.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Represents a helper utility class for {@link Collection} operations.
 */
@UtilityClass
public class CollectionUtils {
    
    private static final Random RANDOM = new Random();
    
    private CollectionUtils() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Retrieves the {@link E} at the given index from the given {@link List}.
     *
     * <p>
     * This method provides out-of-bounds safe element retrieval.
     * </p>
     *
     * @param list  - The list to retrieve the element from.
     * @param index - The index to retrieve at.
     * @param <E>   - The element type.
     * @return an element wrapped in an optional.
     */
    @NotNull
    public static <E> Optional<E> get(@NotNull List<E> list, int index) {
        return get0(list, index, List::size, List::get);
    }
    
    /**
     * Retrieves the {@link E} at the given index from the given {@code array}.
     *
     * <p>
     * This method provides out-of-bounds safe element retrieval.
     * </p>
     *
     * @param array - The list to retrieve the element from.
     * @param index - The index to retrieve at.
     * @param <E>   - The element type.
     * @return an element wrapped in an optional.
     */
    @NotNull
    public static <E> Optional<E> get(@NotNull E[] array, int index) {
        return get0(array, index, _array -> _array.length, (_array, _index) -> _array[_index]);
    }
    
    /**
     * Wraps the given {@link Collection} to {@link String} using the given {@link Wrap}.
     *
     * @param collection - The collection to wrap to string.
     * @param wrap       - The wrap to use.
     * @param <E>        - The element type.
     * @return a string representation of the elements in the collection.
     */
    @NotNull
    public static <E> String wrapToString(@NotNull Collection<E> collection, @NotNull Wrap wrap) {
        return collection.stream().map(String::valueOf).collect(wrap.asCollector());
    }
    
    /**
     * Wraps the given {@link Collection} to {@link String} using the default {@link Wrap}.
     *
     * @param collection - The collection to wrap to string.
     * @param <E>        - The element type.
     * @return a string representation of the elements in the collection.
     */
    @NotNull
    public static <E> String wrapToString(@NotNull Collection<E> collection) {
        return wrapToString(collection, Wrap.DEFAULT);
    }
    
    /**
     * Wraps the given {@code array} to {@link String} using the given {@link Wrap}.
     *
     * @param array - The array to wrap to string.
     * @param wrap  - The wrap to use.
     * @param <E>   - The element type.
     * @return a string representation of the elements in the array.
     */
    @NotNull
    public static <E> String wrapToString(@NotNull E[] array, @NotNull Wrap wrap) {
        return Arrays.stream(array).map(String::valueOf).collect(wrap.asCollector());
    }
    
    /**
     * Wraps the given {@code array} to {@link String} using the default {@link Wrap}.
     *
     * @param array - The array to wrap to string.
     * @param <E>   - The element type.
     * @return a string representation of the elements in the array.
     */
    @NotNull
    public static <E> String wrapToString(@NotNull E[] array) {
        return wrapToString(array, Wrap.DEFAULT);
    }
    
    /**
     * Wraps the given {@link Map} to {@link String} using the given {@link Wrap}.
     *
     * @param map  - The map to wrap to string.
     * @param wrap - The wrap to use.
     * @param <K>  - The key element type.
     * @param <V>  - The value element type.
     * @return a string representation of the elements in the collection.
     */
    @NotNull
    public static <K, V> String wrapToString(@NotNull Map<K, V> map, @NotNull Wrap.MapWrap<K, V> wrap) {
        return map.entrySet().stream().map(entry -> wrap.keyToValue(entry.getKey(), entry.getValue())).collect(wrap.asCollector());
    }
    
    /**
     * Wraps the given {@link Map} to {@link String} using the default {@link Wrap}.
     *
     * @param map - The map to wrap to string.
     * @param <K> - The key element type.
     * @param <V> - The value element type.
     * @return a string representation of the elements in the collection.
     */
    @NotNull
    public static <K, V> String wrapToString(@NotNull Map<K, V> map) {
        return wrapToString(map, Wrap.ofMap());
    }
    
    /**
     * Gets whether the given {@code array} if {@code null} or empty.
     *
     * @param array - The array to check.
     * @param <E>   - The element type.
     * @return {@code true} if the given array is null or empty; {@code false} otherwise.
     */
    public static <E> boolean isNullOrEmpty(@Nullable E[] array) {
        return array == null || array.length == 0;
    }
    
    /**
     * Gets the next {@link T} value in the given {@code array}, wrapping to the start if out-of-bounds.
     *
     * @param values  - The array to retrieve the value from.
     * @param current - The current element in the array.
     * @param <T>     - The element type.
     * @return the next value in the array.
     */
    @Nullable
    public static <T> T getNextValue(@NotNull T[] values, @NotNull T current) {
        for (int i = 0; i < values.length; i++) {
            final T value = values[i];
            
            if (current.equals(value)) {
                return values[(i + 1) % values.length];
            }
        }
        
        return null;
    }
    
    /**
     * Gets the previous {@link T} value in the given {@code array}, wrapping to the end if out-of-bounds.
     *
     * @param values  - The array to retrieve the value from.
     * @param current - The current element in the array.
     * @param <T>     - The element type.
     * @return the next value in the array.
     */
    @Nullable
    public static <T> T getPreviousValue(@NotNull T[] values, @NotNull T current) {
        for (int i = 0; i < values.length; i++) {
            final T value = values[i];
            
            if (current.equals(value)) {
                return values[(i - 1 + values.length) % values.length];
            }
        }
        
        return null;
    }
    
    /**
     * Retrieves a random element from the given {@link Collection}.
     *
     * @param collection - The collection from which to retrieve a random element.
     * @param <E>        - The element type.
     * @return a random element from the collection, or {@code null} if the collection is empty.
     */
    @Nullable
    public static <E> E randomElement(@NotNull Collection<E> collection) {
        if (collection.isEmpty()) {
            return null;
        }
        
        final int randomElementIndex = RANDOM.nextInt(collection.size());
        
        return collection instanceof List<E> list
               ? list.get(randomElementIndex)
               : collection.stream().skip(randomElementIndex).findFirst().orElse(null);
    }
    
    /**
     * Retrieves a random element from the given {@link Collection}.
     *
     * @param collection   - The collection from which to retrieve a random element.
     * @param defaultValue - The default value to use if the collection is empty or the retrieved element is {@code null}.
     * @param <E>          - The element type.
     * @return a random element from the collection, or {@code defaultValue} if the collection is empty or the retrieved element is {@code null}.
     */
    @NotNull
    public static <E> E randomElement(@NotNull Collection<E> collection, @NotNull E defaultValue) {
        final E value = randomElement(collection);
        
        return value != null ? value : defaultValue;
    }
    
    /**
     * Retrieves a random element from the given {@link Collection}, or the first element in the collection if the collection
     * is empty or the retrieved element is {@code null}.
     *
     * @param collection - The collection from which to retrieve a random element.
     * @param <E>        - The element type.
     * @return a random element from the collection, or {@code null} if the collection is empty.
     */
    @NotNull
    public static <E> E randomElementOrFirst(@NotNull Collection<E> collection) {
        final E value = randomElement(collection);
        
        return value != null
               ? value
               : Objects.requireNonNull(
                       collection instanceof List<E> list ? list.getFirst() : collection.stream().findFirst().orElse(null),
                       "Collection must not be empty!"
               );
    }
    
    /**
     * Retrieves a random element from the given {@code array}.
     *
     * @param array - The array from which to retrieve a random element.
     * @param <E>   - The element type.
     * @return a random element from the array, or {@code null} if the collection is empty.
     */
    @Nullable
    public static <E> E randomElement(@NotNull E[] array) {
        return array.length == 0 ? null : array[RANDOM.nextInt(array.length)];
    }
    
    /**
     * Retrieves a random element from the given {@code array}.
     *
     * @param array        - The array from which to retrieve a random element.
     * @param defaultValue - The default value to use if the array is empty or the retrieved element is {@code null}.
     * @param <E>          - The element type.
     * @return a random element from the array, or {@code null} if the collection is empty.
     */
    @NotNull
    public static <E> E randomElement(@NotNull E[] array, @NotNull E defaultValue) {
        final E value = randomElement(array);
        
        return value != null ? value : defaultValue;
    }
    
    /**
     * Retrieves a random element from the given {@code array}, or the first element in the array if the array is empty or
     * the retrieved element is {@code null}.
     *
     * @param array - The array from which to retrieve a random element.
     * @param <E>   - The element type.
     * @return a random element from the collection, or {@code null} if the collection is empty.
     */
    @NotNull
    public static <E> E randomElementOrFirst(@NotNull E[] array) {
        final E value = randomElement(array);
        
        return value != null ? value : Objects.requireNonNull(array[0], "Array must not be empty!");
    }
    
    /**
     * Gets whether the given {@code array} contains the given {@link T} element.
     *
     * <p>
     * This method uses {@link Objects#equals(Object, Object)} check instead of identity check.
     * </p>
     *
     * @param array   - The array to check.
     * @param element - The element to check.
     * @param <T>     - The element type.
     * @return {@code true} if the given element is in the given array; {@code false} otherwise.
     */
    public static <T> boolean contains(@Nullable T[] array, @NotNull T element) {
        if (array == null) {
            return false;
        }
        
        for (@Nullable T value : array) {
            if (Objects.equals(element, value)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Creates a copy of the given {@code array}.
     *
     * @param original - The array to copy.
     * @return a copy of the given array.
     */
    public static int[] arrayCopy(int[] original) {
        final int[] copy = new int[original.length];
        System.arraycopy(original, 0, copy, 0, original.length);
        
        return copy;
    }
    
    /**
     * Creates a copy of the given {@code array}.
     *
     * @param original - The array to copy.
     * @return a copy of the given array.
     */
    public static long[] arrayCopy(long[] original) {
        final long[] copy = new long[original.length];
        System.arraycopy(original, 0, copy, 0, original.length);
        
        return copy;
    }
    
    /**
     * Creates a copy of the given {@code array}.
     *
     * @param original - The array to copy.
     * @return a copy of the given array.
     */
    public static double[] arrayCopy(double[] original) {
        final double[] copy = new double[original.length];
        System.arraycopy(original, 0, copy, 0, original.length);
        
        return copy;
    }
    
    /**
     * Creates a copy of the given {@code array}.
     *
     * @param original - The array to copy.
     * @return a copy of the given array.
     */
    public static float[] arrayCopy(float[] original) {
        final float[] copy = new float[original.length];
        System.arraycopy(original, 0, copy, 0, original.length);
        
        return copy;
    }
    
    /**
     * Creates a copy of the given {@code array}.
     *
     * @param original - The array to copy.
     * @return a copy of the given array.
     */
    public static char[] arrayCopy(char[] original) {
        final char[] copy = new char[original.length];
        System.arraycopy(original, 0, copy, 0, original.length);
        
        return copy;
    }
    
    /**
     * Creates a copy of the given {@code array}.
     *
     * @param original - The array to copy.
     * @return a copy of the given array.
     */
    public static byte[] arrayCopy(byte[] original) {
        final byte[] copy = new byte[original.length];
        System.arraycopy(original, 0, copy, 0, original.length);
        
        return copy;
    }
    
    /**
     * Creates a copy of the given {@code array}.
     *
     * @param original - The array to copy.
     * @return a copy of the given array.
     */
    public static short[] arrayCopy(short[] original) {
        final short[] copy = new short[original.length];
        System.arraycopy(original, 0, copy, 0, original.length);
        
        return copy;
    }
    
    /**
     * Creates a copy of the given {@code array}.
     *
     * @param original - The array to copy.
     * @return a copy of the given array.
     */
    public static boolean[] arrayCopy(boolean[] original) {
        final boolean[] copy = new boolean[original.length];
        System.arraycopy(original, 0, copy, 0, original.length);
        
        return copy;
    }
    
    /**
     * Creates a copy of range of the given {@code array}.
     *
     * @param source       - The original array.
     * @param length       - The new length of the array.
     * @param defaultValue - The supplier for supplementary value where the value of the element in the original array is {@code null}.
     * @return a copy of the given array.
     */
    public static <T> @NotNull T @NotNull [] arrayCopy(@Nullable T @NotNull [] source, int length, @NotNull Supplier<T> defaultValue) {
        final T[] arrayCopy = Arrays.copyOf(source, length);
        
        Arrays.setAll(
                arrayCopy, i -> {
                    final T value = arrayCopy[i];
                    
                    return value != null ? value : defaultValue.get();
                }
        );
        
        return arrayCopy;
    }
    
    /**
     * Converts the given varargs into a {@link List}.
     *
     * @param varargs - The varargs to convert into a list.
     * @param <T>     - The element type.
     * @return a list of varargs.
     * @throws IllegalArgumentException if the given varargs are empty.
     */
    @NotNull
    public static <T> List<T> varargsAsList(@NotNull T... varargs) throws IllegalArgumentException {
        return Arrays.asList(Validate.varargs(varargs));
    }
    
    @NotNull
    private static <I, E> Optional<E> get0(@NotNull I indexed, int index, @NotNull Function<I, Integer> getLength, @NotNull BiFunction<I, Integer, E> get) {
        final int length = getLength.apply(indexed);
        
        return index < 0 || index >= length
               ? Optional.empty()
               : Optional.ofNullable(get.apply(indexed, index));
    }
    
}
