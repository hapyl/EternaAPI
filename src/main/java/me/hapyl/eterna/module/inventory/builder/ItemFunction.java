package me.hapyl.eterna.module.inventory.builder;

import com.google.common.collect.Sets;
import me.hapyl.eterna.module.annotate.SelfReturn;
import me.hapyl.eterna.module.util.Buildable;
import me.hapyl.eterna.module.util.DescriptivePredicate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Represents an {@link ItemFunction} that may be applied to an {@link ItemBuilder}.
 */
public abstract class ItemFunction implements Cloneable {
    
    private static final Set<Action> defaultActions = Set.of(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK);
    
    @NotNull protected Set<Action> actions;
    
    protected boolean allowInventoryClick;
    protected boolean cancelClick;
    
    protected int cooldown;
    
    protected DescriptivePredicate<Player> predicate;
    
    private ItemFunction() {
        this.actions = Sets.newHashSet();
        this.allowInventoryClick = false;
        this.cancelClick = true;
        this.cooldown = 0;
    }
    
    /**
     * Executes this {@link ItemFunction}.
     *
     * @param player - The player who executed the function.
     */
    public abstract void execute(@NotNull Player player);
    
    /**
     * Gets whether inventory click is allowed.
     *
     * @return {@code true} if inventory click is allowed; {@code false} otherwise.
     */
    public boolean isAllowInventoryClick() {
        return allowInventoryClick;
    }
    
    /**
     * Gets whether click event should be cancelled.
     *
     * @return {@code true} if click event should be cancelled, {@code false} otherwise.
     */
    public boolean isCancelClick() {
        return cancelClick;
    }
    
    /**
     * Gets the {@link ItemFunction} cooldown.
     *
     * @return the function cooldown.
     */
    public int getCooldown() {
        return cooldown;
    }
    
    /**
     * Gets the {@link ItemFunction} predicate.
     *
     * @return the function predicate.
     */
    @Nullable
    public DescriptivePredicate<Player> predicate() {
        return predicate;
    }
    
    /**
     * Clones this {@link ItemFunction}.
     *
     * @return a cloned function.
     */
    @Override
    public ItemFunction clone() {
        try {
            final ItemFunction clone = (ItemFunction) super.clone();
            clone.actions = Set.copyOf(this.actions);
            clone.predicate = DescriptivePredicate.copyOfNullable(this.predicate);
            
            return clone;
        }
        catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
    
    @ApiStatus.Internal
    final void execute0(@NotNull Player player, @NotNull ItemStack item) {
        // Check for the cooldown on the item
        if (player.hasCooldown(item)) {
            return;
        }
        
        // Check for predicate before cooldown
        if (predicate != null && !predicate.test(player)) {
            player.sendMessage(Component.text(predicate.reason(), NamedTextColor.RED));
            return;
        }
        
        this.execute(player);
        
        // Start cooldown
        if (cooldown > 0) {
            player.setCooldown(item, cooldown);
        }
    }
    
    /**
     * Creates new {@link Builder} for {@link ItemFunction}.
     *
     * @param clickAction - The action to perform.
     * @return a new builder.
     */
    @NotNull
    public static Builder builder(@NotNull Consumer<Player> clickAction) {
        return new Builder(new ItemFunction() {
            @Override
            public void execute(@NotNull Player player) {
                clickAction.accept(player);
            }
        });
    }
    
    /**
     * Represents a {@link Builder} for {@link ItemFunction}.
     */
    public static class Builder implements Buildable<ItemFunction> {
        
        private final ItemFunction function;
        
        private Builder(@NotNull ItemFunction function) {
            this.function = function;
        }
        
        /**
         * Adds the given {@link Action} to supported actions.
         *
         * @param action - The action to add.
         */
        @SelfReturn
        public Builder accepts(@NotNull Action action) {
            this.function.actions.add(action);
            return this;
        }
        
        /**
         * Adds the given {@link Action} to supported actions.
         *
         * @param actions - The actions to add.
         */
        @SelfReturn
        public Builder accepts(@NotNull Collection<Action> actions) {
            this.function.actions.addAll(actions);
            return this;
        }
        
        /**
         * Sets whether click event should be cancelled.
         *
         * @param cancelClick - {@code true} to cancel the event, {@code false} otherwise.
         */
        @SelfReturn
        public Builder cancelClicks(boolean cancelClick) {
            this.function.cancelClick = cancelClick;
            return this;
        }
        
        /**
         * Sets whether to allow the function to execute from within an inventory.
         *
         * @param allowInventoryClick - {@code true} to allow, {@code false} otherwise.
         */
        @SelfReturn
        public Builder allowInventoryClick(boolean allowInventoryClick) {
            this.function.allowInventoryClick = allowInventoryClick;
            return this;
        }
        
        /**
         * Sets the cooldown of the function, in ticks.
         * <p>Note that cooldowns are per-function, but they use Minecraft cooldown system, meaning each function starts its own cooldown, but cannot be executed unless any cooldown is over.</p>
         *
         * @param cooldown - The cooldown to set, in ticks.
         */
        @SelfReturn
        public Builder cooldown(int cooldown) {
            this.function.cooldown = cooldown;
            return this;
        }
        
        /**
         * Sets the predicate of this function.
         * <p>The function will not be executed and the {@link DescriptivePredicate#reason()} will be sent to a {@link Player} if the prediate fails.</p>
         *
         * @param predicate - The predicate to set.
         */
        @SelfReturn
        public Builder predicate(@NotNull DescriptivePredicate<Player> predicate) {
            this.function.predicate = predicate;
            return this;
        }
        
        /**
         * Builds the {@link ItemFunction}.
         *
         * @return a new {@link ItemFunction}.
         */
        @Override
        @NotNull
        public ItemFunction build() {
            if (function.actions.isEmpty()) {
                function.actions.addAll(defaultActions);
            }
            
            return function;
        }
    }
    
}
