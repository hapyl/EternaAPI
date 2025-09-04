package me.hapyl.eterna.module.util;

import me.hapyl.eterna.module.annotate.UtilityClass;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

/**
 * A {@link Predicate} utility.
 */
@UtilityClass
public final class Predicates {
    
    private Predicates() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Gets an always {@code true} predicate.
     *
     * @param <T> - The predicate type.
     * @return an always {@code true} predicate.
     */
    @Nonnull
    public static <T> Predicate<T> truthy() {
        return t -> true;
    }
    
    /**
     * Gets an always {@code false} predicate.
     *
     * @param <T> - The predicate type.
     * @return an always {@code false} predicate.
     */
    @Nonnull
    public static <T> Predicate<T> falsy() {
        return t -> false;
    }
    
    /**
     * Gets a new negated {@link Predicate}.
     *
     * @param predicate - The predicate to {@link Predicate#negate()}.
     * @param <T>       - The predicate type.
     * @return a new negated {@link Predicate}.
     */
    @Nonnull
    public static <T> Predicate<T> not(@Nonnull Predicate<T> predicate) {
        return predicate.negate();
    }
}
