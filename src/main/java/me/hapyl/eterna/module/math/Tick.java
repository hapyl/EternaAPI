package me.hapyl.eterna.module.math;

import me.hapyl.eterna.module.annotate.UtilityClass;
import org.jetbrains.annotations.NotNull;

/**
 * A helper utility class for converting minecraft ticks.
 */
@UtilityClass
public final class Tick {
    
    /**
     * Defines an infinity character used for negative ticks.
     */
    public static final String INFINITY_CHAR = "∞";
    
    private Tick() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Converts the given {@code tick} to seconds and formats it to one decimal place with an <code>s</code> suffix.
     *
     * <pre>{@code
     * Tick.format(20); // 1.0s
     * Tick.format(40); // 2.0s
     * Tick.format(15); // 0.7s
     * Tick.format(69); // 3.4s
     * Tick.format(-1); // ∞
     * }</pre>
     *
     * @param tick – The number of ticks to format.
     * @return the formatted time in seconds.
     */
    @NotNull
    public static String format(int tick) {
        return tick < 0 ? INFINITY_CHAR : "%.1fs".formatted((double) tick / 20);
    }
    
    /**
     * Converts the given {@code tick} to seconds and formats it with either no decimals (if whole) or one decimal.
     *
     * <pre>{@code
     * Tick.round(20); // 1s
     * Tick.round(40); // 2s
     * Tick.round(15); // 0.8s
     * Tick.round(69); // 3.4s
     * Tick.round(-1); // ∞
     * }</pre>
     *
     * @param tick – The number of ticks to round.
     * @return the rounded time in seconds.
     */
    @NotNull
    public static String round(int tick) {
        if (tick < 0) {
            return INFINITY_CHAR;
        }
        
        final double decimal = (double) tick / 20;
        
        return tick % 20 == 0 ? "%.0fs".formatted(decimal) : "%.1fs".formatted(decimal);
    }
    
    /**
     * Converts hours into ticks.
     *
     * @param hour – The number of hours.
     * @return the equivalent duration in ticks.
     */
    public static int fromHours(float hour) {
        return (int) (hour * 20 * 60 * 60);
    }
    
    /**
     * Converts minutes into ticks.
     *
     * @param minutes – The number of minutes.
     * @return the equivalent duration in ticks.
     */
    public static int fromMinutes(float minutes) {
        return (int) (minutes * 20 * 60);
    }
    
    /**
     * Converts seconds into ticks.
     *
     * @param second – The number of seconds.
     * @return the equivalent duration in ticks.
     */
    public static int fromSeconds(float second) {
        return Math.round(second * 20f);
    }
    
    /**
     * Converts milliseconds into ticks.
     *
     * @param millis – The number of milliseconds.
     * @return the equivalent duration in ticks.
     */
    public static int fromMillis(float millis) {
        return Math.round(millis / 50f);
    }
    
    /**
     * Converts ticks into hours.
     *
     * @param tick – The number of ticks.
     * @return the equivalent duration in hours.
     */
    public static float toHours(int tick) {
        return tick / 20f / 60f / 60f;
    }
    
    /**
     * Converts ticks into minutes.
     *
     * @param tick – The number of ticks.
     * @return the equivalent duration in minutes.
     */
    public static float toMinutes(int tick) {
        return tick / 20f / 60f;
    }
    
    /**
     * Converts ticks into seconds.
     *
     * @param tick – The number of ticks.
     * @return the equivalent duration in seconds.
     */
    public static float toSeconds(int tick) {
        return tick / 20f;
    }
    
    /**
     * Converts ticks into milliseconds.
     *
     * @param ticks – The number of ticks.
     * @return the equivalent duration in milliseconds.
     */
    public static float toMillis(int ticks) {
        return ticks * 50f;
    }
    
}
