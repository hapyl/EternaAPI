package me.hapyl.eterna.module.math;

import me.hapyl.eterna.module.util.BukkitUtils;

/**
 * Ticks manipulations.
 */
public final class Tick {

    /**
     * Rounds tick to its string form. {@see BukkitUtils#roundTick(int)}
     *
     * @param tick - tick to round.
     * @return rounded tick.
     */
    public static String round(int tick) {
        return BukkitUtils.roundTick(tick);
    }

    /**
     * Rounds tick to its string form. {@see BukkitUtils#roundTick(int, String)}
     *
     * @param tick   - tick to round.
     * @param suffix - suffix to add.
     * @return rounded tick.
     */
    public static String round(int tick, String suffix) {
        return BukkitUtils.roundTick(tick, suffix);
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