package me.hapyl.eterna.module.inventory.menu;

import com.google.common.collect.Maps;
import io.papermc.paper.dialog.Dialog;
import me.hapyl.eterna.EternaColors;
import me.hapyl.eterna.module.annotate.EventLike;
import me.hapyl.eterna.module.inventory.builder.ItemBuilder;
import me.hapyl.eterna.module.inventory.menu.action.PlayerMenuAction;
import me.hapyl.eterna.module.inventory.menu.action.SecurePlayerMenuAction;
import me.hapyl.eterna.module.inventory.menu.pattern.SlotPattern;
import me.hapyl.eterna.module.inventory.menu.pattern.SlotPatternApplier;
import me.hapyl.eterna.module.util.Cooldown;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

/**
 * Represents a {@code container} based menu system, supporting:
 *
 * <ul>
 *     <li>Types:
 *     <ul>
 *         <li>{@link PlayerMenuType#chest(ChestSize)}
 *         <li>{@link PlayerMenuType#dispenser()}
 *         <li>{@link PlayerMenuType#hopper()}
 *     </ul>
 *
 *     <li>Items:
 *     <ul>
 *         <li>{@link PlayerMenu#setItem(int, ItemStack)}
 *         <li>{@link PlayerMenu#setItem(int, ItemStack, PlayerMenuAction)}
 *         <li>{@link PlayerMenu#setItem(int, ItemStack, PlayerMenuAction.Builder)}
 *     </ul>
 *
 *     <li>Player actions:
 *     <ul>
 *         <li>{@link PlayerMenu#setAction(int, PlayerMenuAction)}
 *         <li>{@link PlayerMenu#setAction(int, PlayerMenuAction.Builder)}
 *     </ul>
 * </ul>
 *
 * <p>
 * Note that the design of the click processing expects any click to refresh the {@link PlayerMenu} after each click, which is the
 * intended way of using this module.
 * </p>
 *
 * <p>
 * If this pattern isn't followed, it cannot be guaranteed that the module works as intended.
 * </p>
 *
 * <h1>Do not use {@link PlayerMenu} for processing sensitive data!</h1>
 *
 * <p>
 * Inventory-based menus are prone to de-syncs and dupes; Even though {@link SecurePlayerMenuAction} provides more secure clicks, it is not
 * guaranteed that items won't be duped.
 * </p>
 *
 * <p>
 * For input validation, prefer Paper's {@link Dialog}, as it's more trustworthy than a random menu implementation of a random guy on the internet.
 * </p>
 *
 * @see PlayerPageMenu
 * @see PlayerMenuAction#builder()
 * @see SecurePlayerMenuAction
 */
public abstract class PlayerMenu implements Cooldown {
    
    /**
     * Defines the {@link String} texture used as a "Return" button.
     *
     * @see PlayerMenu#setReturnButton(Component, Function)
     */
    @NotNull
    public static final String ITEM_RETURN_TEXTURE = "bd69e06e5dadfd84e5f3d1c21063f2553b2fa945ee1d4d7152fdc5425bc12a9";
    
    /**
     * Defines the {@link Material} used as a "Close Menu" button.
     *
     * @see PlayerMenu#setCloseButton()
     */
    @NotNull
    public static final Material ITEM_CLOSE_MENU = Material.BARRIER;
    
    @ApiStatus.Internal
    protected static final Map<UUID, PlayerMenu> playerMenus = Maps.newHashMap();
    
    protected final Player player;
    protected final Properties properties;
    protected final Inventory inventory;
    
    private final PlayerMenuTitle title;
    private final PlayerMenuType guiType;
    
    private final Map<Integer, PlayerMenuAction> actions;
    
    /**
     * Creates a new {@link PlayerMenu} for the given {@link Player}.
     *
     * @param player  - The player owning this menu.
     * @param title   - The title of this menu.
     * @param guiType - The type of this menu.
     */
    public PlayerMenu(@NotNull Player player, @NotNull PlayerMenuTitle title, @NotNull PlayerMenuType guiType) {
        this.player = player;
        this.title = title;
        this.guiType = guiType;
        this.inventory = guiType.createInventory(title.asComponent());
        this.properties = new Properties();
        this.actions = Maps.newHashMap();
    }
    
