package me.hapyl.eterna.module.util;

import me.hapyl.eterna.module.annotate.UtilityClass;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A somewhat helpful utility class to deal with {@code null} (and non-{@code null}!) objects.
 */
@UtilityClass
public class Nulls {
    
    private Nulls() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Accepts the given {@link Consumer} if, and only if, the given {@code E} is non-{@code null}.
     *
     * @param object - The object to check.
     * @param action - The action to perform.
     */
    public static <E> void acceptNonNull(@Nullable E object, @Nonnull Consumer<E> action) {
        if (object != null) {
            action.accept(object);
        }
    }
    
    /**
     * Accepts the given {@link Runnable} if, and only if, the given {@code E} is {@code null}.
     *
     * @param object - The object to check.
     * @param action - The action to perform.
     */
    public static <E> void acceptNull(@Nullable E object, @Nonnull Runnable action) {
        if (object == null) {
            action.run();
        }
    }
    
    /**
     * Gets whether the given array or <b>any</b> of its elements are {@code null}.
     *
     * @param array - The array to check.
     * @return {@code true} if the given array or any of its elements are {@code null}, {@code false} otherwise.
     */
    public static boolean anyNull(@Nullable Object... array) {
        if (array == null) {
            return true;
        }
        
        for (Object object : array) {
            if (object == null) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Gets whether the given array and <b>all</b> of its elements are {@code null}.
     *
     * @param array - The array to check.
     * @return {@code true} if the array is null or all of its elements are {@code null}, {@code false} otherwise.
     */
    public static boolean allNull(@Nullable Object... array) {
        if (array == null) {
            return true;
        }
        
        for (Object object : array) {
            if (object != null) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Gets whether the given array and all of its elements are non-{@code null}.
     *
     * @param array - The array to check.
     * @return {@code true} if the array is non-null and all of its elements are non-{@code null}, {@code false} otherwise.
     */
    public static boolean allNonNull(@Nullable Object... array) {
        if (array == null) {
            return false;
        }
        
        for (Object object : array) {
            if (object == null) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Gets the given value if it is non-{@code null}; otherwise returns the default value.
     *
     * @param object       - The value to check.
     * @param defaultValue - The value to return if {@code t} is {@code null}.
     * @return The non-{@code null} value.
     */
    @Nonnull
    public static <T> T nonNull(@Nullable T object, @Nonnull T defaultValue) {
        return object != null ? object : defaultValue;
    }
    
    /**
     * Applies the given {@link Function} to the given {@code T} if it is non-{@code null}, returning the result if also non-{@code null};
     * otherwise returns the default value.
     *
     * @param object             - The object to unwrap.
     * @param unwrappingFunction - The function to apply if {@code object} is non-{@code null}.
     * @param defaultValue       - The value to return if either {@code object} or the result of the function is {@code null}.
     * @return The unwrapped value or {@code defaultValue} if unavailable.
     */
    public static <T, R> R unwrap(@Nullable T object, @Nonnull Function<T, R> unwrappingFunction, @Nullable R defaultValue) {
        if (object == null) {
            return null;
        }
        
        final R r = unwrappingFunction.apply(object);
        return r != null ? r : defaultValue;
    }
    
    /**
     * Applies the given {@link Function} to the given {@code T} if it is non-{@code null}, returning the result.
     *
     * @param object             - The object to unwrap.
     * @param unwrappingFunction - The unwrapping function.
     * @return the unwrapped object, or {@code null}.
     */
    @Nullable
    public static <T, R> R unwrap(@Nullable T object, @Nonnull Function<T, R> unwrappingFunction) {
        return unwrap(object, unwrappingFunction, null);
    }
    
}
