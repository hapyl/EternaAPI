package me.hapyl.eterna.module.util;

import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * Represents an object that can be removed in some way.
 */
public interface Removable {
    
    /**
     * Removes this object.
     */
    void remove();
    
    /**
     * Gets whether this {@link Removable} should be removed.
     *
     * <p>
     * This is an optional operation designed to be used in {@link Collection#removeIf(Predicate)}.
     * </p>
     *
     * @return {@code true} if this object should be removed; {@code false} otherwise.
     * @see #removeIfShould()
     */
    default boolean shouldRemove() {
        return false;
    }
    
    /**
     * Removes this object if {@link #shouldRemove()} returns {@code true}.
     *
     * <p>
     * This is an optional operation designed to be used in {@link Collection#removeIf(Predicate)}, as example:
     *
     * <pre>{@code
     * list.removeIf(Removable::removeIfShould);
     * }</pre>
     * <p>
     * instead of:
     * <pre>{@code
     * list.removeIf(removable -> {
     *     if (removable.shouldRemove()) {
     *         removable.remove();
     *         return true;
     *     }
     *
     *     return false;
     * });
     * }</pre>
     * </p>
     *
     * @return {@code true} if this object was removed; {@code false} otherwise.
     */
    @ApiStatus.NonExtendable
    default boolean removeIfShould() {
        if (this.shouldRemove()) {
            this.remove();
            return true;
        }
        
        return false;
    }
    
}
