package me.hapyl.eterna.module.util;

import javax.annotation.Nonnull;

/**
 * Represents an {@link Object} that can be named.
 * <br>
 * The name can be mutable or immutable, up to implementation.
 */
public interface Named {

    /**
     * Gets the name of this object.
     *
     * @return the name of this object.
     */
    @Nonnull
    String getName();

    /**
     * Sets the name of this object.
     * <p>The default implementation is as follows:
     * <pre>{@code
     *     default void setName(@Nonnull String name) {
     *         throw new UnsupportedOperationException();
     *     }
     * }</pre></p>
     *
     * @param name - The name to set.
     */
    default void setName(@Nonnull String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the name of this object in the lower case.
     * <p>The default implementation is as follows:
     * <pre>{@code
     *     @Nonnull
     *     default String getNameLowerCase() {
     *         return getName().toLowerCase();
     *     }
     * }</pre></p>
     *
     * @return the name of this object in the lower case.
     */
    @Nonnull
    default String getNameLowerCase() {
        return getName().toLowerCase();
    }

}
