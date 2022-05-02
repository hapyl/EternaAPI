package kz.hapyl.spigotutils.module.util;

@FunctionalInterface
public interface Action<T> {
	void use(T t);

	interface AB<A, B> {
		void use(A a, B b);
	}

	interface ABC<A, B, C> {
		void use(A a, B b, C c);
	}

}
