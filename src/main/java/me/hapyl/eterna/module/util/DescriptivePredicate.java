package me.hapyl.eterna.module.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

/**
 * Represents a {@link Predicate} with a support for {@link #reason()} why the {@link DescriptivePredicate#test(Object)} failed.
 *
 * @param <T> - The predicate type.
 */
public interface DescriptivePredicate<T> extends Predicate<T> {
    
    /**
     * The reason why {@link DescriptivePredicate#test(Object)} failed.
     *
     * @return reason why {@link DescriptivePredicate#test(Object)} failed.
     */
    @Nonnull
    String reason();
    
    @Nonnull
    static <T> DescriptivePredicate<T> of(@Nonnull Predicate<T> predicate, @Nonnull String reason) {
        return new DescriptivePredicate<>() {
            @Nonnull
            @Override
            public String reason() {
                return reason;
            }
            
            @Override
            public boolean test(T t) {
                return predicate.test(t);
            }
        };
    }
    
    @Nullable
    static <T> DescriptivePredicate<T> clone(@Nullable DescriptivePredicate<T> predicate) {
        return predicate != null ? new DescriptivePredicate<T>() {
            @Nonnull
            @Override
            public String reason() {
                return predicate.reason();
            }
            
            @Override
            public boolean test(T t) {
                return predicate.test(t);
            }
        } : null;
    }
    
}
