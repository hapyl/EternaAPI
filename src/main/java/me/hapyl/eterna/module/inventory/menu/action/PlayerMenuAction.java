package me.hapyl.eterna.module.inventory.menu.action;

import me.hapyl.eterna.module.annotate.SelfReturn;
import me.hapyl.eterna.module.inventory.menu.PlayerMenu;
import me.hapyl.eterna.module.player.PlayerAction;
import me.hapyl.eterna.module.util.Buildable;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Represents an action that can be performed by a {@link Player} in a {@link PlayerMenu}.
 *
 * <p>
 * Note that {@link PlayerMenuAction} is designed to only exist within a single instance of {@link PlayerMenu} and are re-created each {@link PlayerMenu#onOpen()}.
 * </p>
 *
 * <p>
 * But even so, these actions are prone to being exploited via cheats or de-syncs. If you want a more secure version of {@link PlayerMenuAction}, use {@link #ofSecure(Consumer)}.
 * </p>
 *
 * @see SecurePlayerMenuAction
 * @see HotbarPlayerMenuAction
 * @see #builder()
 */
@FunctionalInterface
public interface PlayerMenuAction {
    
    /**
     * Performs this action for the given {@link Player}.
     *
     * @param menu         - The menu this action was performed in.
     * @param player       - The player for whom to perform the action.
     * @param clickType    - The click type used for the action.
     * @param slot         - The raw slot the player clicked at.
     * @param hotbarNumber - The hotbar number clicked, or {@code -1} if not a hotbar click.
     */
    void use(@NotNull PlayerMenu menu, @NotNull Player player, @NotNull ClickType clickType, int slot, int hotbarNumber);
    
    /**
     * A static factory method for creating a simple {@link PlayerMenuAction}, which performs the given {@link PlayerAction} for any {@link ClickType} as
     * long as the {@link Player} clicked at the slot.
     *
     * @param action - The consumer to accept.
     * @return a new player action.
     */
    @NotNull
    static PlayerMenuAction of(@NotNull PlayerAction action) {
        return (menu, player, actionType, slot, numberClick) -> action.use(player);
    }
    
    /**
     * A static factory method for creating {@link SecurePlayerMenuAction}.
     *
     * @param action - The consumer to accept.
     * @return a new secure player action.
     * @see SecurePlayerMenuAction
     */
    @NotNull
    static PlayerMenuAction ofSecure(@NotNull PlayerAction action) {
        return new SecurePlayerMenuAction() {
            @Override
            public void useSecurely(@NotNull Player player) {
                action.use(player);
            }
        };
    }
    
    /**
     * A static factory method for creating {@link HotbarPlayerMenuAction}.
     *
     * @param consumer - The consumer to accept.
     * @return a new hotbar player action.
     * @see HotbarPlayerMenuAction
     */
    @NotNull
    static PlayerMenuAction ofHotbar(@NotNull BiConsumer<Player, Integer> consumer) {
        return new HotbarPlayerMenuAction() {
            @Override
            public void use(@NotNull Player player, @Range(from = 0, to = 8) int hotbarNumber) {
                consumer.accept(player, hotbarNumber);
            }
        };
    }
    
    /**
     * A static factory method for creating {@link PlayerMenuAction} that will update the {@link PlayerMenu} by re-opening it.
     *
     * @return a new player action.
     */
    @NotNull
    static PlayerMenuAction ofMenuUpdate() {
        return (menu, player, clickType, slot, hotbarNumber) -> menu.openMenu();
    }
    
    /**
     * Creates a new {@link Builder} for {@link PlayerMenuAction}, allowing per-click actions.
     *
     * @return a new builder.
     */
    @NotNull
    static Builder builder() {
        return new Builder();
    }
    
    /**
     * Represents a {@link Builder} for {@link PlayerMenuAction}.
     */
    final class Builder implements Buildable<PlayerMenuAction> {
        private final PlayerMenuActionImpl playerAction;
        
        Builder() {
            this.playerAction = new PlayerMenuActionImpl();
        }
        
        /**
         * Maps the given {@link PlayerAction} to the {@link ClickType#LEFT}.
         *
         * @param action - The action to map.
         */
        @SelfReturn
        public Builder left(@NotNull PlayerAction action) {
            this.playerAction.mapped.put(ClickType.LEFT, action);
            return this;
        }
        
        /**
         * Maps the given {@link PlayerAction} to the {@link ClickType#RIGHT}.
         *
         * @param action - The action to map.
         */
        @SelfReturn
        public Builder right(@NotNull PlayerAction action) {
            this.playerAction.mapped.put(ClickType.RIGHT, action);
            return this;
        }
        
        /**
         * Maps the given {@link PlayerAction} to the {@link ClickType#SHIFT_LEFT}.
         *
         * @param action - The action to map.
         */
        @SelfReturn
        public Builder shiftLeft(@NotNull PlayerAction action) {
            this.playerAction.mapped.put(ClickType.SHIFT_LEFT, action);
            return this;
        }
        
        /**
         * Maps the given {@link PlayerAction} to the {@link ClickType#SHIFT_RIGHT}.
         *
         * @param action - The action to map.
         */
        @SelfReturn
        public Builder shiftRight(@NotNull PlayerAction action) {
            this.playerAction.mapped.put(ClickType.SHIFT_RIGHT, action);
            return this;
        }
        
        /**
         * Maps the given {@link PlayerAction} to the {@link ClickType#MIDDLE}.
         *
         * @param action - The action to map.
         */
        @SelfReturn
        public Builder middle(@NotNull PlayerAction action) {
            this.playerAction.mapped.put(ClickType.MIDDLE, action);
            return this;
        }
        
        /**
         * Maps the given {@link PlayerAction} to the {@link ClickType#DROP}.
         *
         * @param action - The action to map.
         */
        @SelfReturn
        public Builder drop(@NotNull PlayerAction action) {
            this.playerAction.mapped.put(ClickType.DROP, action);
            return this;
        }
        
        /**
         * Maps the given {@link PlayerAction} to the {@link ClickType#CONTROL_DROP}.
         *
         * @param action - The action to map.
         */
        @SelfReturn
        public Builder controlDrop(@NotNull PlayerAction action) {
            this.playerAction.mapped.put(ClickType.CONTROL_DROP, action);
            return this;
        }
        
        /**
         * Maps the given {@link PlayerAction} to the {@link ClickType#SWAP_OFFHAND}.
         *
         * @param action - The action to map.
         */
        @SelfReturn
        public Builder swapOffhand(@NotNull PlayerAction action) {
            this.playerAction.mapped.put(ClickType.SWAP_OFFHAND, action);
            return this;
        }
        
        /**
         * Builds the {@link PlayerMenuAction}.
         *
         * @return a built player menu action.
         */
        @NotNull
        @Override
        public PlayerMenuAction build() {
            return this.playerAction;
        }
    }
    
}
