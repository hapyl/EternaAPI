package kz.hapyl.spigotutils.module.util;

import java.util.concurrent.ThreadLocalRandom;

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

	public static double nextDouble() {
		return ThreadLocalRandom.current().nextDouble();
	}

	public static double nextDouble(int bound) {
		return ThreadLocalRandom.current().nextDouble(bound);
	}

	public static double nextDouble(int origin, int bound) {
		return ThreadLocalRandom.current().nextDouble(origin, bound);
	}

	public static long nextLong() {
		return ThreadLocalRandom.current().nextLong();
	}

	public static long nextLong(int bound) {
		return ThreadLocalRandom.current().nextLong(bound);
	}

	public static long nextLong(int origin, int bound) {
		return ThreadLocalRandom.current().nextLong(origin, bound);
	}

	public static double nextGaussian() {
		return ThreadLocalRandom.current().nextGaussian();
	}

	public static boolean nextBoolean() {
		return ThreadLocalRandom.current().nextBoolean();
	}

	public static boolean nextFloatAndCheckBetween(float origin, float bound) {
		Validate.isTrue(origin >= 0.0f && bound <= 1.0f,
				String.format("original must be >= 0.0f and bound must be <= 1.0f! (%s, %s)", origin, bound));
		final float v = nextFloat();
		return v >= origin && v < bound;
	}

}
