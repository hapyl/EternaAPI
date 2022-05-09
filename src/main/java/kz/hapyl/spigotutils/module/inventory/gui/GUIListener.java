package kz.hapyl.spigotutils.module.inventory.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerQuitEvent;

public class GUIListener implements Listener {

    @EventHandler()
    public void handleGUIDrag(InventoryDragEvent ev) {
        final Player player = (Player) ev.getWhoClicked();
        final GUI gui = GUI.getPlayerGUI(player);

        if (gui == null || !gui.compareInventory(ev.getInventory())) {
            return;
        }

        if (!gui.isAllowDrag()) {
            ev.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleGUIListener(InventoryClickEvent ev) {
        final Player player = (Player) ev.getWhoClicked();
        final GUI gui = GUI.getPlayerGUI(player);

        if (gui == null || !gui.compareInventory(ev.getInventory())) {
            return;
        }

        final int slot = ev.getRawSlot();
        final ClickType click = ev.getClick();

        if (click.isShiftClick() && !gui.isAllowShiftClick()) {
            ev.setCancelled(true);
        }

        // cancel test
        final CancelType cancelType = gui.getCancelType();

        if (slot >= gui.getSize() && (cancelType == CancelType.INVENTORY || cancelType == CancelType.EITHER)) {
            ev.setCancelled(true);
        }

        if (slot < gui.getSize() && (cancelType == CancelType.GUI || cancelType == CancelType.EITHER)) {
            ev.setCancelled(true);
        }

        if (slot < gui.getSize() && !gui.isIgnoredSlot(slot)) {
            ev.setCancelled(true);
        }

        if (gui.getListener() != null) {
            gui.getListener().listen(player, gui, ev);
        }

        if (gui.hasEvent(slot)) {
            gui.acceptEvent(slot, player, click);
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleInventoryCloseEvent(InventoryCloseEvent ev) {
        final Player player = (Player) ev.getPlayer();
        final GUI gui = GUI.getPlayerGUI(player);
        if (gui != null && gui.getCloseEvent() != null) {
            gui.getCloseEvent().invoke(player);
            GUI.removePlayerGUI(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleInventoryOpenEvent(InventoryOpenEvent ev) {
        final Player player = (Player) ev.getPlayer();
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