package me.hapyl.eterna.module.player.tablist;

import javax.annotation.Nonnull;

public enum PingBars {

    /**
     * No ping, the icon will be crossed out with an 'X'.
     */
    NO_PING(-1),
    /**
     * Five bars of ping.
     */
    FIVE(149),
    /**
     * Four bars of ping.
     */
    FOUR(299),
    /**
     * Three bars of ping.
     */
    THREE(599),
    /**
     * Two bars of ping.
     */
    TWO(999),
    /**
     * One bar of ping.
     */
    ONE(1001);

    private final int value;

    PingBars(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    /**
     * Gets a {@link PingBars} by an integer value.
     *
     * @param value - Integer value.
     * @return a ping bar.
     */
    @Nonnull
    public static PingBars byValue(int value) {
        if (value < 0) {
            return NO_PING;
        }

        for (final PingBars ping : values()) {
            if (value <= ping.value) {
                return ping;
            }
        }

        return ONE;
    }

}
