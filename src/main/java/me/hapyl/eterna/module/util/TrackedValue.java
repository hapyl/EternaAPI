package me.hapyl.eterna.module.util;

import javax.annotation.Nonnull;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Used to track a value... I guess?
 */
public abstract class TrackedValue<T, C> {
    
    protected Supplier<T> supplier;
    protected T value;
    
    public TrackedValue(@Nonnull Supplier<T> supplier) {
        this.supplier = supplier;
        this.value = supplier.get();
    }
    
    public boolean hasChanged() {
        final T value = supplier.get();
        
        return !this.value.equals(value);
    }
    
    @Nonnull
    public C get() {
        return convert(value = supplier.get());
    }
    
    @Nonnull
    public abstract C convert(@Nonnull T t);
    
    @Nonnull
    public static <T, C> TrackedValue<T, C> of(@Nonnull Supplier<T> supplier, @Nonnull Function<T, C> get) {
        return new TrackedValue<>(supplier) {
            @Nonnull
            @Override
            public C convert(@Nonnull T t) {
                return get.apply(t);
            }
        };
    }
    
    @Nonnull
    public static <T> TrackedValue<T, T> of(@Nonnull Supplier<T> supplier) {
        return new TrackedValue<>(supplier) {
            @Nonnull
            @Override
            public T convert(@Nonnull T t) {
                return t;
            }
        };
    }
    
}
