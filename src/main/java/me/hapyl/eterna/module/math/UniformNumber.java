package me.hapyl.eterna.module.math;

import me.hapyl.eterna.module.util.BukkitUtils;
import org.bukkit.util.NumberConversions;

import javax.annotation.Nonnull;

/**
 * Represents a number that can either be a fixed value or a random number
 * within a specified range.
 */
public class UniformNumber {

    private final double min;
    private final double max;

    private UniformNumber(double min, double max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Returns the value as an <code>integer</code>.
     * <p>If the range is fixed, the value will always be the fixed number.
     * Otherwise, a random <code>integer</code> within the range is returned.</p>
     *
     * @return the value as an <code>integer</code>.
     */
    public int asInt() {
        return (int) getValue();
    }

    /**
     * Returns the value as a <code>long</code>.
     * <p>If the range is fixed, the value will always be the fixed number.
     * Otherwise, a random <code>long</code> within the range is returned.</p>
     *
     * @return the value as a <code>long</code>.
     */
    public long asLong() {
        return (long) getValue();
    }

    /**
     * Returns the value as a <code>double</code>.
     * <p>If the range is fixed, the value will always be the fixed number.
     * Otherwise, a random <code>double</code> within the range is returned.</p>
     *
     * @return the value as a <code>double</code>.
     */
    public double asDouble() {
        return getValue();
    }

    /**
     * Returns the value as a <code>float</code>.
     * <p>If the range is fixed, the value will always be the fixed number.
     * Otherwise, a random <code>float</code> within the range is returned.</p>
     *
     * @return the value as a <code>float</code>.
     */
    public float asFloat() {
        return (float) getValue();
    }

    private double getValue() {
        if (min == max) {
            return min;
        }

        return BukkitUtils.RANDOM.nextDouble(min, max + 1);
    }

    /**
     * Parses a string to create a {@link UniformNumber}.
     * <p>The string can represent:
     * <ul>
     *     <li>A fixed value, e.g., "5" (min and max are the same).</li>
     *     <li>A range, e.g., "1..5" (min is 1 and max is 5).</li>
     * </ul>
     * <p>Throws {@link IllegalArgumentException} if the parsed minimum
     * value is greater than the maximum value.</p>
     *
     * @param string - the string to parse.
     * @return a {@link UniformNumber} representing the parsed value or range.
     */
    @Nonnull
    public static UniformNumber parse(@Nonnull String string) {
        double min;
        double max;

        // Parse range 1..5 results in a random number between 1 and 5 (inclusive)
        if (string.contains("..")) {
            final String[] range = string.split("\\.\\.");

            min = NumberConversions.toDouble(range[0]);
            max = NumberConversions.toDouble(range[1]);
        }
        // Else parse normally
        else {
            min = NumberConversions.toDouble(string);
            max = min;
        }

        if (min > max) {
            throw new IllegalArgumentException("'min' (%s) cannot be greater than 'max' (%s)!".formatted(min, max));
        }

        return new UniformNumber(min, max);
    }
}
