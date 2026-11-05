package me.hapyl.eterna.module.math;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public final class TickImpl implements Tick {
    
    static final double RATIO_TICKS_TO_SECONDS = 20;
    static final double RATIO_TICKS_TO_MINUTES = 1200;
    static final double RATIO_TICKS_TO_HOURS = 72000;
    
    static final Tick ZERO = new TickImpl(0);
    static final Tick ONE = new TickImpl(1);
    
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
    
    @Override
    public boolean isZero() {
        return ticks == 0;
    }
    
    @Override
    public boolean isOne() {
        return ticks == 1;
    }
    
    @Override
    public boolean isPositive() {
        return ticks > 0;
    }
    
    @Override
    public boolean isNegative() {
        return ticks < 0;
    }
    
    @Override
    public int compareTo(@NotNull Tick that) {
        return Integer.compare(this.ticks, that.ticks());
    }
    
    @Override
    public String toString() {
        return "Tick{" + ticks + "}";
    }
    
}