package me.hapyl.eterna.module.util;

import me.hapyl.eterna.module.annotate.FactoryMethod;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * A tuple that holds immutable bi-object.
 *
 * @param <A> - First element.
 * @param <B> - Second element.
 */
public class Tuple<A, B> {

    private final A a;
    private final B b;

    /**
     * A bi-object immutable data structure.
     *
     * @param a - First element.
     * @param b - Second element.
     */
    public Tuple(@Nonnull A a, @Nonnull B b) {
        this.a = Objects.requireNonNull(a, "Tuple does not permit null values.");
        this.b = Objects.requireNonNull(b, "Tuple does not permit null values.");
    }

    /**
     * Gets the first element.
     *
     * @return the first element.
     */
    @Nonnull
    public A a() {
        return a;
    }

    /**
     * Gets the first element.
     *
     * @return the first element.
     */
    @Nonnull
    public A getA() {
        return a;
    }

    /**
     * Gets the second element.
     *
     * @return the second element.
     */
    @Nonnull
    public B b() {
        return b;
    }

    /**
     * Gets the second element.
     *
     * @return the second element.
     */
    @Nonnull
    public B getB() {
        return b;
    }

    /**
     * Returns true if the given {@link Object} is a {@link Tuple} and the elements are identical.
     *
     * @param obj - Object.
     * @return true if the given object is a tuple and the elements are identical.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (this.getClass() != obj.getClass()) {
            return false;
        }

        final Tuple<?, ?> other = (Tuple<?, ?>) obj;
        return Objects.equals(this.a, other.a) && Objects.equals(this.b, other.b);
    }

    /**
     * Gets the hash code for elements in this {@link Tuple}.
     *
     * @return the hash code for elements in this tuple.
     */
    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }

    @Nonnull
    @FactoryMethod(net.minecraft.util.Tuple.class)
    public static <A, B> net.minecraft.util.Tuple<A, B> of(@Nonnull A a, @Nonnull B b) {
        return new net.minecraft.util.Tuple<>(a, b);
    }

}