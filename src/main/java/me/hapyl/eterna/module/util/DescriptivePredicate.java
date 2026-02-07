package me.hapyl.eterna.module.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

/**
 * Represents a {@link Predicate} with a support for {@link #reason()} to explain why the {@link DescriptivePredicate#test(Object)} failed.
 *
 * @param <T> - The predicate type.
 */
public interface DescriptivePredicate<T> extends Predicate<T> {
    
    /**
     * Gets the reason why the {@link DescriptivePredicate#test(Object)} failed.
     *
     * @return the reason why the test has failed.
     */
    @NotNull
    String reason();
    
    /**
     * A static factory method for creating a {@link DescriptivePredicate}.
     *
     * @param predicate - The predicate.
     * @param reason    - The fail reason.
     * @param <T>       - The predicate type.
     * @return a new descriptive predicate.
     */
    @NotNull
    static <T> DescriptivePredicate<T> of(@NotNull Predicate<T> predicate, @NotNull String reason) {
        return new DescriptivePredicate<>() {
            @NotNull
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
    
    /**
     * A static factory method for creating a {@link DescriptivePredicate} from the given predicate.
     *
     * @param predicate - The predicate.
     * @param <T>       - The predicate type.
     * @return a new descriptive predicate.
     */
    @Nullable
    static <T> DescriptivePredicate<T> copyOfNullable(@Nullable DescriptivePredicate<T> predicate) {
        return predicate != null ? new DescriptivePredicate<>() {
            @NotNull
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
