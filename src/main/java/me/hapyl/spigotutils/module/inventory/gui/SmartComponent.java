package me.hapyl.spigotutils.module.inventory.gui;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;

/**
 * Stores a list of items and their actions.
 *
 * @author Hapyl
 */
public class SmartComponent {

    private final LinkedHashMap<ItemStack, GUIClick> map;

    public SmartComponent() {
        this.map = new LinkedHashMap<>();
    }

    /**
     * Adds an item to the component with a click action.
     *
     * @param item   Item.
     * @param action Action to perform when clicked.
     */
    public SmartComponent add(ItemStack item, GUIClick action) {
        this.map.put(item, action);
        return this;
    }

    /**
     * Adds an item to the component with a click action.
     *
     * @param item   Item.
     * @param action Action to perform when clicked.
     */
    public SmartComponent add(ItemStack item, Action action) {
        this.map.put(item, new GUIClick(action));
        return this;
    }

    /**
     * Adds an item to the component with a click action with multiple click types.
     *
     * @param item       Item.
     * @param action     Action to perform when clicked.
     * @param clickTypes Click types.
     */
    public SmartComponent add(ItemStack item, Action action, ClickType... clickTypes) {
        final GUIClick guiClick = this.map.computeIfAbsent(item, a -> new GUIClick(action));

        for (ClickType click : clickTypes) {
            guiClick.addPerClick(click, action);
        }

        return this.add(item, guiClick);
    }

    /**
     * Adds an item to the component with a click action with multiple click types.
     *
     * @param item Item.
     */
    public SmartComponent add(ItemStack item) {
        return this.add(item, (Action) null);
    }

    /**
     * Applies the component to a GUI.
     *
     * @param gui      GUI to apply to.
     * @param pattern  Pattern to apply.
     * @param startRow Row to start from.
     */
    public void apply(GUI gui, SlotPattern pattern, int startRow) {
        pattern.apply(gui, getMap(), startRow);
    }

    @Deprecated
    public void fillItems(GUI gui) {
        SlotPattern.DEFAULT.apply(gui, getMap(), 1);
        //        fillItems(gui, SlotPattern.DEFAULT);
    }

    @Deprecated
    public void fillItems(GUI gui, SlotPattern pattern) {
        pattern.apply(gui, getMap(), 1);
        //        gui.fillItems(getMap(), pattern, 1);
    }

    @Deprecated
    public void fillItems(GUI gui, SlotPattern pattern, int startLine) {
        pattern.apply(gui, getMap(), startLine);
        //        gui.fillItems(getMap(), pattern, startLine);
    }

    public int getMenuSize() {
        return GUI.getSmartMenuSize(map.keySet());
    }

    public LinkedHashMap<ItemStack, GUIClick> getMap() {
        return this.map;
    }

}
