package me.hapyl.spigotutils.module.inventory.gui;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;

public class SmartComponent {

    private final LinkedHashMap<ItemStack, GUIClick> map;

    public SmartComponent() {
        this.map = new LinkedHashMap<>();
    }

    public SmartComponent add(ItemStack item, GUIClick action) {
        this.map.put(item, action);
        return this;
    }

    public SmartComponent add(ItemStack item, Action action) {
        this.map.put(item, new GUIClick(action));
        return this;
    }

    public SmartComponent add(ItemStack item, Action action, ClickType... clickTypes) {
        final GUIClick guiClick = this.map.computeIfAbsent(item, a -> new GUIClick(action));

        for (ClickType click : clickTypes) {
            guiClick.addPerClick(click, action);
        }

        return this.add(item, guiClick);
    }

    public SmartComponent add(ItemStack item) {
        return this.add(item, (Action) null);
    }

    public void fillItems(GUI gui) {
        fillItems(gui, SlotPattern.DEFAULT);
    }

    public void fillItems(GUI gui, SlotPattern pattern) {
        gui.fillItems(getMap(), pattern, 1);
    }

    public void fillItems(GUI gui, SlotPattern pattern, int startLine) {
        gui.fillItems(getMap(), pattern, startLine);
    }

    // this should be called to calculate GUI size based on items
    public int getMenuSize() {
        return GUI.getSmartMenuSize(map.keySet());
    }

    public LinkedHashMap<ItemStack, GUIClick> getMap() {
        return this.map;
    }

}
