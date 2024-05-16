package me.hapyl.spigotutils.module.reflect.field;

import javax.annotation.Nonnull;

@SuppressWarnings("all")
public class EmptyFieldAccess<T> extends FieldAccess<T> {
    public EmptyFieldAccess() {
        super(null, null, null);
    }

    @Deprecated
    @Nonnull
    @Override
    public T read(int index) {
        return null;
    }

    @Deprecated
    @Override
    public void write(int index, T value) {
    }

    @Override
    public final String toString() {
        return "EmptyFieldAccess[]";
    }
}
