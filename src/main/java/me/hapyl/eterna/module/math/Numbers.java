package me.hapyl.eterna.module.math;

import me.hapyl.eterna.module.annotate.UtilityClass;

import javax.annotation.Nullable;

/**
 * Simple util class for numbers.
 */
@UtilityClass
public final class Numbers {
    
    private static final float EPSILON = 1e-6f;
    
    private Numbers() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Clamps a number between a min and max value.
     *
     * @param var - variable to clamp.
     * @param min - minimum value.
     * @param max - maximum value.
     * @return clamped value.
     * @deprecated {@link Math#clamp(long, int, int)}
     */
    @Deprecated
    public static int clamp(int var, int min, int max) {
        return Math.min(Math.max(var, min), max);
    }
    
    /**
     * Clamps a number between a min and max value.
     *
     * @param var - variable to clamp.
     * @param min - minimum value.
     * @param max - maximum value.
     * @return clamped value.
     * @deprecated {@link Math#clamp(long, long, long)}
     */
    @Deprecated
    public static long clamp(long var, long min, long max) {
        return Math.min(Math.max(var, min), max);
    }
    
    /**
     * Clamps a number between a min and max value.
     *
     * @param var - variable to clamp.
     * @param min - minimum value.
     * @param max - maximum value.
     * @return clamped value.
     * @deprecated {@link Math#clamp(float, float, float)}
     */
    @Deprecated
    public static float clamp(float var, float min, float max) {
        return Math.min(Math.max(var, min), max);
    }
    
    /**
     * Clamps a number between a min and max value.
     *
     * @param var - variable to clamp.
     * @param min - minimum value.
     * @param max - maximum value.
     * @return clamped value.
     * @deprecated {@link Math#clamp(double, double, double)}
     */
    @Deprecated
    public static double clamp(double var, double min, double max) {
        return Math.min(Math.max(var, min), max);
    }
    
    /**
     * Clamps a number between a min and max value.
     *
     * @param var - variable to clamp.
     * @param min - minimum value.
     * @param max - maximum value.
     * @return clamped value.
     * @deprecated {@link Math#clamp(long, int, int)}
     */
    @Deprecated
    public static byte clamp(byte var, byte min, byte max) {
        return (byte) Math.min(Math.max(var, min), max);
    }
    
    /**
     * Clamps a number between a min and max value.
     *
     * @param var - variable to clamp.
     * @param min - minimum value.
     * @param max - maximum value.
     * @deprecated {@link Math#clamp(long, int, int)}
     */
    @Deprecated
    public static short clamp(short var, short min, short max) {
        return (short) Math.min(Math.max(var, min), max);
    }
    
    /**
     * Clamps a number between a 0 and 1.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static int clamp01(int var) {
        return Math.clamp(var, 0, 1);
    }
    
    /**
     * Clamps a number between a 0 and 1.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static long clamp01(long var) {
        return Math.clamp(var, 0, 1);
    }
    
    /**
     * Clamps a number between a 0 and 1.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static float clamp01(float var) {
        return Math.clamp(var, 0, 1);
    }
    
    /**
     * Clamps a number between a 0 and 1.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static double clamp01(double var) {
        return Math.clamp(var, 0, 1);
    }
    
    /**
     * Clamps a number between a 0 and 1.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static byte clamp01(byte var) {
        return (byte) Math.clamp(var, (byte) 0, (byte) 1);
    }
    
    /**
     * Clamps a number between a 0 and 1.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static short clamp01(short var) {
        return (short) Math.clamp(var, (short) 0, (short) 1);
    }
    
    /**
     * Clamps a number between a -1 and 1.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static int clamp1neg1(int var) {
        return Math.clamp(var, -1, 1);
    }
    
    /**
     * Clamps a number between a -1 and 1.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static long clamp1neg1(long var) {
        return Math.clamp(var, -1, 1);
    }
    
    /**
     * Clamps a number between a -1 and 1.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static float clamp1neg1(float var) {
        return Math.clamp(var, -1, 1);
    }
    
    /**
     * Clamps a number between a -1 and 1.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static double clamp1neg1(double var) {
        return Math.clamp(var, -1, 1);
    }
    
    /**
     * Clamps a number between a -1 and 1.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static byte clamp1neg1(byte var) {
        return (byte) Math.clamp(var, (byte) -1, (byte) 1);
    }
    
    /**
     * Clamps a number between a -1 and 1.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static short clamp1neg1(short var) {
        return (short) Math.clamp(var, (short) -1, (short) 1);
    }
    
    /**
     * Clamps a number between min and max value of an Integer.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static int clampInt(int var) {
        return Math.clamp(var, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    
    /**
     * Clamps a number between min and max value of a Long.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static long clampLong(long var) {
        return Math.clamp(var, Long.MIN_VALUE, Long.MAX_VALUE);
    }
    
    /**
     * Clamps a number between min and max value of a Float.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static float clampFloat(float var) {
        return Math.clamp(var, Float.MIN_VALUE, Float.MAX_VALUE);
    }
    
    /**
     * Clamps a number between min and max value of a Double.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static double clampDouble(double var) {
        return Math.clamp(var, Double.MIN_VALUE, Double.MAX_VALUE);
    }
    
    /**
     * Clamps a number between min and max value of a Byte.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static byte clampByte(byte var) {
        return (byte) Math.clamp(var, Byte.MIN_VALUE, Byte.MAX_VALUE);
    }
    
    /**
     * Clamps a number between min and max value of a Short.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static short clampShort(short var) {
        return (short) Math.clamp(var, Short.MIN_VALUE, Short.MAX_VALUE);
    }
    
    /**
     * Returns {@code true} if the given {@link Object} is an {@link Integer}, {@code false} otherwise.
     *
     * @param object - Object to check.
     * @return {@code true} if the given {@link Object} is an {@link Integer}, {@code false} otherwise.
     */
    public static boolean isInt(@Nullable Object object) {
        if (object == null) {
            return false;
        }
        else if (object instanceof Integer) {
            return true;
        }
        else {
            try {
                Integer.parseInt(object.toString());
            }
            catch (Exception ignored) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns {@code true} if the given {@link Object} is a {@link Long}, {@code false} otherwise.
     *
     * @param object - Object to check.
     * @return {@code true} if the given {@link Object} is a {@link Long}, {@code false} otherwise.
     */
    public static boolean isLong(@Nullable Object object) {
        if (object == null) {
            return false;
        }
        else if (object instanceof Long) {
            return true;
        }
        else {
            try {
                Long.parseLong(object.toString());
            }
            catch (Exception ignored) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns {@code true} if the given {@link Object} is a {@link Float}, {@code false} otherwise.
     *
     * @param object - Object to check.
     * @return {@code true} if the given {@link Object} is a {@link Float}, {@code false} otherwise.
     */
    public static boolean isFloat(@Nullable Object object) {
        if (object == null) {
            return false;
        }
        else if (object instanceof Float) {
            return true;
        }
        else {
            try {
                Float.parseFloat(object.toString());
            }
            catch (Exception ignored) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns {@code true} if the given {@link Object} is a {@link Double}, {@code false} otherwise.
     *
     * @param object - Object to check.
     * @return {@code true} if the given {@link Object} is a {@link Double}, {@code false} otherwise.
     */
    public static boolean isDouble(@Nullable Object object) {
        if (object == null) {
            return false;
        }
        else if (object instanceof Double) {
            return true;
        }
        else {
            try {
                Double.parseDouble(object.toString());
            }
            catch (Exception ignored) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns {@code true} if the given {@link Object} is a {@link Byte}, {@code false} otherwise.
     *
     * @param object - Object to check.
     * @return {@code true} if the given {@link Object} is a {@link Byte}, {@code false} otherwise.
     */
    public static boolean isByte(@Nullable Object object) {
        if (object == null) {
            return false;
        }
        else if (object instanceof Byte) {
            return true;
        }
        else {
            try {
                Byte.parseByte(object.toString());
            }
            catch (Exception ignored) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns {@code true} if the given {@link Object} is a {@link Short}, {@code false} otherwise.
     *
     * @param object - Object to check.
     * @return {@code true} if the given {@link Object} is a {@link Short}, {@code false} otherwise.
     */
    public static boolean isShort(@Nullable Object object) {
        if (object == null) {
            return false;
        }
        else if (object instanceof Short) {
            return true;
        }
        else {
            try {
                Short.parseShort(object.toString());
            }
            catch (Exception ignored) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Attempts to convert the given {@link Object} to an {@link Integer}.
     * If the object is {@code null}, or if it cannot be
     * converted to an {@link Integer}, returns the specified default value.
     *
     * @param object - The {@link Object} to convert to an integer.
     * @param def    - The default value to return if the object is {@code null} or cannot be converted.
     * @return the {@link Integer} value of the object, or the default value if conversion is not possible.
     */
    public static int getInt(@Nullable Object object, int def) {
        if (object == null) {
            return def;
        }
        
        else if (object instanceof Number number) {
            return number.intValue();
        }
        else {
            try {
                return Integer.parseInt(object.toString());
            }
            catch (NumberFormatException ignored0) {
                return def;
            }
        }
    }
    
    /**
     * Attempts to convert the given {@link Object} to an {@link Integer}.
     * If the object is {@code null}, or if it cannot be
     * converted to an {@link Integer}, returns {@code 0}.
     *
     * @param object - The {@link Object} to convert to an integer.
     * @return the {@link Integer} value of the object, or {@code 0} if conversion is not possible.
     */
    public static int getInt(@Nullable Object object) {
        return getInt(object, 0);
    }
    
    /**
     * Attempts to convert the given {@link Object} to a {@link Long}.
     * If the object is {@code null}, or if it cannot be
     * converted to an {@link Long}, returns the specified default value.
     *
     * @param object - The {@link Object} to convert to an integer.
     * @param def    - The default value to return if the object is {@code null} or cannot be converted.
     * @return the {@link Long} value of the object, or the default value if conversion is not possible.
     */
    public static long getLong(@Nullable Object object, long def) {
        if (object == null) {
            return def;
        }
        else if (object instanceof Number number) {
            return number.longValue();
        }
        else {
            try {
                return Long.parseLong(object.toString());
            }
            catch (NumberFormatException ignored0) {
                return def;
            }
        }
    }
    
    /**
     * Attempts to convert the given {@link Object} to a {@link Long}.
     * If the object is {@code null}, or if it cannot be
     * converted to an {@link Long}, returns {@code 0}.
     *
     * @param object - The {@link Object} to convert to an integer.
     * @return the {@link Long} value of the object, or {@code 0} if conversion is not possible.
     */
    public static long getLong(@Nullable Object object) {
        return getLong(object, 0L);
    }
    
    /**
     * Attempts to convert the given {@link Object} to a {@link Float}.
     * If the object is {@code null}, or if it cannot be
     * converted to an {@link Float}, returns the specified default value.
     *
     * @param object - The {@link Object} to convert to an integer.
     * @param def    - The default value to return if the object is {@code null} or cannot be converted.
     * @return the {@link Float} value of the object, or the default value if conversion is not possible.
     */
    public static float getFloat(@Nullable Object object, float def) {
        if (object == null) {
            return def;
        }
        else if (object instanceof Number number) {
            return number.floatValue();
        }
        else {
            try {
                return Float.parseFloat(object.toString());
            }
            catch (NumberFormatException ignored0) {
                return def;
            }
        }
    }
    
    /**
     * Attempts to convert the given {@link Object} to a {@link Float}.
     * If the object is {@code null}, or if it cannot be
     * converted to an {@link Float}, returns {@code 0.0f}.
     *
     * @param object - The {@link Object} to convert to an integer.
     * @return the {@link Float} value of the object, or {@code 0.0f} if conversion is not possible.
     */
    public static float getFloat(@Nullable Object object) {
        return getFloat(object, 0.0f);
    }
    
    /**
     * Attempts to convert the given {@link Object} to a {@link Float}.
     * If the object is {@code null}, or if it cannot be
     * converted to an {@link Float}, returns the specified default value.
     *
     * @param object - The {@link Object} to convert to an integer.
     * @param def    - The default value to return if the object is {@code null} or cannot be converted.
     * @return the {@link Float} value of the object, or the default value if conversion is not possible.
     */
    public static double getDouble(@Nullable Object object, double def) {
        if (object == null) {
            return def;
        }
        else if (object instanceof Number number) {
            return number.doubleValue();
        }
        else {
            try {
                return Double.parseDouble(object.toString());
            }
            catch (NumberFormatException ignored0) {
                return def;
            }
        }
    }
    
    /**
     * Attempts to convert the given {@link Object} to a {@link Double}.
     * If the object is {@code null}, or if it cannot be
     * converted to an {@link Double}, returns {@code 0.0d}.
     *
     * @param object - The {@link Object} to convert to an integer.
     * @return the {@link Double} value of the object, or {@code 0.0d} if conversion is not possible.
     */
    public static double getDouble(@Nullable Object object) {
        return getDouble(object, 0.0d);
    }
    
    /**
     * Attempts to convert the given {@link Object} to a {@link Byte}.
     * If the object is {@code null}, or if it cannot be
     * converted to an {@link Byte}, returns the specified default value.
     *
     * @param object - The {@link Object} to convert to an integer.
     * @param def    - The default value to return if the object is {@code null} or cannot be converted.
     * @return the {@link Byte} value of the object, or the default value if conversion is not possible.
     */
    public static byte getByte(@Nullable Object object, byte def) {
        if (object == null) {
            return def;
        }
        else if (object instanceof Number number) {
            return number.byteValue();
        }
        else {
            try {
                return Byte.parseByte(object.toString());
            }
            catch (NumberFormatException ignored0) {
                return def;
            }
        }
    }
    
    /**
     * Attempts to convert the given {@link Object} to a {@link Byte}.
     * If the object is {@code null}, or if it cannot be
     * converted to an {@link Byte}, returns {@code (byte)0x0}.
     *
     * @param object - The {@link Object} to convert to an integer.
     * @return the {@link Byte} value of the object, or {@code (byte)0x0} if conversion is not possible.
     */
    public static byte getByte(@Nullable Object object) {
        return getByte(object, (byte) 0x0);
    }
    
    /**
     * Attempts to convert the given {@link Object} to a {@link Short}.
     * If the object is {@code null}, or if it cannot be
     * converted to an {@link Short}, returns the specified default value.
     *
     * @param object - The {@link Object} to convert to an integer.
     * @param def    - The default value to return if the object is {@code null} or cannot be converted.
     * @return the {@link Short} value of the object, or the default value if conversion is not possible.
     */
    public static short getShort(@Nullable Object object, short def) {
        if (object == null) {
            return def;
        }
        if (object instanceof Number number) {
            return number.shortValue();
        }
        else {
            try {
                return Short.parseShort(object.toString());
            }
            catch (NumberFormatException ignored0) {
                return def;
            }
        }
    }
    
    /**
     * Attempts to convert the given {@link Object} to a {@link Short}.
     * If the object is {@code null}, or if it cannot be
     * converted to an {@link Short}, returns {@code (short)0}.
     *
     * @param object - The {@link Object} to convert to an integer.
     * @return the {@link Short} value of the object, or {@code (short)0} if conversion is not possible.
     */
    public static short getShort(@Nullable Object object) {
        return getShort(object, (short) 0);
    }
    
    /**
     * Gets whether the two {@code double} are equals.
     *
     * @param a - The first double.
     * @param b - The second double.
     * @return {@code true} if the two {@code double} are equals, {@code false} otherwise.
     */
    public static boolean doubleEquals(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }
    
    /**
     * Gets whether the two {@code float} are equals.
     *
     * @param a - The first float.
     * @param b - The second float.
     * @return {@code true} if the two {@code float} are equals, {@code false} otherwise.
     */
    public static boolean floatEquals(float a, float b) {
        return Math.abs(a - b) < EPSILON;
    }
}
