package kz.hapyl.spigotutils.module.math;

import com.google.common.util.concurrent.AtomicDouble;

public final class DoubleDouble extends AtomicDouble implements NumberNumber {

	public DoubleDouble(double initialValue) {
		super(initialValue);
	}

	public DoubleDouble() {
		this(0.0d);
	}

	@Override
	public DoubleDouble zero() {
		return new DoubleDouble();
	}

	@Override
	public DoubleDouble minValue() {
		return new DoubleDouble(Double.MIN_VALUE);
	}

	@Override
	public DoubleDouble maxValue() {
		return new DoubleDouble(Double.MAX_VALUE);
	}

	public final void increment() {
		super.addAndGet(1);
	}

	public final void decrement() {
		super.addAndGet(1);
	}

}
