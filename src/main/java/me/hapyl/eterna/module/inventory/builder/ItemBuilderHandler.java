package me.hapyl.eterna.module.inventory.builder;

import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaKeyed;
import me.hapyl.eterna.module.registry.Key;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.Consumer;

@ApiStatus.Internal
public final class ItemBuilderHandler extends EternaKeyed implements Listener {
    
    public ItemBuilderHandler(@NotNull EternaKey key) {
        super(key);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void handleInventoryClick(InventoryClickEvent ev) {
        final Player player = (Player) ev.getWhoClicked();
        final ItemStack item = ev.getCurrentItem();
        final ClickType clickType = ev.getClick();
        
        if (item == null) {
            return;
        }
        
        fetchFunctionList(
                item, functions -> {
                    for (ItemFunction function : clickType.isLeftClick()
                                                 ? functions.matchFunctions(Set.of(Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK))
                                                 : functions.matchFunctions(Set.of(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK))) {
                        if (function.allowInventoryClick) {
                            function.execute0(player, item);
                            
                            if (function.cancelClick) {
                                ev.setCancelled(true);
                            }
                        }
                    }
                }
        );
        
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void handleClick(PlayerInteractEvent ev) {
        // I just don't like double clicking.
        if (ev.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }
        
        final Player player = ev.getPlayer();
        final Action action = ev.getAction();
        final ItemStack item = player.getInventory().getItemInMainHand();
        
        fetchFunctionList(
                item, functions -> {
                    functions.matchFunctions(action).forEach(function -> {
                        function.execute0(player, item);
                        
                        if (function.cancelClick) {
                            ev.setCancelled(true);
                        }
                    });
                    
                    if (functions.listener == null) {
                        return;
                    }
                    
                    functions.listener.onClick(player, ev);
                    
                    switch (action) {
                        case RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK -> functions.listener.onRightClick(player, ev);
                        case LEFT_CLICK_AIR, LEFT_CLICK_BLOCK -> functions.listener.onLeftClick(player, ev);
                        case PHYSICAL -> functions.listener.onPhysicalClick(player, ev);
                    }
                }
        );
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void handleBlockPlace(BlockPlaceEvent ev) {
        fetchFunctionList(
                ev.getItemInHand(), functionList -> {
                    if (functionList.listener == null) {
                        return;
                    }
                    
                    functionList.listener.onBlockPlace(ev.getPlayer(), ev);
                }
        );
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void handleBlockBreak(BlockBreakEvent ev) {
        final Player player = ev.getPlayer();
        
        fetchFunctionList(
                player.getInventory().getItemInMainHand(), functionList -> {
                    if (functionList.listener == null) {
                        return;
                    }
                    
                    functionList.listener.onBlockBreak(player, ev);
                }
        );
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void handleItemDrop(PlayerDropItemEvent ev) {
        fetchFunctionList(
                ev.getItemDrop().getItemStack(), functionList -> {
                    if (functionList.listener == null) {
                        return;
                    }
                    
                    functionList.listener.onItemDrop(ev.getPlayer(), ev);
                }
        );
    }
    
    private void fetchFunctionList(ItemStack item, Consumer<ItemFunctionList> consumer) {
        final Key key = ItemBuilder.getItemKey(item);
        final ItemFunctionList functions = ItemBuilder.functionByKey(key);
        
        if (functions != null) {
            consumer.accept(functions);
        }
    }
    
}
