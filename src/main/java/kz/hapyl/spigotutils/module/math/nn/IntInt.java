package kz.hapyl.spigotutils.module.math.nn;

import java.util.concurrent.atomic.AtomicInteger;

public class IntInt extends AtomicInteger implements NumberNumber<Integer> {

    public IntInt(Integer initialValue) {
        super(initialValue);
    }

    public IntInt() {
        this(0);
    }

    @Override
    public Integer zero() {
        return 0;
    }

    @Override
    public Integer one() {
        return 1;
    }

    @Override
    public Integer minValue() {
        return Integer.MIN_VALUE;
    }

    @Override
    public Integer maxValue() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void increment(Integer integer) {
        set(get() + integer);
    }

    @Override
    public void decrement(Integer integer) {
        set(get() - integer);
    }

}
