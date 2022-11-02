package me.hapyl.spigotutils.module.inventory;

import me.hapyl.spigotutils.module.inventory.item.Event;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ItemEventHandler {

    public static final ItemEventHandler EMPTY = new ItemEventHandler();

    /**
     * Create new instance of Event Handler.
     * Keep in mind that item <b>MUST</b> have an ID for this to work.
     */
    public ItemEventHandler() {
    }

    /**
     * Called whenever player clicked with this item.
     */
    @Event
    public void onClick(Player player, PlayerInteractEvent ev) {
    }

    /**
     * Called whenever player right-clicked with this item.
     */
    @Event
    public void onRightClick(Player player, PlayerInteractEvent ev) {
    }

    /**
     * Called whenever player left-clicked with this item.
     */
    @Event
    public void onLeftClick(Player player, PlayerInteractEvent ev) {
    }

    /**
     * Called whenever player created a block while holding this item.
     */
    @Event
    public void onBlockPlace(Player player, BlockPlaceEvent ev) {
    }

    /**
     * Called whenever player breaks a block while holding this item.
     */
    @Event
    public void onBlockBreak(Player player, BlockBreakEvent ev) {
    }

    /**
     * Called whenever player drops this item.
     */
    @Event
    public void onItemDrop(Player player, PlayerDropItemEvent ev) {
    }

    /**
     * Called whenever player steps on a pressure plate while holding this item.
     */
    @Event
    public void onPhysicalClick(Player player, PlayerInteractEvent ev) {
    }
}
