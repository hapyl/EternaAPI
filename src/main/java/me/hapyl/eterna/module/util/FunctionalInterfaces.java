package me.hapyl.eterna.module.util;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A utility class for working with {@link FunctionalInterface} objects.
 */
public final class FunctionalInterfaces {

    private FunctionalInterfaces() {
    }

    /**
     * Tests the given predicate with the specified value, handling null values gracefully.
     * <p>
     * This method allows testing nullable 'optional' predicates without requiring explicit null checks.
     * If either the predicate or the test value is {@code null}, the method returns {@code false}.
     *
     * @param predicate – The predicate to be tested, may be {@code null}.
     * @param test      – The value to be tested against the predicate, may be {@code null}.
     * @param <T>       – The type of the input to the predicate.
     * @return {@code true} if the predicate is non-null, the test value is non-null, and the predicate evaluates to {@code true}; {@code false} otherwise.
     */
    public static <T> boolean test(@Nullable Predicate<T> predicate, @Nullable T test) {
        return predicate != null && test != null && predicate.test(test);
    }

    public static <T> void accept(@Nullable Consumer<T> consumer, @Nullable T value) {
        if (consumer != null) {
            consumer.accept(value);
        }
    }

}
