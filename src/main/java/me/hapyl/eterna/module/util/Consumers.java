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
     * Gets an empty {@link Consumer}.
     *
     * @return an empty consumer.
     */
    @Nonnull
    static <T> Consumer<T> empty() {
        return t -> {
        };
    }
    
    /**
     * Gets an empty {@link BiConsumer}.
     *
     * @return an empty bi-consumer.
     */
    @Nonnull
    static <T, U> BiConsumer<T, U> emptyBi() {
        return (t, u) -> {
        };
    }
    
    /**
     * A functional interface for accepting one parameter.
     *
     * @param <A> - The type of the first parameter.
     * @see Consumer
     */
    @FunctionalInterface
    interface Consumer1<A> extends Consumer<A> {
        @Override
        void accept(A a);
    }
    
    /**
     * A functional interface for accepting two parameters.
     *
     * @param <A> - The type of the first parameter.
     * @param <B> - The type of the second parameter.
     * @see BiConsumer
     */
    @FunctionalInterface
    interface Consumer2<A, B> extends BiConsumer<A, B> {
        @Override
        void accept(A a, B b);
    }
    
    /**
     * A functional interface for accepting three parameters.
     *
     * @param <A> - The type of the first parameter.
     * @param <B> - The type of the second parameter.
     * @param <C> - The type of the third parameter.
     */
    @FunctionalInterface
    interface Consumer3<A, B, C> {
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
    interface Consumer4<A, B, C, D> {
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
    interface Consumer5<A, B, C, D, E> {
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
    interface Consumer6<A, B, C, D, E, F> {
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
    interface Consumer7<A, B, C, D, E, F, G> {
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
    interface Consumer8<A, B, C, D, E, F, G, H> {
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
    interface Consumer9<A, B, C, D, E, F, G, H, I> {
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
    interface Consumer10<A, B, C, D, E, F, G, H, I, J> {
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