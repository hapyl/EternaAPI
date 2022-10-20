package me.hapyl.spigotutils.module.util;

import javax.annotation.Nullable;

public class Nulls {

    public static <E> void runIfNotNull(@Nullable E obj, Action<E> action) {
        if (obj != null) {
            action.use(obj);
        }
    }

    public static <E> void runIfNull(@Nullable E e, Runnable action) {
        if (e == null) {
            action.run();
        }
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

}
