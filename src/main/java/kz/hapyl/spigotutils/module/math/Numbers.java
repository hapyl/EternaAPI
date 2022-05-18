package kz.hapyl.spigotutils.module.math;

import kz.hapyl.spigotutils.module.util.Validate;

/**
 * Simple util class for numbers.
 */
public class Numbers {

    public static int clamp(int var, int min, int max) {
        return Math.min(Math.max(var, min), max);
    }

    public static float clamp(float var, float min, float max) {
        return Math.min(Math.max(var, min), max);
    }

    public static double clamp(double var, double min, double max) {
        return Math.min(Math.max(var, min), max);
    }

    public static long clamp(long var, long min, long max) {
        return Math.min(Math.max(var, min), max);
    }

    public static byte clamp(byte var, byte min, byte max) {
        return (byte) Math.min(Math.max(var, min), max);
    }

    public static boolean isInt(Object obj) {
        return Validate.isInt(obj);
    }

    public static boolean isLong(Object obj) {
        return Validate.isLong(obj);
    }

    public static boolean isDouble(Object obj) {
        return Validate.isDouble(obj);
    }

    public static boolean isFloat(Object obj) {
        return Validate.isFloat(obj);
    }

}
