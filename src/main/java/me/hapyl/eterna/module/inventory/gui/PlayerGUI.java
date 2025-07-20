package me.hapyl.eterna.module.inventory.gui;

import com.google.common.collect.Maps;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Range;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.*;
import java.util.function.Consumer;

/**
 * Represents a powerful GUI, or menu, per-player builder.
 */
public abstract class PlayerGUI {
    
    public static final char ARROW_FORWARD = '➜';
    public static final Material ITEM_ARROW_BACK = Material.AIR;
    public static final Material ITEM_CLOSE_MENU = Material.BARRIER;
    
    public static final ClickType[] ALLOWED_CLICK_TYPES = {
            ClickType.LEFT,
            ClickType.SHIFT_LEFT,
            ClickType.RIGHT,
            ClickType.SHIFT_RIGHT,
            ClickType.WINDOW_BORDER_LEFT,
            ClickType.WINDOW_BORDER_RIGHT,
            ClickType.MIDDLE,
            ClickType.NUMBER_KEY,
            ClickType.DOUBLE_CLICK,
            ClickType.DROP,
            ClickType.CONTROL_DROP,
            ClickType.CREATIVE
    };
    
    protected static final Map<UUID, PlayerGUI> playerInventory = Maps.newHashMap();
    
    protected final Player player;
    
    private final String name;
    private final int size;
    private final Properties properties;
    private final Map<Integer, ActionList> actions;
    private final Set<Integer> ignoredClicks;
    private final Inventory inventory;
    
    protected boolean reopen;
    
    private CancelType cancelType;
    
    /**
     * Creates a new instance of {@link PlayerGUI}.
     *
     * @param player - The owning player.
     * @param name   - The GUi name.
     * @param rows   - The rows of GUI.
     */
    public PlayerGUI(@Nonnull Player player, @Nonnull String name, @Range(from = 1, to = 6) int rows) {
        this.player = player;
        this.name = name;
        this.size = Math.clamp(rows * 9L, 9, 54);
        this.cancelType = CancelType.EITHER;
        this.actions = new HashMap<>();
        this.ignoredClicks = new HashSet<>();
        this.properties = new Properties();
        this.inventory = Bukkit.createInventory(null, this.size, Chat.color(name));
    }
    
    /**
     * Updates the GUI.
     * <p>This method is called in {@link #openInventory()}, before clearing any existing items and actions, meaning all item and action setting must be done within this method.</p>
     *
     * @implNote Implementation must not call {@link #openInventory()} inside this method!
     */
    public abstract void onUpdate();
    
    /**
     * Gets the player owning this GUI.
     *
     * @return the player owning this GUI.
     */
    @Nonnull
    public Player player() {
        return player;
    }
    
    /**
     * Gets the current cancel type of this GUI.
     *
     * @return the current cancel type of this GUI.
     */
    @Nonnull
    public CancelType getCancelType() {
        return cancelType;
    }
    
    /**
     * Sets the cancel type of this GUI.
     *
     * @param cancelType - The new cancel type.
     */
    public void setCancelType(@Nonnull CancelType cancelType) {
        this.cancelType = cancelType;
    }
    
    /**
     * Gets the properties of this GUI.
     *
     * @return the properties of this GUI.
     * @see Properties
     */
    @Nonnull
    public Properties getProperties() {
        return properties;
    }
    
    /**
     * Gets whether the given slot is ignored.
     * <p>Ignored slots do not respect {@link #cancelType} and never cancel clicks.</p>
     *
     * @param slot - The slot to check.
     * @return {@code true} if the given slot is ignored, {@code false} otherwise.
     */
    public boolean isIgnoredSlot(int slot) {
        return this.ignoredClicks.contains(slot);
    }
    
    /**
     * Marks the given slots as ignored.
     * <p>Ignored slots do not respect {@link #cancelType} and never cancel clicks.</p>
     *
     * @param slots - The slots to mark as ignored.
     */
    public void addIgnoredSlots(int... slots) {
        for (int slot : slots) {
            ignoredClicks.add(slot);
        }
    }
    
    /**
     * Removes the given slots from being ignored.
     * <p>Ignored slots do not respect {@link #cancelType} and never cancel clicks.</p>
     *
     * @param slots - The slots to remove.
     */
    public final void removeIgnoredSlots(int... slots) {
        for (int slot : slots) {
            ignoredClicks.remove(slot);
        }
    }
    
