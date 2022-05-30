package me.hapyl.spigotutils.module.util;

import me.hapyl.spigotutils.module.math.Numbers;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CollectionUtils {

	@Nullable
	public static <E> E get(List<E> arrayList, int index) {
		return getOrDefault(arrayList, index, null);
	}

	@Nullable
	public static <E> E get(E[] array, int index) {
		return getOrDefault(array, index, null);
	}

	public static <E> E getOrDefault(E[] array, int index, E def) {
		return index >= array.length ? def : array[index];
	}

	public static <E> E getOrDefault(List<E> arrayList, int index, E def) {
		return index >= arrayList.size() ? def : arrayList.get(index);
	}

	@Nullable
	public static <E> E get(Set<E> hashSet, int index) {
		return getOrDefault(hashSet, index, null);
	}

	public static <E> E getOrDefault(Set<E> hashSet, int index, E def) {
		return index >= hashSet.size() ? def : (E)hashSet.toArray()[index];
	}

	public static <E> E addAndGet(List<E> list, E toAdd) {
		list.add(toAdd);
		return toAdd;
	}

	public static <E> String wrapToString(E[] array, Wrap wrap) {
		final StringBuilder builder = new StringBuilder();
		builder.append(wrap.start());
		for (int i = 0; i < array.length; i++) {
			final E e = array[i];
			builder.append(e.toString());
			if (i != array.length - 1) {
				builder.append(wrap.between());
			}
		}
		return builder.append(wrap.end()).toString();
	}

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

	public static <E> String wrapToString(E[] array) {
		return wrapToString(array, new Wrap() {
			@Override
			public String start() {
				return "[";
			}

			@Override
			public String between() {
				return ", ";
			}

			@Override
			public String end() {
				return "]";
			}
		});
	}

	public static <E> boolean nullOrEmpty(E[] array) {
		return array == null || array.length == 0;
	}

	@SafeVarargs
	public static <E> List<E> migrateSilent(List<E>... lists) {
		return migrate(false, lists);
	}

	@SafeVarargs
	public static <E> List<E> migrate(List<E>... lists) {
		return migrate(true, lists);
	}

	@SafeVarargs
	public static <E> List<E> migrate(boolean clearLists, List<E>... lists) {
		final List<E> newList = new ArrayList<>();
		if (lists.length == 0) {
			return newList;
		}
		short count = 0;
		for (final List<E> list : lists) {
			if (count >= Short.MAX_VALUE) {
				return newList;
			}
			newList.addAll(list);
			list.clear();
			++count;
		}
		return newList;
	}

	public static <E> void forEachAndClear(Collection<E> collection, Consumer<E> action) {
		collection.forEach(action);
		collection.clear();
	}

	public static <K> void addMapValue(Map<K, Integer> hashMap, K value, int toAdd) {
		hashMap.put(value, hashMap.getOrDefault(value, 0) + toAdd);
	}

	public static <K> void clampMapValue(Map<K, Integer> hashMap, K value, int min, int max) {
		final int integer = hashMap.getOrDefault(value, 0);
		hashMap.put(value, Numbers.clamp(integer, min, max));
	}

	public static <E> E addAndGet(Set<E> set, E toAdd) {
		set.add(toAdd);
		return toAdd;
	}

	public static <K, V> Map<K, V> newMapAndPut(K key, V value) {
		final HashMap<K, V> newMap = new HashMap<>();
		newMap.put(key, value);
		return newMap;
	}

	public static <K, V> Gap<K, V> newGap() {
		return new Gap<>();
	}

	@SafeVarargs
	public static <E> void addAll(List<E> list, E... toAdd) {
		list.addAll(Arrays.asList(toAdd));
	}

	@SafeVarargs
	public static <E> void addAll(Set<E> hashSet, E... toAdd) {
		hashSet.addAll(Arrays.asList(toAdd));
	}

	@Nullable
	public static <E> E randomElement(Set<E> hashSet) {
		return randomElement(hashSet, null);
	}

	public static <E> E randomElement(Set<E> hashSet, E def) {
		if (hashSet.isEmpty()) {
			return def;
		}
		return getOrDefault(hashSet, ThreadLocalRandom.current().nextInt(hashSet.size()), def);
	}

	@Nullable
	public static <E> E randomElement(E[] array) {
		return randomElement(array, null);
	}

	public static <E> E randomElement(E[] array, E def) {
		if (array.length == 0) {
			return def;
		}
		return array[ThreadRandom.nextInt(array.length)];
	}

	@Nullable
	public static <E> E randomElement(List<E> list) {
		return randomElement(list, null);
	}

	public static <E> E randomElement(List<E> list, E def) {
		if (list.isEmpty()) {
			return def;
		}
		return getOrDefault(list, ThreadLocalRandom.current().nextInt(list.size()), def);
	}

	public static <E> void forEachConcurrent(List<E> arrayList, Consumer<E> action) {
		ArrayList<E> copy = new ArrayList<>(arrayList);
		for (final E t : copy) {
			action.accept(t);
		}
		copy.clear();
	}

	public static <E> void addIfAbsent(List<E> arrayList, E element) {
		if (!arrayList.contains(element)) {
			arrayList.add(element);
		}
	}

	public static <E> void forEachConcurrent(Set<E> hashSet, Consumer<E> action) {
		Set<E> copy = new HashSet<>(hashSet);
		for (final E t : copy) {
			action.accept(t);
		}
		copy.clear();
	}

	@SafeVarargs
	public static <E> void addAll(Collection<E> collection, E... elements) {
		collection.addAll(Arrays.asList(elements));
	}

	public static <E> void addAll(Collection<E> collection, Collection<E> anotherCollection) {
		collection.addAll(anotherCollection);
	}

	@SafeVarargs
	public static <E> void removeAll(Collection<E> collection, E... elements) {
		for (final E element : elements) {
			collection.remove(element);
		}
	}

	public static <E> E[] setToArray(Set<E> hashSet) {
		return (E[])hashSet.toArray();
	}

	public static <E> List<E> arrayToList(E[] array) {
		return new ArrayList<>(Arrays.asList(array));
	}

	public static <E> Set<E> arrayToSet(E[] array) {
		return new HashSet<>(Arrays.asList(array));
	}

	public static List<Integer> intArrayToList(int[] array) {
		final List<Integer> list = new ArrayList<>();
		for (final int i : array) {
			list.add(i);
		}
		return list;
	}

	public static <K, V> void forEachConcurrent(Map<K, V> hashMap, BiConsumer<K, V> action) {
		Map<K, V> copy = new HashMap<>(hashMap);
		copy.forEach(action);
		copy.clear();
	}

}
