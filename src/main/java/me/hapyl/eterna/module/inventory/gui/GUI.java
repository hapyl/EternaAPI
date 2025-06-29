package me.hapyl.eterna.module.inventory.gui;

import com.google.common.collect.Maps;
import me.hapyl.eterna.module.annotate.Super;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Range;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * A powerful GUI creator class.
 * Allows adding items, click events, close and open events or even adding your own listener.
 * <p>
 * It is recommended to use {@link PlayerGUI} or it's subclasses instead of this class.
 */
public class GUI {

    public static final char ARROW_FORWARD = '➜';
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
    
    protected static final Map<UUID, GUI> playerInventory = Maps.newHashMap();

    private final String name;
    private final int size;
    private final Set<Integer> ignoredClicks;
    private final Properties properties;
    private final Map<Integer, GUIClick> bySlot;
    private final Inventory inventory;

    protected boolean reopen;
    
    private Action openEvent;
    private Action closeEvent;
    private me.hapyl.eterna.module.inventory.gui.EventListener listener;
    private GUIEventHandler eventHandler;
    private CancelType cancelType;
    
    public GUI(@Nonnull String name, int rows) {
        this.name = name;
        this.size = Math.clamp(rows * 9L, 9, 54);
        this.bySlot = new HashMap<>();
        this.cancelType = CancelType.EITHER;
        this.ignoredClicks = new HashSet<>();
        this.properties = new Properties();
        this.inventory = Bukkit.createInventory(null, this.size, Chat.color(name));
    }

    /**
     * Renames the player inventory if it is opened to a player, does nothing otherwise.
     *
     * @param player  - Player.
     * @param newName - New name.
     */
    public void rename(@Nonnull Player player, @Nonnull String newName) {
        final InventoryView view = player.getOpenInventory();

        if (!view.getTopInventory().equals(inventory)) {
            return;
        }

        view.setTitle(Chat.format(newName));
    }

    /**
     * Returns current GUI's cancel type.
     *
     * @return current cancel type.
     */
    public CancelType getCancelType() {
        return cancelType;
    }

    /**
     * Sets a new cancel type for this GUI.
     *
     * @param cancelType - CancelType.
     */
    public void setCancelType(CancelType cancelType) {
        this.cancelType = cancelType;
    }

    /**
     * Returns if only GUI clicks should be cancelled.
     *
     * @return if only GUI clicks should be cancelled.
     */
    public final boolean onlyCancelGUI() {
        return cancelType == CancelType.GUI;
    }

    /**
     * Sets a custom event listener for this GUI.
     *
     * @param listener - New Listener. Null to remove listener.
     */
    public final void setEventListener(@Nullable me.hapyl.eterna.module.inventory.gui.EventListener listener) {
        this.listener = listener;
    }

    @Nullable
    public GUIEventHandler getEventHandler() {
        return eventHandler;
    }

    public final void setEventHandler(@Nullable GUIEventHandler handler) {
        this.eventHandler = handler;
    }

    /**
     * Removes current event listener for this GUI.
     */
    public final void removeEventListener() {
        setEventListener(null);
    }

    /**
     * Returns current custom event listener.
     *
     * @return current custom event listener.
     */
    @Nullable
    public EventListener getListener() {
        return listener;
    }

    /**
     * @deprecated {@link this#setCancelType(CancelType)}
     */
    @Deprecated
    public final void setOnlyCancelGUI(boolean flag) {
        setCancelType(flag ? CancelType.GUI : CancelType.EITHER);
    }

    /**
     * Return is provided slot is ignored by {@link org.bukkit.event.inventory.InventoryClickEvent}
     * and should not be cancelled no matter what {@link CancelType} is.
     *
     * @param slot - Slot.
     * @return if slot is ignored.
     */
    public final boolean isIgnoredSlot(int slot) {
        return this.ignoredClicks.contains(slot);
    }

    public Properties getProperties() {
        return properties;
    }

