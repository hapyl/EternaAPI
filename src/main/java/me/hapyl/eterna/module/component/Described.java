package me.hapyl.eterna.module.component;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an object that may have a description.
 */
public interface Described {
    
    /**
     * Gets the description of this object.
     *
     * @return the description of this object.
     */
    @NotNull
    Component getDescription();
    
    /**
     * Sets the description of this object.
     *
     * <p>
     * The default implementation throws an {@link UnsupportedOperationException}.
     * </p>
     *
     * @param description - The description to set.
     */
    default void setDescription(@NotNull Component description) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Gets the default {@link Component} description to be used as a placeholder.
     *
     * @return the default description.
     */
    @NotNull
    static Component defaultValue() {
        class Holder {
            private static final Component VALUE = Component.text("No description.");
        }
        
        return Holder.VALUE;
    }
    
}
