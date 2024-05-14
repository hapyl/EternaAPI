package me.hapyl.spigotutils.module.reflect.wrapper;

import javax.annotation.Nonnull;

public class Wrapper<T> {

    protected final T obj;

    public Wrapper(T obj) {
        this.obj = obj;
    }

    @Nonnull
    public T getWrappedObject() {
        return obj;
    }

}
