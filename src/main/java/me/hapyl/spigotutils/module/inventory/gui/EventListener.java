package me.hapyl.spigotutils.module.inventory.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface EventListener {

	void listen(Player player, GUI gui, InventoryClickEvent event);

}
