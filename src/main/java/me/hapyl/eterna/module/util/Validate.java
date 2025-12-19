package me.hapyl.eterna.module.util;

import me.hapyl.eterna.module.annotate.UtilityClass;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Function;

/**
 * A somewhat helpful validator class.
 */
@UtilityClass
public final class Validate {
    
    private Validate() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Checks whether the given {@code object} is non-{@code null}, throws a {@link IllegalArgumentException} otherwise.
     *
     * @param object  - The object to check.
     * @param message - The message of the exception.
     */
    public static void nonNull(@Nullable Object object, @Nonnull String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
    
    /**
     * Checks whether the given {@code object} is non-{@code null}, throws a {@link IllegalArgumentException} otherwise.
     *
     * @param object - The object to check.
     */
    public static void nonNull(@Nullable Object object) {
        nonNull(object, "Object must not be null!");
    }
    
    /**
     * Checks whether the given {@code boolean} expression is truthy, throws a {@link IllegalArgumentException} otherwise.
     *
     * @param expression - The expression to check.
     * @param message    - The message of the exception.
     */
    public static void isTrue(boolean expression, @Nonnull String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }
    
    /**
     * Checks whether the given {@code boolean} expression is truthy, throws a {@link IllegalArgumentException} otherwise.
     *
     * @param expression - The expression to check.
     */
    public static void isTrue(boolean expression) {
        isTrue(expression, "The expression must be true!");
    }
    
    /**
     * Requires that the given {@code object} satisfies the provided validation {@link Function}.
     * <p>
     * If the function returns {@code false}, an {@link IllegalArgumentException} is thrown with the specified message.
     * Otherwise, the original object is returned.
     * </p>
     *
     * @param object  - The object to validate.
     * @param fn      - The validation function to apply.
     * @param message - The exception message if validation fails.
     * @return The validated object.
     */
    @Nonnull
    public static <E> E requireValid(@Nonnull E object, @Nonnull Function<E, Boolean> fn, @Nonnull String message) {
        if (!fn.apply(object)) {
            throw new IllegalArgumentException(message);
        }
        
        return object;
    }
    
    /**
     * Returns one of the two given values depending on the evaluation of the given expression.
     * <p>
     * If the expression is {@code true}, the {@code ifTrue} value is returned;
     * otherwise, the {@code ifFalse} value is returned.
     * </p>
     *
     * @param expression - The condition to evaluate.
     * @param ifTrue     - The value to return if the expression is {@code true}.
     * @param ifFalse    - The value to return if the expression is {@code false}.
     * @return The value corresponding to the result of the expression.
     */
    public static <T> T valueIf(boolean expression, @Nonnull T ifTrue, @Nonnull T ifFalse) {
        return expression ? ifTrue : ifFalse;
    }
    
    /**
     * Gets whether the given {@link String} contains at least of the given {@link Object}.
     * <p>
     * Each object is converted into a string via {@link Object#toString()}.
     * </p>
     *
     * @param string  - The string to check.
     * @param objects - The objects to check.
     * @return {@code true} if the given string contains at least one of the given objects.
     */
    public static boolean stringContainsAny(@Nonnull String string, @Nonnull Object... objects) {
        if (objects.length == 0) {
            return false;
        }
        
        for (Object object : objects) {
            if (string.contains(String.valueOf(object))) {
                return true;
            }
        }
        
        return true;
    }
    
    /**
     * Gets whether the given {@link String} contains all the given {@link Object}.
     * <p>
     * Each object is converted into a string via {@link Object#toString()}.
     * </p>
     *
     * @param string  - The string to check.
     * @param objects - The objects to check.
     * @return {@code true} if the given string contains all the given objects.
     */
    public static boolean stringContainsAll(@Nonnull String string, @Nonnull Object... objects) {
        if (objects.length == 0) {
            return false;
        }
        
        for (Object object : objects) {
            if (!string.contains(String.valueOf(object))) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Checks whether the given array length matches the expected length.
     *
     * @param array  - The array to check.
     * @param length - The length to expect.
     */
    public static void arrayLength(@Nonnull Object[] array, int length) {
        if (array.length != length) {
            throw new IllegalArgumentException("Array length must be %s, not %s!".formatted(length, array.length));
        }
    }
    
    /**
     * Checks whether the given array length is within {@code min} and {@code max}.
     *
     * @param array - The array to check.
     * @param min   - The minimum length of the array (inclusive).
     * @param max   - The maximum length of the array (inclusive).
     */
    public static void arrayLength(@Nonnull Object[] array, int min, int max) {
        final int length = array.length;
        
        if (length < min) {
            throw new IllegalArgumentException("Array length must be higher or equals to %s!".formatted(min));
        }
        else if (length > max) {
            throw new IllegalArgumentException("Array length must be lower or equals to %s!".formatted(max));
        }
    }
    
    /**
     * Validates that the given array is non-null and has at least of element.
     *
     * @param varargs - The array to check.
     * @return {@code true} if the given array is non-null and has a least of element; {@code false} otherwise.
     */
    @Nonnull
    public static <T> T[] varargs(@Nullable T[] varargs) {
        return varargs(varargs, "There must be at least one argument!");
    }
    
    /**
     * Validates that the given array is non-null and has at least of element.
     *
     * @param varargs - The array to check.
     * @param message - The exception message.
     * @return {@code true} if the given array is non-null and has a least of element; {@code false} otherwise.
     */
    @Nonnull
    public static <T> T[] varargs(@Nullable T[] varargs, @Nonnull String message) {
        if (varargs == null || varargs.length == 0) {
            throw new IllegalArgumentException(message);
        }
        
        return varargs;
    }
    
    /**
     * Validates that the provided {@link Class} is not a primitive.
     * <p>To be more precise, it checks whether the class is java primitive, not the object of the primitive.</p>
     *
     * @param clazz - The class to check.
     * @return the same class if it's not a primitive.
     */
    @Nonnull
    public static <T> Class<T> notPrimitive(@Nonnull Class<T> clazz) {
        class Holder {
            private static final Map<Class<?>, Class<?>> primitiveToObject = Map.of(
                    boolean.class, Boolean.class,
                    byte.class, Byte.class,
                    short.class, Short.class,
                    int.class, Integer.class,
                    long.class, Long.class,
                    float.class, Float.class,
                    double.class, Double.class,
                    char.class, Character.class,
                    void.class, Void.class
            );
        }
        
        if (Holder.primitiveToObject.containsKey(clazz)) {
            throw new IllegalArgumentException("Provided class must not be primitive! (%s -> %s)".formatted(clazz.getSimpleName(), Holder.primitiveToObject.get(clazz).getSimpleName()));
        }
        
        return clazz;
    }
}
