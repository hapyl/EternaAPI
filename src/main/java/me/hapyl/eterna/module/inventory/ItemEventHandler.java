package me.hapyl.eterna.module.inventory;

import me.hapyl.eterna.module.inventory.item.Event;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import javax.annotation.Nonnull;

public class ItemEventHandler {

    /**
     * Called whenever a player clicked with this item.
     */
    @Event
    public void onClick(@Nonnull Player player, @Nonnull PlayerInteractEvent ev) {
    }

    /**
     * Called whenever a player right-clicked with this item.
     */
    @Event
    public void onRightClick(@Nonnull Player player, @Nonnull PlayerInteractEvent ev) {
    }

    /**
     * Called whenever a player left-clicked with this item.
     */
    @Event
    public void onLeftClick(@Nonnull Player player, @Nonnull PlayerInteractEvent ev) {
    }

    /**
     * Called whenever a player created a block while holding this item.
     */
    @Event
    public void onBlockPlace(@Nonnull Player player, @Nonnull BlockPlaceEvent ev) {
    }

    /**
     * Called whenever a player breaks a block while holding this item.
     */
    @Event
    public void onBlockBreak(@Nonnull Player player, @Nonnull BlockBreakEvent ev) {
    }

    /**
     * Called whenever a player drops this item.
     */
    @Event
    public void onItemDrop(@Nonnull Player player, @Nonnull PlayerDropItemEvent ev) {
    }

    /**
     * Called whenever a player steps on a pressure plate while holding this item.
     */
    @Event
    public void onPhysicalClick(@Nonnull Player player, @Nonnull PlayerInteractEvent ev) {
    }
}
