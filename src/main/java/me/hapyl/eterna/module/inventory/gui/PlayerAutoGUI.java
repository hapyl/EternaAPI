package me.hapyl.eterna.module.inventory.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.function.Consumer;

/**
 * This GUI will autofill the inventory with the items provided by the {@link SmartComponent}.
 */
public class PlayerAutoGUI extends PlayerGUI {

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
    @OverridingMethodsMustInvokeSuper
    public void onUpdate() {
        component.apply(this, pattern, 1);
    }
    
    public void addItem(ItemStack item, Consumer<Player> action) {
        component.add(item, action);
    }

    public void addItem(ItemStack item) {
        component.add(item);
    }

}
