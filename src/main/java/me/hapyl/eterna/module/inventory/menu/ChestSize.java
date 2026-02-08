package me.hapyl.eterna.module.inventory.menu;

import me.hapyl.eterna.module.annotate.StrictEnumOrdinal;
import me.hapyl.eterna.module.util.OrderedEnum;
import org.jetbrains.annotations.NotNull;

/**
 * Defines the {@link PlayerMenuType#chest(ChestSize)} size of the chest.
 */
@StrictEnumOrdinal
public enum ChestSize implements OrderedEnum<ChestSize> {
    
    /**
     * Defines a {@link ChestSize} of one row, or {@code 9} items.
     */
    SIZE_1(4),
    
    /**
     * Defines a {@link ChestSize} if two rows, or {@code 18} items.
     */
    SIZE_2(4),
    
    /**
     * Defines a {@link ChestSize} if three rows, or {@code 27} items.
     */
    SIZE_3(13),
    
    /**
     * Defines a {@link ChestSize} if four rows, or {@code 36} items.
     */
    SIZE_4(13),
    
    /**
     * Defines a {@link ChestSize} if five rows, or {@code 45} items.
     */
    SIZE_5(22),
    
    /**
     * Defines a {@link ChestSize} if six rows, or {@code 54} items.
     */
    SIZE_6(22);
    
    /**
     * Defines the maximum width of a vanilla minecraft container.
     */
    public static final int CONTAINER_WIDTH = 9;
    
    private final int size;
    private final int centre;
    
    ChestSize(int centre) {
        this.size = (ordinal() + 1) * CONTAINER_WIDTH;
        this.centre = centre;
    }
    
    /**
     * Gets the width index of this {@link ChestSize}.
     *
     * @return the width index of this chest size.
     */
    public int getRowIndex() {
        return ordinal();
    }
    
    /**
     * Gets the size as number of slots for this {@link ChestSize}.
     *
     * @return the size as number of slots for this chest size.
     */
    public int getSize() {
        return size;
    }
    
    /**
     * Gets the slot for upmost middle row for this {@link ChestSize}.
     *
     * @return the slot for upmost middle row for this chest size.
     */
    public int getCentre() {
        return centre;
    }
    
    @NotNull
    @Override
    public ChestSize current() {
        return this;
    }
}
