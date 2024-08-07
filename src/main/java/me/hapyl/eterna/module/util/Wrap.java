package me.hapyl.eterna.module.util;

import me.hapyl.eterna.module.annotate.FactoryMethod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a wrapper for a collection.
 */
public interface Wrap {

    /**
     * Default wrapped: [element, element, element]
     */
    Wrap DEFAULT = of("[", ", ", "]");

    /**
     * The start of the collection.
     */
    String start();

    /**
     * The between of elements.
     */
    String between();

    /**
     * The end of the collection.
     */
    String end();

    @Nonnull
    @FactoryMethod(Wrap.class)
    static Wrap of(@Nullable String start, @Nullable String between, @Nullable String end) {
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