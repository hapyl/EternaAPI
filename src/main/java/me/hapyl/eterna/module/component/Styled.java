package me.hapyl.eterna.module.component;

import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an object that may be styled.
 */
public interface Styled {
    
    /**
     * Gets the {@link Style} of this object.
     *
     * @return the style of this object.
     */
    @NotNull
    Style getStyle();
    
    /**
     * Sets the {@link Style} of this object.
     *
     * <p>
     * The default implementation throws an {@link UnsupportedOperationException}.
     * </p>
     *
     * @param style - The style to set.
     */
    default void setStyle(@NotNull Style style) {
        throw new UnsupportedOperationException("setStyle()");
    }
    
}
