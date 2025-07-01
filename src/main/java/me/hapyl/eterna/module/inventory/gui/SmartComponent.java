package me.hapyl.eterna.module.inventory.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.function.Consumer;

/**
 * Stores a list of items and their actions.
 *
 * @author hapyl
 */
public class SmartComponent {
    
    private final LinkedHashMap<ItemStack, ActionList> map;
    
    public SmartComponent() {
        this.map = new LinkedHashMap<>();
    }
    
    /**
     * Adds an item to the component with a click action.
     *
     * @param item   Item.
     * @param action Action to perform when clicked.
     */
    public SmartComponent add(@Nonnull ItemStack item, @Nullable ActionList action) {
        this.map.put(item, action);
        return this;
    }
    
    /**
     * Adds an item to the component with a click action.
     *
     * @param item   Item.
     * @param action Action to perform when clicked.
     */
    public SmartComponent add(@Nonnull ItemStack item, @Nullable Consumer<Player> action) {
        this.map.put(item, action != null ? new ActionList(action) : null);
        return this;
    }
    
    /**
     * Adds an item to the component with a click action.
     *
     * @param item   - Item.
     * @param action - Action.
     * @see StrictAction
     */
    public SmartComponent add(@Nonnull ItemStack item, @Nonnull StrictAction action) {
        this.map.put(item, action.makeAction());
        return this;
    }
    
    /**
     * Adds an item to the component with a click action with multiple click types.
     *
     * @param item       Item.
     * @param action     Action to perform when clicked.
     * @param clickTypes Click types.
     */
    public SmartComponent add(@Nonnull ItemStack item, @Nonnull Consumer<Player> action, @Nonnull ClickType... clickTypes) {
        final ActionList guiClick = this.map.computeIfAbsent(item, a -> new ActionList(action));
        
        for (ClickType click : clickTypes) {
            guiClick.setAction(click, action);
        }
        
        return this.add(item, guiClick);
    }
    
    /**
     * Sets the action for the given item.
     * <br>
     * This does not add another item, only modifies the existing one.
     * If there is no existing item, it will be added.
     *
     * @param item   - Item.
     * @param action - Action.
     * @param types  - Types.
     */
    public SmartComponent set(@Nonnull ItemStack item, @Nonnull Consumer<Player> action, @Nonnull ClickType... types) {
        this.map.compute(
                item, (i, click) -> {
                    click = click != null ? click : new ActionList();
                    
                    for (ClickType type : types) {
                        click.setAction(type, action);
                    }
                    
                    return click;
                }
        );
        
        return this;
    }
    
    /**
     * Adds an item to the component with a click action with multiple click types.
     *
     * @param item Item.
     */
    public SmartComponent add(@Nonnull ItemStack item) {
        return this.add(item, (ActionList) null);
    }
    
    /**
     * Applies the component to a GUI.
     *
     * @param gui      GUI to apply to.
     * @param pattern  Pattern to apply.
     * @param startRow Row to start from.
     */
    public void apply(@Nonnull PlayerGUI gui, @Nonnull SlotPattern pattern, int startRow) {
        pattern.apply(gui, map, startRow);
    }
    
    /**
     * @deprecated {@link SmartComponent#apply(PlayerGUI, SlotPattern, int)}
     */
    @Deprecated
    public void fillItems(PlayerGUI gui) {
        SlotPattern.DEFAULT.apply(gui, map, 1);
    }
    
    /**
     * @deprecated {@link SmartComponent#apply(PlayerGUI, SlotPattern, int)}
     */
    @Deprecated
    public void fillItems(PlayerGUI gui, SlotPattern pattern) {
        pattern.apply(gui, map, 1);
    }
    
    /**
     * @deprecated {@link SmartComponent#apply(PlayerGUI, SlotPattern, int)}
     */
    @Deprecated
    public void fillItems(PlayerGUI gui, SlotPattern pattern, int startLine) {
        pattern.apply(gui, map, startLine);
    }
    
}
