package me.hapyl.eterna.module.inventory.gui;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * This GUI provides a nicer way to update the inventory, clearing all click events and items before settings everything up.
 */
public abstract class PlayerDynamicGUI extends PlayerGUI {

    public PlayerDynamicGUI(Player player, String name, int rows) {
        super(player, name, rows);
    }

    public PlayerDynamicGUI(Player player) {
        super(player);
    }

    public PlayerDynamicGUI(Player player, int rows, String... menuName) {
        super(player, rows, menuName);
    }

    public abstract void setupInventory(@Nonnull Arguments arguments);

    @Override
    public final void openInventory() {
        update(Arguments.EMPTY);
    }

    public final void update() {
        update(Arguments.EMPTY);
    }

    public final void update(@Nonnull Arguments arguments) {
        clearClickEvents();
        clearItems();

        setupInventory(arguments);
        super.openInventory();
    }

}
