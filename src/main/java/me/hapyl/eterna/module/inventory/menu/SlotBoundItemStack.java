package me.hapyl.eterna.module.inventory.menu;

import me.hapyl.eterna.module.inventory.ItemStacks;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an {@link ItemStack} holder bound to a specific {@code slot}.
 */
public interface SlotBoundItemStack {
    
    /**
     * Gets the {@code slot} the {@link ItemStack} is bound to.
     *
     * @return the {@code slot} the item is bound to.
     */
    int slot();
    
    /**
     * Gets the {@link ItemStack}.
     *
     * @return the item.
     */
    @NotNull
    ItemStack itemStack();
    
    /**
     * Creates an empty {@link SlotBoundItemStack} with an empty {@link ItemStack}.
     *
     * <p>The bound slot for empty holder is {@code -1}, which means it will not be set in the {@link PlayerMenu}.</p>
     *
     * @return an empty {@link SlotBoundItemStack}.
     */
    @NotNull
    static SlotBoundItemStack empty() {
        return new SlotBoundItemStackImpl(-1, ItemStacks.empty());
    }
    
    /**
     * A static factory method for creating {@link SlotBoundItemStack}.
     *
     * @param slot      - The slot to bound the item to.
     * @param itemStack - The item stack.
     * @return a new {@link SlotBoundItemStack}.
     */
    @NotNull
    static SlotBoundItemStack of(int slot, @NotNull ItemStack itemStack) {
        return new SlotBoundItemStackImpl(slot, itemStack);
    }
    
    @ApiStatus.Internal
    final class SlotBoundItemStackImpl implements SlotBoundItemStack {
        
        private final int slot;
        private final ItemStack itemStack;
        
        SlotBoundItemStackImpl(int slot, @NotNull ItemStack itemStack) {
            this.slot = slot;
            this.itemStack = itemStack;
        }
        
        @Override
        public int slot() {
            return slot;
        }
        
        @NotNull
        @Override
        public ItemStack itemStack() {
            return itemStack;
        }
    }
    
}
