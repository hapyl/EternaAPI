package me.hapyl.spigotutils.module.inventory.gui;

import org.bukkit.entity.Player;

import javax.annotation.Nullable;

/**
 * This GUI provides a nicer way to update the inventory, clearing all click events and items before settings everything up.
 *
 * @param <E> - Optional parameter for update method.
 */
public abstract class PlayerDynamicGUI<E> extends PlayerGUI {

    public PlayerDynamicGUI(Player player, String name, int rows) {
        super(player, name, rows);
    }

    public PlayerDynamicGUI(Player player) {
        super(player);
    }

    public PlayerDynamicGUI(Player player, int rows, String... menuName) {
        super(player, rows, menuName);
    }

    public abstract void setupInventory(@Nullable E optional);

    public void update(@Nullable E optional) {
        clearClickEvents();
        clearItems();

        setupInventory(optional);
        openInventory();
    }

}
