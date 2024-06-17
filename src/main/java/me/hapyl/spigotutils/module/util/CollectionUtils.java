package me.hapyl.spigotutils.module.util;

import me.hapyl.spigotutils.module.math.Numbers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Utility class for collections.
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
    public static <E> E get(List<E> arrayList, int index) {
        return getOrDefault(arrayList, index, null);
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
    public static <E> E get(E[] array, int index) {
        return getOrDefault(array, index, null);
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
    public static <E> E get(Set<E> hashSet, int index) {
        return getOrDefault(hashSet, index, null);
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
    public static <E> E getOrDefault(E[] array, int index, @Nonnull E def) {
        return index >= array.length ? def : array[index];
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
    @SuppressWarnings("unchecked")
    public static <E> E getOrDefault(Collection<E> collection, int index, @Nonnull E def) {
        return index >= collection.size() ? def : (E) collection.toArray()[index];
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
    public static <E> E getOrDefault(List<E> arrayList, int index, E def) {
        return index >= arrayList.size() ? def : arrayList.get(index);
    }

    /**
     * Gets the element from a set, or def if the index is out of bounds.
     *
     * @param hashSet - set to get from.
     * @param index   - index of the element to get.
     * @param def     - default value to return if the index is out of bounds.
     * @param <E>     - type of the set.
     * @return the element at the index, or def if the index is out of bounds.
     */
    public static <E> E getOrDefault(Set<E> hashSet, int index, E def) {
        return index >= hashSet.size() ? def : (E) hashSet.toArray()[index];
    }

    /**
     * Adds the element to list and returns the list.
     *
     * @param list  - list to add to.
     * @param toAdd - element to add.
     * @param <E>   - type of the list.
     * @return the list.
     */
    public static <E> E addAndGet(List<E> list, E toAdd) {
        list.add(toAdd);
        return toAdd;
    }

    /**
     * Wraps an array to string, according to wrapper.
     *
     * @param array - array to wrap.
     * @param wrap  - wrap to use.
     * @param <E>   - type of the array.
     * @return the wrapped string.
     */
    public static <E> String wrapToString(E[] array, Wrap wrap) {
        final StringBuilder builder = new StringBuilder(wrap.start());

        for (int i = 0; i < array.length; i++) {
            final E e = array[i];
            builder.append(e.toString());
            if (i != array.length - 1) {
                builder.append(wrap.between());
            }
        }
        return builder.append(wrap.end()).toString();
    }

    /**
     * Wraps a collection to string according to wrap.
     *
     * @param collection - collection to wrap.
     * @param wrap       - wrap to use.
     * @param <E>        - type of the collection.
     * @return the wrapped string.
     */
    public static <E> String wrapToString(Collection<E> collection, Wrap wrap) {
        final StringBuilder builder = new StringBuilder();
        builder.append(wrap.start());
        int i = 0;
        for (final E e : collection) {
            builder.append(e.toString());
            if (i != (collection.size() - 1)) {
                builder.append(wrap.between());
            }
            ++i;
        }
        return builder.append(wrap.end()).toString();
    }

    /**
     * Wraps a collection to a string according to default wrap.
     *
     * @param collection - collection to wrap.
     * @return the wrapped string.
     */
    public static <E> String wrapToString(Collection<E> collection) {
        return wrapToString(collection, Wrap.DEFAULT);
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
    public static <K, V> String wrapToString(Map<K, V> map, MapWrap<K, V> wrap) {
        final StringBuilder builder = new StringBuilder(wrap.start());

        int i = 0;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (i != 0) {
                builder.append(wrap.between());
            }

            builder.append(wrap.keyToValue(entry.getKey(), entry.getValue()));

            i++;
        }

        return builder.toString();
    }

    /**
     * Wraps array to string using default wrap
     *
     * @param array - array to wrap.
     * @param <E>   - type of the array.
     * @return the wrapped string.
     */
    public static <E> String wrapToString(E[] array) {
        return wrapToString(array, Wrap.DEFAULT);
    }

    /**
     * Returns true if an array is null or empty.
     *
     * @param array - array to check.
     * @param <E>   - type of the array.
     * @return true if an array is null or empty.
     */
    public static <E> boolean nullOrEmpty(E[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Migrates all the lists into one and returns it.
     *
     * @param lists - List to migrate.
     * @param <E>   - type of the lists.
     * @return the new list.
     */
    @SafeVarargs
    public static <E> List<E> migrateSilent(List<E>... lists) {
        return migrate(false, lists);
    }

    /**
     * Migrates all the lists into one, clears them and returns a new list.
     *
     * @param lists - List to migrate.
     * @param <E>   - type of the lists.
     * @return the new list.
     */
    @SafeVarargs
    public static <E> List<E> migrate(List<E>... lists) {
        return migrate(true, lists);
    }

    /**
     * Migrates one list to another, according to migrator.
     *
     * @param from     - list to migrate from.
     * @param to       - list to migrate to.
     * @param migrator - migrator to use.
     * @param <E>      - type of the list.
     * @param <N>      - type of the list.
     * @param <T>      - type of the list.
     * @return the migrated list.
     */
    public static <E, N, T extends Collection<N>> T migrate(E[] from, T to, Migrator<E, N> migrator) {
        for (E e : from) {
            to.add(migrator.migrate(e));
        }
        return to;
    }

    /**
     * Migrates one array to another according to migrator.
     *
     * @param from     - array to migrate from.
     * @param to       - array to migrate to.
     * @param migrator - migrator to use.
     * @param <E>      - type of the array.
     * @param <N>      - type of the array.
     * @return the migrated array.
     */
    public static <E, N> N[] migrate(E[] from, N[] to, Migrator<E, N> migrator) {
        if (to.length != from.length) {
            throw new IndexOutOfBoundsException("%s != %s".formatted(from.length, to.length));
        }

        for (int i = 0; i < to.length; i++) {
            to[i] = migrator.migrate(from[i]);
        }

        return to;
    }

    /**
     * Returns the next value in the array.
     *
     * @param values  - array to get from.
     * @param current - current value.
     * @param <T>     - type of the array.
     * @return the next value.
     */
    public static <T> T getNextValue(T[] values, T current) {
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
     *
     * @param values  - array to get from.
     * @param current - current value.
     * @param <T>     - type of the array.
     * @return the previous value.
     */
    public static <T> T getPreviousValue(T[] values, T current) {
        for (int i = 0; i < values.length; i++) {
            final T t = values[i];
            if (t.equals(current)) {
                return i == 0 ? values[values.length - 1] : values[i - 1];
            }
        }
        return current;
    }

    @SafeVarargs
    public static <E> List<E> migrate(boolean clearLists, List<E>... lists) {
        final List<E> newList = new ArrayList<>();
        if (lists.length == 0) {
            return newList;
        }
        short count = 0;
        for (final List<E> list : lists) {
            if (count == Short.MAX_VALUE) {
                return newList;
            }
            newList.addAll(list);
            list.clear();
            ++count;
        }
        return newList;
    }

    /**
     * Iterates over the collection and then clears it.
     * Useful to iterate over something like entities to remove them.
     *
     * @param collection - collection to iterate over.
     * @param action     - action to perform.
     * @param <E>        - type of the collection.
     */
    public static <E> void forEachAndClear(Collection<E> collection, Consumer<E> action) {
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
     */
    public static <K> void addMapValue(Map<K, Integer> hashMap, K value, int toAdd) {
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
     */
    public static <K> void clampMapValue(Map<K, Integer> hashMap, K value, int min, int max) {
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
    public static <E> E addAndGet(Set<E> set, E toAdd) {
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
    public static <K, V> Map<K, V> newMapAndPut(K key, V value) {
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
    public static <E> void addAll(List<E> list, E... toAdd) {
        list.addAll(Arrays.asList(toAdd));
    }

    /**
     * Adds all the objects to the set.
     *
     * @param hashSet - set to add to.
     * @param toAdd   - objects to add.
     * @param <E>     - type of the set.
     */
    @SafeVarargs
    public static <E> void addAll(Set<E> hashSet, E... toAdd) {
        hashSet.addAll(Arrays.asList(toAdd));
    }

    /**
     * Adds all elements to the collection.
     *
     * @param collection - collection to add to.
     * @param elements   - elements to add.
     * @param <E>        - type of the collection.
     */
    @SafeVarargs
    public static <E> void addAll(Collection<E> collection, E... elements) {
        collection.addAll(Arrays.asList(elements));
    }

    /**
     * Adds all elements to the collection from another collection.
     *
     * @param collection        - collection to add to.
     * @param anotherCollection - collection to add from.
     * @param <E>               - type of the collection.
     */
    public static <E> void addAll(Collection<E> collection, Collection<E> anotherCollection) {
        collection.addAll(anotherCollection);
    }

    /**
     * Returns a random element from the set.
     *
     * @param hashSet - set to get from.
     * @param <E>     - type of the set.
     * @return a random element.
     */
    @Nullable
    public static <E> E randomElement(Set<E> hashSet) {
        return randomElement(hashSet, null);
    }

    /**
     * Returns a random element from the set or first if a set is empty.
     *
     * @param hashSet - set to get from.
     * @return a random element.
     */
    @Nullable
    public static <E> E randomElementOrFirst(Set<E> hashSet) {
        return randomElement(hashSet, get(hashSet, 0));
    }

    /**
     * Returns a random element from the set or def if a set is empty.
     *
     * @param hashSet - set to get from.
     * @param def     - default value.
     * @param <E>     - type of the set.
     * @return a random element.
     */
    public static <E> E randomElement(Set<E> hashSet, E def) {
        if (hashSet.isEmpty()) {
            return def;
        }

        return getOrDefault(hashSet, ThreadLocalRandom.current().nextInt(hashSet.size()), def);
    }

    /**
     * Gets a random element from a collection, or default if a collection is empty.
     *
     * @param collection - Collection.
     * @param def        - Default value.
     * @param <E>        - Type.
     * @return a random element or def.
     */
    public static <E> E randomElement(Collection<E> collection, E def) {
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
    public static <E> E randomElement(E[] array) {
        return randomElement(array, null);
    }

    /**
     * Returns a random element from the array or first element if array is empty.
     *
     * @param array - array to get from.
     * @return a random element.
     */
    @Nullable
    public static <E> E randomElementOrFirst(E[] array) {
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
    public static <E> E randomElement(E[] array, E def) {
        if (array.length == 0) {
            return def;
        }
        return array[ThreadRandom.nextInt(array.length)];
    }

    /**
     * Returns a random element from the list.
     *
     * @param list - list to get from.
     * @param <E>  - type of the list.
     * @return a random element.
     */
    @Nullable
    public static <E> E randomElement(List<E> list) {
        return randomElement(list, null);
    }

    /**
     * Returns a random element from the list or first element if the list is empty.
     *
     * @param list - list to get from.
     * @return a random element.
     */
    @Nullable
    public static <E> E randomElementOrFirst(List<E> list) {
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
     * Iterates through the copy of the list and executes the action.
     *
     * @param arrayList - list to iterate through.
     * @param action    - action to execute.
     * @param <E>       - type of the list.
     */
    public static <E> void forEachConcurrent(@Nonnull List<E> arrayList, @Nonnull Consumer<E> action) {
        ArrayList<E> copy = new ArrayList<>(arrayList);
        for (final E t : copy) {
            action.accept(t);
        }
        copy.clear();
    }

    /**
     * Iterates through the copy of the set and executes the action.
     *
     * @param hashSet - set to iterate through.
     * @param action  - action to execute.
     * @param <E>     - type of the set.
     */
    public static <E> void forEachConcurrent(@Nonnull Set<E> hashSet, @Nonnull Consumer<E> action) {
        Set<E> copy = new HashSet<>(hashSet);
        for (final E t : copy) {
            action.accept(t);
        }
        copy.clear();
    }

    /**
     * Iterates through the copy of the map and executes the action.
     *
     * @param hashMap - map to iterate through.
     * @param action  - action to execute.
     * @param <K>     - type of the map key.
     * @param <V>     - type of the map value.
     */
    public static <K, V> void forEachConcurrent(@Nonnull Map<K, V> hashMap, @Nonnull BiConsumer<K, V> action) {
        Map<K, V> copy = new HashMap<>(hashMap);
        copy.forEach(action);
        copy.clear();
    }

    /**
     * Adds the element to the list if it is not already present.
     *
     * @param arrayList - list to add to.
     * @param element   - element to add.
     * @param <E>       - type of the list.
     */
    public static <E> void addIfAbsent(@Nonnull List<E> arrayList, @Nonnull E element) {
        if (!arrayList.contains(element)) {
            arrayList.add(element);
        }
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
     * Converts set to array.
     *
     * @param hashSet - set to convert.
     * @param <E>     - type of the set.
     * @return the array.
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    public static <E> E[] setToArray(@Nonnull Set<E> hashSet) {
        return (E[]) hashSet.toArray();
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
    public static List<Integer> intArrayToList(int[] array) {
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

}
