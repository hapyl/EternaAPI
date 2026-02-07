package me.hapyl.eterna.module.util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents a {@link Tuple} that holds immutable pair of objects.
 *
 * @param <A> - The first element type.
 * @param <B> - The second element type.
 */
public class Tuple<A, B> {
    
    private final A a;
    private final B b;
    
    /**
     * Creates a new {@link Tuple}.
     *
     * @param a - The first element.
     * @param b - The second element.
     * @throws NullPointerException if either of elements is {@code null}.
     */
    public Tuple(@NotNull A a, @NotNull B b) {
        this.a = Objects.requireNonNull(a, "Tuple does not permit null values.");
        this.b = Objects.requireNonNull(b, "Tuple does not permit null values.");
    }
    
    /**
     * Gets the {@link A} first element.
     *
     * @return the first element.
     */
    @NotNull
    public A a() {
        return a;
    }
    
    /**
     * Gets the {@link B} second element.
     *
     * @return the second element.
     */
    @NotNull
    public B b() {
        return b;
    }
    
    /**
     * Gets whether the given {@link Object} is a {@link Tuple} and its elements are identical.
     *
     * @param obj - The object to check.
     * @return {@code true} if the given object is a tuple its elements are identical.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        
        final Tuple<?, ?> that = (Tuple<?, ?>) obj;
        return Objects.equals(this.a, that.a) && Objects.equals(this.b, that.b);
    }
    
    /**
     * Gets the hash code of the elements in this {@link Tuple}
     *
     * @return the hash code for elements in this tuple.
     */
    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
    
    /**
     * A static factory method for creating a {@link Tuple}.
     *
     * @param a   - The first element.
     * @param b   - The second element.
     * @param <A> - The first element type.
     * @param <B> - The second element type.
     * @return a new tuple.
     * @throws NullPointerException if either of elements is {@code null}.
     */
    @NotNull
    public static <A, B> Tuple<A, B> of(@NotNull A a, @NotNull B b) {
        return new Tuple<>(a, b);
    }
    
}