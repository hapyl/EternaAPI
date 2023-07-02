package me.hapyl.spigotutils.module.inventory.gui;

import com.google.common.collect.Lists;
import me.hapyl.spigotutils.module.inventory.ItemBuilder;
import me.hapyl.spigotutils.module.math.Numbers;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a GUI that may have multiple pages.
 *
 * @param <T> The type of the contents.
 */
public abstract class PlayerPageGUI<T> extends PlayerGUI {

    private Fit fit;
    private LinkedList<T> contents;
    private int startRow;
    private int endRow;
    private int previousPageSlot;
    private int nextPageSlot;
    @Nullable
    private ItemStack emptyContentsItem;
    private int emptyContentsSlot;

    public PlayerPageGUI(Player player, String name, int rows) {
        super(player, name, rows);

        final int size = rows * 9;

        this.contents = Lists.newLinkedList();
        this.fit = Fit.SLIM;
        this.startRow = 1;
        this.endRow = rows - 1;
        this.previousPageSlot = size - 7;
        this.nextPageSlot = size - 3;
        this.emptyContentsItem = ItemBuilder.of(Material.MINECART, "Empty").asIcon();

        this.emptyContentsSlot = switch (rows) {
            case 6, 5, 4 -> 22;
            case 3, 2 -> 13;
            default -> 4;
        };
    }

    /**
     * Returns current slot for empty contents item.
     *
     * @return current slot for empty contents item.
     */
    public int getEmptyContentsSlot() {
        return emptyContentsSlot;
    }

    /**
     * Sets current slot for empty contents item.
     * Will be clamped between 0 and getSize()
     *
     * @param emptyContentsSlot - New slot.
     */
    public void setEmptyContentsSlot(int emptyContentsSlot) {
        this.emptyContentsSlot = Numbers.clamp(emptyContentsSlot, 0, getSize());
    }

    /**
     * Returns item that will be displayed when the contents are empty.
     *
     * @return The item that will be displayed when the contents are empty.
     */
    @Nullable
    public ItemStack getEmptyContentsItem() {
        return emptyContentsItem;
    }

    /**
     * Sets the item that will be displayed when the contents are empty.
     * Set to null to display nothing.
     *
     * @param emptyContentsItem - The item that will be displayed when the contents are empty.
     */
    public void setEmptyContentsItem(@Nullable ItemStack emptyContentsItem) {
        this.emptyContentsItem = emptyContentsItem;
    }

    /**
     * Returns current contents of the GUI.
     *
     * @return The current contents of the GUI.
     */
    @Nonnull
    public LinkedList<T> getContents() {
        return contents;
    }

    /**
     * Sets the contents of the GUI.
     *
     * @param contents - The contents of the GUI.
     * @see #setContentsAsCopyOf(LinkedList)
     */
    public void setContents(@Nonnull LinkedList<T> contents) {
        this.contents = contents;
    }

    public void setContents(@Nonnull List<T> contents) {
        setContents(Lists.newLinkedList(contents));
    }

    /**
     * Sets the contents of the GUI as copy of another list.
     *
     * @param contents - The contents of the GUI.
     */
    public void setContentsAsCopyOf(@Nonnull LinkedList<T> contents) {
        setContents(Lists.newLinkedList(contents));
    }

    public void setContentsAsCopyOf(@Nonnull List<T> contents) {
        setContentsAsCopyOf(Lists.newLinkedList(contents));
    }

    /**
     * Returns the maximum number of items that can fit in a page.
     *
     * @return The maximum number of items that can fit in a page.
     */
    public int maxItemsPerPage() {
        return (endRow - startRow) * fit.canFit;
    }

    /**
     * Returns how ItemStack should be made.
     *
     * @param player  - Player, who is viewing the GUI.
     * @param content - The content.
     * @param index   - Index of the content between 0 and {@link #maxItemsPerPage()}.
     * @param page    - Page of the content between 1 and {@link #getMaxPage()}.
     * @return The ItemStack that will be displayed.
     */
    @Nonnull
    public abstract ItemStack asItem(Player player, T content, int index, int page);

    /**
     * Executed when a player clicks on an item.
     *
     * @param player  - Player, who clicked the item.
     * @param content - The content.
     * @param index   - Index of the content between 0 and {@link #maxItemsPerPage()}.
     * @param page    - Page of the content between 1 and {@link #getMaxPage()}.
     */
    @Deprecated
    public void onClick(@Nonnull Player player, @Nonnull T content, int index, int page) {
    }

    /**
     * Executed when a player clicks on an item.
     *
     * @param player    - PLayer who clicked.
     * @param content   - The content that was clicked on.
     * @param index     - Index of the content between 0 and {@link #maxItemsPerPage()}.
     * @param page      - Page of the content between 1 and {@link #getMaxPage()}.
     * @param clickType - Click type.
     */
    public void onClick(@Nonnull Player player, @Nonnull T content, int index, int page, @Nonnull ClickType clickType) {
    }

    /**
     * Executed whenever player changes page.
     *
     * @param player       - Player, who changed the page.
     * @param previousPage - Previous page.
     * @param newPage      - New page.
     */
    public void onPageChange(Player player, int previousPage, int newPage) {
    }

