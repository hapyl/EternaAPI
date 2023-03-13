package me.hapyl.spigotutils.module.util;

import javax.annotation.Nullable;

/**
 * Indicates that the class contains a value of some type.
 *
 * @param <E> - The type of value.
 */
public class Holder<E> {

    private final E e;

    public Holder(E e) {
        this.e = e;
    }

    /**
     * Returns the value.
     *
     * @return the value.
     */
    @Nullable
    public E get() {
        return e;
    }
}
