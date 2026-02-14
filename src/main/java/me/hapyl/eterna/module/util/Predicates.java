package me.hapyl.eterna.module.util;

import me.hapyl.eterna.module.annotate.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * Represents a utility class for {@link Predicate}.
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
    @NotNull
    public static <T> Predicate<T> truthy() {
        return t -> true;
    }
    
    /**
     * Gets an always {@code false} predicate.
     *
     * @param <T> - The predicate type.
     * @return an always {@code false} predicate.
     */
    @NotNull
    public static <T> Predicate<T> falsy() {
        return t -> false;
    }
    
    /**
     * Gets a negated {@link Predicate}.
     *
     * @param predicate - The predicate to negate.
     * @param <T>       - The predicate type.
     * @return a negated predicate.
     * @deprecated Use {@link Predicate#not(Predicate)}.
     */
    @NotNull
    @Deprecated
    public static <T> Predicate<T> not(@NotNull Predicate<T> predicate) {
        return predicate.negate();
    }
}
