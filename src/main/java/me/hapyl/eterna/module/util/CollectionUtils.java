package me.hapyl.eterna.module.util;

import me.hapyl.eterna.module.annotate.NullabilityBasedOnParameter;
import me.hapyl.eterna.module.math.Numbers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.*;

/**
 * Utility class for collections, such as {@link Set}, {@link List}, {@link Map}, primitive arrays, etc...
 */
public class CollectionUtils {

    /**
     * Gets the first element of a list
     *
     * @param arrayList - list to get from.
     * @param index     - index of the element to get.
     * @param <E>       - type of the list.
     * @return the element at the index, or null if the index is out of bounds.
     */
    @Nullable
    public static <E> E get(@Nonnull List<E> arrayList, int index) {
        return getFromIndexed(index, arrayList, List::size, List::get);
    }

    /**
     * Gets the element from a set, or null if the index is out of bounds.
     *
     * @param hashSet - set to get from.
     * @param index   - index of the element to get.
     * @param <E>     - type of the set.
     * @return the element at the index, or null if the index is out of bounds.
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public static <E> E get(@Nonnull Collection<E> hashSet, int index) {
        return getFromIndexed(index, hashSet, Collection::size, (s, i) -> (E) s.toArray()[i]);
    }

    /**
     * Gets the element from array index, or null if the index is out of bounds.
     *
     * @param array - array to get from.
     * @param index - index of the element to get.
     * @param <E>   - type of the array.
     * @return the element at the index, or null if the index is out of bounds.
     */
    @Nullable
    public static <E> E get(@Nonnull E[] array, int index) {
        return getFromIndexed(index, array, a -> a.length, (a, i) -> a[i]);
    }

    /**
     * Gets the element from list, or def if the index is out of bounds.
     *
     * @param arrayList - list to get from.
     * @param index     - index of the element to get.
     * @param def       - default value to return if the index is out of bounds.
     * @param <E>       - type of the list.
     * @return the element at the index, or def if the index is out of bounds.
     */
    @Nonnull
    public static <E> E getOrDefault(@Nonnull List<E> arrayList, int index, @Nonnull E def) {
        final E e = get(arrayList, index);

        return e != null ? e : def;
    }

    /**
     * Gets the element from collection by index, or def if the index is out of bounds.
     *
     * @param collection - Collection.
     * @param index      - Index.
     * @param def        - Default value.
     * @param <E>        - type of the collection.
     * @return the element at index or def if the index is out of bounds.
     */
    @Nonnull
    public static <E> E getOrDefault(@Nonnull Collection<E> collection, int index, @Nonnull E def) {
        final E e = get(collection, index);

        return e != null ? e : def;
    }

    /**
     * Gets the element from array index, or def if the index is out of bounds.
     *
     * @param array - array to get from.
     * @param index - index of the element to get.
     * @param def   - default value to return if the index is out of bounds.
     * @param <E>   - type of the array.
     * @return the element at the index, or def if the index is out of bounds.
     */
    @Nonnull
    public static <E> E getOrDefault(@Nonnull E[] array, int index, @Nonnull E def) {
        final E e = get(array, index);

        return e != null ? e : def;
    }

    /**
     * Adds the element to list and returns the list.
     *
     * @param list  - list to add to.
     * @param toAdd - element to add.
     * @param <E>   - type of the list.
     * @return the list.
     */
    @Nonnull
    public static <E> E addAndGet(@Nonnull List<E> list, @Nonnull E toAdd) {
        list.add(toAdd);
        return toAdd;
    }

    /**
     * Wraps a collection to string according to wrap.
     *
     * @param collection - collection to wrap.
     * @param wrap       - wrap to use.
     * @param <E>        - type of the collection.
     * @return the wrapped string.
     */
    @Nonnull
    public static <E> String wrapToString(@Nonnull Collection<E> collection, @Nonnull Wrap wrap) {
        final StringBuilder builder = new StringBuilder(wrap.start());
        int i = 0;

        for (final E e : collection) {
            if (i++ != 0) {
                builder.append(wrap.between());
            }

            builder.append(e.toString());
        }

        return builder.append(wrap.end()).toString();
    }