    /**
     * Called whenever {@link #openMenu()} is invoked.
     *
     * <p>
     * This happens after the {@link Inventory} has been cleaned, removing all previous {@link ItemStack} and {@link PlayerMenuAction}.
     * </p>
     */
    @ApiStatus.OverrideOnly
    public abstract void onOpen();
    
    /**
     * Gets whether this {@link PlayerMenu} can be opened for the given {@link Player}.
     *
     * @param player - The player to check.
     * @return {@code true} if this menu can be opened for the player; {@code false} otherwise.
     */
    public boolean canOpen(@NotNull Player player) {
        return true;
    }
    
    /**
     * Called whenever the {@link Player} naturally closes this {@link PlayerMenu}.
     *
     * <p>
     * Even though calling {@link #openMenu()} technically closes any open inventory, this method is specifically <b>not</b>
     * called for those cases, and only called when the {@link Player} themselves closes the inventory.
     * </p>
     */
    @EventLike
    public void onClose() {
    }
    
    /**
     * Called whenever the {@link Player} clicks at the given {@code slot}.
     *
     * @param actionType  - The click type used.
     * @param slot        - The clicked slot.
     * @param hotbarIndex - The hotbar index, or {@code -1} if not a hotbar click.
     */
    @OverridingMethodsMustInvokeSuper
    public void onClick(@NotNull ClickType actionType, int slot, int hotbarIndex) {
        final PlayerMenuAction action = actions.get(slot);
        
        // If there isn't any actions on that slot, return
        if (action == null) {
            return;
        }
        
        // Check for `respectItemClick`, which means there must be an item on the slot
        // in order for the action to be executed
        if (this.properties.respectItemOnClicks && getItem(slot) == null) {
            return;
        }
        
        // Check for cooldown
        if (this.properties.isOnCooldown()) {
            this.player.sendMessage(Component.text("Please slow down...", NamedTextColor.RED));
            return;
        }
        
        // Start cooldown
        this.properties.markCooldown();
        
        action.use(this, player, actionType, slot, hotbarIndex);
    }
    
    /**
     * Gets the {@link Player} owning this {@link PlayerMenu}.
     *
     * @return the player owning this menu.
     */
    @NotNull
    public Player player() {
        return player;
    }
    
    /**
     * Gets the {@link PlayerMenuType}.
     *
     * @return the menu type.
     */
    @NotNull
    public PlayerMenuType getType() {
        return guiType;
    }
    
    /**
     * Gets the {@link PlayerMenu} size, or the total number of slots.
     *
     * @return the menu size, or the total number of slots.
     */
    public int getMenuSize() {
        return inventory.getSize();
    }
    
    /**
     * Gets the {@link PlayerMenu} width, or the numbers of columns.
     *
     * @return the menu width, or the numbers of columns.
     */
    public int getMenuWidth() {
        return guiType.width();
    }
    
    /**
     * Gets the {@link PlayerMenu} height, or the number of rows.
     *
     * @return the menu height, or the number of rows.
     */
    public int getMenuHeight() {
        return guiType.height();
    }
    
    /**
     * Clears every {@link ItemStack} present.
     *
     * <p><b>This only clears the {@link ItemStack}, {@link PlayerMenuAction} will not be cleared.</b></p>
     */
    public final void clearItems() {
        inventory.clear();
    }
    
    /**
     * Clears {@link ItemStack} on the given {@code slot}.
     *
     * <p><b>This only clears the {@link ItemStack}, {@link PlayerMenuAction} will not be cleared.</b></p>
     *
     * @param slot - The slot to clear the item at.
     */
    public final void clearItem(int slot) {
        inventory.setItem(Math.clamp(slot, 0, inventory.getSize() - 1), null);
    }
    
    /**
     * Clears every {@link PlayerMenuAction} present.
     *
     * <p><b>This only clears the {@link PlayerMenuAction}, {@link ItemStack} will not be cleared.</b></p>
     */
    public final void clearActions() {
        actions.clear();
    }
    
    /**
     * Clears {@link PlayerMenuAction} on the given {@code slot}.
     *
     * <p><b>This only clears the {@link PlayerMenuAction}, {@link ItemStack} will not be cleared.</b></p>
     *
     * @param slot - The slot to clear the action at.
     */
    public final void clearAction(int slot) {
        actions.remove(slot);
    }
    
