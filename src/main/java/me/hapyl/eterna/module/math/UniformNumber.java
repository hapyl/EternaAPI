package me.hapyl.eterna.module.math;

import me.hapyl.eterna.module.util.BukkitUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a uniformly distributed numeric value within a fixed range.
 *
 * <p>
 * Each call to a numeric accessor returns a value sampled uniformly between
 * the {@code min} and {@code max}.
 * </p>
 */
public class UniformNumber implements AsNumber {
    
    private final double min;
    private final double max;
    
    private UniformNumber(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("'min' (%s) cannot be greater than 'max' (%s)!".formatted(min, max));
        }
        
        this.min = min;
        this.max = max;
    }
    
    /**
     * Gets a randomly sampled value as an {@code int}.
     *
     * @return The sampled value cast to {@code int}.
     */
    @Override
    public int asInt() {
        return (int) getValue();
    }
    
    /**
     * Gets a randomly sampled value as a {@code long}.
     *
     * @return The sampled value cast to {@code long}.
     */
    @Override
    public long asLong() {
        return (long) getValue();
    }
    
    /**
     * Gets a randomly sampled value as a {@code float}.
     *
     * @return The sampled value cast to {@code float}.
     */
    @Override
    public float asFloat() {
        return (float) getValue();
    }
    
    @Override
    public double asDouble() {
        return getValue();
    }
    
    private double getValue() {
        return min == max ? min : BukkitUtils.RANDOM.nextDouble(min, max + 1);
    }
    
    /**
     * A static factory method for creating a {@link UniformNumber} from the given {@code double} bounds.
     *
     * @param min - The minimum bound.
     * @param max - The maximum bound.
     * @return A new {@link UniformNumber}.
     */
    @NotNull
    public static UniformNumber from(final double min, final double max) {
        return new UniformNumber(min, max);
    }
    
    /**
     * A static factory method for parsing a {@link UniformNumber} from a {@link String} representation.
     *
     * <p>
     * Ranges may be specified using the {@code min..max} syntax; If no range is present, the value is treated as a constant.
     * </p>
     *
     * @param string - The string to parse.
     * @return The parsed {@link UniformNumber}.
     */
    @NotNull
    public static UniformNumber parse(@NotNull String string) {
        double min;
        double max;
        
        // Parse range 1..5 results in a random number between 1 and 5 (inclusive)
        if (string.contains("..")) {
            final String[] range = string.split("\\.\\.");
            
            min = Numbers.toDouble(range[0]);
            max = Numbers.toDouble(range[1]);
        }
        // Else parse normally
        else {
            min = Numbers.toDouble(string);
            max = min;
        }
        
        return new UniformNumber(min, max);
    }
}
