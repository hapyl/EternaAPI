package me.hapyl.spigotutils.module.util;

import me.hapyl.spigotutils.module.math.Numbers;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Easy access to ThreadLocalRandom.
 */
public class ThreadRandom {

    public static int nextInt() {
        return ThreadLocalRandom.current().nextInt();
    }

    public static int nextInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }

    public static int nextInt(int origin, int bound) {
        return ThreadLocalRandom.current().nextInt(origin, bound);
    }

    public static float nextFloat() {
        return ThreadLocalRandom.current().nextFloat();
    }

    public static float nextFloat(float bound) {
        return nextFloat(0, bound);
    }

    public static float nextFloat(float origin, float bound) {
        return (float) Numbers.clamp(nextDouble(origin, bound), Float.MIN_VALUE, Float.MAX_VALUE);
    }

    public static double nextDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    public static double nextDouble(double bound) {
        return ThreadLocalRandom.current().nextDouble(bound);
    }

    public static double nextDouble(double origin, double bound) {
        return ThreadLocalRandom.current().nextDouble(origin, bound);
    }

    public static long nextLong() {
        return ThreadLocalRandom.current().nextLong();
    }

    public static long nextLong(long bound) {
        return ThreadLocalRandom.current().nextLong(bound);
    }

    public static long nextLong(long origin, long bound) {
        return ThreadLocalRandom.current().nextLong(origin, bound);
    }

    public static double nextGaussian() {
        return ThreadLocalRandom.current().nextGaussian();
    }

    public static boolean nextBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    public static boolean nextDoubleAndCheckBetween(float origin, float bound) {
        final double random = Math.random();
        return random >= origin && random < bound;
    }

    public static boolean nextFloatAndCheckBetween(float origin, float bound) {
        Validate.isTrue(
                origin >= 0.0f && bound <= 1.0f,
                String.format("original must be >= 0.0f and bound must be <= 1.0f! (%s, %s)", origin, bound)
        );
        final float v = nextFloat();
        return v >= origin && v < bound;
    }

}
