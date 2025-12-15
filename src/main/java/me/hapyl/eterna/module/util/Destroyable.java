package me.hapyl.eterna.module.util;

/**
 * Represents a destroyable object, which will be completely destroyed upon calling {@link #destroy()}.
 * <p>
 * The destroy method is up to implementation, but generally means "remove everything I've done".
 * </p>
 */
public interface Destroyable {
    
    /**
     * Completely destroys this object, as it never existed,
     * resetting everything modified to its previous state.
     */
    void destroy();
    
}
