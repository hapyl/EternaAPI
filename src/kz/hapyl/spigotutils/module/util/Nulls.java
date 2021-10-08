package kz.hapyl.spigotutils.module.util;

public class Nulls {

	public static void runIfNotNull(Object obj, Action<Object> action) {
		if (obj != null) {
			action.use(obj);
		}
	}

}
