package me.hapyl.spigotutils.module.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Helpful null check class.
 */
public class Nulls {

    /**
     * Runs the action if the object is NOT null.
     *
     * @param obj    - Object to check.
     * @param action - Action to run.
     * @param <E>    - Type of object.
     */
    public static <E> void runIfNotNull(@Nullable E obj, Action<E> action) {
        if (obj != null) {
            action.use(obj);
        }
    }

    /**
     * Runs the action if the object IS null.
     *
     * @param e      - Object to check.
     * @param action - Action to run.
     * @param <E>    - Type of object.
     */
    public static <E> void runIfNull(@Nullable E e, Runnable action) {
        if (e == null) {
            action.run();
        }
    }

    /**
     * Returns true if the object is null, else otherwise.
     * <i>Don't ask, I don't know why...</i>
     *
     * @param obj - Object to check.
     * @return - True if object is null, else otherwise.
     */
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    /**
     * Returns true if any of the objects are null, false otherwise.
     *
     * @param obj - Objects to check.
     * @return - True if any of the objects are null, false otherwise.
     */
    public static boolean anyNull(Object... obj) {
        if (obj == null || obj.length == 0) {
            return true;
        }

        for (Object o : obj) {
            if (o == null) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns true if all the objects are null, false otherwise.
     *
     * @param obj - Objects to check.
     * @return - True if all the objects are null, false otherwise.
     */
    public static boolean allNull(Object... obj) {
        if (obj == null || obj.length == 0) {
            return true;
        }

        for (Object o : obj) {
            if (o != null) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns true if all the objects are not null, false otherwise.
     *
     * @param obj - Objects to check.
     * @return - True if all the objects are not null, false otherwise.
     */
    public static boolean nonNull(Object... obj) {
        return !anyNull(obj);
    }

    /**
     * Counts amount of null objects.
     *
     * @param obj - Objects to check.
     * @return - Amount of null objects.
     */
    public static int countNulls(Object... obj) {
        int count = 0;
        for (Object o : obj) {
            if (o == null) {
                count++;
            }
        }
        return count;
    }

    /**
     * Counts amount of non-null objects.
     *
     * @param obj - Objects to check.
     * @return - Amount of non-null objects.
     */
    public static int countNonNulls(Object... obj) {
        int count = 0;
        for (Object o : obj) {
            if (o != null) {
                count++;
            }
        }
        return count;
    }

    /**
     * Gets the given object is it is <b>non null</b>, <code>def</code> otherwise.
     *
     * @param t   - Object.
     * @param def - Default.
     * @return the given object or default.
     */
    @Nonnull
    public static <T> T getOrDefault(@Nullable T t, @Nonnull T def) {
        return t != null ? t : def;
    }
}
