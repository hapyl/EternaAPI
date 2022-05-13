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

    public void setCancelType(CancelType cancelType) {
        this.cancelType = cancelType;
    }

    public CancelType getCancelType() {
        return cancelType;
    }

    public final boolean onlyCancelGUI() {
        return cancelType == CancelType.GUI;
    }

    public final void setEventListener(EventListener listener) {
        this.listener = listener;
    }

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

    protected final boolean isIgnoredSlot(int slot) {
        return this.ignoredClicks.contains(slot);
    }

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

    public final void resetItem(int slot) {
        this.setItem(slot, null);
    }

    public final void resetClick(int slot) {
        this.setItem(slot, null);
    }

    public final void reset(int slot) {
        this.setItem(slot, null, (Action) null);
    }

    public final void clearClickEvents() {
        bySlot.clear();
        closeEvent = null;
        openEvent = null;
    }

    // Set Item
    public final void setItem(int slot, @Nullable ItemStack item, Action action) {
        this.setItem(slot, item, new GUIClick(action));
    }

    public final void setItem(int slot, @Nullable ItemStack item, Action action, ClickType... types) {
        Validate.isTrue(types.length != 0, "there must be at least 1 type");
        final GUIClick guiClick = getOrNew(slot, action);
        for (final ClickType type : types) {
            guiClick.addPerClick(type, action);
        }
        this.setItem(slot, item, guiClick);
    }

    public final void setItem(int slot, @Nullable ItemStack item) {
        this.setItem(slot, item, (Action) null);
    }

    public final void setItemIf(boolean condition, int slot, @Nullable ItemStack item) {
        if (condition) {
            this.setItem(slot, item);
        }
    }

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

    // Set Click
    public final void setClick(int slot, Action action) {
        this.setClick(slot, new GUIClick(action));
    }

    public final void addClick(int slot, Action action, ClickType... types) {
        this.setClick(slot, action, types);
    }

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

    public final void setArrowBack(GUI guiTo) {
        this.setArrowBack(this.getSize() - 5, guiTo);
    }

    public final void setArrowBack(String to, Action action) {
        this.setArrowBack(this.getSize() - 5, to, action);
    }

    public final void setCloseMenuItem(int slot) {
        this.setItem(slot, new ItemBuilder(Material.BARRIER).setName("&cClose Menu").toItemStack());
        this.setClick(slot, HumanEntity::closeInventory);
    }

    public final void setCloseMenuItem() {
        this.setCloseMenuItem(this.getSize() - 4);
    }

    public final void setArrowBack(int slot, String to, Action action) {
        this.setItem(slot, new ItemBuilder(Material.ARROW).setName("&aGo Back").addLore("To " + to).toItemStack(), action);
    }

    public final void setArrowBack(int slot, GUI guiTo) {
        this.setItem(
                slot,
                new ItemBuilder(Material.ARROW).setName("&aGo Back").addLore("To " + guiTo.getName()).toItemStack(),
                guiTo::openInventory
        );
    }

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

    private GUIClick getOrNew(int slot, Action action) {
        return this.bySlot.getOrDefault(slot, new GUIClick(action));
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

    public final void fillItem(int start, int end, ItemStack item) {
        this.fillItem(start, end, item, null);
    }

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
     */
    public GUI fillColumn(int column, ItemStack stack) {
        Validate.notNullItemStack(stack);
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

    public final void fillItems(SmartComponent component, SlotPattern pattern, int startLine) {
        fillItems(component.getMap(), pattern, startLine);
    }

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

    public final SmartComponent fillItems() {
        return this.newSmartComponent();
    }

    public final SmartComponent newSmartComponent() {
        return new SmartComponent();
    }

    public static int getSmartMenuSize(Collection<?> collection) {
        return (int) Math.ceil((float) collection.size() / 5);
    }

    public static <T> int getSmartMenuSize(T[] collection) {
        return (int) Math.ceil((float) collection.length / 5);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public final void closeInventory(Player player) {
        removePlayerGUI(player);
        player.closeInventory();
    }

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
