package me.hapyl.spigotutils.module.util;

/**
 * Represents a wrapper for an array.
 */
public interface Wrap {

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
     * Default wrapped: [element, element, element]
     */
    Wrap DEFAULT = of("[", ", ", "]");

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

}