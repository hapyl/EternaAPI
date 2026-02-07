package me.hapyl.eterna.module.util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Represents a {@link StrictReference} of a <b>non-null</b> object, that may be referred to <b>once</b>.
 *
 * @param <R> - The referent type.
 */
public final class StrictReference<R> {
    
    private R referent;
    
    StrictReference(@NotNull R referent) {
        this.referent = Objects.requireNonNull(referent, "Cannot create reference to a null!");
    }
    
    /**
     * Gets the referent object and {@code nullates} it.
     *
     * @return the referent.
     * @throws IllegalStateException for duplicate refer calls.
     */
    @NotNull
    public R refer() {
        if (this.referent == null) {
            throw new IllegalStateException("Illegal refer() call! The object has already been referenced.");
        }
        
        final R reference = this.referent;
        this.referent = null;
        
        return reference;
    }
    
    /**
     * Gets whether the referent exists.
     *
     * @return {@code true} if the referent exists; {@code false} otherwise.
     */
    public boolean hasReferent() {
        return this.referent != null;
    }
    
    /**
     * A static factory method for creating a {@link StrictReference}.
     *
     * @param supplier - The supplier for an object to refer to.
     * @param <R>      - The referent type.
     * @return a new {@link StrictReference}.
     * @throws NullPointerException if the return value of the supplier is {@code null}.
     */
    @NotNull
    public static <R> StrictReference<R> refer(@NotNull Supplier<R> supplier) {
        return new StrictReference<>(supplier.get());
    }
}
