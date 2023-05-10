package me.hapyl.spigotutils.module.util;

import java.util.function.BiFunction;

public interface MapWrap<K, V> extends Wrap {

    MapWrap<?, ?> DEFAULT = of("[", ", ", "]", (key, value) -> key + " = " + value);

    String keyToValue(K key, V value);

    static <K, V> MapWrap<K, V> of(String start, String between, String end, BiFunction<K, V, String> function) {
        return new MapWrap<>() {

            @Override
            public String keyToValue(K key, V value) {
                return function.apply(key, value);
            }

            @Override
            public String start() {
                return start;
            }

            @Override
            public String between() {
                return between;
            }

            @Override
            public String end() {
                return end;
            }
        };
    }
}
