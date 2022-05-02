package kz.hapyl.spigotutils.module.math.gometry;

import java.util.concurrent.atomic.AtomicLong;

public class LongLong extends AtomicLong {

	public static LongLong ZERO      = new LongLong();
	public static LongLong MIN_VALUE = new LongLong(Long.MIN_VALUE);
	public static LongLong MAX_VALUE = new LongLong(Long.MAX_VALUE);

	public LongLong(long initialValue) {
		super(initialValue);
	}

	public LongLong() {
		this(0);
	}

	public void increment() {
		super.incrementAndGet();
	}

	public void decrement() {
		super.decrementAndGet();
	}

}
