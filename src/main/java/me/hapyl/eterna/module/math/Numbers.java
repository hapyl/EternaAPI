package me.hapyl.eterna.module.math;

import me.hapyl.eterna.module.annotate.UtilityClass;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * A utility class to convert numbers.
 */
@UtilityClass
public final class Numbers {
    
    /**
     * Defines the epsilon for decimal checks.
     */
    public static final float EPSILON = 1e-6f;
    
    private Numbers() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Clamps the given {@link Integer} between {@code 0} and {@code 1} (inclusive).
     *
     * @param value - The value to clamp.
     * @return the clamped value.
     */
    public static int clamp01(int value) {
        return Math.clamp(value, 0, 1);
    }
    
    /**
     * Clamps the given {@link Long} between {@code 0} and {@code 1} (inclusive).
     *
     * @param value - The value to clamp.
     * @return the clamped value.
     */
    public static long clamp01(long value) {
        return Math.clamp(value, 0, 1);
    }
    
    /**
     * Clamps the given {@link Float} between {@code 0} and {@code 1} (inclusive).
     *
     * @param value - The value to clamp.
     * @return the clamped value.
     */
    public static float clamp01(float value) {
        return Math.clamp(value, 0, 1);
    }
    
    /**
     * Clamps the given {@link Double} between {@code 0} and {@code 1} (inclusive).
     *
     * @param value - The value to clamp.
     * @return the clamped value.
     */
    public static double clamp01(double value) {
        return Math.clamp(value, 0, 1);
    }
    
    /**
     * Clamps the given {@link Byte} between {@code 0} and {@code 1} (inclusive).
     *
     * @param value - The value to clamp.
     * @return the clamped value.
     */
    public static byte clamp01(byte value) {
        return (byte) Math.clamp(value, (byte) 0, (byte) 1);
    }
    
    /**
     * Clamps the given {@link Short} between {@code 0} and {@code 1} (inclusive).
     *
     * @param value - The value to clamp.
     * @return the clamped value.
     */
    public static short clamp01(short value) {
        return (short) Math.clamp(value, (short) 0, (short) 1);
    }
    
    /**
     * Clamps the given {@link Integer} between {@code -1} and {@code 1} (inclusive).
     *
     * @param value - The value to clamp.
     * @return the clamped value.
     */
    public static int clamp1neg1(int value) {
        return Math.clamp(value, -1, 1);
    }
    
    /**
     * Clamps the given {@link Long} between {@code -1} and {@code 1} (inclusive).
     *
     * @param value - The value to clamp.
     * @return the clamped value.
     */
    public static long clamp1neg1(long value) {
        return Math.clamp(value, -1, 1);
    }
    
    /**
     * Clamps the given {@link Float} between {@code -1} and {@code 1} (inclusive).
     *
     * @param value - The value to clamp.
     * @return the clamped value.
     */
    public static float clamp1neg1(float value) {
        return Math.clamp(value, -1, 1);
    }
    
    /**
     * Clamps the given {@link Double} between {@code -1} and {@code 1} (inclusive).
     *
     * @param value - The value to clamp.
     * @return the clamped value.
     */
    public static double clamp1neg1(double value) {
        return Math.clamp(value, -1, 1);
    }
    
    /**
     * Clamps the given {@link Byte} between {@code -1} and {@code 1} (inclusive).
     *
     * @param value - The value to clamp.
     * @return the clamped value.
     */
    public static byte clamp1neg1(byte value) {
        return (byte) Math.clamp(value, (byte) -1, (byte) 1);
    }
    
    /**
     * Clamps the given {@link Short} between {@code -1} and {@code 1} (inclusive).
     *
     * @param value - The value to clamp.
     * @return the clamped value.
     */
    public static short clamp1neg1(short value) {
        return (short) Math.clamp(value, (short) -1, (short) 1);
    }
    
    /**
     * Gets whether the given {@link Object} is an {@link Integer}.
     *
     * @return {@code true} if the given object is an {@code integer}; {@code false} otherwise.
     */
    public static boolean isInt(@Nullable Object object) {
        return isNumber0(object, Integer.class, Integer::parseInt);
    }
    
