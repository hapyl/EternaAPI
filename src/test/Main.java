package test;

import kz.hapyl.spigotutils.module.garbage.Weak;

public class Main {

	public static void main(String[] args) {
		final Weak<Integer> weak = new Weak<>(1);

		final Integer integer = weak.getAndNullate();

		System.out.println("old_ " + integer);
		System.out.println("before_ " + weak.get());
	}

}