    /**
     * Adds ignored slots.
     * <b>Clicks at ignored slots are NOT cancelled no matter what CancelType is.</b>
     *
     * @param slots - Slots to ignore. Keep null to ignore all slots.
     */
    public final void addIgnoredSlots(Integer... slots) {
        // add all slots
        if (slots.length == 0) {
            for (int i = 0; i <= this.getSize(); i++) {
                this.ignoredClicks.add(i);
            }
            return;
        }
        Collections.addAll(this.ignoredClicks, slots);
    }

    /**
     * Removes ignored slots.
     *
     * @param slots - Slots to remove.
     */
    public final void removeIgnoredSlots(Integer... slots) {
        // remove all
        if (slots.length == 0) {
            this.ignoredClicks.clear();
            return;
        }
        for (final Integer slot : slots) {
            this.ignoredClicks.remove(slot);
        }
    }

    /**
     * Clears item in provided slot. <b>Keep in mind that this does NOT reset click event.
     * To avoid ghost clicks use {@link GUI#resetClick(int)} to reset click or {@link GUI#clearEverything()} to clear this GUi.</b>
     *
     * @param slot - Slot to clear item at.
     */
    public final void resetItem(int slot) {
        this.setItem(slot, null);
    }

    /**
     * Clears click event on provided slot.
     *
     * @param slot - Slot to clear event at.
     */
    public final void resetClick(int slot) {
        bySlot.remove(slot);
    }

    /**
     * Clears both item and event at provided slot.
     *
     * @param slot - Slot to clear at.
     */
    public final void reset(int slot) {
        resetItem(slot);
        resetClick(slot);
    }

    /**
     * Returns an item on provided slot.
     *
     * @param slot - Slot.
     * @return an item on provided slot.
     */
    @Nullable
    public final ItemStack getItem(int slot) {
        return inventory.getItem(slot);
    }

    @Nullable
    public final GUIClick getClick(int slot) {
        return bySlot.get(slot);
    }

    /**
     * Clears clicks events.
     */
    public final void clearClickEvents() {
        bySlot.clear();
    }

    /**
     * Clears all items and clicks events.
     */
    public final void clearItemsAndClicks() {
        clearItems();
        clearClickEvents();
    }

    /**
     * Returns a copy of current by slot map.
     *
     * @return a copy of current by slot map.
     */
    public Map<Integer, GUIClick> getBySlot() {
        return Maps.newHashMap(bySlot);
    }

    /**
     * Clears close and open events.
     */
    @Deprecated(forRemoval = true)
    public final void clearEvents() {
        closeEvent = null;
        openEvent = null;
    }

    /**
     * Sets an item to provided slot.
     *
     * @param slot   - Slot to put item at.
     * @param item   - Item to set.
     * @param action - Click action of provided slot.
     * @throws IndexOutOfBoundsException if slot is out of bounds. (slot > {@link GUI#getSize()})
     */
    public final void setItem(int slot, @Nullable ItemStack item, @Nullable Action action) {
        setItem(slot, item, action == null ? null : new GUIClick(action));
    }

    /**
     * Sets a given item to the given slot.
     *
     * @param slot   - Slot.
     * @param item   - Item.
     * @param action - Action.
     */
    public final void setItem(int slot, @Nonnull ItemStack item, @Nonnull StrictAction action) {
        setItem(slot, item, action.makeGUIClick());
    }

    /**
     * Sets an item to provided slot.
     *
     * @param slot   - Slot to put item at.
     * @param item   - Item to set.
     * @param action - Click action of provided slot.
     * @param types  - Click Types action is applied to.
     * @throws IndexOutOfBoundsException if slot is out of bounds. (slot > {@link GUI#getSize()})
     * @throws IllegalArgumentException  if the given {@link ClickType} are empty or null.
     */
    public final void setItem(int slot, @Nullable ItemStack item, @Nonnull Action action, @Range(from = 1, to = Byte.MAX_VALUE) ClickType... types) {
        Validate.isTrue(types.length != 0, "there must be at least 1 type");

        final GUIClick guiClick = getOrNew(slot);

        for (final ClickType type : types) {
            guiClick.setAction(type, action);
        }

        this.setItem(slot, item, guiClick);
    }

