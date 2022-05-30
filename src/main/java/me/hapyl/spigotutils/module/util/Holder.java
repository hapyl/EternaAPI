package me.hapyl.spigotutils.module.util;

public class Holder<E> {

	private final E e;

	public Holder(E e) {
		this.e = e;
	}

	public E get() {
		return e;
	}
}
