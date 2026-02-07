package me.hapyl.eterna.module.inventory.builder;

import me.hapyl.eterna.module.annotate.EventLike;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an event listener for an {@link ItemBuilder}.
 */
public class ItemEventListener {
    
    /**
     * Called whenever a {@link Player} clicks with the item.
     *
     * @param player - The player who clicked.
     * @param ev     - The handling event.
     */
    @EventLike
    public void onClick(@NotNull Player player, @NotNull PlayerInteractEvent ev) {
    }
    
    /**
     * Called whenever a {@link Player} clicks specifically with a {@link Action#RIGHT_CLICK_AIR} or {@link Action#RIGHT_CLICK_BLOCK}.
     *
     * @param player - The player who right-clicked.
     * @param ev     - The handling event.
     */
    @EventLike
    public void onRightClick(@NotNull Player player, @NotNull PlayerInteractEvent ev) {
    }
    
    /**
     * Called whenever a {@link Player} clicks specifically with a {@link Action#LEFT_CLICK_AIR} or {@link Action#LEFT_CLICK_BLOCK}.
     *
     * @param player - The player who left-clicked.
     * @param ev     - The handling event.
     */
    @EventLike
    public void onLeftClick(@NotNull Player player, @NotNull PlayerInteractEvent ev) {
    }
    
    /**
     * Called whenever a {@link Player} places a block while holding the matching item in their main hand.
     *
     * @param player - The player who placed the block.
     * @param ev     - The handling event.
     */
    @EventLike
    public void onBlockPlace(@NotNull Player player, @NotNull BlockPlaceEvent ev) {
    }
    
    /**
     * Called whenever a {@link Player} breaks a block while holding the matching item in their main hand.
     *
     * @param player - The player who broke the block.
     * @param ev     - The handling event.
     */
    @EventLike
    public void onBlockBreak(@NotNull Player player, @NotNull BlockBreakEvent ev) {
    }
    
    /**
     * Called whenever a {@link Player} drops the item.
     *
     * @param player - The player who dropped the item.
     * @param ev     - The handling event.
     */
    @EventLike
    public void onItemDrop(@NotNull Player player, @NotNull PlayerDropItemEvent ev) {
    }
    
    /**
     * Called whenever a {@link Player} performs a physical action while holding the matching item in their main hand.
     * <p>Physical action refers to stepping on pressure plate, triggering a tripwire, etc.</p>
     *
     * @param player - The player who performed a physical action.F
     * @param ev     - The handling event.
     */
    @EventLike
    public void onPhysicalClick(@NotNull Player player, @NotNull PlayerInteractEvent ev) {
    }
}
