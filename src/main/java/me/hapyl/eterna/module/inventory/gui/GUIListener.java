package me.hapyl.eterna.module.inventory.gui;

import me.hapyl.eterna.module.chat.Chat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nullable;

/**
 * A listener implementation for GUIs.
 */
@ApiStatus.Internal
public final class GUIListener implements Listener {
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleGUIDrag(InventoryDragEvent ev) {
        final Player player = (Player) ev.getWhoClicked();
        final PlayerGUI gui = fetchGUI(player, ev.getInventory());
        
        if (gui == null) {
            return;
        }
        
        if (!gui.getProperties().isAllowDrag()) {
            ev.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleGUIListener(InventoryClickEvent ev) {
        final Player player = (Player) ev.getWhoClicked();
        final PlayerGUI gui = fetchGUI(player, ev.getInventory());
        
        if (gui == null) {
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
        
        if (gui instanceof GUIEventListener handler) {
            handler.onClick(slot, ev);
        }
        
        if (gui.hasEvent(slot)) {
            if (!gui.getProperties().isClickCooldown()) {
                Chat.sendMessage(player, "&cPlease slow down!");
                ev.setCancelled(true);
                return;
            }
            
            gui.acceptEvent(slot, player, click);
            gui.getProperties().markCooldownClick();
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleInventoryCloseEvent(InventoryCloseEvent ev) {
        final Player player = (Player) ev.getPlayer();
        final PlayerGUI gui = fetchGUI(player, ev.getInventory());
        
        if (gui == null) {
            return;
        }
        
        // If reopened the same GUI, ignore it
        if (gui.reopen) {
            gui.reopen = false;
            return;
        }
        
        if (gui instanceof GUIEventListener handler) {
            handler.onClose(ev);
        }
        
        PlayerGUI.playerInventory.remove(player.getUniqueId(), gui);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleInventoryOpenEvent(InventoryOpenEvent ev) {
        final Player player = (Player) ev.getPlayer();
        final PlayerGUI gui = fetchGUI(player, ev.getInventory());
        
        if (gui instanceof GUIEventListener handler) {
            handler.onOpen(ev);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePlayerLeave(PlayerQuitEvent ev) {
        final Player player = ev.getPlayer();
        final PlayerGUI gui = PlayerGUI.getPlayerGUI(player);
        
        if (gui == null) {
            return;
        }
        
        PlayerGUI.playerInventory.remove(player.getUniqueId());
    }
    
    @Nullable
    private static PlayerGUI fetchGUI(Player player, Inventory inventory) {
        final PlayerGUI gui = PlayerGUI.getPlayerGUI(player);
        
        if (gui == null || !gui.compareInventory(inventory)) {
            return null;
        }
        
        return gui;
    }
    
}