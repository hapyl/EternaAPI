package me.hapyl.eterna.module.inventory.menu.action;

import me.hapyl.eterna.module.inventory.menu.PlayerMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Represents a {@link PlayerMenuAction} which is called whenever a {@link Player} clicks via {@link ClickType#NUMBER_KEY}.
 */
public abstract class HotbarPlayerMenuAction implements PlayerMenuAction {
    
    /**
     * Performs this action for the given {@link Player}.
     *
     * @param player       - The player for whom to perform the action.
     * @param hotbarNumber - The hotbar number index, from {@code 0} - {@code 8} (inclusive).
     */
    public abstract void use(@NotNull Player player, @Range(from = 0, to = 8) int hotbarNumber);
    
    /**
     * Called whenever the given {@link Player} used non-hotbar click.
     *
     * @param player - The player who used non-hotbar click.
     */
    public void useNonHotbar(@NotNull Player player) {
    }
    
    @Override
    public final void use(@NotNull PlayerMenu menu, @NotNull Player player, @NotNull ClickType clickType, int slot, int hotbarNumber) {
        if (hotbarNumber != -1) {
            this.use(player, hotbarNumber);
        }
        else {
            this.useNonHotbar(player);
        }
    }
}
