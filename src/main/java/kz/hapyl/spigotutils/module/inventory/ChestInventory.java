package kz.hapyl.spigotutils.module.inventory;

import kz.hapyl.spigotutils.module.player.PlayerLib;
import kz.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * Advanced Chest Inventory builder
 */
@Deprecated(since = "2.1")
public final class ChestInventory {

    public static final Map<UUID, ChestInventory> perPlayerMenu = new HashMap<>();
    public static final Map<UUID, ChestInventory> perPlayerLastMenu = new HashMap<>();

    private int closeMenuSlot = 999;
    private int onEveryClickDelay;
    private int[] ignoreSlots = {};
    private int[] returnItems = {};

    private Inventory inventory;
    private final Map<Integer, ChestInventoryRunnable> runnablePerSlot = new HashMap<>();
    private final Map<Integer, ChestInventoryClick> eventPerSlot = new HashMap<>();
    private BiConsumer<InventoryClickEvent, ChestInventory> consumerClickEvent;
    private BiConsumer<Player, ChestInventory> atClose;
    private BiConsumer<Player, ChestInventory> atOpen;
    private BiConsumer<Player, ChestInventory> outside;
    private BiConsumer<Player, ChestInventory> onEveryClick;

    private boolean cancelClick = true;
    private boolean predicate = true;
    private boolean built;
    private boolean cancelOnlyMenu;
    private boolean ignoreCloseEvent;
    private boolean returnAllItems;
    private boolean allowDrag;
    private boolean allowShiftClick;
    private boolean allowShiftClickMenu;
    private boolean allowSwapClick;
    private boolean staticMenu;
    private boolean doubleClick;

    private Sound openSound, closeSound;
    private float openSoundPitch, closeSoundPitch;
    private final Map<Byte, Object> byteVariables = new HashMap<>();

    private static final ItemStack ITEM_CLOSE_MENU;

