package me.hapyl.eterna.module.player.dialog.entry;

import org.jetbrains.annotations.Nullable;

/**
 * Represents an option index for {@link DialogEntryOptions}.
 */
public enum OptionIndex {
    
    /**
     * Represents the index {@code 1}.
     */
    OPTION_1,
    
    /**
     * Represents the index {@code 2}.
     */
    OPTION_2,
    
    /**
     * Represents the index {@code 3}.
     */
    OPTION_3,
    
    /**
     * Represents the index {@code 4}.
     */
    OPTION_4,
    
    /**
     * Represents the index {@code 5}.
     */
    OPTION_5;
    
    /**
     * Gets the integer index.
     *
     * @return the integer index.
     */
    public int index() {
        return this.ordinal() + 1;
    }
    
    /**
     * Gets the {@link OptionIndex} from the given {@code integer}.
     *
     * @param i - The option index.
     * @return the option index, or {@code null} if out of bounds.
     */
    @Nullable
    public static OptionIndex fromInt(int i) {
        return i < 0 || i >= values().length ? null : values()[i];
    }
}
