package me.hapyl.spigotutils.module.util;

import me.hapyl.spigotutils.module.annotate.StaticShortcut;

import java.util.*;

// Short for Collection Utils
public final class Clts extends CollectionUtils {

    public static <K, V> Map<K, V> newMap() {
        return new HashMap<>();
    }

    public static <T> List<T> newList() {
        return new ArrayList<>();
    }

    public static <T> Queue<T> newQueue() {
        return new LinkedList<>();
    }

    public static <T> Set<T> newHashSet() {
        return new HashSet<>();
    }

    @StaticShortcut
    public static <T> Set<T> newSet() {
        return newHashSet();
    }

}