    static {
        ITEM_CLOSE_MENU = new ItemStack(Material.BARRIER);
        ItemMeta meta = ITEM_CLOSE_MENU.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Close Menu");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Click to close the menu."));
        ITEM_CLOSE_MENU.setItemMeta(meta);
    }

    private ChestInventory() {
    }

    /**
     * Constructor for a menu.
     *
     * @param displayName - Name of the menu.
     * @param rows        - Rows of slots inventory will have.
     */
    public ChestInventory(String displayName, int rows) {
        if (rows > 6 || rows < 0) {
            Bukkit.getLogger().warning("Too many or not enough slots!");
            return;
        }
        this.inventory = Bukkit.createInventory(
                null,
                rows * 9,
                ChatColor.translateAlternateColorCodes('&', displayName)
        );
    }

    /**
     * Returns items on provided slots whenever inventory is closed.
     *
     * @param slots - Separate slots with comma.
     */
    public ChestInventory returnItemsOnClose(int... slots) {
        this.returnItems = slots;
        return this;
    }

    public static final ItemStack ICON_LOADING = new ItemStack(Material.CLOCK);

    static {
        final ItemMeta itemMeta = ICON_LOADING.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + "Loading...");
        ICON_LOADING.setItemMeta(itemMeta);
    }

    public ChestInventory setPredicate(boolean predicate) {
        this.predicate = predicate;
        return this;
    }

    public boolean isPredicate() {
        return this.predicate;
    }

    public ChestInventory fillLoadingItem() {
        this.fill(ICON_LOADING);
        return this;
    }


    public ChestInventory allowSwapOffhandClick() {
        this.allowSwapClick = true;
        return this;
    }

    /**
     * If true, will ignore close event.
     */
    public void ignoreCloseEvent() {
        this.ignoreCloseEvent = true;
    }

    public ChestInventory removeCloseEvent() {
        this.atClose = null;
        return this;
    }

    /**
     * Provide a slot to put barrier 'Close Menu' item.
     *
     * @param slot - Slot to put at.
     */
    public ChestInventory setCloseMenuItem(int slot) {
        this.closeMenuSlot = slot;
        setItem(slot, ITEM_CLOSE_MENU);
        return this;
    }

    /**
     * Plays a sound whenever player opens the menu.
     *
     * @param sound  - Sound.
     * @param pitch- Pitch.
     */
    public ChestInventory addOpenSound(Sound sound, float pitch) {
        this.openSound = sound;
        this.openSoundPitch = pitch;
        return this;
    }

    /**
     * Plays a sound whenever player closes the menu.
     *
     * @param sound - Sound.
     * @param pitch - Pitch.
     */
    public ChestInventory addCloseSound(Sound sound, float pitch) {
        this.closeSound = sound;
        this.closeSoundPitch = pitch;
        return this;
    }

    /**
     * If used, you will be able to drag items while the menu is open. By default it's disabled to prevent item lost.
     */
    public ChestInventory allowDrag() {
        this.allowDrag = true;
        return this;
    }

    public ChestInventory allowDoubleClick() {
        this.doubleClick = true;
        return this;
    }

    public boolean isAllowDoubleClick() {
        return this.doubleClick;
    }

    /**
     * If used, you will be able to shift-click items while the menu is open. By default it's disabled to prevent item lost.
     */
    public ChestInventory allowShiftClick() {
        this.allowShiftClick = true;
        return this;
    }

    /**
     * If enabled, only click inside menu will be cancelled, compare to cancelling any clicks.
     */
    public ChestInventory cancelOnlyMenuClicks() {
        this.cancelOnlyMenu = true;
        return this;
    }

    /**
     * Provide slots that will be ignored by the 'ignoreClicks', player will be able to put and take items from these slots.
     *
     * @param slots - Slot(s).
     */
    public ChestInventory setIgnoreCancelSlots(int... slots) {
        this.ignoreSlots = slots;
        return this;
    }

    /**
     * Sets an item to certain slot. Item's used as icon only, clicks based on slots.
     *
     * @param slot - Slot to put in.
     * @param item - ItemStack.
     */
    public ChestInventory setItem(int slot, ItemStack item) {
        return setItem(slot, item, (ChestInventoryClick) null);
    }

    public ChestInventory smartItemFill(Collection<ItemStack> items, int startAtLine) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Cannot smart fill empty collection!");
        }

        final List<Integer> list = this.convertBytesIntoSlots(this.calculateBytes(items), startAtLine);
        if (list.isEmpty()) {
            return this;
        }

        final Iterator<ItemStack> iterator = items.iterator();
        for (final Integer integer : list) {
            if (iterator.hasNext()) {
                this.setItem(integer, iterator.next());
            }
        }

        return this;

    }

    public static List<Integer> convertItemsIntoSmartSlots(Collection<ItemStack> stack, int startAtLine) {
        final ChestInventory dummy = new ChestInventory();
        return dummy.convertBytesIntoSlots(dummy.calculateBytes(stack), startAtLine);
    }

    private List<Integer> convertBytesIntoSlots(byte[][] bytes, int startAtLine) {
        final List<Integer> slots = new ArrayList<>();

        for (int i = 0; i < bytes.length; i++) {
            for (int j = 0; j < 9; ++j) {
                if (bytes[i][j] == 1) {
                    slots.add(j + (9 * (i + startAtLine)));
                }
            }
        }
        return slots;
    }

    private final byte[][] slotPattern = {
            { 0, 0, 0, 0, 1, 0, 0, 0, 0 },  // 1
            { 0, 0, 0, 1, 0, 1, 0, 0, 0 },  // 2
            { 0, 0, 1, 0, 1, 0, 1, 0, 0 },  // 3
            { 0, 0, 1, 1, 0, 1, 1, 0, 0 },  // 4
            { 0, 0, 1, 1, 1, 1, 1, 0, 0 }   // 5
    };

    private byte[][] calculateBytes(Collection<?> items) {
        int size = items.size();
        final int sizeScaled = (int) Math.ceil((float) size / 5);
        byte[][] bytes = new byte[sizeScaled][9];

        int line = 0;
        int count = 0;

        for (int i = 0; i < size; i++) {
            if (count >= 4) {
                bytes[line] = this.slotPattern[count];
                ++line;
                count = 0;
                continue;
            }
            if (i == (size - 1)) {
                bytes[line] = this.slotPattern[count];
            }
            ++count;
        }
        return bytes;
    }


    /**
     * Sets an item to certain slot, and adds click even to the slot. Item's used as icon only, clicks based on slots.
     *
     * @param slot         - Slot to put in.
     * @param item         - ItemStack.
     * @param playerAction - Click action.
     */
    public ChestInventory setItem(int slot, ItemStack item, BiConsumer<Player, ChestInventory> playerAction) {
        this.setItem(slot, item);
        this.setClickEvent(slot, playerAction);
        return this;
    }

    public static final ClickType[] CLICK_LEFT = { ClickType.LEFT, ClickType.SHIFT_LEFT };
    public static final ClickType[] CLICK_RIGHT = { ClickType.RIGHT, ClickType.SHIFT_RIGHT };

    public ChestInventory setItem(int slot, ItemStack stack, Runnable run, ClickType... types) {
        this.setItem(slot, stack);
        this.setClickEvent(slot, run, types);
        return this;
    }

    /**
     * Sets an item to certain slot, and adds click even to the slot per certain click. Item's used as icon only, clicks based on slots.
     *
     * @param slot         - Slot to put in.
     * @param item         - ItemStack.
     * @param playerAction - Click action.
     */
    public ChestInventory setItem(int slot, ItemStack item, BiConsumer<Player, ChestInventory> playerAction, ClickType... type) {
        this.setItem(slot, item);
        this.setClickEvent(slot, playerAction, type);
        return this;
    }

    public ChestInventory setClickEvent(int slot, Runnable runnable) {
        this.runnablePerSlot.put(slot, new ChestInventoryRunnable(runnable));
        return this;
    }

    public ChestInventory setClickEvent(int slot, Runnable runnable, ClickType... types) {
        if (this.staticMenu && !this.built) {
            return this;
        }
        if (runnablePerSlot.containsKey(slot) && types.length > 0) {
            final ChestInventoryRunnable old = runnablePerSlot.get(slot);
            old.addClick(runnable, types);
            runnablePerSlot.put(slot, old);
        }
        else {
            runnablePerSlot.put(slot, new ChestInventoryRunnable(runnable, types));
        }
        return this;
    }

    /**
     * Sets click event to certain slot. This will not affect the item if there is one.
     *
     * @param slot         - Slot to bind click to.
     * @param playerAction - Click action.
     */
    public ChestInventory setClickEvent(int slot, BiConsumer<Player, ChestInventory> playerAction, ClickType... type) {
        if (this.staticMenu && !this.built) {
            return this;
        }
        if (eventPerSlot.containsKey(slot) && type.length > 0) {
            final ChestInventoryClick old = eventPerSlot.get(slot);
            old.addClick(playerAction, type);
            eventPerSlot.put(slot, old);
        }
        else {
            eventPerSlot.put(slot, new ChestInventoryClick(playerAction, type));
        }
        return this;
    }

    /**
     * Sets a material to certain slot. Item's used as icon only, clicks based on slots.
     *
     * @param slot - Slot to put at.
     * @param item - Material.
     */
    public ChestInventory setItem(int slot, Material item) {
        return setItem(slot, new ItemStack(item), (ChestInventoryClick) null);
    }

    /**
     * Puts an item to the slot, 'complicated' version.
     *
     * @param slot   - Slot to put in.
     * @param item   - ItemStack.
     * @param action - ClickAction.
     */
    public ChestInventory setItem(int slot, ItemStack item, ChestInventoryClick action) {
        if (this.staticMenu && !this.built) {
            return this;
        }
        if (this.getSize() < slot) {
            throw new IndexOutOfBoundsException("There is only " + this.getSize() + " slots! Given " + slot);
        }
        if (action != null) {
            this.setClickEvent(slot, action);
        }
        this.inventory.setItem(slot, item);
        return this;
    }

    public ChestInventory staticMenu() {
        this.staticMenu = true;
        return this;
    }

    /**
     * @return - Inventory size.
     */
    public int getSize() {
        return this.inventory.getSize();
    }

    /**
     * @return - Inventory size / 9.
     */
    public int getRows() {
        return this.getSize() / 9;
    }

    /**
     * Removes item from certain slot. Item's used as icon only, clicks based on slots. To remove click action use 'removeClickEvent'
     *
     * @param slot - Slot to remove from.
     */
    public ChestInventory removeItem(int slot) {
        this.inventory.setItem(slot, null);
        return this;
    }

    /**
     * If used, menu won't allow you to take or put item's in it.
     */
    public ChestInventory cancelClick(boolean t) {
        this.cancelClick = t;
        return this;
    }

    /**
     * Fills entire inventory with an item.
     *
     * @param item - Item to fill with.
     */
    public ChestInventory fill(ItemStack item) {
        for (int i = 0; i < this.inventory.getSize(); i++) {
            setItem(i, item);
        }
        return this;
    }

    /**
     * Fills items between start and stop slots.
     *
     * @param start - Start index.
     * @param stop  - Stop index.
     * @param item  - Item to fill with.
     */
    public ChestInventory fill(int start, int stop, ItemStack item) {
        for (int i = start; i <= stop; i++) {
            setItem(i, item);
        }
        return this;
    }

    /**
     * Fills a line (9 horizontal slots) of the chest.
     *
     * @param line  - Line
     * @param stack - Item to fil
     */
    public ChestInventory fillLine(int line, ItemStack stack) {
        line = Validate.ifTrue(line < 0, 0, line);
        line = Validate.ifTrue(line > this.getSize() / 9, this.getSize() / 9, line);

        for (int i = 0; i < this.getSize(); ++i) {
            if (i / 9 == line) {
                this.setItem(i, stack);
            }
        }
        return this;
    }

    public ChestInventory fillColumn(int column, ItemStack stack) {
        Validate.notNullItemStack(stack);
        column = Validate.ifTrue(column > 8, 8, column);

        for (int i = 0; i < this.getSize(); ++i) {
            if (i % 9 == column) {
                this.setItem(i, stack);
            }
        }
        return this;
    }

    public ChestInventory fillBorder(ItemStack item) {
        Validate.notNullItemStack(item);

        for (int i = 0; i < this.inventory.getSize(); i++) {
            if ((i > 0 && i < 9) || (i > this.inventory.getSize() - 9 && i < this.inventory.getSize() - 1) || (i % 9 == 0) || (i % 9 == 8)) {
                this.inventory.setItem(i, item);
            }
        }
        return this;
    }

    public ChestInventory fillInner(ItemStack item) {
        Validate.notNullItemStack(item);

        for (int i = 10; i < this.getInventory().getSize() - 10; i++) {
            if ((i % 9 != 0) && (i % 9 != 8)) {
                this.setItem(i, item);
            }
        }

        return this;
    }

    /**
     * Removes click event from certain slot. This will not affect the item if there is one.
     *
     * @param slot - Slot to remove from.
     */
    public ChestInventory removeClickEvent(int slot) {
        eventPerSlot.remove(slot);
        return this;
    }

    /**
     * Removes all click event. This will not affect the item if there is one.
     *
     * @return
     */
    public ChestInventory removeClickEvents() {
        eventPerSlot.clear();
        return this;
    }

    /**
     * Sets click event to certain slot.
     *
     * @param slot  - Slot to set to.
     * @param event - ClickAction.
     */
    public ChestInventory setClickEvent(int slot, ChestInventoryClick event) {
        eventPerSlot.put(slot, event);
        return this;
    }

    public ChestInventory setEventListener(BiConsumer<InventoryClickEvent, ChestInventory> event) {
        this.consumerClickEvent = event;
        return this;
    }

    /**
     * If player click outside of menu, this will trigger.
     *
     * @param event - Consumer.
     */
    public ChestInventory setClickOutsideMenuEvent(BiConsumer<Player, ChestInventory> event) {
        this.outside = event;
        return this;
    }

    /**
     * Triggers whenever player closes the inventory.
     *
     * @param event - Consumer.
     */
    public ChestInventory setCloseEvent(BiConsumer<Player, ChestInventory> event) {
        this.atClose = event;
        return this;
    }

    public ChestInventory setOpenEvent(BiConsumer<Player, ChestInventory> event) {
        this.atOpen = event;
        return this;
    }

    /**
     * If used, all items in the inventory will be returned to a player whenever they close inventory.
     */
    public ChestInventory returnAllItems() {
        this.returnItems = new int[] {};
        this.returnAllItems = true;
        return this;
    }

    /**
     * Add a consumer that will trigger whenever player click anywhere in the inventory.
     *
     * @param click - Consumer.
     */
    public ChestInventory onEveryClick(BiConsumer<Player, ChestInventory> click) {
        this.onEveryClick = click;
        this.onEveryClickDelay = 1;
        return this;
    }

    /**
     * Add a consumer that will trigger whenever player click anywhere in the inventory with a delay.
     *
     * @param click - Consumer.
     * @param delay - Delay in ticks.
     */
    public ChestInventory onEveryClick(BiConsumer<Player, ChestInventory> click, int delay) {
        this.onEveryClick = click;
        this.onEveryClickDelay = delay;
        return this;
    }

    // 1.1 - Variables!
    // Store up to 127 data values.
    @Nullable
    public Object getVariable(int memorySlot, Object defaultValue) {
        isInLimits(memorySlot);
        return byteVariables.getOrDefault((byte) memorySlot, defaultValue);
    }

    public void setVariable(int memorySlot, Object value) {
        isInLimits(memorySlot);
        byteVariables.put((byte) memorySlot, value);
    }

    public void wipeAllData(boolean youSure) {
        if (youSure) {
            byteVariables.clear();
        }
    }

    private void isInLimits(int i) {
        if (i > 127 || i < 0) {
            throw new IndexOutOfBoundsException("Memory slot must be greater than 0 and not higher than 127!");
        }
    }

    /**
     * Builds the inventory so it can be opened and used. Make sure to build inventory before opening to a player.
     *
     * @deprecated openInventory() now builds inventory if it wasn't
     */
    @Deprecated
    public ChestInventory build() {
        return this.build(false);
    }

    /**
     * @deprecated openInventory() now builds inventory if it wasn't
     */
    @Deprecated
    public ChestInventory build(boolean makeStatic) {
        this.built = true;
        this.staticMenu = makeStatic;
        return this;
    }

    /**
     * Opens the inventory to a player.
     *
     * @param player - Player to open to.
     */
    public void openInventory(Player player) {

        if (!this.predicate) {
            PlayerLib.playSoundMessage(
                    player,
                    Sound.ENTITY_VILLAGER_NO,
                    1.0f,
                    "&cYou cannot access this menu right now!"
            );
            return;
        }

        if (!this.built) {
            this.build(false);
        }

        final UUID uuid = player.getUniqueId();
        final ChestInventory lastMenu = ChestInventory.perPlayerLastMenu.get(uuid);
        if (lastMenu != null) {
            ChestInventory.perPlayerLastMenu.remove(uuid);
        }
        perPlayerMenu.put(uuid, this);
        player.openInventory(this.inventory);
    }

    // static members

    @Nullable
    public static ChestInventory getLastMenu(Player player) {
        return perPlayerLastMenu.getOrDefault(player.getUniqueId(), null);
    }

    @Nullable
    public static ChestInventory getPlayerInventory(Player player) {
        return perPlayerMenu.getOrDefault(player.getUniqueId(), null);
    }

    boolean isValidMenu(Inventory sample) {
        return this.inventory != null && sample != null && this.inventory == sample;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public ChestInventory allowShiftClickMenuOnly() {
        this.allowShiftClickMenu = true;
        return this;
    }

    // utils

    public boolean isSlotIgnored(int slot) {
        if (ignoreSlots.length == 0) {
            return false;
        }
        else {
            for (int ignoreSlot : this.ignoreSlots) {
                if (ignoreSlot == slot) {
                    return true;
                }
            }
        }
        return false;
    }

    private static String format(String arg0, Object... arg1) {
        return String.format(ChatColor.translateAlternateColorCodes('&', arg0), arg1);
    }

    // getters

    public BiConsumer<InventoryClickEvent, ChestInventory> getConsumerClickEvent() {
        return consumerClickEvent;
    }

    public int getCloseMenuSlot() {
        return closeMenuSlot;
    }

    public int getOnEveryClickDelay() {
        return onEveryClickDelay;
    }

    public int[] getIgnoreSlots() {
        return ignoreSlots;
    }

    public int[] getReturnItems() {
        return returnItems;
    }

    public Map<Integer, ChestInventoryRunnable> getRunnablePerSlot() {
        return runnablePerSlot;
    }

    public Map<Integer, ChestInventoryClick> getEventPerSlot() {
        return eventPerSlot;
    }

    public BiConsumer<Player, ChestInventory> getAtClose() {
        return atClose;
    }

    public BiConsumer<Player, ChestInventory> getAtOpen() {
        return atOpen;
    }

    public BiConsumer<Player, ChestInventory> getOutside() {
        return outside;
    }

    public BiConsumer<Player, ChestInventory> getOnEveryClick() {
        return onEveryClick;
    }

    public boolean isCancelClick() {
        return cancelClick;
    }

    public boolean isBuilt() {
        return built;
    }

    public boolean isCancelOnlyMenu() {
        return cancelOnlyMenu;
    }

    public boolean isIgnoreCloseEvent() {
        return ignoreCloseEvent;
    }

    public boolean isReturnAllItems() {
        return returnAllItems;
    }

    public boolean isAllowDrag() {
        return allowDrag;
    }

    public boolean isAllowShiftClick() {
        return allowShiftClick;
    }

    public boolean isAllowShiftClickMenu() {
        return allowShiftClickMenu;
    }

    public boolean isAllowSwapClick() {
        return allowSwapClick;
    }

    public boolean isStaticMenu() {
        return staticMenu;
    }

    public Sound getOpenSound() {
        return openSound;
    }

    public Sound getCloseSound() {
        return closeSound;
    }

    public float getOpenSoundPitch() {
        return openSoundPitch;
    }

    public float getCloseSoundPitch() {
        return closeSoundPitch;
    }

    public Map<Byte, Object> getByteVariables() {
        return byteVariables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ChestInventory that = (ChestInventory) o;
        return closeMenuSlot == that.closeMenuSlot &&
                onEveryClickDelay == that.onEveryClickDelay &&
                cancelClick == that.cancelClick &&
                built == that.built &&
                cancelOnlyMenu == that.cancelOnlyMenu &&
                ignoreCloseEvent == that.ignoreCloseEvent &&
                returnAllItems == that.returnAllItems &&
                allowDrag == that.allowDrag &&
                allowShiftClick == that.allowShiftClick &&
                allowShiftClickMenu == that.allowShiftClickMenu &&
                allowSwapClick == that.allowSwapClick &&
                staticMenu == that.staticMenu &&
                Float.compare(that.openSoundPitch, openSoundPitch) == 0 &&
                Float.compare(that.closeSoundPitch, closeSoundPitch) == 0 &&
                Arrays.equals(ignoreSlots, that.ignoreSlots) &&
                Arrays.equals(returnItems, that.returnItems) &&
                Objects.equals(inventory, that.inventory) &&
                Objects.equals(runnablePerSlot, that.runnablePerSlot) &&
                Objects.equals(eventPerSlot, that.eventPerSlot) &&
                Objects.equals(consumerClickEvent, that.consumerClickEvent) &&
                Objects.equals(atClose, that.atClose) &&
                Objects.equals(atOpen, that.atOpen) &&
                Objects.equals(outside, that.outside) &&
                Objects.equals(onEveryClick, that.onEveryClick) &&
                openSound == that.openSound &&
                closeSound == that.closeSound &&
                Objects.equals(byteVariables, that.byteVariables);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(
                closeMenuSlot,
                onEveryClickDelay,
                inventory,
                runnablePerSlot,
                eventPerSlot,
                consumerClickEvent,
                atClose,
                atOpen,
                outside,
                onEveryClick,
                cancelClick,
                built,
                cancelOnlyMenu,
                ignoreCloseEvent,
                returnAllItems,
                allowDrag,
                allowShiftClick,
                allowShiftClickMenu,
                allowSwapClick,
                staticMenu,
                openSound,
                closeSound,
                openSoundPitch,
                closeSoundPitch,
                byteVariables
        );
        result = 31 * result + Arrays.hashCode(ignoreSlots);
        result = 31 * result + Arrays.hashCode(returnItems);
        return result;
    }
}
