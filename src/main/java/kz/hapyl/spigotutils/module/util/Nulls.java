package kz.hapyl.spigotutils.module.util;

public class Nulls {

	public static <E> void runIfNotNull(E obj, Action<E> action) {
		if (obj != null) {
			action.use(obj);
		}
	}

}
