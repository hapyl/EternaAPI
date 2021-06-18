package kz.hapyl.spigotutils.module.math;

import java.util.concurrent.atomic.AtomicInteger;

public final class IntInt extends AtomicInteger implements NumberNumber {

	public IntInt(int initialValue) {
		super(initialValue);
	}

	public IntInt() {
		this(0);
	}

	@Override
	public IntInt zero() {
		return new IntInt();
	}

	@Override
	public IntInt minValue() {
		return new IntInt(Integer.MIN_VALUE);
	}

	@Override
	public IntInt maxValue() {
		return new IntInt(Integer.MAX_VALUE);
	}

	public final void increment() {
		super.incrementAndGet();
	}

	public final void decrement() {
		super.decrementAndGet();
	}

}
