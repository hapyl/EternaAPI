package me.hapyl.eterna.module.util;

/**
 * Represents an object that can {@link #tick(int)} with a dedicated {@code tick} value.
 */
public interface IndexedTicking {
    
    /**
     * The tick method.
     *
     * @param tick - The total number of ticks.
     */
    void tick(int tick);
    
}