    /**
     * Gets whether the given {@link Object} is a {@link Long}.
     *
     * @return {@code true} if the given object is a {@code long}; {@code false} otherwise.
     */
    public static boolean isLong(@Nullable Object object) {
        return isNumber0(object, Long.class, Long::parseLong);
    }
    
    /**
     * Gets whether the given {@link Object} is a {@link Float}.
     *
     * @return {@code true} if the given object is a {@code float}; {@code false} otherwise.
     */
    public static boolean isFloat(@Nullable Object object) {
        return isNumber0(object, Float.class, Float::parseFloat);
    }
    
    /**
     * Gets whether the given {@link Object} is a {@link Double}.
     *
     * @return {@code true} if the given object is a {@code double}; {@code false} otherwise.
     */
    public static boolean isDouble(@Nullable Object object) {
        return isNumber0(object, Double.class, Double::parseDouble);
    }
    
    /**
     * Gets whether the given {@link Object} is a {@link Byte}.
     *
     * @return {@code true} if the given object is a {@code byte}; {@code false} otherwise.
     */
    public static boolean isByte(@Nullable Object object) {
        return isNumber0(object, Byte.class, Byte::parseByte);
    }
    
    /**
     * Gets whether the given {@link Object} is a {@link Short}.
     *
     * @return {@code true} if the given object is a {@code short}; {@code false} otherwise.
     */
    public static boolean isShort(@Nullable Object object) {
        return isNumber0(object, Short.class, Short::parseShort);
    }
    
    /**
     * Converts the given {@link Object} to an {@link Integer}.
     *
     * @param object       - The object to convert.
     * @param defaultValue - The default value, if the object cannot be converted.
     * @return the converted value, or {@code defaultValue} if the object cannot be converted.
     */
    public static int toInt(@Nullable Object object, int defaultValue) {
        return toNumber0(object, Integer.class, Integer::parseInt, defaultValue);
    }
    
    /**
     * Converts the given {@link Object} to an {@link Integer}.
     *
     * @param object - The object to convert.
     * @return the converted value, or {@code 0} if the object cannot be converted.
     */
    public static int toInt(@Nullable Object object) {
        return toInt(object, 0);
    }
    
    /**
     * Converts the given {@link Object} to a {@link Long}.
     *
     * @param object       - The object to convert.
     * @param defaultValue - The default value, if the object cannot be converted.
     * @return the converted value, or {@code defaultValue} if the object cannot be converted.
     */
    public static long toLong(@Nullable Object object, long defaultValue) {
        return toNumber0(object, Long.class, Long::parseLong, defaultValue);
    }
    
    /**
     * Converts the given {@link Object} to a {@link Long}.
     *
     * @param object - The object to convert.
     * @return the converted value, or {@code 0L} if the object cannot be converted.
     */
    public static long toLong(@Nullable Object object) {
        return toLong(object, 0L);
    }
    
    /**
     * Converts the given {@link Object} to a {@link Float}.
     *
     * @param object       - The object to convert.
     * @param defaultValue - The default value, if the object cannot be converted.
     * @return the converted value, or {@code defaultValue} if the object cannot be converted.
     */
    public static float toFloat(@Nullable Object object, float defaultValue) {
        return toNumber0(object, Float.class, Float::parseFloat, defaultValue);
    }
    
    /**
     * Converts the given {@link Object} to a {@link Float}.
     *
     * @param object - The object to convert.
     * @return the converted value, or {@code 0.0f} if the object cannot be converted.
     */
    public static float toFloat(@Nullable Object object) {
        return toFloat(object, 0.0f);
    }
    
    /**
     * Converts the given {@link Object} to a {@link Double}.
     *
     * @param object       - The object to convert.
     * @param defaultValue - The default value, if the object cannot be converted.
     * @return the converted value, or {@code defaultValue} if the object cannot be converted.
     */
    public static double toDouble(@Nullable Object object, double defaultValue) {
        return toNumber0(object, Double.class, Double::parseDouble, defaultValue);
    }
    
