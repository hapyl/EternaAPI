package me.hapyl.spigotutils.module.inventory.gui;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedHashMap;

/**
 * Stores a list of items and their actions.
 *
 * @author hapyl
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
    public SmartComponent add(@Nonnull ItemStack item, @Nonnull GUIClick action) {
        this.map.put(item, action);
        return this;
    }

    /**
     * Adds an item to the component with a click action.
     *
     * @param item   Item.
     * @param action Action to perform when clicked.
     */
    public SmartComponent add(@Nonnull ItemStack item, @Nullable Action action) {
        this.map.put(item, action != null ? new GUIClick(action) : null);
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
        this.map.put(item, action.makeGUIClick());
        return this;
    }

    /**
     * Adds an item to the component with a click action with multiple click types.
     *
     * @param item       Item.
     * @param action     Action to perform when clicked.
     * @param clickTypes Click types.
     */
    public SmartComponent add(@Nonnull ItemStack item, @Nonnull Action action, @Nonnull ClickType... clickTypes) {
        final GUIClick guiClick = this.map.computeIfAbsent(item, a -> new GUIClick(action));

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
    public SmartComponent set(@Nonnull ItemStack item, @Nonnull Action action, @Nonnull ClickType... types) {
        this.map.compute(item, (i, click) -> {
            click = click != null ? click : new GUIClick();

            for (ClickType type : types) {
                click.setAction(type, action);
            }

            return click;
        });

        return this;
    }

    /**
     * Adds an item to the component with a click action with multiple click types.
     *
     * @param item Item.
     */
    public SmartComponent add(@Nonnull ItemStack item) {
        return this.add(item, (Action) null);
    }

    /**
     * Applies the component to a GUI.
     *
     * @param gui      GUI to apply to.
     * @param pattern  Pattern to apply.
     * @param startRow Row to start from.
     */
    public void apply(@Nonnull GUI gui, @Nonnull SlotPattern pattern, int startRow) {
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

    @Nonnull
    protected LinkedHashMap<ItemStack, GUIClick> getMap() {
        return this.map;
    }

}