    /**
     * Wraps an array to string, according to wrapper.
     *
     * @param array - array to wrap.
     * @param wrap  - wrap to use.
     * @param <E>   - type of the array.
     * @return the wrapped string.
     */
    @Nonnull
    public static <E> String wrapToString(@Nonnull E[] array, @Nonnull Wrap wrap) {
        final StringBuilder builder = new StringBuilder(wrap.start());

        for (int i = 0; i < array.length; i++) {
            if (i != 0) {
                builder.append(wrap.between());
            }

            builder.append(array[i].toString());
        }

        return builder.append(wrap.end()).toString();
    }

    /**
     * Wraps a collection to a string according to default wrap.
     *
     * @param collection - collection to wrap.
     * @return the wrapped string.
     */
    @Nonnull
    public static <E> String wrapToString(@Nonnull Collection<E> collection) {
        return wrapToString(collection, Wrap.DEFAULT);
    }

    /**
     * Wraps array to string using default wrap
     *
     * @param array - array to wrap.
     * @param <E>   - type of the array.
     * @return the wrapped string.
     */
    @Nonnull
    public static <E> String wrapToString(@Nonnull E[] array) {
        return wrapToString(array, Wrap.DEFAULT);
    }

    /**
     * Wraps a map to a string according to a wrapper.
     *
     * @param map  - Map to wrap.
     * @param wrap - Wrapper.
     * @param <K>  - Key type.
     * @param <V>  - Value type.
     * @return the wrapped string.
     */
    @Nonnull
    public static <K, V> String wrapToString(@Nonnull Map<K, V> map, @Nonnull MapWrap<K, V> wrap) {
        final StringBuilder builder = new StringBuilder(wrap.start());

        int i = 0;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (i++ != 0) {
                builder.append(wrap.between());
            }

            builder.append(wrap.keyToValue(entry.getKey(), entry.getValue()));
        }

