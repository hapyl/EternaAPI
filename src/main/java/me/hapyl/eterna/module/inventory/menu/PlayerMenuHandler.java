package me.hapyl.eterna.module.inventory.menu;

import com.destroystokyo.paper.event.player.PlayerRecipeBookClickEvent;
import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaKeyed;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * A listener for {@link PlayerMenu}.
 */
@ApiStatus.Internal
public final class PlayerMenuHandler extends EternaKeyed implements Listener {
    
    public PlayerMenuHandler(@NotNull EternaKey key) {
        super(key);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleInventoryDragEvent(InventoryDragEvent ev) {
        fetchPlayerMenu((Player) ev.getWhoClicked(), ev.getInventory()).ifPresent(menu -> {
            if (!menu.properties.cancelDragging) {
                ev.setCancelled(true);
            }
        });
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleInventoryClickEvent(InventoryClickEvent ev) {
        fetchPlayerMenu(((Player) ev.getWhoClicked()), ev.getInventory()).ifPresent(menu -> {
            final int slot = ev.getRawSlot();
            final ClickType clickType = ev.getClick();
            
            // Cancel shift clicks
            if (clickType.isShiftClick() && menu.properties.cancelShiftClick) {
                ev.setCancelled(true);
            }
            
            // Cancel number clicks
            if (clickType == ClickType.NUMBER_KEY && menu.properties.cancelNumberClick) {
                ev.setCancelled(true);
            }
            
            // Inventory cancel type
            final CancelType cancelType = menu.properties.cancelType;
            final int inventorySize = menu.getMenuSize();
            
            if (slot >= inventorySize && (cancelType == CancelType.OUTSIDE_ONLY || cancelType == CancelType.EITHER)) {
                ev.setCancelled(true);
            }
            
            if (slot < inventorySize && (cancelType == CancelType.INSIDE_ONLY || cancelType == CancelType.EITHER)) {
                ev.setCancelled(true);
            }
            
            // Process click events
            menu.onClick(clickType, slot, ev.getHotbarButton());
        });
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleInventoryCloseEvent(InventoryCloseEvent ev) {
        final Player player = (Player) ev.getPlayer();
        
        fetchPlayerMenu(player, ev.getInventory()).ifPresent(menu -> {
            // Call event method
            menu.onClose();
            
            // Cleanup
            PlayerMenu.playerMenus.remove(player.getUniqueId());
        });
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePlayerQuitEvent(PlayerQuitEvent ev) {
        PlayerMenu.playerMenus.remove(ev.getPlayer().getUniqueId());
    }
    
    // Do not allow using recipe book while inside any menu to prevent duplication for inventories when the book is visible
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePlayerRecipeBookClickEvent(PlayerRecipeBookClickEvent ev) {
        if (PlayerMenu.playerMenus.containsKey(ev.getPlayer().getUniqueId())) {
            ev.setCancelled(true);
        }
    }
    
    @NotNull
    private static Optional<PlayerMenu> fetchPlayerMenu(@NotNull Player player, @NotNull Inventory inventory) {
        final PlayerMenu currentMenu = PlayerMenu.playerMenus.get(player.getUniqueId());
        
        return currentMenu != null && currentMenu.compareInventory(inventory)
               ? Optional.of(currentMenu)
               : Optional.empty();
    }
    
}