    /**
     * Clears the menu completely, removing all {@link ItemStack} and {@link PlayerMenuAction}.
     */
    public final void clear() {
        this.clearItems();
        this.clearActions();
    }
    
    /**
     * Clears both {@link ItemStack} and {@link PlayerMenuAction} at the given {@code slot}.
     *
     * @param slot - The slot to clear the item and action at.
     */
    public final void clear(int slot) {
        this.clearItem(slot);
        this.clearAction(slot);
    }
    
    /**
     * Gets the {@link ItemStack} at the given {@code slot}.
     *
     * @param slot - The slot to retrieve the item at.
     * @return the item stack at the given {@code slot}.
     */
    @Nullable
    public ItemStack getItem(int slot) {
        return inventory.getItem(slot);
    }
    
    // *-* Set Item *-* //
    
    /**
     * Sets the given {@link ItemStack} at the given {@code slot}.
     *
     * @param slot - The slot to set the item at.
     * @param item - The item to set.
     */
    public void setItem(int slot, @NotNull ItemStack item) {
        setItem0(slot, item, null);
    }
    
    /**
     * Sets the given {@link ItemStack} and {@link PlayerMenuAction} at the given {@code slot}.
     *
     * @param slot   - The slot to set the item and action at.
     * @param item   - The item to set.
     * @param action - The action to set.
     */
    public void setItem(int slot, @NotNull ItemStack item, @NotNull PlayerMenuAction action) {
        setItem0(slot, item, action);
    }
    
    /**
     * Sets the given {@link ItemStack} and {@link PlayerMenuAction.Builder} at the given {@code slot}.
     *
     * @param slot    - The slot to set the item and action at.
     * @param item    - The item to set.
     * @param builder - The action builder.
     * @throws IllegalStateException if the builder doesn't have at least one action.
     */
    public void setItem(int slot, @NotNull ItemStack item, @NotNull PlayerMenuAction.Builder builder) {
        setItem0(slot, item, builder.build());
    }
    
    // *-* Set Action *-* //
    
    /**
     * Sets the given {@link PlayerMenuAction} at the given {@code slot}.
     *
     * @param slot   - The slot to set the action at.
     * @param action - The action to set.
     */
    public void setAction(int slot, @NotNull PlayerMenuAction action) {
        setItem0(slot, null, action);
    }
    
    /**
     * Sets the given {@link PlayerMenuAction} at the given {@code slot}.
     *
     * @param slot    - The slot to set the action at.
     * @param builder - The action builder to set.
     * @throws IllegalStateException if the builder doesn't have at least one action.
     */
    public void setAction(int slot, @NotNull PlayerMenuAction.Builder builder) {
        setItem0(slot, null, builder.build());
    }
    
    // *-* Return Button *-* //
    
    /**
     * Gets the {@code slot} where to put the {@code "Return To Menu"} button.
     *
     * @return the {@code slot} where to put the {@code "Return To Menu"} button.
     */
    public int getReturnButtonSlot() {
        return getMenuSize() - 5;
    }
    
    /**
     * Sets the {@code "Return To Menu"} button at the {@link #getReturnButtonSlot()}.
     *
     * @param returnTo - The name of the menu.
     * @param menuTo   - The menu to return to upon clicking the button.
     */
    public void setReturnButton(@Nullable Component returnTo, @NotNull Function<Player, PlayerMenu> menuTo) {
        final ItemBuilder builder = ItemBuilder.playerHead(ITEM_RETURN_TEXTURE).setName(Component.text("Return"));
        
        if (returnTo != null) {
            builder.addLore(Component.text("To ").append(returnTo));
        }
        
        setItem0(
                getReturnButtonSlot(),
                builder.asIcon(),
                PlayerMenuAction.of(player -> menuTo.apply(player).openMenu())
        );
    }
    
    /**
     * Gets the {@code slot} where to put the {@code "Close Menu"} button.
     *
     * @return the {@code slot} where to put the {@code "Close Menu"} button.
     */
    public int getCloseButtonSlot() {
        return getMenuSize() - 5;
    }
    
