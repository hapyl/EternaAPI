package kz.hapyl.spigotutils.module.inventory.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GUIListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void handleGUIListener(InventoryClickEvent ev) {
		final Player player = (Player)ev.getWhoClicked();
		final GUI gui = GUI.getPlayerGUI(player);

		if (gui == null) {
			return;
		}

		if (!gui.compareInventory(ev.getInventory())) {
			return;
		}

		final int slot = ev.getRawSlot();

		if (slot >= gui.getSize() && !gui.onlyCancelGUI()) {
			ev.setCancelled(true);
		}

		if (slot < gui.getSize() && !gui.isIgnoredSlot(slot)) {
			ev.setCancelled(true);
		}

		if (gui.getListener() != null) {
			gui.getListener().listen(player, gui, ev);
		}

		if (gui.hasEvent(slot)) {
			gui.acceptEvent(slot, player, ev.getClick());
		}

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void handleInventoryCloseEvent(InventoryCloseEvent ev) {
		final Player player = (Player)ev.getPlayer();
		final GUI gui = GUI.getPlayerGUI(player);
		if (gui != null && gui.getCloseEvent() != null) {
			gui.getCloseEvent().invoke(player);
			GUI.removePlayerGUI(player);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void handleInventoryOpenEvent(InventoryOpenEvent ev) {
		final Player player = (Player)ev.getPlayer();
		final GUI gui = GUI.getPlayerGUI(player);
		if (gui != null && gui.getOpenEvent() != null) {
			gui.getOpenEvent().invoke(player);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void handlePlayerLeave(PlayerQuitEvent ev) {
		final Player player = ev.getPlayer();
		final GUI gui = GUI.getPlayerGUI(player);

		if (gui == null) {
			return;
		}

		GUI.removePlayerGUI(player);

	}

}