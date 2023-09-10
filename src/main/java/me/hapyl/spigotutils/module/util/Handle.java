package me.hapyl.spigotutils.module.util;

import javax.annotation.Nonnull;

public interface Handle<T> {

    @Nonnull
    T getHandle();

}
