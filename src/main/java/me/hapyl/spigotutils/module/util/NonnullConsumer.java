package me.hapyl.spigotutils.module.util;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public interface NonnullConsumer<T> extends Consumer<T> {

    @Nonnull
    static <A> NonnullConsumer<A> empty() {
        return t -> {
        };
    }

}
