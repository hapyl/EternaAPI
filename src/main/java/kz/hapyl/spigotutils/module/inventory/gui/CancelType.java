package kz.hapyl.spigotutils.module.inventory.gui;

/**
 * Note that cancel type only cancels {@link org.bukkit.event.inventory.InventoryClickEvent}.
 * Click Event will still fire.
 */
public enum CancelType {
    /**
     * Cancel only clicks inside GUI.
     */
    GUI,
    /**
     * Cancel only clicks outside GUI.
     */
    INVENTORY,
    /**
     * Cancel both clicks inside and outside of GUI.
     */
    EITHER,
    /**
     * Doesn't cancel any clicks.
     */
    NEITHER
}