    /**
     * Sets the {@code "Close Menu"} button at {@link #getCloseButtonSlot()} that closes this {@link PlayerMenu} upon clicking on it.
     */
    public void setCloseButton() {
        setItem0(
                getReturnButtonSlot(),
                new ItemBuilder(ITEM_CLOSE_MENU).setName(Component.text("Close Menu", NamedTextColor.RED)).asItemStack(),
                PlayerMenuAction.of(HumanEntity::closeInventory)
        );
    }
    
    // *-* Fill Operations *-* //
    
    /**
     * Fills the given {@link ItemStack} at the given range.
     *
     * @param from - The start index to fill at (inclusive).
     * @param to   - The end index to fill at (inclusive).
     * @param item - The item to fill with.
     */
    public void fillItem(int from, int to, @NotNull ItemStack item) {
        fillItem(from, to, item, null);
    }
    
    /**
     * Fills the given {@link ItemStack} and {@link PlayerMenuAction} at the given range.
     *
     * @param from   - The start index to fill at (inclusive).
     * @param to     - The end index to fill at (inclusive).
     * @param item   - The item to fill with.
     * @param action - The action to fill with.
     */
    public void fillItem(int from, int to, @NotNull ItemStack item, @Nullable PlayerMenuAction action) {
        for (int i = from; i <= to; i++) {
            setItem0(i, item, action);
        }
    }
    
    /**
     * Fills the given {@code row} with the given {@link ItemStack}.
     *
     * @param row  - The row to fill.
     * @param item - The item to fill with.
     */
    public void fillRow(int row, @NotNull ItemStack item) {
        fillRow(row, item, null);
    }
    
    /**
     * Fills the given {@code row} with the given {@link ItemStack} and {@link PlayerMenuAction}.
     *
     * @param row    - The row to fill.
     * @param item   - The item to will with.
     * @param action - The action to will with.
     */
    public void fillRow(int row, @NotNull ItemStack item, @Nullable PlayerMenuAction action) {
        final int inventorySize = getMenuSize();
        final int inventoryWidth = getMenuWidth();
        
        // Clamp row between `0` - `inventorySize / inventoryWidth`
        row = Math.clamp(row, 0, inventorySize / inventoryWidth);
        
        for (int i = 0; i < inventorySize; ++i) {
            if (i / inventoryWidth == row) {
                setItem0(i, item, action);
            }
        }
    }
    
    /**
     * Fills the given {@code column} with the given {@link ItemStack}.
     *
     * @param column - The column to fill.
     * @param item   - The item to fill with.
     */
    public void fillColumn(int column, @NotNull ItemStack item) {
        fillColumn(column, item, null);
    }
    
    /**
     * Fills the given {@code column} with the given {@link ItemStack} and {@link PlayerMenuAction}.
     *
     * @param column - The column to fill.
     * @param item   - The item to will with.
     * @param action - The action to will with.
     */
    public void fillColumn(int column, @NotNull ItemStack item, @Nullable PlayerMenuAction action) {
        final int inventorySize = getMenuSize();
        final int inventoryWidth = getMenuWidth();
        
        // Clamp column between `0` - `inventoryWidth - 1`
        column = Math.clamp(column, 0, inventoryWidth - 1);
        
        for (int i = 0; i < inventorySize; ++i) {
            if (i % inventoryWidth == column) {
                setItem0(i, item, action);
            }
        }
    }
    
    /**
     * Fills the outer slots of this {@link PlayerMenu}.
     *
     * @param item - The item to fill with.
     */
    public void fillOuter(@NotNull ItemStack item) {
        fillOuter(item, null);
    }
    
    /**
     * Fills the outer slots of this {@link PlayerMenu}.
     *
     * @param item   - The item to fill with.
     * @param action - The action to fill with.
     */
    public void fillOuter(@NotNull ItemStack item, @Nullable PlayerMenuAction action) {
        final int inventoryHeight = getMenuHeight();
        final int inventoryWidth = getMenuWidth();
        
        for (int i = 0; i < getMenuSize(); i++) {
            final int row = i / inventoryWidth;
            final int column = i % inventoryWidth;
            
            if (row == 0 || row == inventoryHeight - 1 || column == 0 || column == inventoryWidth - 1) {
                setItem0(i, item, action);
            }
        }
    }
    
