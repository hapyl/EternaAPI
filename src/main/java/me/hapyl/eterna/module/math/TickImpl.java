package me.hapyl.eterna.module.math;

public final class TickImpl implements Tick {
    
    static final double RATIO_TICKS_TO_SECONDS = 20;
    static final double RATIO_TICKS_TO_MINUTES = 1200;
    static final double RATIO_TICKS_TO_HOURS = 72000;
    
    private final int ticks;
    
    TickImpl(int ticks) {
        this.ticks = ticks;
    }
    
    @Override
    public int ticks() {
        return ticks;
    }
    
    @Override
    public float seconds() {
        return (float) (ticks / RATIO_TICKS_TO_SECONDS);
    }
    
    @Override
    public float minutes() {
        return (float) (ticks / RATIO_TICKS_TO_MINUTES);
    }
    
    @Override
    public float hours() {
        return (float) (ticks / RATIO_TICKS_TO_HOURS);
    }
    
}