package me.hapyl.spigotutils.module.util;

import javax.annotation.Nonnull;

/**
 * Indicates that the class contains a value of some type.
 *
 * @param <E> - The type of value.
 */
public class Holder<E> {

    private final E e;

    public Holder(E e) {
        if (e == null) {
            throw new NullPointerException("Holder does not permit null values!");
        }
        this.e = e;
    }

    /**
     * Returns the value.
     *
     * @return the value.
     */
    @Nonnull
    public E get() {
        return e;
    }
}
