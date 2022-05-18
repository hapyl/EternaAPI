package kz.hapyl.spigotutils.module.math.nn;

import com.google.common.util.concurrent.AtomicDouble;

public class FloatFloat extends AtomicDouble implements NumberNumber<Float> {

    @Override
    public Float zero() {
        return 0.0f;
    }

    @Override
    public Float one() {
        return 1.0f;
    }

    @Override
    public Float minValue() {
        return Float.MIN_VALUE;
    }

    @Override
    public Float maxValue() {
        return Float.MAX_VALUE;
    }

    @Override
    public void increment(Float f) {
        set(get() + f);
    }

    @Override
    public void decrement(Float f) {
        set(get() - f);
    }
}
