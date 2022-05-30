package me.hapyl.spigotutils.module.inventory.item;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class CustomItemListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void handlePlayerInteractEvent(PlayerInteractEvent ev) {
		final Player player = ev.getPlayer();
		final ItemStack item = ev.getItem();
		if (item == null || ev.getHand() == EquipmentSlot.OFF_HAND || ev.getAction() == Action.PHYSICAL) {
			return;
		}

		final CustomItem customItem = getItem(item);
		if (customItem == null) {
			return;
		}

		customItem.onClick(player, item, ev);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void handleInventoryClick(InventoryClickEvent ev) {
		final HumanEntity whoClicked = ev.getWhoClicked();
		final ItemStack item = ev.getCurrentItem();
		if (!(whoClicked instanceof Player player) || item == null) {
			return;
		}

		final CustomItem customItem = getItem(item);
		if (customItem == null) {
			return;
		}

		customItem.onInventoryClick(player, item, ev);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void handleItemPickup(EntityPickupItemEvent ev) {
		if (ev.getEntity() instanceof Player player) {
			final ItemStack stack = ev.getItem().getItemStack();
			final CustomItem customItem = getItem(stack);
			if (customItem == null) {
				return;
			}
			customItem.onPickup(player, stack, ev);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void handleBlockPlace(BlockPlaceEvent ev) {
		final ItemStack item = ev.getPlayer().getInventory().getItemInMainHand();
		final CustomItem customItem = getItem(item);
		if (customItem == null) {
			return;
		}
		customItem.onPlace(ev.getPlayer(), item, ev);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void handlePlayerDrop(PlayerDropItemEvent ev) {
		final ItemStack item = ev.getItemDrop().getItemStack();
		final CustomItem customItem = getItem(item);
		if (customItem == null) {
			return;
		}
		customItem.onDrop(ev.getPlayer(), item, ev);
	}

	private CustomItem getItem(ItemStack stack) {
		return CustomItemHolder.getInstance().byItemStack(stack);
	}

}