    /**
     * Resets the item at the given slot, setting it to {@link Material#AIR}.
     *
     * @param slot - The slot to reset the item at.
     */
    public void resetItem(int slot) {
        setItem(slot, null);
    }
    
    /**
     * Resets the action at the given slot, clearing it.
     *
     * @param slot - The slot to reset the action at.
     */
    public void resetAction(int slot) {
        actions.remove(slot);
    }
    
    /**
     * Resets the item and the action at the given slot.
     *
     * @param slot - The slot to reset.
     */
    public void reset(int slot) {
        resetItem(slot);
        resetAction(slot);
    }
    
    /**
     * Clears all actions.
     */
    public void clearActions() {
        actions.clear();
    }
    
    /**
     * Clears all items, setting them to {@link Material#AIR}.
     */
    public final void clearItems() {
        for (int i = 0; i < size; i++) {
            inventory.setItem(i, new ItemStack(Material.AIR));
        }
    }
    
    /**
     * Gets the item at the given slot.
     *
     * @param slot - The slot to rewrite the item at.
     * @return the item at the given slot, or {@code null} if there is no item.
     */
    @Nullable
    public ItemStack getItem(int slot) {
        return inventory.getItem(slot);
    }
    
    /**
     * Gets the {@link ActionList} for the given slot.
     *
     * @param slot - The slot to rewrite the action list at.
     * @return the {@link ActionList} for the given slot, or {@code null} if there are no actions at that slot.
     */
    @Nullable
    public ActionList getAction(int slot) {
        return actions.get(slot);
    }
    
    /**
     * Gets an immutable copy of actions.
     *
     * @return an immutable copy of actions.
     */
    @Nonnull
    public Map<Integer, ActionList> getActions() {
        return Map.copyOf(actions);
    }
    
    /**
     * Sets the given item at the given slot with the given action, defaulting to {@link ClickType#LEFT} click.
     *
     * @param slot   - The slot to set the item at.
     * @param item   - The item to set.
     * @param action - The action to map; providing {@code null} action will ignore it, while non-{@code null} actions override the existing ones.
     */
    public void setItem(int slot, @Nullable ItemStack item, @Nullable Consumer<Player> action) {
        setItem(slot, item, action != null ? new ActionList(action) : null);
    }
    
    /**
     * Sets the given item at the given slot with the given {@link StrictAction}.
     *
     * @param slot   - The slot to set the item at.
     * @param item   - The item to set.
     * @param action - The action to map.
     * @see StrictAction
     */
    public void setItem(int slot, @Nonnull ItemStack item, @Nonnull StrictAction action) {
        setItem(slot, item, action.makeAction());
    }
    
    /**
     * Sets the given item at the given slot with the given action, mapped to the given click types.
     *
     * @param slot   - The slot to set the item at.
     * @param item   - The item to set.
     * @param action - The action to map.
     * @param types  - The click types to map the action to.
     * @throws IllegalArgumentException if less than {@code 1} click types provided.
     */
    public void setItem(int slot, @Nullable ItemStack item, @Nonnull Consumer<Player> action, @Nonnull @Range(from = 1, to = Byte.MAX_VALUE) ClickType... types) {
        Validate.isTrue(types.length > 0, "There must be at least one click type!");
        
        final ActionList actionList = actions.getOrDefault(slot, new ActionList());
        
        for (ClickType type : types) {
            actionList.setAction(type, action);
        }
        
        setItem(slot, item, actionList);
    }
    
    /**
     * Sets the given item at the given slot.
     * <p>This will ignore the action at the given slot, keeping it whatever it was, if anything.</p>
     *
     * @param slot - The slot to set the item at.
     * @param item - The item to set.
     */
    public void setItem(int slot, @Nullable ItemStack item) {
        setItem(slot, item, (ActionList) null);
    }
    
    /**
     * Sets the action at the given slot.
     * <p>This will override the existing action at the given slot.</p>
     *
     * @param slot   - The slot to set the action at.
     * @param action - The action to set.
     */
    public void setAction(int slot, @Nonnull Consumer<Player> action) {
        actions.put(slot, new ActionList(action));
    }
    
