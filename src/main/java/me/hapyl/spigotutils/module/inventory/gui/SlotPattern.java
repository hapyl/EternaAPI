package me.hapyl.spigotutils.module.inventory.gui;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class SlotPattern {

    /**
     * Fills inner layer left to right.
     */
    public static final SlotPattern INNER_LEFT_TO_RIGHT = new SlotPattern(new byte[][] {
            { 0, 1, 0, 0, 0, 0, 0, 0, 0 }, // 1
            { 0, 1, 1, 0, 0, 0, 0, 0, 0 }, // 2
            { 0, 1, 1, 1, 0, 0, 0, 0, 0 }, // 3
            { 0, 1, 1, 1, 1, 0, 0, 0, 0 }, // 4
            { 0, 1, 1, 1, 1, 1, 0, 0, 0 }, // 5
            { 0, 1, 1, 1, 1, 1, 1, 0, 0 }, // 6
            { 0, 1, 1, 1, 1, 1, 1, 1, 0 }, // 7
    });

    /**
     * Fills inner layer right to left.
     */
    public static final SlotPattern INNER_RIGHT_TO_LEFT = new SlotPattern(new byte[][] {
            { 0, 0, 0, 0, 0, 0, 0, 1, 0 }, // 1
            { 0, 0, 0, 0, 0, 0, 1, 1, 0 }, // 2
            { 0, 0, 0, 0, 0, 1, 1, 1, 0 }, // 3
            { 0, 0, 0, 0, 1, 1, 1, 1, 0 }, // 4
            { 0, 0, 0, 1, 1, 1, 1, 1, 0 }, // 5
            { 0, 0, 1, 1, 1, 1, 1, 1, 0 }, // 6
            { 0, 1, 1, 1, 1, 1, 1, 1, 0 }, // 7
    });

    /**
     * More chunky version of default pattern.
     */
    public static final SlotPattern CHUNKY = new SlotPattern(new byte[][] {
            { 0, 0, 0, 0, 1, 0, 0, 0, 0 }, // 1
            { 0, 0, 0, 1, 0, 1, 0, 0, 0 }, // 2
            { 0, 0, 0, 1, 1, 1, 0, 0, 0 }, // 3
            { 0, 0, 1, 1, 0, 1, 1, 0, 0 }, // 4
            { 0, 0, 1, 1, 1, 1, 1, 0, 0 }, // 5
            { 0, 1, 1, 1, 0, 1, 1, 1, 0 }, // 6
            { 0, 1, 1, 1, 1, 1, 1, 1, 0 }, // 7
            { 1, 1, 1, 1, 0, 1, 1, 1, 1 }, // 8
            { 1, 1, 1, 1, 1, 1, 1, 1, 1 }  // 9
    });

    /**
     * Standard pattern.
     */
    public static final SlotPattern DEFAULT = new SlotPattern(new byte[][] {
            { 0, 0, 0, 0, 1, 0, 0, 0, 0 }, // 1
            { 0, 0, 0, 1, 0, 1, 0, 0, 0 }, // 2
            { 0, 0, 1, 0, 1, 0, 1, 0, 0 }, // 3
            { 0, 0, 1, 1, 0, 1, 1, 0, 0 }, // 4
            { 0, 0, 1, 1, 1, 1, 1, 0, 0 }  // 5
    });

    public static final SlotPattern LEFT_TO_RIGHT = new SlotPattern(new byte[][] {
            { 1, 1, 0, 0, 0, 0, 0, 0, 0 }, // 1
            { 1, 1, 0, 0, 0, 0, 0, 0, 0 }, // 2
            { 1, 1, 1, 0, 0, 0, 0, 0, 0 }, // 3
            { 1, 1, 1, 1, 0, 0, 0, 0, 0 }, // 4
            { 1, 1, 1, 1, 1, 0, 0, 0, 0 }, // 5
            { 1, 1, 1, 1, 1, 1, 0, 0, 0 }, // 6
            { 1, 1, 1, 1, 1, 1, 1, 0, 0 }, // 7
            { 1, 1, 1, 1, 1, 1, 1, 1, 0 }, // 8
            { 1, 1, 1, 1, 1, 1, 1, 1, 1 }, // 9
    });

    public static final SlotPattern RIGHT_TO_LEFT = new SlotPattern(new byte[][] {
            { 0, 0, 0, 0, 0, 0, 0, 0, 1 }, // 1
            { 0, 0, 0, 0, 0, 0, 0, 1, 1 }, // 2
            { 0, 0, 0, 0, 0, 0, 1, 1, 1 }, // 3
            { 0, 0, 0, 0, 0, 1, 1, 1, 1 }, // 4
            { 0, 0, 0, 0, 1, 1, 1, 1, 1 }, // 5
            { 0, 0, 0, 1, 1, 1, 1, 1, 1 }, // 6
            { 0, 0, 1, 1, 1, 1, 1, 1, 1 }, // 7
            { 0, 1, 1, 1, 1, 1, 1, 1, 1 }, // 8
            { 1, 1, 1, 1, 1, 1, 1, 1, 1 }, // 9
    });

    public static final SlotPattern FANCY = new SlotPattern(new byte[][] {
            { 0, 0, 0, 0, 1, 0, 0, 0, 0 }, // 1
            { 0, 0, 0, 1, 0, 1, 0, 0, 0 }, // 1
            { 0, 0, 0, 1, 1, 1, 0, 0, 0 }, // 1
            { 0, 0, 1, 1, 0, 1, 1, 0, 0 }, // 1
            { 0, 0, 1, 1, 1, 1, 1, 0, 0 }, // 1
            { 0, 1, 1, 1, 0, 1, 1, 1, 0 }, // 1
            { 0, 1, 1, 1, 1, 1, 1, 1, 0 }, // 1
    });

    private final byte[][] pattern;

    /**
     * @throws IllegalArgumentException if pattern length is not 9
     */
    public SlotPattern(byte[][] pattern) {
        for (byte[] bytes : pattern) {
            if (bytes.length != 9) {
                throw new IllegalArgumentException("pattern length must be 9, not " + bytes.length);
            }
        }
        this.pattern = pattern;
    }

    // Converts Collection of ItemStack to slots depending on items size.
    public List<Integer> getSlots(Collection<ItemStack> items, int startLine) {
        return getSlots(calculateBytes(items), startLine);
    }

    // Converts bytes into list of slots to put items to.
    public List<Integer> getSlots(byte[][] bytes, int startLine) {
        final List<Integer> slots = new ArrayList<>();

        for (int i = 0; i < bytes.length; i++) {
            for (int j = 0; j < 9; ++j) {
                if (bytes[i][j] == 1) {
                    slots.add(j + (9 * (i + startLine)));
                }
            }
        }
        return slots;
    }

    // Calculates Collection of ItemStack to byte based on current pattern.
    public byte[][] calculateBytes(Collection<ItemStack> items) {
        final int size = items.size();
        final int sizeScaled = (int) Math.ceil(size / 5.0f);
        final byte[][] bytes = new byte[sizeScaled][9];

        int line = 0;
        int count = 0;

        for (int i = 0; i < size; i++) {
            if (count >= (pattern.length - 1)) {
                bytes[line] = pattern[count];
                ++line;
                count = 0;
                continue;
            }
            if (i == (size - 1) && line < bytes.length) {
                bytes[line] = pattern[count];
            }
            ++count;
        }
        return bytes;
    }

    public byte[][] getPattern() {
        return pattern;
    }
}
