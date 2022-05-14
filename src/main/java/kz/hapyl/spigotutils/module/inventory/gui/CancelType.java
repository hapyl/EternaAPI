package kz.hapyl.spigotutils.module.inventory.gui;

public enum CancelType {
    GUI, // cancels only GUI's inventory
    INVENTORY, // cancels only player's inventory clicks
    EITHER, // cancels both player's inventory and gui inventory
    NEITHER // doesn't cancel anything
}
