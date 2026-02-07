package me.hapyl.eterna.module.inventory.menu.action;

import me.hapyl.eterna.module.inventory.menu.PlayerMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a more secure version of {@link PlayerMenuAction} that is less prone to cheats and de-syncs.
 *
 * <p>
 * Using {@link SecurePlayerMenuAction} does <b>not</b> guarantee full security of the operation. Do not perform transactions via
 * {@link PlayerMenu} as it not meant to be used for it.
 * </p>
 */
public abstract class SecurePlayerMenuAction implements PlayerMenuAction {
    
    private final AtomicInteger useTime;
    
    /**
     * Creates a new {@link SecurePlayerMenuAction}.
     */
    public SecurePlayerMenuAction() {
        this.useTime = new AtomicInteger();
    }
    
    /**
     * Executes whenever this {@link SecurePlayerMenuAction} used for the first time.
     *
     * @param player - The player who clicked.
     */
    public abstract void useSecurely(@NotNull Player player);
    
    /**
     * Executes whenever this {@link SecurePlayerMenuAction} used not for the first time.
     *
     * <p>
     * The default behaviour sends an error message for the first click, and closes the {@link PlayerMenu} for the following clicks.
     * </p>
     *
     * @param player  - The player who clicked.
     * @param useTime - The used time, always {@code > 0}.
     */
    public void useInsecurely(@NotNull Player player, @Range(from = 1, to = Integer.MAX_VALUE) final int useTime) {
        if (useTime == 1) {
            player.sendMessage(Component.text("You're clicking too fast!", NamedTextColor.RED));
        }
        else {
            player.sendMessage(Component.text("The menu you were interacting with was closed due to security reasons.", NamedTextColor.RED));
            player.closeInventory();
        }
    }
    
    @Override
    public final void use(@NotNull PlayerMenu menu, @NotNull Player player, @NotNull ClickType clickType, int slot, int hotbarNumber) {
        final int useTime = this.useTime.getAndIncrement();
        
        if (useTime == 0) {
            this.useSecurely(player);
        }
        else {
            this.useInsecurely(player, useTime);
        }
    }
    
}
