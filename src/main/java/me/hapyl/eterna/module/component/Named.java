package me.hapyl.eterna.module.component;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an object that may have a name.
 */
public interface Named {
    
    /**
     * Gets the name of this object.
     *
     * @return the name of this object.
     */
    @NotNull
    Component getName();
    
    /**
     * Sets the name of this object.
     *
     * <p>
     * The default implementation throws an {@link UnsupportedOperationException}.
     * </p>
     *
     * @param name - The name to set.
     */
    default void setName(@NotNull Component name) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Gets the default {@link Component} name to be used as a placeholder.
     *
     * @return the default name.
     */
    @NotNull
    static Component defaultValue() {
        class Holder {
            private static final Component VALUE = Component.text("Unnamed.");
        }
        
        return Holder.VALUE;
    }
    
}
