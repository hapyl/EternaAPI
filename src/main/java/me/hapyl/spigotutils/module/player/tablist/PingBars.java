package me.hapyl.spigotutils.module.player.tablist;

import me.hapyl.spigotutils.module.util.ThreadRandom;

public enum PingBars {

    NO_PING(-1),
    FIVE(149),
    FOUR(299),
    THREE(599),
    TWO(999),
    ONE(1001);

    private final int value;

    PingBars(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static PingBars random() {
        return values()[ThreadRandom.nextInt(values().length)];
    }
}
