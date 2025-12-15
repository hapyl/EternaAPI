package me.hapyl.eterna.module.nms;

import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;

/**
 * Represents an {@link Enum} which constants may to converted into a Nms equivalent.
 *
 * @param <N> - The Nms equivalent of this {@link Enum} constant.
 * @see NmsHelper NmsHelper for backwards references
 */
@ApiStatus.Internal
public interface NmsEnum<N> {
    
    /**
     * Gets the Nms equivalent of this {@link Enum} constant.
     *
     * @return the Nms equivalent of this {@link Enum} constant.
     */
    @Nonnull
    N toNms();
    
}