    /**
     * Sets an item to provided slots with different left and right click actions.
     *
     * @param slot       - Slot to put item at.
     * @param item       - Item to set.
     * @param leftClick  - Left click action.
     * @param rightClick - Right click action.
     */
    public final void setItem(int slot, @Nullable ItemStack item, @Nonnull Action leftClick, @Nonnull Action rightClick) {
        setItem(slot, item);
        setClick(slot, leftClick, rightClick);
    }

    /**
     * Sets an item to provided slot.
     *
     * @param slot - Slot to put item at.
     * @param item - Item to set.
     * @throws IndexOutOfBoundsException if slot is out of bounds. (slot > {@link GUI#getSize()})
     */
    public final void setItem(int slot, @Nullable ItemStack item) {
        setItem(slot, item, (Action) null);
    }

    /**
     * Sets an item to provided slot if condition is true.
     *
     * @param condition - Condition to check before setting the item.
     * @param slot      - Slot to put item at.
     * @param item      - Item to set.
     * @throws IndexOutOfBoundsException if slot is out of bounds. (slot > {@link GUI#getSize()})
     */
    public final void setItemIf(boolean condition, int slot, @Nullable ItemStack item) {
        if (condition) {
            this.setItem(slot, item);
        }
    }

    /**
     * Sets an item to provided slot if condition is true.
     *
     * @param condition - Condition to check before setting the item.
     * @param slot      - Slot to put item at.
     * @param item      - Item to set.
     * @param action-   Click action.
     * @throws IndexOutOfBoundsException if slot is out of bounds. (slot > {@link GUI#getSize()})
     */
    public final void setItemIf(boolean condition, int slot, @Nullable ItemStack item, @Nullable Action action) {
        if (condition) {
            this.setItem(slot, item, action);
        }
    }

    /**
     * Sets a click action on provided slot.
     *
     * @param slot   - Slot to put click at.
     * @param action - Action.
     */
    public final void setClick(int slot, Action action) {
        this.setClick(slot, new GUIClick(action));
    }

    /**
     * Sets a separate click for a left and right click.
     *
     * @param slot       - Slot to put click at.
     * @param leftClick  - Left click action.
     * @param rightClick - Right click action.
     */
    public final void setClick(int slot, Action leftClick, Action rightClick) {
        this.setClick(slot, leftClick, ClickType.LEFT);
        this.setClick(slot, rightClick, ClickType.RIGHT);
    }

    /**
     * Sets a click action on provided slot for provided ClickTypes.
     *
     * @param slot   - Slot to put click at.
     * @param action - Action.
     * @param types  - ClickType action will be applied to.
     */
    public final void addClick(int slot, Action action, ClickType... types) {
        this.setClick(slot, action, types);
    }

    /**
     * Sets a click action on provided slot for provided ClickTypes.
     *
     * @param slot   - Slot to put click at.
     * @param action - Action.
     * @param types  - ClickType action will be applied to.
     * @throws IllegalArgumentException if the given {@link ClickType} are empty or null.
     */
    public final void setClick(int slot, Action action, ClickType... types) {
        Validate.isTrue(types.length != 0, "there must be at least 1 type");

        final GUIClick guiClick = getOrNew(slot);

        for (final ClickType type : types) {
            guiClick.setAction(type, action);
        }

        this.setClick(slot, guiClick);
    }

    /**
     * Puts a preset item that bring to provided
     * menu upon clicks.
     *
     * @param guiTo - GUI to locate upon click.
     * @apiNote Default slot is (size - 5)
     */
    public final void setArrowBack(GUI guiTo) {
        this.setArrowBack(this.getSize() - 5, guiTo);
    }

    /**
     * Puts a preset item that executes action upon a click.
     *
     * @param to     - Lore of the arrow. (Go back to %s)
     * @param action - Click.
     */
    public final void setArrowBack(String to, Action action) {
        this.setArrowBack(this.getSize() - 5, to, action);
    }

    /**
     * Puts a preset item that executes action upon click at provided slot.
     *
     * @param slot   - Slot to put at.
     * @param to     - Lore of the arrow. (Go back to %s)
     * @param action - Click.
     */
    public final void setArrowBack(int slot, String to, Action action) {
        this.setItem(slot, new ItemBuilder(Material.ARROW).setName("&aGo Back").addLore("To " + to).toItemStack(), action);
    }