    /**
     * Fills the inner slots of this {@link PlayerMenu}.
     *
     * @param item - The item to fill with.
     */
    public void fillInner(@NotNull ItemStack item) {
        fillInner(item, null);
    }
    
    /**
     * Fills the inner slots of this {@link PlayerMenu}.
     *
     * @param item   - The item to fill with.
     * @param action - The action to fill with.
     */
    public void fillInner(@NotNull ItemStack item, @Nullable PlayerMenuAction action) {
        final int inventoryHeight = getMenuHeight();
        final int inventoryWidth = getMenuWidth();
        
        for (int i = 0; i < getMenuSize(); i++) {
            final int row = i / inventoryWidth;
            final int column = i % inventoryWidth;
            
            if (row != 0 || row != inventoryHeight - 1 || column != 0 || column != inventoryWidth - 1) {
                setItem0(i, item, action);
            }
        }
    }
    
    // *-* Properties *-* //
    
    /**
     * Sets whether this {@link PlayerMenu} cancels dragging items within it.
     *
     * <p><b>Note that this only refers to cancelling the click event, actions will be executed regardless.</b></p>
     *
     * @param cancelDragging - {@code true} to cancel dragging; {@code false} otherwise.
     */
    public void setCancelDragging(boolean cancelDragging) {
        this.properties.cancelDragging = cancelDragging;
    }
    
    /**
     * Sets whether this {@link PlayerMenu} cancels shift-clicking items.
     *
     * <p><b>Note that this only refers to cancelling the click event, actions will be executed regardless.</b></p>
     *
     * @param cancelShiftClick - {@code true} to cancel shift-clicking; {@code false} otherwise.
     */
    public void setCancelShiftClick(boolean cancelShiftClick) {
        this.properties.cancelShiftClick = cancelShiftClick;
    }
    
    /**
     * Sets whether this {@link PlayerMenu} cancels number clicks.
     *
     * <p><b>Note that this only refers to cancelling the click event, actions will be executed regardless.</b></p>
     *
     * @param allowNumberClick - {@code true} to cancel number clicks; {@code false} otherwise.
     */
    public void setCancelNumberClick(boolean allowNumberClick) {
        this.properties.cancelNumberClick = allowNumberClick;
    }
    
    /**
     * Sets the {@link CancelType} of this {@link PlayerMenu}.
     *
     * <p><b>Note that this only refers to cancelling the click event, actions will be executed regardless.</b></p>
     *
     * @param cancelType - The new cancel type.
     * @see CancelType
     */
    public void setCancelType(@NotNull CancelType cancelType) {
        this.properties.cancelType = cancelType;
    }
    
    /**
     * Gets the cooldown of this {@link PlayerMenu}, in ticks.
     *
     * @return the cooldown of this menu, in ticks.
     */
    @Override
    public int getCooldown() {
        return this.properties.cooldown;
    }
    
    /**
     * Sets the cooldown of this {@link PlayerMenu}, in ticks.
     *
     * @param cooldown - The cooldown to set.
     */
    @Override
    public void setCooldown(int cooldown) {
        this.properties.cooldown = cooldown;
    }
    
    /**
     * Creates a new {@link SlotPatternApplier} for this {@link PlayerMenu}.
     *
     * @param pattern - The pattern to create the applier for.
     * @return a new {@link SlotPatternApplier}.
     */
    @NotNull
    public final SlotPatternApplier newSlotPatternApplier(@NotNull SlotPattern pattern) {
        return SlotPatternApplier.of(this, pattern);
    }
    
    /**
     * Creates a new {@link SlotPatternApplier} for this {@link PlayerMenu}.
     *
     * @param pattern - The pattern to create the applier for.
     * @param from    - The starting size, inclusive.
     * @return a new {@link SlotPatternApplier}.
     */
    @NotNull
    public final SlotPatternApplier newSlotPatternApplier(@NotNull SlotPattern pattern, @NotNull ChestSize from) {
        return SlotPatternApplier.of(this, pattern, from);
    }
    
