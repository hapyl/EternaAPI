package me.hapyl.eterna.module.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A specialized {@link Comparable} interface that provides additional methods for comparison.
 *
 * @param <T> – The type of objects that may be compared.
 */
public interface ComparableTo<T> extends Comparable<T> {

    /**
     * Checks if the given object is equal to this object based on comparison.
     *
     * @param t – The object to be compared.
     * @return {@code true} if the objects are equal, {@code false} otherwise.
     */
    default boolean isEquals(@Nonnull T t) {
        return compareTo(t) == 0;
    }

    /**
     * Compares two objects for equality.
     *
     * @param a – The first object to be compared, may be {@code null}.
     * @param b – The second object to be compared, may be {@code null}.
     * @return {@code 0} if the objects are equal, {@code -1} if either object is {@code null} or they are not equal.
     */
    static <T> int comparingObjects(@Nullable T a, @Nullable T b) {
        if (a == null || b == null) {
            return -1;
        }

        return a == b ? 0 : -1;
    }

}
