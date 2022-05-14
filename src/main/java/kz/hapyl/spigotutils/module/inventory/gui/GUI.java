package kz.hapyl.spigotutils.module.inventory.gui;

import kz.hapyl.spigotutils.module.annotate.Super;
import kz.hapyl.spigotutils.module.inventory.ChestInventory;
import kz.hapyl.spigotutils.module.inventory.ItemBuilder;
import kz.hapyl.spigotutils.module.math.Numbers;
import kz.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Create ChestGUI
 */
public class GUI {

    public static final char ARROW_FORWARD = '➜';

    // todo: move to registry
    private static final Map<UUID, GUI> playerInventory = new HashMap<>();

    private final String name;
    private final int size;

    private final Set<Integer> ignoredClicks;

    private Action openEvent;
    private Action closeEvent;
    private EventListener listener;

    private boolean allowDrag;
    private boolean allowShiftClick;

    private CancelType cancelType;

    private final Map<Integer, GUIClick> bySlot;
    private final Inventory inventory;

    public GUI(String name, int rows) {
        this.name = name;
        this.size = Numbers.clamp(rows * 9, 9, 54);
        this.bySlot = new HashMap<>();
        this.cancelType = CancelType.EITHER;
        this.ignoredClicks = new HashSet<>();
        this.inventory = Bukkit.createInventory(null, this.size, name);
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
     * Returns current GUI's cancel type.
     *
     * @return current cancel type.
     */
    public CancelType getCancelType() {
        return cancelType;
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
    public final void setEventListener(@Nullable EventListener listener) {
        this.listener = listener;
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
    public final void setOnlyCanelGUI(boolean flag) {
        setOnlyCancelGUI(flag);
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
     * Clears item in provided slot.
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
        this.setItem(slot, getItem(slot), (Action) null);
    }

    /**
     * Clears both item and event at provided slot.
     *
     * @param slot - Slot to clear at.
     */
    public final void reset(int slot) {
        this.setItem(slot, null, (Action) null);
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

    /**
     * Clears events, such as clicks events, close and open events.
     */
    public final void clearClickEvents() {
        bySlot.clear();
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
    public final void setItem(int slot, @Nullable ItemStack item, Action action) {
        this.setItem(slot, item, new GUIClick(action));
    }

    /**
     * Sets an item to provided slot.
     *
     * @param slot   - Slot to put item at.
     * @param item   - Item to set.
     * @param action - Click action of provided slot.
     * @param types  - Click Types action is applied to.
     * @throws IndexOutOfBoundsException if slot is out of bounds. (slot > {@link GUI#getSize()})
     */
    public final void setItem(int slot, @Nullable ItemStack item, Action action, ClickType... types) {
        Validate.isTrue(types.length != 0, "there must be at least 1 type");
        final GUIClick guiClick = getOrNew(slot, action);
        for (final ClickType type : types) {
            guiClick.addPerClick(type, action);
        }
        this.setItem(slot, item, guiClick);
    }

    /**
     * Sets an item to provided slot.
     *
     * @param slot - Slot to put item at.
     * @param item - Item to set.
     * @throws IndexOutOfBoundsException if slot is out of bounds. (slot > {@link GUI#getSize()})
     */
    public final void setItem(int slot, @Nullable ItemStack item) {
        this.setItem(slot, item, (Action) null);
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

    // Private Set Item (super)
    private void setItem(int slot, @Nullable ItemStack item, @Nullable GUIClick action) {
        if (slot > this.size) {
            throw new IndexOutOfBoundsException(String.format("There are only %s slots, given %s.", this.size, slot));
        }
        if (action != null) {
            this.bySlot.put(slot, action);
        }
        item = notNull(item);
        this.inventory.setItem(slot, item);
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
     */
    public final void setClick(int slot, Action action, ClickType... types) {
        Validate.isTrue(types.length != 0, "there must be at least 1 type");
        final GUIClick guiClick = getOrNew(slot, action);
        for (final ClickType type : types) {
            guiClick.addPerClick(type, action);
        }
        this.setClick(slot, guiClick);
    }

    // Private Set Click (super)
    private void setClick(int slot, GUIClick action) {
        if (action != null) {
            this.bySlot.put(slot, action);
        }
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

    public Action getCloseEvent() {
        return closeEvent;
    }

    private GUIClick getOrNew(int slot, Action action) {
        return this.bySlot.getOrDefault(slot, new GUIClick(action));
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

    @Super
    public final void fillItems(Map<ItemStack, GUIClick> items, SlotPattern pattern, int startLine) {
        final List<Integer> slots = pattern.getSlots(items.keySet(), startLine);
        final Iterator<ItemStack> iterator = items.keySet().iterator();
        for (Integer slot : slots) {
            if (size < slot || !iterator.hasNext()) {
                continue;
            }

            final ItemStack item = iterator.next();
            setItem(slot, item, items.get(item));
        }
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
     * @deprecated {@link GUI#fillItems(Map, SlotPattern, int)}
     */
    @Deprecated
    protected final void fillItems(LinkedHashMap<ItemStack, GUIClick> hashMap, int startLine) {
        final List<Integer> slots = ChestInventory.convertItemsIntoSmartSlots(hashMap.keySet(), startLine);
        final Iterator<ItemStack> iterator = hashMap.keySet().iterator();
        for (final Integer slot : slots) {
            if (this.size >= slot) {
                if (iterator.hasNext()) {
                    final ItemStack nextItem = iterator.next();
                    if (hashMap.getOrDefault(nextItem, null) == null) {
                        this.setItem(slot, nextItem);
                    }
                    else {
                        this.setItem(slot, nextItem, hashMap.get(nextItem));
                    }
                }
            }
        }
    }

    protected final void fillItems(LinkedHashMap<ItemStack, GUIClick> hashMap) {
        fillItems(hashMap, 1);
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
     * Returns BukkitInventory of this GUI.
     *
     * @return BukkitInventory of this GUI.
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Closes inventory. This will remove instance of players GUI instantly.
     *
     * @param player - Player to close inventory for.
     */
    public final void closeInventory(Player player) {
        removePlayerGUI(player);
        player.closeInventory();
    }

    /**
     * Opens this GUI to player. This creates GUI instance.
     *
     * @param player - Player to open inventory.
     */
    public final void openInventory(Player player) {
        setPlayerGUI(player, this);
        player.openInventory(this.inventory);
    }

    public final void acceptEvent(int slot, Player player, ClickType type) {
        final Map<ClickType, Action> events = bySlot.get(slot).getEvents();

        // only 1 action
        if (events.size() == 1) {
            final Action action = events.get(ClickType.UNKNOWN);
            if (action == null) {
                return;
            }
            action.invoke(player);
            return;
        }

        events.forEach((click, action) -> {
            if (action == null) {
                return;
            }
            if (click == type) {
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

    private ItemStack notNull(ItemStack item) {
        return (item == null) ? new ItemStack(Material.AIR) : item;
    }

    public int getSize() {
        return this.getInventory().getSize();
    }

    // static members
    @Nullable
    public static GUI getPlayerGUI(Player player) {
        return playerInventory.getOrDefault(player.getUniqueId(), null);
    }

    public static void setPlayerGUI(Player player, GUI gui) {
        playerInventory.put(player.getUniqueId(), gui);
    }

    public static void removePlayerGUI(Player player) {
        if (getPlayerGUI(player) == null) {
            return;
        }
        playerInventory.remove(player.getUniqueId());
    }

    public static void clearAll() {
        for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.closeInventory();
            removePlayerGUI(onlinePlayer);
        }
        playerInventory.clear();
    }

    public boolean isAllowDrag() {
        return allowDrag;
    }

    public final void setAllowDrag(boolean allowDrag) {
        this.allowDrag = allowDrag;
    }

    public final boolean isAllowShiftClick() {
        return allowShiftClick;
    }

    public final void setAllowShiftClick(boolean allowShiftClick) {
        this.allowShiftClick = allowShiftClick;
    }

    public final boolean compareInventory(Inventory inventory) {
        return this.inventory.equals(inventory);
    }

    public final void setCloseEvent(Action close) {
        this.closeEvent = close;
    }

    public final void setOpenEvent(Action open) {
        this.openEvent = open;
    }

    public final void clearItems() {
        for (int i = 0; i < this.getSize(); i++) {
            this.setItem(i, null);
        }
    }

    /**
     * Clears everything this GUI has to offer, such as items, click, close and open events.
     */
    public final void clearEverything() {
        clearClickEvents();
        for (int i = 0; i < this.getSize(); i++) {
            this.setItem(i, null);
        }
    }

}
