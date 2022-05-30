package me.hapyl.spigotutils.module.inventory.gui;

import me.hapyl.spigotutils.module.annotate.Super;
import org.bukkit.entity.Player;

public class SmartGUI extends PlayerGUI {

    private final SmartComponent smart;

    public SmartGUI(Player player, String name, SmartComponent smart, int additionalRows) {
        super(player, name, smart.getMenuSize() + additionalRows);
        this.smart = smart;
    }

    public SmartGUI(Player player, String name, SmartComponent smart) {
        this(player, name, smart, 0);
    }

    @Super
    public final void openInventory(SlotPattern pattern, int startLine) {
        smart.fillItems(this, pattern, startLine);
        super.openInventory();
    }

    public final void openInventory(SlotPattern pattern) {
        openInventory(pattern, 1);
    }

    public final void openInventory(int startLine) {
        openInventory(SlotPattern.DEFAULT, startLine);
    }

    public final void openInventory() {
        openInventory(SlotPattern.DEFAULT, 1);
    }

}
