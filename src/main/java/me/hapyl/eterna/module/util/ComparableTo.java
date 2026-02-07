package me.hapyl.eterna.module.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a {@link Comparable} interface that provides additional methods for comparing.
 *
 * @param <T> – The type of objects that may be compared.
 */
public interface ComparableTo<T> extends Comparable<T> {
    
    /**
     * Gets whether the given {@link T} is identical to this object.
     *
     * @param t – The object to compare to.
     * @return {@code true} if the objects are equal; {@code false} otherwise.
     */
    default boolean isEquals(@NotNull T t) {
        return compareTo(t) == 0;
    }
    
    /**
     * A static helper method for comparing the two {@link T}.
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