    /**
     * Converts the given {@link Object} to a {@link Double}.
     *
     * @param object - The object to convert.
     * @return the converted value, or {@code 0.0d} if the object cannot be converted.
     */
    public static double toDouble(@Nullable Object object) {
        return toDouble(object, 0.0d);
    }
    
    /**
     * Converts the given {@link Object} to a {@link Byte}.
     *
     * @param object       - The object to convert.
     * @param defaultValue - The default value, if the object cannot be converted.
     * @return the converted value, or {@code defaultValue} if the object cannot be converted.
     */
    public static byte toByte(@Nullable Object object, byte defaultValue) {
        return toNumber0(object, Byte.class, Byte::parseByte, defaultValue);
    }
    
    /**
     * Converts the given {@link Object} to a {@link Byte}.
     *
     * @param object - The object to convert.
     * @return the converted value, or {@code (byte) 0x0} if the object cannot be converted.
     */
    public static byte toByte(@Nullable Object object) {
        return toByte(object, (byte) 0x0);
    }
    
    /**
     * Converts the given {@link Object} to a {@link Short}.
     *
     * @param object       - The object to convert.
     * @param defaultValue - The default value, if the object cannot be converted.
     * @return the converted value, or {@code defaultValue} if the object cannot be converted.
     */
    public static short toShort(@Nullable Object object, short defaultValue) {
        return toNumber0(object, Short.class, Short::parseShort, defaultValue);
    }
    
    /**
     * Converts the given {@link Object} to a {@link Short}.
     *
     * @param object - The object to convert.
     * @return the converted value, or {@code (short) 0} if the object cannot be converted.
     */
    public static short toShort(@Nullable Object object) {
        return toShort(object, (short) 0);
    }
    
    /**
     * Checks if two {@code double} values are approximately equal, within a small epsilon tolerance.
     *
     * @param a - The first value.
     * @param b - The second value.
     * @return {@code true} if the values are equal, {@code false} otherwise.
     */
    public static boolean doubleEquals(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }
    
    /**
     * Checks if two {@code float} values are approximately equal, within a small epsilon tolerance.
     *
     * @param a - The first value
     * @param b - The second value.
     * @return {@code true} if the values are equal, {@code false} otherwise.
     */
    public static boolean floatEquals(float a, float b) {
        return Math.abs(a - b) < EPSILON;
    }
    
    /**
     * Squares the given {@code int}.
     *
     * @param value - The value to square.
     * @return a squared {@code int}.
     */
    public static int square(int value) {
        return value * value;
    }
    
    /**
     * Squares the given {@code long}.
     *
     * @param value - The value to square.
     * @return a squared {@code long}.
     */
    public static long square(long value) {
        return value * value;
    }
    
    /**
     * Squares the given {@code float}.
     *
     * @param value - The value to square.
     * @return a squared {@code float}.
     */
    public static float square(float value) {
        return value * value;
    }
    
    /**
     * Squares the given {@code double}.
     *
     * @param value - The value to square.
     * @return a squared {@code double}.
     */
    public static double square(double value) {
        return value * value;
    }
    
    @NotNull
    @ApiStatus.Internal
    private static <N extends Number> N toNumber0(@Nullable Object object, @NotNull Class<N> numberClass, @NotNull Function<String, N> parseFunction, @NotNull N defaultValue) {
        if (object == null) {
            return defaultValue;
        }
        else if (numberClass.isInstance(object)) {
            return numberClass.cast(object);
        }
        else {
            try {
                return parseFunction.apply(object.toString());
            }
            catch (NumberFormatException ex) {
                return defaultValue;
            }
        }
    }
    
    @ApiStatus.Internal
    private static <N extends Number> boolean isNumber0(@Nullable Object object, @NotNull Class<N> numberClass, @NotNull Function<String, N> parseFunction) {
        if (object == null) {
            return false;
        }
        else if (numberClass.isInstance(object)) {
            return true;
        }
        else {
            try {
                parseFunction.apply(object.toString());
                return true;
            }
            catch (NumberFormatException ex) {
                return false;
            }
        }
    }
}
