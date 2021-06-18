package kz.hapyl.spigotutils.module.inventory.gui;

import org.bukkit.inventory.ItemStack;

import java.util.*;

public class SmartComponent {

	private final LinkedHashMap<ItemStack, GUIClick> map;

	// FIXME: 019. 05/19/2021 -> fix keyset

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

	public SmartComponent add(ItemStack item) {
		return this.add(item, (Action) null);
	}

	public void fillItems(GUI gui) {
		gui.fillItems(this.getMap());
	}

	public Map<ItemStack, GUIClick> getMap() {
		return this.map;
	}

}
