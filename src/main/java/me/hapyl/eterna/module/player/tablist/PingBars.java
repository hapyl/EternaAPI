package me.hapyl.eterna.module.player.tablist;

import org.jetbrains.annotations.NotNull;

/**
 * Represents the number of ping bars on a {@link TablistEntry} texture.
 */
public enum PingBars {
    
    /**
     * Defines the "No Ping" or "X" ping.
     */
    NO_PING(-1),
    
    /**
     * Defines the five bars of ping.
     */
    FIVE(149),
    
    /**
     * Defines the four bars of ping.
     */
    FOUR(299),
    
    /**
     * Defines the three bars of ping.
     */
    THREE(599),
    
    /**
     * Defines the two bars of ping.
     */
    TWO(999),
    
    /**
     * Defines the one bar of ping.
     */
    ONE(1001);
    
    private final int magicValue;
    
    PingBars(int magicValue) {
        this.magicValue = magicValue;
    }
    
    /**
     * Gets the magic value used for the ping bar.
     *
     * @return the magic value.
     */
    public int getMagicValue() {
        return magicValue;
    }
    
    /**
     * Gets a {@link PingBars} by the given ping value.
     *
     * @param ping - The ping value.
     * @return the ping bars associated with the given ping value, or {@link #NO_PING} if the given value is negative.
     */
    @NotNull
    public static PingBars byPing(final int ping) {
        if (ping < 0) {
            return NO_PING;
        }
        
        for (final PingBars pingBars : values()) {
            if (ping <= pingBars.magicValue) {
                return pingBars;
            }
        }
        
        return ONE;
    }
    
}