    /**
     * Creates a new {@link SlotPatternApplier} for this {@link PlayerMenu}.
     *
     * @param pattern - The pattern to create the applier for.
     * @param from    - The starting size, inclusive.
     * @param to      - The ending size, inclusive.
     * @return a new {@link SlotPatternApplier}.
     */
    @NotNull
    public final SlotPatternApplier newSlotPatternApplier(@NotNull SlotPattern pattern, @NotNull ChestSize from, @NotNull ChestSize to) {
        return SlotPatternApplier.of(this, pattern, from, to);
    }
    
    /**
     * Gets the name of this {@link PlayerMenu}.
     *
     * @return the name of this menu.
     */
    @NotNull
    public final PlayerMenuTitle getTitle() {
        return this.title;
    }
    
    /**
     * Gets the underlying {@link Inventory} of this {@link PlayerMenu}.
     *
     * @return the underlying inventory of this menu.
     */
    @NotNull
    public final Inventory getInventory() {
        return inventory;
    }
    
    /**
     * Opens this {@link PlayerMenu}.
     */
    @OverridingMethodsMustInvokeSuper
    public void openMenu() {
        if (this instanceof DisabledPlayerMenu disabledPlayerMenu) {
            player.sendMessage(disabledPlayerMenu.disableReason());
            return;
        }
        else if (!canOpen(player)) {
            player.sendMessage(Component.text("You're not allowed to open this menu!", EternaColors.RED));
            return;
        }
        
        // Menus are self-clearing after each update
        this.clearItems();
        this.clearActions();
        
        // Call `onUpdate`
        this.onOpen();
        
        // Unregister before opening the inventory so we don't call `onClose()`
        final UUID playerUniqueId = player.getUniqueId();
        
        playerMenus.remove(playerUniqueId);
        
        // And finally open the inventory
        this.player.openInventory(inventory);
        
        // Register after we have opened the inventory
        playerMenus.put(playerUniqueId, this);
    }
    
    /**
     * Closes this {@link PlayerMenu}.
     */
    public void closeMenu() {
        player.closeInventory();
    }
    
    /**
     * Internal method to set {@link ItemStack} and {@link PlayerMenuAction}.
     */
    @ApiStatus.Internal
    public final void setItem0(int slot, @Nullable ItemStack item, @Nullable PlayerMenuAction action) {
        final int inventorySize = getMenuSize();
        
        // Silently fail instead of throwing an exception
        if (slot < 0 || slot >= inventorySize) {
            return;
        }
        
        // Only set item if it's not null to not override the existing item
        if (item != null) {
            this.inventory.setItem(slot, item);
        }
        
        // Only set action if it's not null to not override the existing action
        if (action != null) {
            this.actions.put(slot, action);
        }
    }
    
    /**
     * Compares the given {@link Inventory} to the underlying inventory.
     *
     * @param inventory - The inventory to compare.
     * @return {@code true} if the inventories match, {@code false} otherwise.
     */
    @ApiStatus.Internal
    public final boolean compareInventory(@NotNull Inventory inventory) {
        return this.inventory.equals(inventory);
    }
    
    /**
     * Gets the currently opened {@link PlayerMenu} for the given {@link Player}.
     *
     * @param player - The player for whom to retrieve the menu.
     * @return the currently opened menu for the given player wrapping in an optional.
     */
    @NotNull
    public static Optional<PlayerMenu> getPlayerMenu(@NotNull Player player) {
        return Optional.ofNullable(playerMenus.get(player.getUniqueId()));
    }
    
    /**
     * Represents internal menu properties.
     */
    @ApiStatus.Internal
    public static class Properties {
        protected boolean cancelDragging;
        protected boolean cancelShiftClick;
        protected boolean cancelNumberClick;
        
        protected boolean respectItemOnClicks;
        
        protected CancelType cancelType;
        
        protected int cooldown;
        private long lastClickedAt;
        
        Properties() {
            this.cancelDragging = false;
            this.cancelShiftClick = false;
            this.cancelNumberClick = false;
            this.respectItemOnClicks = false;
            this.cancelType = CancelType.EITHER;
            this.cooldown = 0;
        }
        
        protected boolean isOnCooldown() {
            return this.cooldown > 0 && System.currentTimeMillis() - this.lastClickedAt < (this.cooldown * 50L);
        }
        
        protected void markCooldown() {
            if (this.cooldown > 0) {
                this.lastClickedAt = System.currentTimeMillis();
            }
        }
    }
    
}