    /**
     * Performs a bi-action mapping.
     * <p>This is to be considered a 'lazy' helper method.</p>
     *
     * @param slot       - The slot to set the actions at.
     * @param leftClick  - The action to map on {@link ClickType#LEFT}.
     * @param rightClick - The action to map on {@link ClickType#RIGHT}.
     */
    public void setAction(int slot, @Nonnull Consumer<Player> leftClick, @Nonnull Consumer<Player> rightClick) {
        setAction(slot, leftClick, ClickType.LEFT);
        setAction(slot, rightClick, ClickType.RIGHT);
    }
    
    /**
     * Sets the action at the given slot for the given click types.
     * <p>This will append the action to the types, with exception where an action already exists for that type, where it will be overriden.</p>
     *
     * @param slot   - The slot to map the action at.
     * @param action - The action to map.
     * @param types  - The click type to map.
     */
    public void setAction(int slot, @Nonnull Consumer<Player> action, @Nonnull ClickType... types) {
        Validate.isTrue(types.length > 0, "There must be at least one click type!");
        
        final ActionList actionList = actions.getOrDefault(slot, new ActionList());
        
        for (final ClickType type : types) {
            actionList.setAction(type, action);
        }
        
        actions.put(slot, actionList);
    }
    
    /**
     * Sets the "Go Back" {@link #ITEM_ARROW_BACK}, which clicking on opens the given GUI.
     * <p>This is set to {@code size - 5} slot by default.</p>
     *
     * @param guiTo - The GUI to open.
     */
    public void setArrowBack(@Nonnull PlayerGUI guiTo) {
        setArrowBack(size - 5, guiTo);
    }
    
    /**
     * Sets the "Go Back" {@link #ITEM_ARROW_BACK}, which clicking on performs the given action.
     * <p>This is set to {@code size - 5} slot by default.</p>
     *
     * @param to     - The text to display after "To" in lore of the item.
     * @param action - The action to perform upon clicking.
     */
    public void setArrowBack(@Nonnull String to, @Nonnull Consumer<Player> action) {
        setArrowBack(size - 5, to, action);
    }
    
    /**
     * Sets the "Go Back" {@link #ITEM_ARROW_BACK}, which clicking on opens the given GUI at the given slot.
     *
     * @param slot  - Slot to set the item at.
     * @param guiTo - The GUI to open upon clicking.
     */
    public void setArrowBack(int slot, @Nonnull PlayerGUI guiTo) {
        setArrowBack(slot, guiTo.getName(), p -> guiTo.openInventory());
    }
    
    /**
     * Sets the "Go Back" {@link #ITEM_ARROW_BACK}, which clicking on performs the given action at the given index.
     *
     * @param slot   - The slot to set the item at.
     * @param to     - The text to display after "To" in lore of the item.
     * @param action - The action to perform upon clicking.
     */
    public void setArrowBack(int slot, @Nonnull String to, @Nonnull Consumer<Player> action) {
        setItem(slot, new ItemBuilder(ITEM_ARROW_BACK).setName("&aGo Back").addLore("To " + to).toItemStack(), action);
    }
    
    /**
     * Sets the "Close Menu" {@link #ITEM_CLOSE_MENU}, which clicking on closes the GUI.
     * <p>This is set to {@code size - 4} slot by default.</p>
     */
    public void setCloseMenuItem() {
        setCloseMenuItem(size - 4);
    }
    
    /**
     * Sets the "Close Menu" {@link #ITEM_CLOSE_MENU}, which clicking on closes the GUI at the given slot.
     *
     * @param slot - The slot to set the item at.
     */
    public void setCloseMenuItem(int slot) {
        setItem(slot, new ItemBuilder(ITEM_CLOSE_MENU).setName("&cClose Menu").toItemStack(), HumanEntity::closeInventory);
    }
    
    /**
     * Fills the given item at the given indexes.
     *
     * @param from - From index, inclusive.
     * @param to   - To index, inclusive.
     * @param item - The item to fill.
     */
    public void fillItem(int from, int to, @Nonnull ItemStack item) {
        fillItem(from, to, item, null);
    }
    
    /**
     * Fills the given item at the given indexes.
     *
     * @param from   - From index, inclusive.
     * @param to     - To index, inclusive.
     * @param item   - The item to fill.
     * @param action - The action to fill.
     */
    public void fillItem(int from, int to, @Nonnull ItemStack item, @Nullable Consumer<Player> action) {
        for (int i = from; i <= to; i++) {
            setItem(i, item);
            
            if (action != null) {
                setAction(i, action);
            }
        }
    }
    
