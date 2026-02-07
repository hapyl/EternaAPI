package me.hapyl.eterna.module.inventory.menu.pattern;

import com.google.common.collect.Maps;
import me.hapyl.eterna.module.inventory.menu.ChestSize;
import me.hapyl.eterna.module.inventory.menu.PlayerMenu;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * Defines a slot filling pattern for a {@link PlayerMenu} based on the number of {@link ItemStack} to place.
 *
 * <p>
 * Each pattern represents how items should be positioned in the {@link PlayerMenu}, depending on the {@link List#size()}
 * of the given {@link ItemStack} list.
 * </p>
 *
 * <p>
 * Before setting items, the {@link List} of items is split into multiple {@code sublists},
 * each corresponding to a row, where the sublist size matches the maximum {@code pattern} size.
 * </p>
 *
 * <p>
 * For example, given you're using the {@link #DEFAULT} pattern:
 * <pre>{@code
 * List.of("stone") --------------------> | _ _ _ _ 1 _ _ _ _ |
 *
 * List.of("stone", "apple", "carrot") -> | _ _ 1 _ 2 _ 3 _ _ |
 *
 * List.of(
 *      "stone", "apple", "carrot",
 *      "diamond", "emerald", "potato"
 *      ) ------------------------------> | _ _ 1 2 3 4 5 _ _ |
 *                                        | _ _ _ _ 6 _ _ _ _ |
 * }</pre>
 * </p>
 *
 * <p>
 * If the number of {@link ItemStack} exceeds the size of the largest {@code pattern}, the pattern with the largest matching
 * size smaller or equal to the number of {@link ItemStack} will be used. <i>(As seen in the example above.)</i>
 * </p>
 */
public final class SlotPattern {
    
    /**
     * Defines the <i>inner left-to-right</i> {@link SlotPattern}, as example:
     * <pre>{@code
     * | _ 1 _ _ _ _ _ _ _ |
     * | _ 1 2 _ _ _ _ _ _ |
     * | _ 1 2 3 _ _ _ _ _ |
     * | _ 1 2 3 4 _ _ _ _ |
     * | _ 1 2 3 4 5 _ _ _ |
     * | _ 1 2 3 4 5 6 _ _ |
     * | _ 1 2 3 4 5 6 7 _ |
     * }</pre>
     */
    public static final SlotPattern INNER_LEFT_TO_RIGHT;
    
    /**
     * Defines the <i>inner right-to-right</i> {@link SlotPattern}, as example:
     * <pre>{@code
     * | _ _ _ _ _ _ _ 1 _ |
     * | _ _ _ _ _ _ 1 2 _ |
     * | _ _ _ _ _ 1 2 3 _ |
     * | _ _ _ _ 1 2 3 4 _ |
     * | _ _ _ 1 2 3 4 5 _ |
     * | _ _ 1 2 3 4 5 6 _ |
     * | _ 1 2 3 4 5 6 7 _ |
     * }</pre>
     */
    public static final SlotPattern INNER_RIGHT_TO_LEFT;
    
    /**
     * Defines the <i>chunky</i> {@link SlotPattern}, as example:
     * <pre>{@code
     * | _ _ _ _ 1 _ _ _ _ |
     * | _ _ _ 1 _ 2 _ _ _ |
     * | _ _ _ 1 2 3 _ _ _ |
     * | _ _ 1 2 _ 3 4 _ _ |
     * | _ _ 1 2 3 4 5 _ _ |
     * | _ 1 2 3 _ 4 5 6 _ |
     * | _ 1 2 3 4 5 6 7 _ |
     * | 1 2 3 4 _ 5 6 7 8 |
     * | 1 2 3 4 5 6 7 8 9 |
     * }</pre>
     */
    public static final SlotPattern CHUNKY;
    
    /**
     * Defines the <i>default</i> {@link SlotPattern}, as example:
     * <pre>{@code
     * | _ _ _ _ 1 _ _ _ _ |
     * | _ _ _ 1 _ 2 _ _ _ |
     * | _ _ 1 _ 2 _ 3 _ _ |
     * | _ _ 1 2 _ 3 4 _ _ |
     * | _ _ 1 2 3 4 5 _ _ |
     * }</pre>
     */
    public static final SlotPattern DEFAULT;
    
    /**
     * Defines the inner right-to-right {@link SlotPattern}, as example:
     * <pre>{@code
     * | 1 _ _ _ _ _ _ _ _ |
     * | 1 2 _ _ _ _ _ _ _ |
     * | 1 2 3 _ _ _ _ _ _ |
     * | 1 2 3 4 _ _ _ _ _ |
     * | 1 2 3 4 5 _ _ _ _ |
     * | 1 2 3 4 5 6 _ _ _ |
     * | 1 2 3 4 5 6 7 _ _ |
     * | 1 2 3 4 5 6 7 8 _ |
     * | 1 2 3 4 5 6 7 8 9 |
     * }</pre>
     */
    public static final SlotPattern LEFT_TO_RIGHT;
    
    /**
     * Defines the inner right-to-right {@link SlotPattern}, as example:
     * <pre>{@code
     * | _ _ _ _ _ _ _ _ 1 |
     * | _ _ _ _ _ _ _ 1 2 |
     * | _ _ _ _ _ _ 1 2 3 |
     * | _ _ _ _ _ 1 2 3 4 |
     * | _ _ _ _ 1 2 3 4 5 |
     * | _ _ _ 1 2 3 4 5 6 |
     * | _ _ 1 2 3 4 5 6 7 |
     * | _ 1 2 3 4 5 6 7 8 |
     * | 1 2 3 4 5 6 7 8 9 |
     * }</pre>
     */
    public static final SlotPattern RIGHT_TO_LEFT;
    
    /**
     * Defines the inner right-to-right {@link SlotPattern}, as example:
     * <pre>{@code
     * | _ _ _ _ 1 _ _ _ _ |
     * | _ _ _ 1 _ 2 _ _ _ |
     * | _ _ _ 1 2 3 _ _ _ |
     * | _ _ 1 2 _ 3 4 _ _ |
     * | _ _ 1 2 3 4 5 _ _ |
     * | _ 1 2 3 _ 4 5 6 _ |
     * | _ 1 2 3 4 5 6 7 _ |
     * }</pre>
     */
    public static final SlotPattern FANCY;
    
    static {
        INNER_LEFT_TO_RIGHT = compile(new byte[][] {
                { 0, 1, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 1, 1, 0, 0, 0, 0, 0, 0 },
                { 0, 1, 1, 1, 0, 0, 0, 0, 0 },
                { 0, 1, 1, 1, 1, 0, 0, 0, 0 },
                { 0, 1, 1, 1, 1, 1, 0, 0, 0 },
                { 0, 1, 1, 1, 1, 1, 1, 0, 0 },
                { 0, 1, 1, 1, 1, 1, 1, 1, 0 }
        });
        
        INNER_RIGHT_TO_LEFT = compile(new byte[][] {
                { 0, 0, 0, 0, 0, 0, 0, 1, 0 },
                { 0, 0, 0, 0, 0, 0, 1, 1, 0 },
                { 0, 0, 0, 0, 0, 1, 1, 1, 0 },
                { 0, 0, 0, 0, 1, 1, 1, 1, 0 },
                { 0, 0, 0, 1, 1, 1, 1, 1, 0 },
                { 0, 0, 1, 1, 1, 1, 1, 1, 0 },
                { 0, 1, 1, 1, 1, 1, 1, 1, 0 }
        });
        
        CHUNKY = compile(new byte[][] {
                { 0, 0, 0, 0, 1, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 1, 0, 0, 0 },
                { 0, 0, 0, 1, 1, 1, 0, 0, 0 },
                { 0, 0, 1, 1, 0, 1, 1, 0, 0 },
                { 0, 0, 1, 1, 1, 1, 1, 0, 0 },
                { 0, 1, 1, 1, 0, 1, 1, 1, 0 },
                { 0, 1, 1, 1, 1, 1, 1, 1, 0 },
                { 1, 1, 1, 1, 0, 1, 1, 1, 1 },
                { 1, 1, 1, 1, 1, 1, 1, 1, 1 }
        });
        
        DEFAULT = compile(new byte[][] {
                { 0, 0, 0, 0, 1, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 1, 0, 0, 0 },
                { 0, 0, 1, 0, 1, 0, 1, 0, 0 },
                { 0, 0, 1, 1, 0, 1, 1, 0, 0 },
                { 0, 0, 1, 1, 1, 1, 1, 0, 0 }
        });
        
        LEFT_TO_RIGHT = compile(new byte[][] {
                { 1, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 1, 1, 0, 0, 0, 0, 0, 0, 0 },
                { 1, 1, 1, 0, 0, 0, 0, 0, 0 },
                { 1, 1, 1, 1, 0, 0, 0, 0, 0 },
                { 1, 1, 1, 1, 1, 0, 0, 0, 0 },
                { 1, 1, 1, 1, 1, 1, 0, 0, 0 },
                { 1, 1, 1, 1, 1, 1, 1, 0, 0 },
                { 1, 1, 1, 1, 1, 1, 1, 1, 0 },
                { 1, 1, 1, 1, 1, 1, 1, 1, 1 }
        });
        
        RIGHT_TO_LEFT = compile(new byte[][] {
                { 0, 0, 0, 0, 0, 0, 0, 0, 1 },
                { 0, 0, 0, 0, 0, 0, 0, 1, 1 },
                { 0, 0, 0, 0, 0, 0, 1, 1, 1 },
                { 0, 0, 0, 0, 0, 1, 1, 1, 1 },
                { 0, 0, 0, 0, 1, 1, 1, 1, 1 },
                { 0, 0, 0, 1, 1, 1, 1, 1, 1 },
                { 0, 0, 1, 1, 1, 1, 1, 1, 1 },
                { 0, 1, 1, 1, 1, 1, 1, 1, 1 },
                { 1, 1, 1, 1, 1, 1, 1, 1, 1 }
        });
        
        FANCY = compile(new byte[][] {
                { 0, 0, 0, 0, 1, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 1, 0, 0, 0 },
                { 0, 0, 0, 1, 1, 1, 0, 0, 0 },
                { 0, 0, 1, 1, 0, 1, 1, 0, 0 },
                { 0, 0, 1, 1, 1, 1, 1, 0, 0 },
                { 0, 1, 1, 1, 0, 1, 1, 1, 0 },
                { 0, 1, 1, 1, 1, 1, 1, 1, 0 }
        });
    }
    
    private final TreeMap<Integer, byte[]> compiled;
    
    private SlotPattern(@NotNull TreeMap<Integer, byte[]> compiled) {
        this.compiled = compiled;
    }
    
    /**
     * Gets the maximum supported pattern width.
     *
     * @return The largest key representing the maximum pattern size.
     */
    public int maxWidth() {
        return this.compiled.lastKey();
    }
    
    /**
     * Gets the {@code byte[]} pattern matching the given size or the closest smaller size.
     *
     * @param size - The size of the item list to match a pattern for.
     * @return The {@code bute[]} representing the pattern for the given size.
     * @throws IllegalArgumentException if no suitable pattern exists for the size.
     */
    public byte[] patternFor(int size) {
        final Map.Entry<Integer, byte[]> entry = compiled.floorEntry(size);
        
        if (entry == null) {
            throw new IllegalArgumentException("Cannot determine pattern for size %s!".formatted(size));
        }
        
        return entry.getValue();
    }
    
    /**
     * Compiles multiple slot patterns into a {@link SlotPattern} instance,
     * indexing each pattern by the number of filled {@code ones} it contains.
     *
     * @param patterns - The {@code array} of {@code byte[]} representing slot patterns.
     * @return A compiled {@link SlotPattern} mapping sizes to patterns.
     * @throws IllegalArgumentException if a pattern length is invalid or the pattern is empty.
     */
    @NotNull
    public static SlotPattern compile(byte[][] patterns) {
        final TreeMap<Integer, byte[]> compiled = Maps.newTreeMap();
        
        for (byte[] pattern : patterns) {
            final int index = countOnes(pattern);
            
            // Validate pattern length and that there aren't any empty patterns
            if (pattern.length != ChestSize.CONTAINER_WIDTH) {
                throw new IllegalArgumentException("Pattern length must be %s, not %s!".formatted(ChestSize.CONTAINER_WIDTH, pattern.length));
            }
            else if (index == 0) {
                throw new IllegalArgumentException("Pattern cannot be empty! (%s)".formatted(Arrays.toString(pattern)));
            }
            
            compiled.put(index, pattern);
        }
        
        return new SlotPattern(compiled);
    }
    
    private static int countOnes(byte[] pattern) {
        int count = 0;
        
        for (byte bit : pattern) {
            if (bit == 1) {
                count++;
            }
        }
        
        return count;
    }
    
}