    /**
     * Puts a preset item that bring to provided menu upon click.
     *
     * @param slot  - Slot to put at.
     * @param guiTo - GUI to navigate upon click.
     */
    public final void setArrowBack(int slot, GUI guiTo) {
        this.setItem(
                slot,
                new ItemBuilder(Material.ARROW).setName("&aGo Back").addLore("To " + guiTo.getName()).toItemStack(),
                guiTo::openInventory
        );
    }

    /**
     * Puts a preset item that closes menu upon click.
     *
     * @param slot - Slot to put item at.
     */
    public final void setCloseMenuItem(int slot) {
        this.setItem(slot, new ItemBuilder(Material.BARRIER).setName("&cClose Menu").toItemStack());
        this.setClick(slot, HumanEntity::closeInventory);
    }

    /**
     * Puts a preset item that closes menu upon click at (size - 4) slot.
     */
    public final void setCloseMenuItem() {
        this.setCloseMenuItem(this.getSize() - 4);
    }

    /**
     * Fills items from start to end inside the gui.
     *
     * @param start - Start index.
     * @param end   - End index, inclusive.
     * @param item  - Item to fill.
     */
    public final void fillItem(int start, int end, ItemStack item) {
        this.fillItem(start, end, item, null);
    }

    /**
     * Fills items from start to end inside the gui
     * and sets a click event.
     *
     * @param start  - Start index.
     * @param end    - End index, inclusive.
     * @param item   - Item to fill.
     * @param action - Click.
     */
    public final void fillItem(int start, int end, ItemStack item, Action action) {
        for (int i = start; i <= end; i++) {
            this.setItem(i, item);
            if (action != null) {
                this.setClick(i, action);
            }
        }
    }

    /**
     * Fills a line (9 horizontal slots) of the GUI.
     *
     * @param line  - Line to fill.
     * @param stack - Item to fill.
     */
    public GUI fillLine(int line, ItemStack stack) {
        line = Validate.ifTrue(line < 0, 0, line);
        line = Validate.ifTrue(line > this.getSize() / 9, this.getSize() / 9, line);

        for (int i = 0; i < this.getSize(); ++i) {
            if (i / 9 == line) {
                this.setItem(i, stack);
            }
        }
        return this;
    }

    /**
     * Fill a column (n of vertical slots) of the GUI.
     *
     * @param column - Column to fill.
     * @param stack  - Item to fill.
     */
    public GUI fillColumn(int column, ItemStack stack) {
        column = Validate.ifTrue(column > 8, 8, column);

        for (int i = 0; i < this.getSize(); ++i) {
            if (i % 9 == column) {
                this.setItem(i, stack);
            }
        }
        return this;
    }

    /**
     * Fills the outer layer of the GUI.
     *
     * @param item - Item to fill.
     */
    public GUI fillOuter(ItemStack item) {
        Validate.notNullItemStack(item);

        for (int i = 0; i < this.inventory.getSize(); i++) {
            if ((i > 0 && i < 9) || (i > this.inventory.getSize() - 9 && i < this.inventory.getSize() - 1) || (i % 9 == 0) || (i % 9 == 8)) {
                this.inventory.setItem(i, item);
            }
        }
        return this;
    }

    /**
     * Fills the inner layer of the GUI.
     *
     * @param item - Item to fill.
     */
    public GUI fillInner(ItemStack item) {
        Validate.notNullItemStack(item);

        for (int i = 10; i < this.getInventory().getSize() - 10; i++) {
            if ((i % 9 != 0) && (i % 9 != 8)) {
                this.setItem(i, item);
            }
        }

        return this;
    }

    public String getName() {
        return name;
    }

    public Action getOpenEvent() {
        return openEvent;
    }

    public final void setOpenEvent(Action open) {
        this.openEvent = open;
    }

    public Action getCloseEvent() {
        return closeEvent;
    }

    public final void setCloseEvent(Action close) {
        this.closeEvent = close;
    }

