package me.hapyl.eterna.module.inventory.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.function.Consumer;

/**
 * Represents a {@link PlayerGUI} with autofill support provided by the {@link SmartComponent}.
 * <p>Note that this implementation is considered as obsolete, because {@link SmartComponent} is supported in any {@link PlayerGUI}, see {@link PlayerGUI#newSmartComponent()}</p>
 */
@ApiStatus.Obsolete
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
