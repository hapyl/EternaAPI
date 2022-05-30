package me.hapyl.spigotutils.module.inventory.gui;

import org.bukkit.entity.Player;

public class PlayerGUI extends GUI {

    private final Player player;

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
