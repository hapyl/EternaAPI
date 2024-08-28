package me.hapyl.eterna.module.util;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Provides a set of functional interfaces for consuming multiple parameters.
 * Each interface represents a consumer that accepts a specific number of parameters.
 * Note: {@link Consumer} and {@link BiConsumer} should be used for single and dual parameter cases respectively.
 */
public interface Consumers {

    /**
     * @deprecated Use {@link Consumer} instead.
     */
    @Deprecated
    interface ConsumerA<A> extends Consumer<A> {
        void accept(@Nonnull A a);
    }

    /**
     * A functional interface for accepting three parameters.
     *
     * @param <A> - The type of the first parameter.
     * @param <B> - The type of the second parameter.
     * @param <C> - The type of the third parameter.
     */
    @FunctionalInterface
    interface ConsumerABC<A, B, C> {
        /**
         * Accepts three parameters.
         *
         * @param a - The first parameter.
         * @param b - The second parameter.
         * @param c - The third parameter.
         */
        void accept(@Nonnull A a, @Nonnull B b, @Nonnull C c);
    }

    /**
     * A functional interface for accepting four parameters.
     *
     * @param <A> - The type of the first parameter.
     * @param <B> - The type of the second parameter.
     * @param <C> - The type of the third parameter.
     * @param <D> - The type of the fourth parameter.
     */
    @FunctionalInterface
    interface ConsumerABCD<A, B, C, D> {
        /**
         * Accepts four parameters.
         *
         * @param a - The first parameter.
         * @param b - The second parameter.
         * @param c - The third parameter.
         * @param d - The fourth parameter.
         */
        void accept(@Nonnull A a, @Nonnull B b, @Nonnull C c, @Nonnull D d);
    }

    /**
     * A functional interface for accepting five parameters.
     *
     * @param <A> - The type of the first parameter.
     * @param <B> - The type of the second parameter.
     * @param <C> - The type of the third parameter.
     * @param <D> - The type of the fourth parameter.
     * @param <E> - The type of the fifth parameter.
     */
    @FunctionalInterface
    interface ConsumerABCDE<A, B, C, D, E> {
        /**
         * Accepts five parameters.
         *
         * @param a - The first parameter.
         * @param b - The second parameter.
         * @param c - The third parameter.
         * @param d - The fourth parameter.
         * @param e - The fifth parameter.
         */
        void accept(@Nonnull A a, @Nonnull B b, @Nonnull C c, @Nonnull D d, @Nonnull E e);
    }

    /**
     * A functional interface for accepting six parameters.
     *
     * @param <A> - The type of the first parameter.
     * @param <B> - The type of the second parameter.
     * @param <C> - The type of the third parameter.
     * @param <D> - The type of the fourth parameter.
     * @param <E> - The type of the fifth parameter.
     * @param <F> - The type of the sixth parameter.
     */
    @FunctionalInterface
    interface ConsumerABCDEF<A, B, C, D, E, F> {
        /**
         * Accepts six parameters.
         *
         * @param a - The first parameter.
         * @param b - The second parameter.
         * @param c - The third parameter.
         * @param d - The fourth parameter.
         * @param e - The fifth parameter.
         * @param f - The sixth parameter.
         */
        void accept(@Nonnull A a, @Nonnull B b, @Nonnull C c, @Nonnull D d, @Nonnull E e, @Nonnull F f);
    }

    /**
     * A functional interface for accepting seven parameters.
     *
     * @param <A> - The type of the first parameter.
     * @param <B> - The type of the second parameter.
     * @param <C> - The type of the third parameter.
     * @param <D> - The type of the fourth parameter.
     * @param <E> - The type of the fifth parameter.
     * @param <F> - The type of the sixth parameter.
     * @param <G> - The type of the seventh parameter.
     */
    @FunctionalInterface
    interface ConsumerABCDEFG<A, B, C, D, E, F, G> {
        /**
         * Accepts seven parameters.
         *
         * @param a - The first parameter.
         * @param b - The second parameter.
         * @param c - The third parameter.
         * @param d - The fourth parameter.
         * @param e - The fifth parameter.
         * @param f - The sixth parameter.
         * @param g - The seventh parameter.
         */
        void accept(@Nonnull A a, @Nonnull B b, @Nonnull C c, @Nonnull D d, @Nonnull E e, @Nonnull F f, @Nonnull G g);
    }

