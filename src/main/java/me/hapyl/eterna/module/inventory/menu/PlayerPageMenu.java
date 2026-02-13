package me.hapyl.eterna.module.inventory.menu;

import com.google.common.collect.Lists;
import me.hapyl.eterna.module.annotate.DefensiveCopy;
import me.hapyl.eterna.module.annotate.EventLike;
import me.hapyl.eterna.module.inventory.builder.ItemBuilder;
import me.hapyl.eterna.module.inventory.menu.action.PlayerMenuAction;
import me.hapyl.eterna.module.inventory.menu.pattern.SlotPattern;
import me.hapyl.eterna.module.inventory.menu.pattern.SlotPatternApplier;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a {@link PlayerMenu} with native page support via {@link SlotPattern}.
 *
 * <p><b>Only supports {@link PlayerMenuType#chest(ChestSize)}.</b></p>
 *
 * @param <T> - The element type.
 */
public abstract class PlayerPageMenu<T> extends PlayerMenu {
    
    /**
     * Defines the default {@link ItemStack} used as "Next Page".
     */
    @NotNull
    public static final ItemStack ITEM_ARROW_NEXT = ItemBuilder.playerHead("19bf3292e126a105b54eba713aa1b152d541a1d8938829c56364d178ed22bf")
                                                               .setName(Component.text("Next Page"))
                                                               .asIcon();
    
    /**
     * Defines the default {@link ItemStack} used as "Previous Page".
     */
    @NotNull
    public static final ItemStack ITEM_ARROW_PREVIOUS = ItemBuilder.playerHead("bd69e06e5dadfd84e5f3d1c21063f2553b2fa945ee1d4d7152fdc5425bc12a9")
                                                                   .setName(Component.text("Previous Page"))
                                                                   .asIcon();
    
    /**
     * Defines the default {@link ItemStack} used as "Empty!".
     */
    @NotNull
    public static final ItemStack ITEM_EMPTY_CONTENTS = ItemBuilder.playerHead("7df0ee9d25b41cb645dd2fe5c7746cbb8a1d37fd3e01e25e013242f9d03a30d6")
                                                                   .setName(Component.text("Empty!"))
                                                                   .asItemStack();
    
    @NotNull private SlotPattern pattern;
    @NotNull private LinkedList<T> contents;
    
    @NotNull private ChestSize from;
    @NotNull private ChestSize to;
    
    @NotNull private SlotBoundItemStack itemArrowNext;
    @NotNull private SlotBoundItemStack itemArrowPrevious;
    @NotNull private SlotBoundItemStack itemEmptyContents;
    
    private int currentPage;
    
    /**
     * Creates a new {@link PlayerPageMenu}.
     *
     * @param player - The player owning this menu.
     * @param title  - The title of this menu.
     * @param size   - The size of this menu.
     */
    public PlayerPageMenu(@NotNull Player player, @NotNull PlayerMenuTitle title, @NotNull ChestSize size) {
        super(player, title, PlayerMenuType.chest(size));
        
        final int inventorySize = super.getMenuSize();
        
        this.itemArrowNext = SlotBoundItemStack.of(inventorySize - 3, ITEM_ARROW_NEXT);
        this.itemArrowPrevious = SlotBoundItemStack.of(inventorySize - 7, ITEM_ARROW_PREVIOUS);
        this.itemEmptyContents = SlotBoundItemStack.of(size.getCentre(), ITEM_EMPTY_CONTENTS);
        
        this.contents = Lists.newLinkedList();
        this.pattern = SlotPattern.DEFAULT;
        this.from = ChestSize.SIZE_1;
        this.to = size.previous();
        this.currentPage = 1;
    }
    
    /**
     * Sets the contents of this {@link PlayerPageMenu}.
     *
     * <p>The contents will be defensively copied before settings.</p>
     *
     * @param contents - The contents to set.
     */
    public void setContents(@NotNull @DefensiveCopy List<T> contents) {
        this.contents = Lists.newLinkedList(contents);
    }
    
    /**
     * Gets the {@link ItemStack} for the given {@link T} element.
     *
     * @param player - The player owning this menu.
     * @param t      - The content.
     * @param index  - The content index, from {@code 0} - {@link #getMaximumItemsPerPage()}.
     * @param page   - The current page, starting from {@code 1}.
     * @return the item stack for the given {@link T} element.
     */
    @NotNull
    public abstract ItemStack asItem(@NotNull Player player, @NotNull T t, int index, int page);
    
    /**
     * Called whenever the {@link Player} clicks at the given {@link T} element.
     *
     * @param player    - The player who clicked.
     * @param t         - The clicked content.
     * @param index     - The clicked content index, from {@code 0} - {@link #getMaximumItemsPerPage()}.
     * @param page      - The current page, starting from {@code 1}.
     * @param clickType - The click type used.
     */
    @EventLike
    public void onClick(@NotNull Player player, @NotNull T t, int index, int page, @NotNull ClickType clickType) {
    }
    
    /**
     * Called whenever a page changes.
     *
     * @param player       - The player who changed the page.
     * @param previousPage - The previous page, starting from {@code 1}.
     * @param newPage      - The current page, starting from {@code 1}.
     */
    @EventLike
    public void onPageChange(@NotNull Player player, int previousPage, int newPage) {
    }
    
    /**
     * Updates this {@link PlayerPageMenu}.
     */
    @Override
    @OverridingMethodsMustInvokeSuper
    public void onOpen() {
        if (contents.isEmpty()) {
            this.setItem(itemEmptyContents.slot(), itemEmptyContents.itemStack(), PlayerMenuAction.of(HumanEntity::closeInventory));
            return;
        }
        
        final int maxItemsPerPage = getMaximumItemsPerPage();
        final SlotPatternApplier component = newSlotPatternApplier(pattern, from, to);
        
        // Set "Previous Page" arrow
        if (currentPage > 1) {
            final ItemStack itemStack = itemArrowPrevious.itemStack();
            final int previousPage = currentPage - 1;
            
            this.setItem(itemArrowPrevious.slot(), itemStack, PlayerMenuAction.of(player -> {
                this.openInventory(previousPage);
                this.onPageChange(player, currentPage, previousPage);
            }));
        }
        
        // Set "Next Page" arrow
        if (currentPage < getMaximumPages()) {
            final ItemStack itemStack = itemArrowNext.itemStack();
            final int nextPage = currentPage + 1;
            
            this.setItem(itemArrowNext.slot(), itemStack, PlayerMenuAction.of(player -> {
                this.openInventory(nextPage);
                this.onPageChange(player, currentPage, nextPage);
            }));
        }
        
        // Setup items
        for (int i = 0; i < maxItemsPerPage; i++) {
            final int index = (currentPage - 1) * maxItemsPerPage + i;
            
            if (index >= contents.size()) {
                break;
            }
            
            final int finalI = i;
            
            final T t = contents.get(index);
            final ItemStack item = asItem(player, t, finalI, currentPage);
            
            component.add(item, (menu, player, clickType, slot, hotbarNumber) -> PlayerPageMenu.this.onClick(player, t, finalI, currentPage, clickType));
        }
        
        component.apply();
    }
    
    /**
     * Opens this {@link PlayerPageMenu} at the current page.
     */
    @Override
    public void openMenu() {
        super.openMenu();
    }
    
    /**
     * Opens this {@link PlayerPageMenu} at the given {@code page}.
     *
     * @param page - The page to open, starting from {@code 1}.
     */
    public final void openInventory(@Range(from = 1, to = Integer.MAX_VALUE) int page) {
        this.currentPage = page;
        this.openMenu();
    }
    
    /**
     * Gets the current page, starting from {@code 1}.
     *
     * @return the current page, starting from {@code 1}.
     */
    @Range(from = 1, to = Integer.MAX_VALUE)
    public int currentPage() {
        return currentPage;
    }
    
    /**
     * Gets the {@link SlotPattern}.
     *
     * @return the slot pattern.
     */
    @NotNull
    public SlotPattern getPattern() {
        return pattern;
    }
    
    /**
     * Sets the {@link SlotPattern}.
     *
     * @param pattern - The pattern to set.
     */
    public void setPattern(@NotNull SlotPattern pattern) {
        this.pattern = pattern;
    }
    
    /**
     * Gets the {@code from} index.
     *
     * @return the {@code from} index.
     */
    @NotNull
    public ChestSize getFrom() {
        return from;
    }
    
    /**
     * Sets the {@code from} index.
     *
     * @param from - The index to set.
     * @throws IllegalArgumentException if the new {@code from} > {@code to}.
     */
    public void setFrom(@NotNull ChestSize from) {
        this.from = from;
        this.validateFromIsNotGreaterThanTo();
    }
    
    /**
     * Gets the {@code to} index.
     *
     * @return the {@code to} index.
     */
    @NotNull
    public ChestSize getTo() {
        return to;
    }
    
    /**
     * Sets the {@code to} index.
     *
     * @param to - The index to set.
     * @throws IllegalArgumentException if the new {@code to} <= {@code from}.
     */
    public void setTo(@NotNull ChestSize to) {
        this.to = to;
        this.validateFromIsNotGreaterThanTo();
    }
    
    /**
     * Sets the {@link SlotBoundItemStack} for {@code "Next Page"} button.
     *
     * @param itemArrowNext - The item to set.
     */
    public void setItemArrowNext(@NotNull SlotBoundItemStack itemArrowNext) {
        this.itemArrowNext = itemArrowNext;
    }
    
    /**
     * Sets the {@link SlotBoundItemStack} for {@code "Previous Page"} button.
     *
     * @param itemArrowPrevious - The item to set.
     */
    public void setItemArrowPrevious(@NotNull SlotBoundItemStack itemArrowPrevious) {
        this.itemArrowPrevious = itemArrowPrevious;
    }
    
    /**
     * Sets the {@link SlotBoundItemStack} for {@code "Empty!"} item, which is shown when there are no {@code contents}.
     *
     * @param itemEmptyContents - The item to set.
     */
    public void setItemEmptyContents(@NotNull SlotBoundItemStack itemEmptyContents) {
        this.itemEmptyContents = itemEmptyContents;
    }
    
    /**
     * Get the maximum items per page that the {@link SlotPattern} may fit.
     *
     * @return the maximum items per page that the slot pattern may fit.
     */
    public int getMaximumItemsPerPage() {
        return (to.getRowIndex() - from.getRowIndex() + 1) * pattern.maxWidth();
    }
    
    /**
     * Gets the maximum pages based on {@code contents} size and {@link #getMaximumItemsPerPage()}.
     *
     * @return the maximum pages based on {@code contents} size and {@link #getMaximumItemsPerPage()}.
     */
    public int getMaximumPages() {
        return (int) Math.ceil((double) contents.size() / getMaximumItemsPerPage());
    }
    
    private void validateFromIsNotGreaterThanTo() {
        if (from.compareTo(to) > 0) {
            throw new IllegalArgumentException("`from` cannot be higher than `to`! (%s, %s)".formatted(from, to));
        }
    }
    
}