    /**
     * Fills smart component with provided pattern.
     * <b>Smart Component will automatically put item on slots based on pattern.</b>
     *
     * @param component - Component.
     * @param pattern   - Pattern.
     * @param startLine - Starting line of the GUI.
     */
    public final void fillItems(SmartComponent component, SlotPattern pattern, int startLine) {
        fillItems(component.getMap(), pattern, startLine);
    }

    /**
     * Fills item stacks with provided pattern.
     * <b>Smart Component will automatically put item on slots based on pattern.</b>
     *
     * @param items     - Item to fill.
     * @param pattern   - Pattern.
     * @param startLine - Starting line of the GUI.
     */
    public final void fillItems(List<ItemStack> items, SlotPattern pattern, int startLine) {
        final HashMap<ItemStack, GUIClick> map = new HashMap<>();
        for (ItemStack item : items) {
            map.put(item, null);
        }

        fillItems(map, pattern, startLine);
    }

    /**
     * @deprecated Use {@link SlotPattern#apply(GUI, LinkedHashMap, int)} instead.
     */
    @Super
    @Deprecated
    public final void fillItems(Map<ItemStack, GUIClick> items, SlotPattern pattern, int startLine) {
        pattern.apply(this, (LinkedHashMap<ItemStack, GUIClick>) items, startLine);
        //        final List<Integer> slots = pattern.getSlots(items.keySet(), startLine);
        //        final Iterator<ItemStack> iterator = items.keySet().iterator();
        //        for (Integer slot : slots) {
        //            if (size < slot || !iterator.hasNext()) {
        //                continue;
        //            }
        //
        //            final ItemStack item = iterator.next();
        //            setItem(slot, item, items.get(item));
        //        }
    }

    /**
     * Returns a smart component to add items to.
     *
     * @return a smart component to add items to.
     */
    public final SmartComponent fillItems() {
        return this.newSmartComponent();
    }

    /**
     * Returns a smart component to add items to.
     *
     * @return a smart component to add items to.
     */
    public final SmartComponent newSmartComponent() {
        return new SmartComponent();
    }

    /**
     * Returns BukkitInventory of this GUI.
     *
     * @return BukkitInventory of this GUI.
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Opens this GUI to player. This creates GUI instance.
     *
     * @param player - Player to open inventory.
     */
    public final void openInventory(Player player) {
        if (this instanceof DisabledGUI) {
            Chat.sendMessage(player, "&cThis feature is currently disabled!");
            return;
        }
        
        final GUI currentGUI = playerInventory.get(player.getUniqueId());
        
        // If the player currently has this GUI while calling openInventory(),
        // it means it was "re-opened", so we mark it as such to prevent calling onClose() etc.
        if (currentGUI != null && currentGUI.equals(this)) {
            reopen = true;
        }
        
        playerInventory.put(player.getUniqueId(), this);
        player.openInventory(this.inventory);
    }

    /**
     * Manually accepts the event.
     *
     * @param slot   - Slot.
     * @param player - Player.
     * @param type   - Click Type.
     */
    public final void acceptEvent(int slot, Player player, ClickType type) {
        final Map<ClickType, Action> events = bySlot.get(slot).getEvents();

        if (properties.respectItemClick && getItem(slot) == null) {
            return;
        }

        events.forEach((clickType, action) -> {
            if (clickType == type && action != null) {
                action.invoke(player);
            }
        });
    }

    public final void acceptEvent(int slot, Player player) {
        this.acceptEvent(slot, player, ClickType.UNKNOWN);
    }

    public final boolean hasEvent(int slot) {
        return bySlot.get(slot) != null;
    }

    public int getSize() {
        return this.getInventory().getSize();
    }

    public int getRows() {
        return this.size / 9;
    }

    @Deprecated
    /**
     * @deprecated {@link GUI#properties}
     */
    public final boolean isRespectItemClick() {
        return properties.isRespectItemClick();
    }

    @Deprecated
    /**
     * @deprecated {@link GUI#properties}
     */
    public final void setRespectItemClick(boolean respectItemClick) {
        properties.setRespectItemClick(respectItemClick);
    }

    @Deprecated
    /**
     * @deprecated {@link GUI#properties}
     */
    public final boolean isAllowDrag() {
        return properties.isAllowDrag();
    }

