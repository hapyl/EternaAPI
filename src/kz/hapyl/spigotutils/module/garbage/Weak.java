package kz.hapyl.spigotutils.module.garbage;

import kz.hapyl.spigotutils.module.annotate.NULLABLE;

public class Weak<E> {

	@NULLABLE
	private E element;

	public Weak(E init) {
		this.element = init;
	}

	public Weak() {
		this(null);
	}

	public void set(E newValue) {
		this.element = newValue;
	}

	public E setGetOld(E newValue) {
		E old = this.element;
		this.set(newValue);
		return old;
	}

	public E getAndNullate() {
		E old = this.element;
		this.element = null;
		return old;
	}

	public E get() {
		return this.element;
	}

	public E getOr(E or) {
		return this.isNull() ? or : this.get();
	}

	public E getNotNull() {
		if (this.isNull()) {
			throw new IllegalArgumentException("Weak object must not be null!");
		}
		return this.get();
	}

	public boolean isNull() {
		return this.element == null;
	}

	@Override
	public String toString() {
		return this.element.toString();
	}
}

