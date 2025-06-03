package me.hapyl.eterna.module.util;

import org.jetbrains.annotations.NotNull;

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
    @Nonnull
    String start();

    /**
     * The between of elements.
     */
    @Nonnull
    String between();

    /**
     * The end of the collection.
     */
    @Nonnull
    String end();

    @Nonnull
    static Wrap of(@Nullable String start, @Nullable String between, @Nullable String end) {
        return new Wrap() {
            @Override
            public @NotNull String start() {
                return start == null ? "" : start;
            }

            @Override
            public @NotNull String between() {
                return between == null ? "" : between;
            }

            @Override
            public @NotNull String end() {
                return end == null ? "" : end;
            }
        };
    }

}