    @Deprecated
    /**
     * @deprecated {@link GUI#properties}
     */
    public final void setAllowDrag(boolean allowDrag) {
        properties.setAllowDrag(allowDrag);
    }

    // static members

    @Deprecated
    /**
     * @deprecated {@link GUI#properties}
     */
    public boolean isAllowNumbers() {
        return properties.allowNumbers;
    }

    @Deprecated
    /**
     * @deprecated {@link GUI#properties}
     */
    public void setAllowNumbers(boolean allowNumbers) {
        properties.setAllowNumbersClick(allowNumbers);
    }

    @Deprecated
    /**
     * @deprecated {@link GUI#properties}
     */
    public final boolean isAllowShiftClick() {
        return properties.isAllowShiftClick();
    }

    @Deprecated
    /**
     * @deprecated {@link GUI#properties}
     */
    public final void setAllowShiftClick(boolean allowShiftClick) {
        properties.setAllowShiftClick(allowShiftClick);
    }

    public final boolean compareInventory(Inventory inventory) {
        return this.inventory == inventory;
    }

    public final void clearItems() {
        for (int i = 0; i < this.getSize(); i++) {
            this.setItem(i, null);
        }
    }

    /**
     * Clears everything this GUI has to offer, such as items, click, close and open events.
     * Recommended to use if updating menus to clear old items and clicks.
     *
     * @deprecated We usually want to clear click events and items, this also clears GUI events, use {@link #clearItemsAndClicks()} instead.
     */
    @Deprecated
    public final void clearEverything() {
        clearClickEvents();
        clearEvents();
        clearItems();
    }

    protected void setItem(int slot, @Nullable ItemStack item, @Nullable GUIClick action) {
        if (slot > this.size) {
            throw new IndexOutOfBoundsException(String.format("There are only %s slots, given %s.", this.size, slot));
        }
        if (action != null) {
            this.bySlot.put(slot, action);
        }
        item = notNull(item);
        this.inventory.setItem(slot, item);
    }

    // Private Set Click (super)
    protected void setClick(int slot, GUIClick action) {
        if (action != null) {
            this.bySlot.put(slot, action);
        }
    }

    /**
     * @deprecated {@link GUI#fillItems(Map, SlotPattern, int)}
     */
    @Deprecated
    protected final void fillItems(LinkedHashMap<ItemStack, GUIClick> hashMap, int startLine) {
        fillItems(hashMap, SlotPattern.DEFAULT, startLine);
    }

    private GUIClick getOrNew(int slot) {
        return this.bySlot.getOrDefault(slot, new GUIClick());
    }

    private ItemStack notNull(ItemStack item) {
        return (item == null) ? new ItemStack(Material.AIR) : item;
    }

    /**
     * Create a hierarchy of string separated by {@link GUI#ARROW_FORWARD}.
     * Useful for sub menu creations.
     *
     * @param strings - Titles.
     * @return strings separated by {@link GUI#ARROW_FORWARD}.
     */
    public static String menuArrowSplit(String... strings) {
        if (strings.length == 0) {
            return "Default Name";
        }
        if (strings.length == 1) {
            return strings[0];
        }
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            builder.append(strings[i].trim());
            if (i != (strings.length - 1)) {
                builder.append(" ").append(ARROW_FORWARD).append(" ");
            }
        }
        return builder.toString();
    }

    /**
     * Returns a minimum GUI size required to put items to.
     *
     * @param collection - Collection of items.
     */
    public static int getSmartMenuSize(Collection<?> collection) {
        return (int) Math.ceil((float) collection.size() / 5);
    }

    /**
     * Returns a minimum GUI size required to put items to.
     *
     * @param collection - Array of items.
     */
    public static <T> int getSmartMenuSize(T[] collection) {
        return (int) Math.ceil((float) collection.length / 5);
    }

    /**
     * Get player's current GUI that is opened, or null if no GUI.
     *
     * @param player - Player.
     * @return player current GUI that is opened, or null if no GUI.
     */
    @Nullable
    public static GUI getPlayerGUI(@Nonnull Player player) {
        return playerInventory.getOrDefault(player.getUniqueId(), null);
    }

}