    /**
     * A functional interface for accepting eight parameters.
     *
     * @param <A> - The type of the first parameter.
     * @param <B> - The type of the second parameter.
     * @param <C> - The type of the third parameter.
     * @param <D> - The type of the fourth parameter.
     * @param <E> - The type of the fifth parameter.
     * @param <F> - The type of the sixth parameter.
     * @param <G> - The type of the seventh parameter.
     * @param <H> - The type of the eighth parameter.
     */
    @FunctionalInterface
    interface ConsumerABCDEFGH<A, B, C, D, E, F, G, H> {
        /**
         * Accepts eight parameters.
         *
         * @param a - The first parameter.
         * @param b - The second parameter.
         * @param c - The third parameter.
         * @param d - The fourth parameter.
         * @param e - The fifth parameter.
         * @param f - The sixth parameter.
         * @param g - The seventh parameter.
         * @param h - The eighth parameter.
         */
        void accept(@Nonnull A a, @Nonnull B b, @Nonnull C c, @Nonnull D d, @Nonnull E e, @Nonnull F f, @Nonnull G g, @Nonnull H h);
    }

    /**
     * A functional interface for accepting nine parameters.
     *
     * @param <A> - The type of the first parameter.
     * @param <B> - The type of the second parameter.
     * @param <C> - The type of the third parameter.
     * @param <D> - The type of the fourth parameter.
     * @param <E> - The type of the fifth parameter.
     * @param <F> - The type of the sixth parameter.
     * @param <G> - The type of the seventh parameter.
     * @param <H> - The type of the eighth parameter.
     * @param <I> - The type of the ninth parameter.
     */
    @FunctionalInterface
    interface ConsumerABCDEFGHI<A, B, C, D, E, F, G, H, I> {
        /**
         * Accepts nine parameters.
         *
         * @param a - The first parameter.
         * @param b - The second parameter.
         * @param c - The third parameter.
         * @param d - The fourth parameter.
         * @param e - The fifth parameter.
         * @param f - The sixth parameter.
         * @param g - The seventh parameter.
         * @param h - The eighth parameter.
         * @param i - The ninth parameter.
         */
        void accept(@Nonnull A a, @Nonnull B b, @Nonnull C c, @Nonnull D d, @Nonnull E e, @Nonnull F f, @Nonnull G g, @Nonnull H h, @Nonnull I i);
    }

    /**
     * A functional interface for accepting ten parameters.
     *
     * @param <A> - The type of the first parameter.
     * @param <B> - The type of the second parameter.
     * @param <C> - The type of the third parameter.
     * @param <D> - The type of the fourth parameter.
     * @param <E> - The type of the fifth parameter.
     * @param <F> - The type of the sixth parameter.
     * @param <G> - The type of the seventh parameter.
     * @param <H> - The type of the eighth parameter.
     * @param <I> - The type of the ninth parameter.
     * @param <J> - The type of the tenth parameter.
     */
    @FunctionalInterface
    interface ConsumerABCDEFGHIJ<A, B, C, D, E, F, G, H, I, J> {
        /**
         * Accepts ten parameters.
         *
         * @param a - The first parameter.
         * @param b - The second parameter.
         * @param c - The third parameter.
         * @param d - The fourth parameter.
         * @param e - The fifth parameter.
         * @param f - The sixth parameter.
         * @param g - The seventh parameter.
         * @param h - The eighth parameter.
         * @param i - The ninth parameter.
         * @param j - The tenth parameter.
         */
        void accept(@Nonnull A a, @Nonnull B b, @Nonnull C c, @Nonnull D d, @Nonnull E e, @Nonnull F f, @Nonnull G g, @Nonnull H h, @Nonnull I i, @Nonnull J j);
    }
}