    /**
     * Opens the inventory to the player.
     *
     * @param page - Page to open at. The first page is 1.
     */
    public final void openInventory(int page) {
        final int toPage = Numbers.clamp(page, 1, getMaxPage());
        clearEverything();

        if (contents.isEmpty()) {
            if (emptyContentsItem != null) {
                setItem(emptyContentsSlot, emptyContentsItem);
            }

            postProcessInventory(player, page);
            super.openInventory();
            return;
        }

        final int maxItemsPerPage = maxItemsPerPage();
        final SmartComponent component = newSmartComponent();

        // Arrow previous arrows
        if (toPage > 1) {
            setItem(previousPageSlot, ItemBuilder.of(Material.ARROW, "Previous Page").asIcon(), player -> {
                openInventory(toPage - 1);
                onPageChange(player, toPage, toPage - 1);
            });
        }

        // Arrow next arrows
        if (toPage < getMaxPage()) {
            setItem(nextPageSlot, ItemBuilder.of(Material.ARROW, "Next Page").asIcon(), player -> {
                openInventory(toPage + 1);
                onPageChange(player, toPage, toPage + 1);
            });
        }

        // Setup items
        for (int i = 0; i < maxItemsPerPage; i++) {
            final int index = (toPage - 1) * maxItemsPerPage + i;
            if (index >= contents.size()) {
                break;
            }

            final int finalI = i;

            final T t = contents.get(index);
            final ItemStack item = asItem(getPlayer(), t, finalI, page);

            for (ClickType type : GUI.getAllowedClickTypes()) {
                component.add(item, clicked -> onClick0(clicked, t, finalI, page, type), type);
            }
        }

        component.apply(this, fit.pattern, startRow);
        postProcessInventory(player, page);

        super.openInventory();
    }

    /**
     * Called after applying the component, but before opening the inventory.
     *
     * @param player - Player.
     * @param page   - Current page.
     */
    public void postProcessInventory(Player player, int page) {
    }

    /**
     * Open the first page to the player.
     */
    @Override
    @Deprecated
    public final void openInventory() {
        openInventory(1);
    }

    /**
     * Returns current fit of the GUI.
     *
     * @return The current fit of the GUI.
     */
    @Nonnull
    public Fit getFit() {
        return fit;
    }

    /**
     * Changes the fit of the GUI.
     *
     * @param fit - New fit.
     */
    public void setFit(@Nonnull Fit fit) {
        this.fit = fit;
    }

    /**
     * Returns the start row of the contents.
     *
     * @return The start row of the contents.
     */
    public int getStartRow() {
        return startRow;
    }

    /**
     * Sets the start row of the contents.
     * Will be clamped between 1 and getRows().
     *
     * @param startRow - The start row of the contents.
     */
    public void setStartRow(int startRow) {
        this.startRow = Numbers.clamp(startRow, 1, getRows());
    }

    /**
     * Returns the end row of the contents.
     *
     * @return The end row of the contents.
     */
    public int getEndRow() {
        return endRow;
    }

    /**
     * Sets the end row of the contents.
     * Will be clamped between startRow + 1 and getRows().
     *
     * @param endRow - The end row of the contents.
     */
    public void setEndRow(int endRow) {
        this.endRow = Numbers.clamp(endRow, startRow + 1, getRows());
    }

    /**
     * Returns the slot where the previous page button is located.
     *
     * @return The slot where the previous page button is located.
     */
    public int getPreviousPageSlot() {
        return previousPageSlot;
    }

    /**
     * Sets the slot where previous page button is located.
     *
     * @param previousPageSlot - The slot where the previous page button is located.
     */
    public void setPreviousPageSlot(int previousPageSlot) {
        this.previousPageSlot = Numbers.clamp(previousPageSlot, 0, getSize() - 1);
    }

    /**
     * Returns the slot where the next page button is located.
     *
     * @return The slot where the next page button is located.
     */
    public int getNextPageSlot() {
        return nextPageSlot;
    }

    /**
     * Sets the slot where next page button is located.
     *
     * @param nextPageSlot - The slot where next page button is located.
     */
    public void setNextPageSlot(int nextPageSlot) {
        this.nextPageSlot = Numbers.clamp(nextPageSlot, 0, getSize() - 1);
    }

    /**
     * Returns max page of the GUI.
     *
     * @return Max page of the GUI.
     */
    public int getMaxPage() {
        return (int) Math.ceil((double) contents.size() / maxItemsPerPage());
    }

    private void onClick0(Player clicked, T t, int finalI, int page, ClickType type) {
        onClick(clicked, t, finalI, page);
        onClick(clicked, t, finalI, page, type);
    }

    /**
     * Represents a Fit or a pattern of the GUI.
     */
    public enum Fit {
        THIN(3, new SlotPattern(new byte[][] {
                { 0, 0, 0, 1, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 1, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 1, 1, 0, 0, 0 },
        })),
        SLIMMER(5, new SlotPattern(new byte[][] {
                { 0, 0, 1, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 1, 1, 0, 0, 0, 0, 0 },
                { 0, 0, 1, 1, 1, 0, 0, 0, 0 },
                { 0, 0, 1, 1, 1, 1, 0, 0, 0 },
                { 0, 0, 1, 1, 1, 1, 1, 0, 0 },
        })),
        SLIM(7, SlotPattern.INNER_LEFT_TO_RIGHT),
        NORMAL(9, SlotPattern.LEFT_TO_RIGHT);

        private final int canFit;
        private final SlotPattern pattern;

        Fit(int canFit, SlotPattern pattern) {
            this.canFit = canFit;
            this.pattern = pattern;
        }

        public int getCanFit() {
            return canFit;
        }

        public SlotPattern getPattern() {
            return pattern;
        }
    }

}
