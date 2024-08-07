package me.hapyl.eterna.module.math.nn;

import com.google.common.util.concurrent.AtomicDouble;

public class DoubleDouble extends AtomicDouble implements NumberNumber<Double> {

    public DoubleDouble(Double initialValue) {
        super(initialValue);
    }

    public DoubleDouble() {
        this(0.0d);
    }

    @Override
    public Double zero() {
        return 0.0d;
    }

    @Override
    public Double one() {
        return 1.0d;
    }

    @Override
    public Double minValue() {
        return Double.MIN_VALUE;
    }

    @Override
    public Double maxValue() {
        return Double.MAX_VALUE;
    }

    @Override
    public void increment(Double d) {
        set(get() + d);
    }

    @Override
    public void decrement(Double d) {
        set(get() - d);
    }


}
