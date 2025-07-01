package me.hapyl.eterna.module.inventory.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import javax.annotation.Nonnull;

/**
 * Represents an event listener for {@link PlayerGUI}, supporting listening to:
 * <ul>
 *     <li>Opening the GUI, with the handling {@link InventoryOpenEvent}
 *     <li>Closing the GUI, with the handling {@link InventoryCloseEvent}
 *     <li>Generic click, with the handling {@link InventoryClickEvent}.
 * </ul>
 */
public interface GUIEventListener {
    
    /**
     * Called whenever the {@link PlayerGUI} opens.
     *
     * @param event - The event that handled the opening of the GUI.
     */
    default void onOpen(@Nonnull InventoryOpenEvent event) {
    }
    
    /**
     * Called whenever the {@link PlayerGUI} closes.
     *
     * @param event - The event that handled the closing of this GUI.
     */
    default void onClose(@Nonnull InventoryCloseEvent event) {
    }
    
    /**
     * Called whenever the player clicks in the {@link PlayerGUI}.
     *
     * @param slot  - The clicked slot.
     * @param event - The event that handled the click.
     */
    default void onClick(int slot, @Nonnull InventoryClickEvent event) {
    }
    
}
