package me.hapyl.eterna.module.util;

import javax.annotation.Nonnull;

/**
 * Represents an {@link Object} that can be named and has a description.
 * <br>
 * The name and description can be mutable or immutable, up to implementation.
 */
public interface Described extends Named {

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    String getName();

    /**
     * Gets the description of this object.
     *
     * @return the description of this object.
     */
    @Nonnull
    String getDescription();

    /**
     * Sets the description of this object.
     * <p>The default implementation is as follows:
     * <pre>{@code
     *     default void setDescription(@Nonnull String description) {
     *         throw new UnsupportedOperationException();
     *     }
     * }</pre></p>
     *
     * @param description - The description to set.
     */
    default void setDescription(@Nonnull String description) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the description of this object in the lower case.
     * <p>The default implementation is as follows:
     * <pre>{@code
     *   @Nonnull
     *     default String getDescriptionLowerCase() {
     *         return getDescription().toLowerCase();
     *     }
     * }</pre></p>
     *
     * @return the description of this object in the lower case.
     */
    @Nonnull
    default String getDescriptionLowerCase() {
        return getDescription().toLowerCase();
    }

}
