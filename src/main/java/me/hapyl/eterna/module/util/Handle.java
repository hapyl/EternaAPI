package me.hapyl.eterna.module.util;

import javax.annotation.Nonnull;

public interface Handle<T> {

    @Nonnull
    T getHandle();

}
