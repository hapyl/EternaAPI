package me.hapyl.eterna.module.registry;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link Key} provider.
 *
 * <p>
 * Used to reduce method overloading by allowing both {@link Key} and {@link Keyed} instances to be treated uniformly.
 * </p>
 *
 * @see Key
 * @see Keyed
 */
public interface KeyLike {
    
    /**
     * Gets the {@link Key} object.
     *
     * @return the key object.
     */
    @NotNull
    Key key();
    
}
