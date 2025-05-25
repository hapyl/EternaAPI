package me.hapyl.eterna.module.inventory.gui;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.util.Nulls;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * A listener implementation for GUIs.
 */
public final class GUIListener implements Listener {
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleGUIDrag(InventoryDragEvent ev) {
        final Player player = (Player) ev.getWhoClicked();
        final GUI gui = GUI.getPlayerGUI(player);
        
        if (gui == null || !gui.compareInventory(ev.getInventory())) {
            return;
        }
        
        if (!gui.getProperties().isAllowDrag()) {
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
        
        if (click.isShiftClick() && !gui.getProperties().isAllowShiftClick()) {
            ev.setCancelled(true);
        }
        
        if (click == ClickType.NUMBER_KEY && !gui.getProperties().isAllowNumbersClick()) {
            ev.setCancelled(true);
        }
        
        // cancel test
        final CancelType cancelType = gui.getCancelType();
        
        if (slot >= gui.getSize() && (cancelType == CancelType.INVENTORY || cancelType == CancelType.EITHER)) {
            ev.setCancelled(true);
        }
        
        if (slot < gui.getSize() && (cancelType == CancelType.GUI || cancelType == CancelType.EITHER) && !gui.isIgnoredSlot(slot)) {
            ev.setCancelled(true);
        }
        
        if (gui.getListener() != null) {
            gui.getListener().listen(player, gui, ev);
        }
        
        if (gui.hasEvent(slot)) {
            if (!gui.getProperties().isClickCooldown(player)) {
                Chat.sendMessage(player, "&cPlease slow down!");
                ev.setCancelled(true);
                return;
            }
            
            gui.acceptEvent(slot, player, click);
            gui.getProperties().markCooldownClick(player);
            
            Nulls.runIfNotNull(gui.getEventHandler(), h -> h.onClick(gui, player, slot));
        }
        
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleInventoryCloseEvent(InventoryCloseEvent ev) {
        final Player player = (Player) ev.getPlayer();
        final GUI gui = GUI.getPlayerGUI(player);
        
        if (gui != null && gui.compareInventory(ev.getInventory())) {
            // If reopened the same GUI, ignore it
            if (gui.reopen) {
                gui.reopen = false;
                return;
            }
            
            final Action closeEvent = gui.getCloseEvent();
            final GUIEventHandler handler = gui.getEventHandler();
            
            if (closeEvent != null) {
                closeEvent.invoke(player);
            }
            
            if (handler != null) {
                handler.onClose(gui, player);
            }
            
            GUI.playerInventory.remove(player.getUniqueId(), gui);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleInventoryOpenEvent(InventoryOpenEvent ev) {
        final Player player = (Player) ev.getPlayer();
        final GUI gui = GUI.getPlayerGUI(player);
        if (gui != null && gui.compareInventory(ev.getInventory()) && gui.getOpenEvent() != null) {
            gui.getOpenEvent().invoke(player);
            
            Nulls.runIfNotNull(gui.getEventHandler(), h -> h.onOpen(gui, player));
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePlayerLeave(PlayerQuitEvent ev) {
        final Player player = ev.getPlayer();
        final GUI gui = GUI.getPlayerGUI(player);
        
        if (gui == null) {
            return;
        }
        
        GUI.playerInventory.remove(player.getUniqueId());
    }
    
}