    /**
     * Fills the given row of the GUI.
     * <p>The row count starts at {@code 0}, which is the first line of the GUI.</p>
     *
     * @param row   - The row to fill.
     * @param stack - The item to fill with.
     */
    public void fillRow(int row, @Nonnull ItemStack stack) {
        fillRow(row, stack, null);
    }
    
    /**
     * Fills the given row of the GUI.
     * <p>The row count starts at {@code 0}, which is the first line of the GUI.</p>
     *
     * @param row    - The row to fill.
     * @param stack  - The item to fill with.
     * @param action - The action to fill with.
     */
    public void fillRow(int row, @Nonnull ItemStack stack, @Nullable Consumer<Player> action) {
        row = Math.clamp(row, 0, size / 9);
        
        for (int i = 0; i < size; ++i) {
            if (i / 9 == row) {
                setItem(i, stack, action);
            }
        }
    }
    
    /**
     * Fills the given column of the GUI.
     *
     * @param column - The column to fill.
     * @param stack  - The item to fill with.
     */
    public void fillColumn(@Range(from = 0, to = 8) int column, @Nonnull ItemStack stack) {
        fillColumn(column, stack, null);
    }
    
    /**
     * Fills the given column of the GUI.
     *
     * @param column - The column to fill.
     * @param stack  - The item to fill with.
     * @param action - The action to fill with.
     */
    public void fillColumn(@Range(from = 0, to = 8) int column, @Nonnull ItemStack stack, @Nullable Consumer<Player> action) {
        column = Math.clamp(column, 0, 8);
        
        for (int i = 0; i < getSize(); ++i) {
            if (i % 9 == column) {
                setItem(i, stack, action);
            }
        }
    }
    
    /**
     * Fills the outer border of the GUI with the given item.
     *
     * @param item – The item to fill with.
     */
    public void fillOuter(@Nonnull ItemStack item) {
        fillOuter(item, null);
    }
    
    /**
     * Fills the outer border of the GUI with the given item.
     *
     * @param item   – The item to fill with.
     * @param action – The action to fill with.
     */
    public void fillOuter(@Nonnull ItemStack item, @Nullable Consumer<Player> action) {
        for (int i = 0; i < size; i++) {
            if ((i > 0 && i < 9) || (i > size - 9 && i < size - 1) || (i % 9 == 0) || (i % 9 == 8)) {
                setItem(i, item, action);
            }
        }
    }
    
    /**
     * Fills the inner area of the GUI with the given item, excluding the outer border.
     *
     * @param item – The item to fill with.
     */
    public void fillInner(@Nonnull ItemStack item) {
        fillInner(item, null);
    }
    
    /**
     * Fills the inner area of the GUI with the given item, excluding the outer border.
     *
     * @param item   – The item to fill with.
     * @param action – The action to fill with.
     */
    public void fillInner(@Nonnull ItemStack item, @Nullable Consumer<Player> action) {
        for (int i = 10; i < size - 10; i++) {
            if ((i % 9 != 0) && (i % 9 != 8)) {
                setItem(i, item, action);
            }
        }
    }
    
    /**
     * Gets the name of this GUI.
     *
     * @return the name of this GUI.
     */
    @Nonnull
    public String getName() {
        return name;
    }
    
    /**
     * Creates a new {@link SmartComponent}.
     *
     * @return a new {@link SmartComponent}.
     * @see SmartComponent
     */
    @Nonnull
    public final SmartComponent newSmartComponent() {
        return new SmartComponent();
    }
    
    /**
     * Gets the bukkit inventory handle.
     *
     * @return the bukkit inventory handle.
     */
    @Nonnull
    public Inventory getInventory() {
        return inventory;
    }
    
