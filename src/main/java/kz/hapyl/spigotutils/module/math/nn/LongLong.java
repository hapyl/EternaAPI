package kz.hapyl.spigotutils.module.math.nn;

import java.util.concurrent.atomic.AtomicLong;

public class LongLong extends AtomicLong implements NumberNumber<Long> {
    public LongLong(Long initialValue) {
        super(initialValue);
    }

    public LongLong() {
        this(0L);
    }

    @Override
    public Long zero() {
        return 0L;
    }

    @Override
    public Long one() {
        return 1L;
    }

    @Override
    public Long minValue() {
        return Long.MIN_VALUE;
    }

    @Override
    public Long maxValue() {
        return Long.MAX_VALUE;
    }

    @Override
    public void increment(Long l) {
        set(get() + l);
    }

    @Override
    public void decrement(Long l) {
        set(get() - l);
    }

}
