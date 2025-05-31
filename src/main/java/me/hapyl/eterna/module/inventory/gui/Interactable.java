package me.hapyl.eterna.module.inventory.gui;

import org.bukkit.event.inventory.InventoryClickEvent;

import javax.annotation.Nonnull;

/**
 * Represents a {@link PlayerGUI} with additional methods that allow direct call, instead of relying on {@link ActionList}.
 */
public interface Interactable {
    
    /**
     * Called whenever the {@link PlayerGUI} opens.
     */
    void onOpen();
    
    /**
     * Called whenever the {@link PlayerGUI} closes.
     */
    void onClose();
    
    /**
     * Called whenever the {@link PlayerGUI} opened while already opened.
     */
    void onReopen();
    
    /**
     * Called whenever the player clicks in the {@link PlayerGUI}.
     *
     * @param slot  - The clicked slot.
     * @param event - The event handling the click.
     */
    void onClick(int slot, @Nonnull InventoryClickEvent event);
    
}
