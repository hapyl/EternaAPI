package me.hapyl.spigotutils.module.math;

import me.hapyl.spigotutils.module.util.BukkitUtils;
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
    public static byte clamp(byte var, byte min, byte max) {
        return (byte) Math.min(Math.max(var, min), max);
    }

    /**
     * Returns if object is integer.
     *
     * @param obj - object to check.
     * @return true if object is integer.
     */
    public static boolean isInt(Object obj) {
        return Validate.isInt(obj);
    }

    /**
     * Returns if object is long.
     *
     * @param obj - object to check.
     * @return true if object is long.
     */
    public static boolean isLong(Object obj) {
        return Validate.isLong(obj);
    }

    /**
     * Returns if object is double.
     *
     * @param obj - object to check.
     * @return true if object is double.
     */
    public static boolean isDouble(Object obj) {
        return Validate.isDouble(obj);
    }

    /**
     * Returns if object is float.
     *
     * @param obj - object to check.
     * @return true if object is float.
     */
    public static boolean isFloat(Object obj) {
        return Validate.isFloat(obj);
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
     * Returns the float from object or default value.
     *
     * @param obj - object to get float from.
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
     * Ticks manipulations.
     */
    public static class Tick {

        /**
         * Round tick to its string form. {@see BukkitUtils#roundTick(int)}
         *
         * @param tick - tick to round.
         * @return rounded tick.
         */
        public static String round(int tick) {
            return BukkitUtils.roundTick(tick);
        }

        /**
         * Convert tick to hour.
         *
         * @param tick - tick to convert.
         * @return hour.
         */
        public static long toHour(int tick) {
            return tick / 20 / 60 / 60;
        }

        /**
         * Convert hour to tick.
         *
         * @param hour - hour to convert.
         * @return tick.
         */
        public static int fromHour(long hour) {
            return (int) (hour * 20 * 60 * 60);
        }

        /**
         * Convert tick to minute.
         *
         * @param tick - tick to convert.
         * @return minute.
         */
        public static long toMinute(int tick) {
            return tick / 20 / 60;
        }

        /**
         * Convert minute to tick.
         *
         * @param minute - minute to convert.
         * @return tick.
         */
        public static int fromMinute(long minute) {
            return (int) (minute * 20 * 60);
        }

        /**
         * Convert tick to second.
         *
         * @param tick - tick to convert.
         * @return second.
         */
        public static long toSecond(long tick) {
            return tick / 20;
        }

        /**
         * Convert second to tick.
         *
         * @param second - second to convert.
         * @return tick.
         */
        public static int fromSecond(long second) {
            return (int) (second * 20);
        }

        /**
         * Convert tick to millisecond.
         *
         * @param tick - tick to convert.
         * @return millisecond.
         */
        public static long toMillis(long tick) {
            return tick * 50;
        }

        /**
         * Convert millisecond to tick.
         *
         * @param millis - millisecond to convert.
         * @return tick.
         */
        public static int fromMillis(long millis) {
            return (int) (millis / 50);
        }

    }

}
