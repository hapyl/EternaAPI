package me.hapyl.eterna.module.util;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/**
 * Represents an immutable duration holder of a given {@link TimeUnit}
 */
public class Duration {
    
    private final long value;
    private final TimeUnit timeUnit;
    
    /**
     * Creates a new {@link Duration}.
     *
     * @param value    - The duration value.
     * @param timeUnit - The time unit.
     */
    public Duration(long value, @NotNull TimeUnit timeUnit) {
        this.value = value;
        this.timeUnit = timeUnit;
    }
    
    /**
     * Gets the raw duration value.
     *
     * @return the duration value.
     */
    public long getValue() {
        return value;
    }
    
    /**
     * Gets the value in {@link TimeUnit#NANOSECONDS}.
     *
     * @return the value in nanoseconds.
     */
    public long asNanos() {
        return timeUnit.toNanos(value);
    }
    
    /**
     * Gets the value in {@link TimeUnit#MILLISECONDS}.
     *
     * @return the value in milliseconds.
     */
    public long asMillis() {
        return timeUnit.toMillis(value);
    }
    
    /**
     * Gets the value in {@link TimeUnit#SECONDS}.
     *
     * @return the value in seconds.
     */
    public long asSeconds() {
        return timeUnit.toSeconds(value);
    }
    
    /**
     * Gets the value in {@link TimeUnit#MINUTES}.
     *
     * @return the value in minutes.
     */
    public long asMinutes() {
        return timeUnit.toMinutes(value);
    }
    
    /**
     * Gets the value in {@link TimeUnit#HOURS}.
     *
     * @return the value in hours.
     */
    public long asHours() {
        return timeUnit.toHours(value);
    }
    
    /**
     * Gets the value in {@link TimeUnit#DAYS}.
     *
     * @return the value in days.
     */
    public long asDays() {
        return timeUnit.toDays(value);
    }
}
