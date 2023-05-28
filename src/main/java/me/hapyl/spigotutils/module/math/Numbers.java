package me.hapyl.spigotutils.module.math;

import me.hapyl.spigotutils.module.util.Validate;

/**
 * Simple util class for numbers.
 */
public class Numbers {

    /**
     * Clamps a number between a min and max value.
     *
     * @param var - variable to clamp.
     * @param min - minimum value.
     * @param max - maximum value.
     * @return clamped value.
     */
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
     */
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
     */
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
     */
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
     */
    public static byte clamp(byte var, byte min, byte max) {
        return (byte) Math.min(Math.max(var, min), max);
    }

    /**
     * Clamps a number between a min and max value.
     *
     * @param var - variable to clamp.
     * @param min - minimum value.
     * @param max - maximum value.
     * @return clamped value.
     */
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
        return Numbers.clamp(var, 0, 1);
    }

    /**
     * Clamps a number between a 0 and 1.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static long clamp01(long var) {
        return Numbers.clamp(var, 0, 1);
    }

    /**
     * Clamps a number between a 0 and 1.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static float clamp01(float var) {
        return Numbers.clamp(var, 0, 1);
    }

    /**
     * Clamps a number between a 0 and 1.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static double clamp01(double var) {
        return Numbers.clamp(var, 0, 1);
    }

    /**
     * Clamps a number between a 0 and 1.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static byte clamp01(byte var) {
        return Numbers.clamp(var, (byte) 0, (byte) 1);
    }

    /**
     * Clamps a number between a 0 and 1.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static short clamp01(short var) {
        return Numbers.clamp(var, (short) 0, (short) 1);
    }

    /**
     * Clamps a number between a -1 and 1.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static int clamp1neg1(int var) {
        return Numbers.clamp(var, -1, 1);
    }

    /**
     * Clamps a number between a -1 and 1.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static long clamp1neg1(long var) {
        return Numbers.clamp(var, -1, 1);
    }

    /**
     * Clamps a number between a -1 and 1.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static float clamp1neg1(float var) {
        return Numbers.clamp(var, -1, 1);
    }

    /**
     * Clamps a number between a -1 and 1.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static double clamp1neg1(double var) {
        return Numbers.clamp(var, -1, 1);
    }

    /**
     * Clamps a number between a -1 and 1.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static byte clamp1neg1(byte var) {
        return Numbers.clamp(var, (byte) -1, (byte) 1);
    }

    /**
     * Clamps a number between a -1 and 1.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static short clamp1neg1(short var) {
        return Numbers.clamp(var, (short) -1, (short) 1);
    }

    /**
     * Clamps a number between min and max value of an Integer.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static int clampInt(int var) {
        return clamp(var, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Clamps a number between min and max value of a Long.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static long clampLong(long var) {
        return clamp(var, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    /**
     * Clamps a number between min and max value of a Float.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static float clampFloat(float var) {
        return clamp(var, Float.MIN_VALUE, Float.MAX_VALUE);
    }

    /**
     * Clamps a number between min and max value of a Double.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static double clampDouble(double var) {
        return clamp(var, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    /**
     * Clamps a number between min and max value of a Byte.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static byte clampByte(byte var) {
        return clamp(var, Byte.MIN_VALUE, Byte.MAX_VALUE);
    }

    /**
     * Clamps a number between min and max value of a Short.
     *
     * @param var - variable to clamp.
     * @return clamped value.
     */
    public static short clampShort(short var) {
        return clamp(var, Short.MIN_VALUE, Short.MAX_VALUE);
    }

    /**
     * Returns if object is integer.
     *
     * @param obj - object to check.
     * @return true if an object is integer.
     */
    public static boolean isInt(Object obj) {
        return Validate.isInt(obj);
    }

    /**
     * Returns if an object is long.
     *
     * @param obj - object to check.
     * @return true if an object is long.
     */
    public static boolean isLong(Object obj) {
        return Validate.isLong(obj);
    }

    /**
     * Returns if an object is float.
     *
     * @param obj - object to check.
     * @return true if an object is float.
     */
    public static boolean isFloat(Object obj) {
        return Validate.isFloat(obj);
    }

    /**
     * Returns if an object is double.
     *
     * @param obj - object to check.
     * @return true if an object is double.
     */
    public static boolean isDouble(Object obj) {
        return Validate.isDouble(obj);
    }

    /**
     * Returns true if the object is a byte; false otherwise.
     *
     * @param obj - Object to check.
     * @return true if the object is a byte; false otherwise.
     */
    public static boolean isByte(Object obj) {
        return Validate.isByte(obj);
    }

    /**
     * Returns true if the object is a short; false otherwise.
     *
     * @param obj - Object to check.
     * @return true if the object is short, false otherwise.
     */
    public static boolean isShort(Object obj) {
        return Validate.isShort(obj);
    }

    /**
     * Returns the integer from object or default value.
     *
     * @param obj - object to get integer from.
     * @param def - default value.
     * @return integer or default value.
     */
    public static int getInt(Object obj, int def) {
        return isInt(obj) ? Validate.getInt(obj) : def;
    }

    /**
     * Returns the long from object or default value.
     *
     * @param obj - object to get long from.
     * @param def - default value.
     * @return long or default value.
     */
    public static long getLong(Object obj, long def) {
        return isLong(obj) ? Validate.getLong(obj) : def;
    }

    /**
     * Returns the float from object or default value.
     *
     * @param obj - object to parse from.
     * @param def - default value.
     * @return float or default value.
     */
    public static float getFloat(Object obj, float def) {
        return isFloat(obj) ? Validate.getFloat(obj) : def;
    }

    /**
     * Returns the double from object or default value.
     *
     * @param obj - object to get double from.
     * @param def - default value.
     * @return double or default value.
     */
    public static double getDouble(Object obj, double def) {
        return isDouble(obj) ? Validate.getDouble(obj) : def;
    }

    /**
     * Gets the byte value from the object.
     *
     * @param obj - Object to get the value from.
     * @param def - Default value if failed.
     * @return the byte value of the object or def.
     */
    public static byte getByte(Object obj, byte def) {
        return isByte(obj) ? Validate.getByte(obj) : def;
    }

    /**
     * Gets the short value from the object.
     *
     * @param obj - Object to get the value from.
     * @param def - Default value if failed.
     * @return the short value of the object or def.
     */
    public static short getShort(Object obj, short def) {
        return isShort(obj) ? Validate.getShort(obj) : def;
    }

}
