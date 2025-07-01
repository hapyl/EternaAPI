package me.hapyl.eterna.module.inventory.gui;

import javax.annotation.Nonnull;

/**
 * {@link PlayerGUI} annotated with this annotation will not be opened.
 */
public interface DisabledGUI {
    
    /**
     * Gets the message that will be sent to the player once they try to open the GUI.
     *
     * @return the message that will be sent to the player once they try to open the GUI.
     */
    @Nonnull
    default String message() {
        return "&4This GUI is currently disabled!";
    }
    
}