        return builder.append(wrap.end()).toString();
    }

    /**
     * Wraps a map to a string using default wrap.
     *
     * @param map - Map to wrap.
     * @param <K> - Key type.
     * @param <V> - Value type.
     * @return the wrapped string.
     */
    @Nonnull
    public static <K, V> String wrapToString(@Nonnull Map<K, V> map) {
        return wrapToString(map, MapWrap.ofDefault());
    }

    /**
     * Returns true if an array is null or empty.
     *
     * @param array - array to check.
     * @param <E>   - type of the array.
     * @return true if an array is null or empty.
     */
    public static <E> boolean nullOrEmpty(@Nullable E[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Returns the next value in the array.
     * <br>
     * This method will return the first element of the array if the next value is out of bounds.
     *
     * @param values  - array to get from.
     * @param current - current value.
     * @param <T>     - type of the array.
     * @return the next value.
     */
    @Nonnull
    public static <T> T getNextValue(@Nonnull T[] values, @Nonnull T current) {
        for (int i = 0; i < values.length; i++) {
            final T t = values[i];

            if (t.equals(current)) {
                return values.length > (i + 1) ? values[i + 1] : values[0];
            }
        }
        return current;
    }

    /**
     * Returns the previous value in the array.
     * <br>
     * This method will return the last element is the previous value is out of bounds.
     *
     * @param values  - array to get from.
     * @param current - current value.
     * @param <T>     - type of the array.
     * @return the previous value.
     */
    public static <T> T getPreviousValue(@Nonnull T[] values, @Nonnull T current) {
        for (int i = 0; i < values.length; i++) {
            final T t = values[i];

            if (t.equals(current)) {
                return i == 0 ? values[values.length - 1] : values[i - 1];
            }
        }
        return current;
    }

    /**
     * Iterates over the collection and then clears it.
     * Useful to iterate over something like entities to remove them.
     *
     * @param collection - collection to iterate over.
     * @param action     - action to perform.
     * @param <E>        - type of the collection.
     */
    public static <E> void forEachAndClear(@Nonnull Collection<E> collection, @Nonnull Consumer<E> action) {
        collection.forEach(action);
        collection.clear();
    }

    /**
     * Adds map integer value.
     *
     * @param hashMap - map to add to.
     * @param value   - value to add.
     * @param toAdd   - value to add.
     * @param <K>     - Key type.
     * @deprecated {@link Map#compute(Object, BiFunction)} exists
     */
    @Deprecated
    public static <K> void addMapValue(@Nonnull Map<K, Integer> hashMap, @Nonnull K value, int toAdd) {
        hashMap.put(value, hashMap.getOrDefault(value, 0) + toAdd);
    }

    /**
     * Clamps map integer value.
     *
     * @param hashMap - map to clamp.
     * @param value   - value to clamp.
     * @param min     - min value.
     * @param max     - max value.
     * @param <K>     - Key type.
     * @deprecated {@link Map#compute(Object, BiFunction)} exists
     */
    @Deprecated
    public static <K> void clampMapValue(@Nonnull Map<K, Integer> hashMap, @Nonnull K value, int min, int max) {
        final int integer = hashMap.getOrDefault(value, 0);
        hashMap.put(value, Numbers.clamp(integer, min, max));
    }

    /**
     * Adds an object to a set and returns it.
     *
     * @param set   - set to add to.
     * @param toAdd - object to add.
     * @param <E>   - type of the set.
     * @return the object added.
     */
    public static <E> E addAndGet(@Nonnull Set<E> set, @Nonnull E toAdd) {
        set.add(toAdd);
        return toAdd;
    }

    /**
     * Creates a new map and inserts the key and value.
     *
     * @param key   - key to insert.
     * @param value - value to insert.
     * @param <K>   - type of the key.
     * @param <V>   - type of the value.
     * @return the new map.
     */
    @Deprecated
    public static <K, V> Map<K, V> newMapAndPut(@Nonnull K key, @Nonnull V value) {
        final HashMap<K, V> newMap = new HashMap<>();
        newMap.put(key, value);
        return newMap;
    }

    /**
     * Adds all the objects to the list.
     *
     * @param list  - list to add to.
     * @param toAdd - objects to add.
     * @param <E>   - type of the list.
     */
    @SafeVarargs
    public static <E> void addAll(@Nonnull List<E> list, @Nonnull E... toAdd) {
        list.addAll(Arrays.asList(toAdd));
    }

    /**
     * Adds all elements to the collection.
     *
     * @param collection - collection to add to.
     * @param elements   - elements to add.
     * @param <E>        - type of the collection.
     */
    @SafeVarargs
    public static <E> void addAll(@Nonnull Collection<E> collection, @Nonnull E... elements) {
        collection.addAll(Arrays.asList(elements));
    }

    /**
     * Returns a random element from the list.
     *
     * @param list - list to get from.
     * @param <E>  - type of the list.
     * @return a random element.
     */
    @Nullable
    public static <E> E randomElement(@Nonnull List<E> list) {
        return randomElement(list, null);
    }

    /**
     * Returns a random element from the list or first element if the list is empty.
     *
     * @param list - list to get from.
     * @return a random element.
     */
    @Nullable
    public static <E> E randomElementOrFirst(@Nonnull List<E> list) {
        return randomElement(list, get(list, 0));
    }

    /**
     * Returns a random element from the list or default value if the list is empty.
     *
     * @param list - list to get from.
     * @param def  - default value.
     * @param <E>  - type of the list.
     * @return a random element.
     */
    public static <E> E randomElement(@Nonnull List<E> list, E def) {
        if (list.isEmpty()) {
            return def;
        }
        return getOrDefault(list, ThreadLocalRandom.current().nextInt(list.size()), def);
    }

    /**
     * Returns a random element from the set.
     *
     * @param collection - set to get from.
     * @param <E>        - type of the set.
     * @return a random element.
     */
    @Nullable
    public static <E> E randomElement(@Nonnull Collection<E> collection) {
        return randomElement(collection, null);
    }

    /**
     * Returns a random element from the set or first if a set is empty.
     *
     * @param collection - set to get from.
     * @return a random element.
     */
    @Nullable
    public static <E> E randomElementOrFirst(@Nonnull Collection<E> collection) {
        return randomElement(collection, get(collection, 0));
    }

    /**
     * Returns a random element from the set or def if a set is empty.
     *
     * @param collection - set to get from.
     * @param def        - default value.
     * @param <E>        - type of the set.
     * @return a random element.
     */
    @NullabilityBasedOnParameter("def")
    public static <E> E randomElement(@Nonnull Collection<E> collection, E def) {
        if (collection.isEmpty()) {
            return def;
        }

        return getOrDefault(collection, ThreadLocalRandom.current().nextInt(collection.size()), def);
    }

    /**
     * Returns a random element from the list.
     *
     * @param array - array to get from.
     * @param <E>   - type of the array.
     * @return a random element.
     */
    @Nullable
    public static <E> E randomElement(@Nonnull E[] array) {
        return randomElement(array, null);
    }

    /**
     * Returns a random element from the array or first element if array is empty.
     *
     * @param array - array to get from.
     * @return a random element.
     */
    @Nullable
    public static <E> E randomElementOrFirst(@Nonnull E[] array) {
        return randomElement(array, array.length == 0 ? null : array[0]);
    }

    /**
     * Returns a random element from the list or default value if array is empty.
     *
     * @param array - array to get from.
     * @param def   - default value.
     * @param <E>   - type of the array.
     * @return a random element.
     */
    @NullabilityBasedOnParameter("def")
    public static <E> E randomElement(@Nonnull E[] array, E def) {
        if (array.length == 0) {
            return def;
        }

        return array[ThreadRandom.nextInt(array.length)];
    }

    /**
     * Removes all the provided elements from the collection.
     *
     * @param collection - collection to remove from.
     * @param elements   - elements to remove.
     * @param <E>        - type of the collection.
     */
    @SafeVarargs
    public static <E> void removeAll(@Nonnull Collection<E> collection, @Nonnull E... elements) {
        for (final E element : elements) {
            collection.remove(element);
        }
    }

    /**
     * Converts array to list.
     *
     * @param array - array to convert.
     * @param <E>   - type of the array.
     * @return the list.
     */
    @Nonnull
    public static <E> List<E> arrayToList(@Nonnull E[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }

    /**
     * Converts set to array.
     *
     * @param hashSet - set to convert.
     * @param <E>     - type of the set.
     * @return the array.
     * @deprecated {@link #collectionToArray(Collection)}
     */
    @Nonnull
    @Deprecated
    @SuppressWarnings("unchecked")
    public static <E> E[] setToArray(@Nonnull Set<E> hashSet) {
        return (E[]) hashSet.toArray();
    }

    /**
     * Converts a {@link Collection} to array.
     *
     * @param collection - Collection.
     * @return array.
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    public static <E> E[] collectionToArray(@Nonnull Collection<E> collection) {
        return (E[]) collection.toArray();
    }

    /**
     * Converts an array to set.
     *
     * @param array - array to convert.
     * @param <E>   - type of the array.
     * @return the set.
     */
    @Nonnull
    public static <E> Set<E> arrayToSet(@Nonnull E[] array) {
        return new HashSet<>(Arrays.asList(array));
    }

    /**
     * Converts an int array to list of integers.
     *
     * @param array - array to convert.
     * @return the list.
     */
    @Nonnull
    public static List<Integer> intArrayToList(@Nonnull int[] array) {
        final List<Integer> list = new ArrayList<>();
        for (final int i : array) {
            list.add(i);
        }
        return list;
    }

    /**
     * Attempts to find an element in a given {@link Collection} that matches the {@link Predicate}.
     *
     * @param collection - Collection.
     * @param predicate  - Predicate.
     * @return the first element that matches the predicate, or null if none does.
     */
    @Nullable
    public static <T> T find(@Nonnull Collection<T> collection, @Nonnull Predicate<T> predicate) {
        for (T t : collection) {
            if (predicate.test(t)) {
                return t;
            }
        }

        return null;
    }

    /**
     * Attempts to find an element in a given {@link Collection} that matches the {@link Predicate} and removes it.
     *
     * @param collection - Collection.
     * @param predicate  - Predicate.
     * @return the first element that matches the predicate, or null if none does.
     */
    @Nullable
    public static <T> T findAndRemove(@Nonnull Collection<T> collection, @Nonnull Predicate<T> predicate) {
        final Iterator<T> iterator = collection.iterator();

        while (iterator.hasNext()) {
            final T next = iterator.next();

            if (predicate.test(next)) {
                iterator.remove();
                return next;
            }
        }

        return null;
    }

    /**
     * Returns <code>true</code> if the given array contains the given element.
     *
     * @param array   - Array to check.
     * @param element - Element to check.
     * @return true if the given array contains the given element.
     */
    public static <T> boolean contains(@Nullable T[] array, @Nonnull T element) {
        if (array == null) {
            return false;
        }

        for (T t : array) {
            if (t.equals(element)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Creates a copy of an array.
     *
     * @param original - Original array.
     * @return a copy of the original array.
     */
    public static int[] arrayCopy(@Nonnull int[] original) {
        final int[] copy = new int[original.length];
        System.arraycopy(original, 0, copy, 0, original.length);
        return copy;
    }

    /**
     * Creates a copy of an array.
     *
     * @param original - Original array.
     * @return a copy of the original array.
     */
    public static long[] arrayCopy(@Nonnull long[] original) {
        final long[] copy = new long[original.length];
        System.arraycopy(original, 0, copy, 0, original.length);
        return copy;
    }

    public static double[] arrayCopy(@Nonnull double[] original) {
        final double[] copy = new double[original.length];
        System.arraycopy(original, 0, copy, 0, original.length);
        return copy;
    }

    /**
     * Creates a copy of an array.
     *
     * @param original - Original array.
     * @return a copy of the original array.
     */
    public static float[] arrayCopy(@Nonnull float[] original) {
        final float[] copy = new float[original.length];
        System.arraycopy(original, 0, copy, 0, original.length);
        return copy;
    }

    /**
     * Creates a copy of an array.
     *
     * @param original - Original array.
     * @return a copy of the original array.
     */
    public static char[] arrayCopy(@Nonnull char[] original) {
        final char[] copy = new char[original.length];
        System.arraycopy(original, 0, copy, 0, original.length);
        return copy;
    }

    /**
     * Creates a copy of an array.
     *
     * @param original - Original array.
     * @return a copy of the original array.
     */
    public static byte[] arrayCopy(@Nonnull byte[] original) {
        final byte[] copy = new byte[original.length];
        System.arraycopy(original, 0, copy, 0, original.length);
        return copy;
    }

    /**
     * Creates a copy of an array.
     *
     * @param original - Original array.
     * @return a copy of the original array.
     */
    public static short[] arrayCopy(@Nonnull short[] original) {
        final short[] copy = new short[original.length];
        System.arraycopy(original, 0, copy, 0, original.length);
        return copy;
    }

    /**
     * Creates a copy of an array.
     *
     * @param original - Original array.
     * @return a copy of the original array.
     */
    public static boolean[] arrayCopy(@Nonnull boolean[] original) {
        final boolean[] copy = new boolean[original.length];
        System.arraycopy(original, 0, copy, 0, original.length);
        return copy;
    }

    private static <E, S> E getFromIndexed(int index, S s, Function<S, Integer> fnSize, BiFunction<S, Integer, E> fnGet) {
        final Integer size = fnSize.apply(s);

        if (index < 0 || index >= size) {
            return null;
        }

        return fnGet.apply(s, index);
    }

}
