package me.hapyl.spigotutils.module.inventory.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * This GUI will autofill the inventory with the items provided by the {@link SmartComponent}.
 */
public abstract class PlayerAutoGUI extends PlayerGUI {

    private final SmartComponent component;
    private SlotPattern pattern;
    private int startRow;

    public PlayerAutoGUI(Player player, String name, int rows) {
        super(player, name, rows);

        this.component = newSmartComponent();
        this.pattern = SlotPattern.DEFAULT;
        this.startRow = 1;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public SlotPattern getPattern() {
        return pattern;
    }

    public void setPattern(SlotPattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public final void openInventory() {
        component.apply(this, pattern, 1);
        super.openInventory();
    }

    public void addItem(ItemStack item, Action action) {
        component.add(item, action);
    }

    public void addItem(ItemStack item) {
        component.add(item);
    }

}
