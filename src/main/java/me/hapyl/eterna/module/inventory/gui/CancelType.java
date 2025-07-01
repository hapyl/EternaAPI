package me.hapyl.eterna.module.inventory.gui;

/**
 * Denotes how to cancel clicks in the {@link PlayerGUI}.
 */
public enum CancelType {
    /**
     * Cancel only clicks inside {@link PlayerGUI}.
     */
    GUI,
    
    /**
     * Cancel only clicks outside {@link PlayerGUI}.
     */
    INVENTORY,
    
    /**
     * Cancel both clicks inside and outside of {@link PlayerGUI}.
     */
    EITHER,
    
    /**
     * Don't cancel any clicks.
     */
    NEITHER
}
