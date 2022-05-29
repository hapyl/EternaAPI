package kz.hapyl.spigotutils.module.util;

public class Nulls {

    public static <E> void runIfNotNull(E obj, Action<E> action) {
        if (obj != null) {
            action.use(obj);
        }
    }

    public static <E> void runIfNull(E e, Runnable action) {
        if (e == null) {
            action.run();
        }
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

}
