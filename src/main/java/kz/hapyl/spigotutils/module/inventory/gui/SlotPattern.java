package kz.hapyl.spigotutils.module.inventory.gui;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class SlotPattern {

    public static final SlotPattern CHUNKY = new SlotPattern(new byte[][] {
            { 0, 0, 0, 0, 1, 0, 0, 0, 0 }, // 1
            { 0, 0, 0, 1, 0, 1, 0, 0, 0 }, // 2
            { 0, 0, 0, 1, 1, 1, 0, 0, 0 }, // 3
            { 0, 0, 1, 1, 0, 1, 1, 0, 0 }, // 4
            { 0, 0, 1, 1, 1, 1, 1, 0, 0 }, // 5
            { 0, 1, 1, 1, 0, 1, 1, 1, 0 }, // 6
            { 0, 1, 1, 1, 1, 1, 1, 1, 0 }, // 7
            { 1, 1, 1, 1, 0, 1, 1, 1, 1 }, // 8
            { 1, 1, 1, 1, 1, 1, 1, 1, 1 }  // 8
    });

    public static final SlotPattern DEFAULT = new SlotPattern(new byte[][] {
            { 0, 0, 0, 0, 1, 0, 0, 0, 0 }, // 1
            { 0, 0, 0, 1, 0, 1, 0, 0, 0 }, // 2
            { 0, 0, 1, 0, 1, 0, 1, 0, 0 }, // 3
            { 0, 0, 1, 1, 0, 1, 1, 0, 0 }, // 4
            { 0, 0, 1, 1, 1, 1, 1, 0, 0 }  // 5
    });

    private final byte[][] pattern;

    /**
     * @throws IllegalArgumentException if pattern length is not 9
     */
    public SlotPattern(byte[][] pattern) {
        for (byte[] bytes : pattern) {
            if (bytes.length != 9) {
                throw new IllegalArgumentException("pattern length must be 8, not " + bytes.length);
            }
        }
        this.pattern = pattern;
    }

    public List<Integer> getSlots(Collection<ItemStack> items, int startLine) {
        return getSlots(calculateBytes(items), startLine);
    }

    // converts bytes into list of slots to put items to
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
            if (i == (size - 1)) {
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