    /**
     * Opens this GUI for the associated player.
     *
     * <p>This method performs several important steps to ensure correct and isolated behavior:
     * <ul>
     *   <li>If this GUI is an instance of {@link DisabledGUI}, it sends the player a message and cancels opening.</li>
     *   <li>Checks if this GUI is already open for the player. If so, it marks it as a reopen to avoid triggering {@code onClose()}.</li>
     *   <li>Registers this GUI as the active one for the player by storing it in the player inventory map.</li>
     *   <li>Clears all previously set actions and items to ensure each opening is treated as a fresh instance.</li>
     *   <li>Calls {@link #onUpdate()} to populate the inventory contents before displaying it.</li>
     *   <li>Opens the inventory for the player.</li>
     * </ul>
     *
     * <p>Subclasses overriding this method <b>must</b> call {@code super.openInventory()} to ensure proper functionality.
     */
    @OverridingMethodsMustInvokeSuper
    public void openInventory() {
        if (this instanceof DisabledGUI disabledGUI) {
            Chat.sendMessage(player, disabledGUI.message());
            return;
        }
        
        final PlayerGUI currentGUI = playerInventory.get(player.getUniqueId());
        
        // If the player currently has this GUI while calling openInventory(),
        // it means it was "re-opened", so we mark it as such to prevent calling onClose() etc.
        if (currentGUI != null && currentGUI.equals(this)) {
            reopen = true;
        }
        
        // Store in hashmap
        playerInventory.put(player.getUniqueId(), this);
        
        // GUIs are now self-clearing after each update
        clearActions();
        clearItems();
        
        // Call onUpdate
        onUpdate();
        
        // And finally open the inventory
        player.openInventory(inventory);
    }
    
    public void closeInventory() {
        player.closeInventory();
    }
    
    /**
     * Gets the size of this GUI.
     *
     * @return the size of this GUI.
     */
    public int getSize() {
        return size;
    }
    
    /**
     * Gets the number of rows of this GUI.
     *
     * @return the number of rows of this GUI.
     */
    public int getRows() {
        return size / 9;
    }
    
    /**
     * Performs an {@link Inventory} identity check to determine the bukkit inventory eligibility.
     *
     * @param inventory - The inventory to compare.
     * @return {@code true} if the given {@link Inventory} is identical in identity to bukkit handle.
     */
    public final boolean compareInventory(Inventory inventory) {
        return this.inventory == inventory;
    }
    
    protected void setItem(int slot, @Nullable ItemStack item, @Nullable ActionList action) {
        if (slot < 0 || slot > size) {
            throw new IndexOutOfBoundsException("Index %s is out of bounds for %s slots!".formatted(slot, this.size));
        }
        
        inventory.setItem(slot, item != null ? item : new ItemStack(Material.AIR));
        
        // Only set action if it's not null to not override the existing action
        if (action != null) {
            actions.put(slot, action);
        }
    }
    
    final void acceptEvent(int slot, Player player, ClickType clickType) {
        // Respect item clicks means there must be an item on a given slot for click to work
        if (properties.respectItemClick && getItem(slot) == null) {
            return;
        }
        
        final ActionList actionList = actions.get(slot);
        
        if (actionList == null) {
            return;
        }
        
        
        actionList.perClickAction.forEach((type, action) -> {
            if (type == clickType) {
                action.accept(player);
            }
        });
    }
    
    final boolean hasEvent(int slot) {
        return actions.get(slot) != null;
    }
    
    /**
     * Joins the given strings with a forward arrow ({@code ➜}) separator, commonly used for menu breadcrumbs.
     *
     * <p>Examples:
     * <ul>
     *   <li>{@code menuArrowSplit("Main", "Settings", "Audio")} ➜ {@code "Main ➜ Settings ➜ Audio"}</li>
     *   <li>{@code menuArrowSplit("Inventory")} ➜ {@code "Inventory"}</li>
     *   <li>{@code menuArrowSplit()} ➜ {@code "Default Name"}</li>
     * </ul>
     *
     * @param strings – The strings to join, in order.
     * @return the joined string using arrows, or {@code "Default Name"} if none are provided.
     */
    @Nonnull
    public static String menuArrowSplit(@Nonnull String... strings) {
        if (strings.length == 0) {
            return "Default Name";
        }
        if (strings.length == 1) {
            return strings[0];
        }
        
        final StringBuilder builder = new StringBuilder();
        
        for (int i = 0; i < strings.length; i++) {
            if (i != 0) {
                builder.append(" ").append(ARROW_FORWARD).append(" ");
            }
            
            builder.append(strings[i].trim());
        }
        
        return builder.toString();
    }
    
    /**
     * Get player's current GUI that is opened, or null if no GUI.
     *
     * @param player - Player.
     * @return player current GUI that is opened, or null if no GUI.
     */
    @Nullable
    public static PlayerGUI getPlayerGUI(@Nonnull Player player) {
        return playerInventory.getOrDefault(player.getUniqueId(), null);
    }
    
}
