package me.hapyl.eterna.module.util;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Represents an object that can be removed in some way.
 */
public interface Removable {
    
    /**
     * Removes the object.
     */
    void remove();
    
    /**
     * An optional operation to be used in {@link Collection#removeIf(Predicate)} on a {@link Map} that determines whether this object should be removed.
     * <p>Note that calling this method on a map will <b><u>not</u></b> call the {@link #remove()} method and {@link #removeIfShould()} must be called to properly remove the entry.</p>
     *
     * @return {@code true} if this object should be removed.
     * @see #removeIfShould()
     */
    default boolean shouldRemove() {
        return false;
    }
    
    /**
     * An optional operation to be used in {@link Collection#removeIf(Predicate)} on a {@link Map} to check {@link #shouldRemove()} and, if {@code true}, call {@link #remove()}.
     *
     * @return {@code true} if this object should be removed.
     */
    default boolean removeIfShould() {
        if (shouldRemove()) {
            remove();
            return true;
        }
        
        return false;
    }
    
}
