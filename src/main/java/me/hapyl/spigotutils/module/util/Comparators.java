package me.hapyl.spigotutils.module.util;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.function.Function;

/**
 * Static util for {@link Comparator} creations.
 */
public final class Comparators {

    /**
     * Creates a {@link Boolean} comparator.
     *
     * @param fn - Comparing function.
     * @return a boolean comparator.
     */
    @Nonnull
    public static <T> Comparator<T> comparingBool(@Nonnull Function<T, Boolean> fn) {
        return (o1, o2) -> {
            final Boolean boolA = fn.apply(o1);
            final Boolean boolB = fn.apply(o2);

            return boolB ? 1 : boolA ? -1 : 0;
        };
    }

}
