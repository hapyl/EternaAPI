package me.hapyl.eterna.module.inventory;

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

import java.util.Set;
import java.util.function.Consumer;

public class ItemBuilderListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void handleInventoryClick(InventoryClickEvent ev) {
        final Player player = (Player) ev.getWhoClicked();
        final ItemStack item = ev.getCurrentItem();
        final ClickType clickType = ev.getClick();

        if (item == null) {
            return;
        }

        fetchFunctionList(item, functionList -> {
            final Set<ItemFunction> functions = clickType.isLeftClick()
                    ? functionList.getFunctions(Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK)
                    : functionList.getFunctions(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK);

            for (ItemFunction function : functions) {
                if (function.allowInventoryClick()) {
                    function.doExecute(player, item);

                    if (function.cancelClicks()) {
                        ev.setCancelled(true);
                    }
                }
            }
        });

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

        fetchFunctionList(item, functionList -> {
            // Functions
            functionList.getFunctions(action).forEach(function -> {
                function.doExecute(player, item);

                if (function.cancelClicks()) {
                    ev.setCancelled(true);
                }
            });

            // Handler
            if (functionList.handler == null) {
                return;
            }

            functionList.handler.onClick(player, ev);

            switch (action) {
                case RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK -> functionList.handler.onRightClick(player, ev);
                case LEFT_CLICK_AIR, LEFT_CLICK_BLOCK -> functionList.handler.onLeftClick(player, ev);
                case PHYSICAL -> functionList.handler.onPhysicalClick(player, ev);
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleBlockPlace(BlockPlaceEvent ev) {
        fetchFunctionList(ev.getItemInHand(), functionList -> {
            if (functionList.handler == null) {
                return;
            }

            functionList.handler.onBlockPlace(ev.getPlayer(), ev);
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleBlockBreak(BlockBreakEvent ev) {
        final Player player = ev.getPlayer();

        fetchFunctionList(player.getInventory().getItemInMainHand(), functionList -> {
            if (functionList.handler == null) {
                return;
            }

            functionList.handler.onBlockBreak(player, ev);
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleItemDrop(PlayerDropItemEvent ev) {
        fetchFunctionList(ev.getItemDrop().getItemStack(), functionList -> {
            if (functionList.handler == null) {
                return;
            }

            functionList.handler.onItemDrop(ev.getPlayer(), ev);
        });
    }

    private void fetchFunctionList(ItemStack item, Consumer<ItemFunctionList> consumer) {
        final Key key = ItemBuilder.getItemKey(item);
        final ItemFunctionList functions = ItemBuilder.getFunctionListByKey(key);

        if (functions != null) {
            consumer.accept(functions);
        }
    }

}
