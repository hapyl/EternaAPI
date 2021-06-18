package kz.hapyl.spigotutils.module.util;

public final class Exceptions {

	/**
	 * Throws RuntimeException with provided message.
	 *
	 * @param message - Message of the exception.
	 */
	public static void throwAbstractError(String message) {
		throw new RuntimeException(message);
	}

	/**
	 * Runs runnable safe of any exceptions, if occurred ignores it.
	 *
	 * @param runnable - To run
	 */
	public static void runSafe(Runnable runnable) {
		Exceptions.runSafe(runnable);
	}

	/**
	 * Runs runnable safe of any exceptions.
	 *
	 * @param runnable - To run
	 * @return true if not exceptions were occurred
	 */
	public static boolean runSafeBool(Runnable runnable) {
		try {
			runnable.run();
			return true;
		}
		catch (Exception ignored0) {
			return false;
		}
	}

	/**
	 * Runs runnable and catches any exceptions, if caught runs another runnable unsafe
	 *
	 * @param runnable - To run
	 * @param ifCaught - Run if caught any exceptions
	 */
	public static void catchAny(Runnable runnable, Runnable ifCaught) {
		if (!Exceptions.runSafeBool(runnable)) {
			ifCaught.run();
		}
	}

	/**
	 * Runs runnable and catches provided exceptions, if caught does nothing.
	 *
	 * @param runnable - To run
	 * @param toCaught - To catch
	 */
	public static void catchExceptions(Runnable runnable, Exception... toCaught) {
		Exceptions.catchExceptions(runnable, null, toCaught);
	}

	/**
	 * Runs runnable and catches provided exceptions, if caught run ifCaught provided runnable.
	 *
	 * @param runnable - To run
	 * @param ifCaught - Runs if caught any of provided exceptions
	 * @param toCaught - To catch
	 */
	public static void catchExceptions(Runnable runnable, Runnable ifCaught, Exception... toCaught) {
		try {
			runnable.run();
		}
		catch (Exception e) {
			if (toCaught.length == 0 || ifCaught == null) {
				return;
			}
			for (final Exception exception : toCaught) {
				if (e == exception) {
					ifCaught.run();
					return;
				}
			}
		}
	}

}

