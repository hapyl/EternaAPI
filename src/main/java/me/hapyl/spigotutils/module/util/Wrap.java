package me.hapyl.spigotutils.module.util;

import java.util.function.BiFunction;

/**
 * Represents a wrapper for an array.
 */
public interface Wrap {

    /**
     * Default wrapped: [element, element, element]
     */
    Wrap DEFAULT = of("[", ", ", "]");

    /**
     * The start of the array.
     */
    String start();

    /**
     * The between of elements.
     */
    String between();

    /**
     * The end of the array.
     */
    String end();

    /**
     * Static method to create a new Wrap.
     */
    static Wrap of(String start, String between, String end) {
        return new Wrap() {
            @Override
            public String start() {
                return start == null ? "" : start;
            }

            @Override
            public String between() {
                return between == null ? "" : between;
            }

            @Override
            public String end() {
                return end == null ? "" : end;
            }
        };
    }

    interface Map<K, V> extends Wrap {

        Wrap.Map<?, ?> DEFAULT = of("[", ", ", "]", (key, value) -> key + " = " + value);

        String keyToValue(K key, V value);

        static <K, V> Wrap.Map<K, V> of(String start, String between, String end, BiFunction<K, V, String> function) {
            return new Wrap.Map<>() {

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
}