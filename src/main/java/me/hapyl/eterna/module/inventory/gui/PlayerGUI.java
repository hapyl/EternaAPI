package me.hapyl.eterna.module.inventory.gui;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Represents a GUI that is opened to a player.
 */
public class PlayerGUI extends GUI {

    protected final Player player;

    public PlayerGUI(Player player, String name, int rows) {
        super(name, rows);
        this.player = player;
    }

    public PlayerGUI(Player player) {
        this(player, "Default Menu", 1);
    }

    public PlayerGUI(Player player, int rows, String... menuName) {
        this(player, menuArrowSplit(menuName), rows);
    }

    public void rename(@Nonnull String newName) {
        super.rename(player, newName);
    }

    @Override
    @Deprecated
    public void rename(@Nonnull Player player, @Nonnull String newName) {
        super.rename(this.player, newName);
    }

    public Player getPlayer() {
        return player;
    }

    public void closeInventory() {
        player.closeInventory();
    }

    public void openInventory() {
        this.openInventory(player);
    }

}
