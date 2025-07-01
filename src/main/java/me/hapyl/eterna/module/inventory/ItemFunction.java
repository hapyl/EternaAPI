package me.hapyl.eterna.module.inventory;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.util.DescriptivePredicate;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.components.UseCooldownComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Represents a function on a {@link ItemStack}, used in {@link ItemBuilder}.
 */
public abstract class ItemFunction implements Cloneable {
    
    public static final Action[] DEFAULT_ACTIONS = { Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK };
    
    private final Set<Action> actions;
    
    private boolean isAllowInventoryClick;
    private boolean isCancelClicks;
    private int cd;
    
    private DescriptivePredicate<Player> predicate;
    
    /**
     * Creates a new {@link ItemFunction} with the given {@link Action}s.
     *
     * @param actions - The actions.
     */
    public ItemFunction(@Nonnull Action... actions) {
        this.actions = Set.of(actions);
        this.isCancelClicks = true;
    }
    
    /**
     * Creates a new {@link ItemFunction} with the {@link #DEFAULT_ACTIONS}.
     */
    public ItemFunction() {
        this(DEFAULT_ACTIONS);
    }
    
    /**
     * Executes this function.
     *
     * @param player - The executor.
     */
    public abstract void execute(@Nonnull Player player);
    
    /**
     * Gets whether clicking this {@link ItemStack} from inside the inventory should {@link #execute(Player)} the functions.
     *
     * @return whether clicking this {@link ItemStack} from inside the inventory should {@link #execute(Player)} the functions.
     */
    public boolean allowInventoryClick() {
        return isAllowInventoryClick;
    }
    
    /**
     * Sets whether clicking this {@link ItemStack} from inventory should {@link #execute(Player)} the functions.
     *
     * @param allowInventoryClick - The new value.
     */
    public ItemFunction allowInventoryClick(boolean allowInventoryClick) {
        isAllowInventoryClick = allowInventoryClick;
        return this;
    }
    
    /**
     * Gets whether clicking this {@link ItemStack} should cancel the handling event, cancelling the click client-side.
     *
     * @return whether clicking this {@link ItemStack} should cancel the handling event, cancelling the click client-side.
     */
    public boolean cancelClicks() {
        return isCancelClicks;
    }
    
    /**
     * Sets whether clicking this {@link ItemStack} should cancel the handling event, cancelling the click client-side.
     *
     * @param cancelClicks - The new value.
     */
    public ItemFunction cancelClicks(boolean cancelClicks) {
        isCancelClicks = cancelClicks;
        return this;
    }
    
    /**
     * Gets the cooldown of this function, in ticks.
     * <p><i>Cooldown is based on vanilla cooldowns, respecting the {@link ItemStack} {@link UseCooldownComponent}.</i></p>
     *
     * @return the cooldown of this function, in ticks.
     * @see ItemBuilder#setCooldown(ItemBuilder.ComponentModifier)
     * @see ItemBuilder#setCooldownGroup(Key)
     */
    public int cooldown() {
        return cd;
    }
    
    /**
     * Gets the cooldown of this function, in ticks.
     * <p><i>Cooldown is based on vanilla cooldowns, respecting the {@link ItemStack} {@link UseCooldownComponent}.</i></p>
     *
     * @see ItemBuilder#setCooldown(ItemBuilder.ComponentModifier)
     * @see ItemBuilder#setCooldownGroup(Key)
     */
    public ItemFunction cooldown(int cd) {
        this.cd = cd;
        return this;
    }
    
    /**
     * Gets the cooldown of this function, in seconds.
     * <p><i>Cooldown is based on vanilla cooldowns, respecting the {@link ItemStack} {@link UseCooldownComponent}.</i></p>
     *
     * @see ItemBuilder#setCooldown(ItemBuilder.ComponentModifier)
     * @see ItemBuilder#setCooldownGroup(Key)
     */
    public ItemFunction cooldownSec(float cdSec) {
        return cooldown((int) (cdSec * 20));
    }
    
    /**
     * Gets whether any of the given {@link Action} will trigger {@link #execute(Player)}.
     *
     * @param actions - The actions to check.
     * @return {@code true} if any of the given {@link Action} will trigger {@link #execute(Player)}, {@code false} otherwise.
     */
    public boolean accepts(@Nonnull Action[] actions) {
        for (Action action : actions) {
            if (this.actions.contains(action)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Gets the {@link DescriptivePredicate} for this function.
     *
     * @return the {@link DescriptivePredicate} for this function.
     */
    @Nullable
    public DescriptivePredicate<Player> predicate() {
        return predicate;
    }
    
    /**
     * Sets the {@link DescriptivePredicate} for this function.
     *
     * @param predicate - The new predicate.
     */
    public ItemFunction predicate(@Nullable DescriptivePredicate<Player> predicate) {
        this.predicate = predicate;
        return this;
    }
    
    @Override
    public ItemFunction clone() {
        try {
            final ItemFunction clone = (ItemFunction) super.clone();
            clone.predicate = DescriptivePredicate.clone(this.predicate);
            
            return clone;
        }
        catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
    
    final void doExecute(@Nonnull Player player, @Nonnull ItemStack item) {
        // Check for cooldown so we don't spam predicate
        if (player.hasCooldown(item)) {
            return;
        }
        
        // Check for predicate before cooldown
        if (predicate != null && !predicate.test(player)) {
            Chat.sendMessage(player, ChatColor.DARK_RED + predicate.reason());
            return;
        }
        
        execute(player);
        
        // Start cooldown
        if (cd > 0) {
            player.setCooldown(item, cd);
        }
    }
    
    @Nonnull
    public static ItemFunction of(@Nonnull Consumer<Player> action) {
        return new ItemFunction() {
            @Override
            public void execute(@Nonnull Player player) {
                action.accept(player);
            }
        };
    }
}
