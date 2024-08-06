package me.hapyl.spigotutils.module.util;

import me.hapyl.spigotutils.module.annotate.FactoryMethod;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * A bi-object immutable data structure.
 *
 * @param a - First element.
 * @param b - Second element.
 */
public record Tuple<A, B>(@Nonnull A a, @Nonnull B b) {

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
    public A getA() {
        return a;
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

    @Nonnull
    @FactoryMethod(Tuple.class)
    public static <A, B> Tuple<A, B> of(@Nonnull A a, @Nonnull B b) {
        return new Tuple<>(a, b);
    }

}