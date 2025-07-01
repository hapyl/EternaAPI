package me.hapyl.eterna.module.inventory.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.eterna.module.util.Validate;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.*;

public final class SlotPattern {

    /**
     * Fills inner layer left to right.
     */
    public static final SlotPattern INNER_LEFT_TO_RIGHT = new SlotPattern(new byte[][] {
            { 0, 1, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 1, 1, 0, 0, 0, 0, 0, 0 },
            { 0, 1, 1, 1, 0, 0, 0, 0, 0 },
            { 0, 1, 1, 1, 1, 0, 0, 0, 0 },
            { 0, 1, 1, 1, 1, 1, 0, 0, 0 },
            { 0, 1, 1, 1, 1, 1, 1, 0, 0 },
            { 0, 1, 1, 1, 1, 1, 1, 1, 0 },
    });

    /**
     * Fills inner layer right to left.
     */
    public static final SlotPattern INNER_RIGHT_TO_LEFT = new SlotPattern(new byte[][] {
            { 0, 0, 0, 0, 0, 0, 0, 1, 0 },
            { 0, 0, 0, 0, 0, 0, 1, 1, 0 },
            { 0, 0, 0, 0, 0, 1, 1, 1, 0 },
            { 0, 0, 0, 0, 1, 1, 1, 1, 0 },
            { 0, 0, 0, 1, 1, 1, 1, 1, 0 },
            { 0, 0, 1, 1, 1, 1, 1, 1, 0 },
            { 0, 1, 1, 1, 1, 1, 1, 1, 0 },
    });

    /**
     * More chunky version of default pattern.
     */
    public static final SlotPattern CHUNKY = new SlotPattern(new byte[][] {
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

    /**
     * Standard pattern.
     */
    public static final SlotPattern DEFAULT = new SlotPattern(new byte[][] {
            { 0, 0, 0, 0, 1, 0, 0, 0, 0 },
            { 0, 0, 0, 1, 0, 1, 0, 0, 0 },
            { 0, 0, 1, 0, 1, 0, 1, 0, 0 },
            { 0, 0, 1, 1, 0, 1, 1, 0, 0 },
            { 0, 0, 1, 1, 1, 1, 1, 0, 0 }
    });

    /**
     * Fills outer layer left to right.
     */
    public static final SlotPattern LEFT_TO_RIGHT = new SlotPattern(new byte[][] {
            { 1, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 1, 1, 0, 0, 0, 0, 0, 0, 0 },
            { 1, 1, 1, 0, 0, 0, 0, 0, 0 },
            { 1, 1, 1, 1, 0, 0, 0, 0, 0 },
            { 1, 1, 1, 1, 1, 0, 0, 0, 0 },
            { 1, 1, 1, 1, 1, 1, 0, 0, 0 },
            { 1, 1, 1, 1, 1, 1, 1, 0, 0 },
            { 1, 1, 1, 1, 1, 1, 1, 1, 0 },
            { 1, 1, 1, 1, 1, 1, 1, 1, 1 },
    });

    /**
     * Fills outer layer right to left.
     */
    public static final SlotPattern RIGHT_TO_LEFT = new SlotPattern(new byte[][] {
            { 0, 0, 0, 0, 0, 0, 0, 0, 1 },
            { 0, 0, 0, 0, 0, 0, 0, 1, 1 },
            { 0, 0, 0, 0, 0, 0, 1, 1, 1 },
            { 0, 0, 0, 0, 0, 1, 1, 1, 1 },
            { 0, 0, 0, 0, 1, 1, 1, 1, 1 },
            { 0, 0, 0, 1, 1, 1, 1, 1, 1 },
            { 0, 0, 1, 1, 1, 1, 1, 1, 1 },
            { 0, 1, 1, 1, 1, 1, 1, 1, 1 },
            { 1, 1, 1, 1, 1, 1, 1, 1, 1 },
    });

    /**
     * Fills outer layer top to bottom, similar (I think) to hypixel's pattern.
     */
    public static final SlotPattern FANCY = new SlotPattern(new byte[][] {
            { 0, 0, 0, 0, 1, 0, 0, 0, 0 },
            { 0, 0, 0, 1, 0, 1, 0, 0, 0 },
            { 0, 0, 0, 1, 1, 1, 0, 0, 0 },
            { 0, 0, 1, 1, 0, 1, 1, 0, 0 },
            { 0, 0, 1, 1, 1, 1, 1, 0, 0 },
            { 0, 1, 1, 1, 0, 1, 1, 1, 0 },
            { 0, 1, 1, 1, 1, 1, 1, 1, 0 },
    });

    private final Map<Integer, byte[]> compiled;
    private final byte[][] pattern;

    private int maxPatternSize;

    /**
     * Creates a new slot pattern.
     *
     * @param pattern - the pattern to use.
     * @throws IllegalArgumentException if there is an empty line in the pattern.
     * @throws IllegalArgumentException if the pattern[i] length is not 9.
     */
    public SlotPattern(byte[][] pattern) {
        for (byte[] bytes : pattern) {
            Validate.isTrue(countOnes(bytes) != 0, "Do not use empty lines in pattern! %s".formatted(Arrays.toString(bytes)));
            Validate.isTrue(bytes.length == 9, "Pattern must have a length of 9, not %s!".formatted(bytes.length));
        }

        this.maxPatternSize = 0;
        this.pattern = pattern;
        this.compiled = Maps.newHashMap();
        compile();
    }

    @Nonnull
    public static SlotPatternBuilder builder() {
        return new SlotPatternBuilder();
    }

    /**
     * Applies the pattern to the gui.
     *
     * @param gui      the gui to apply the pattern to.
     * @param items    the items to apply to the gui.
     * @param startRow the row to start the pattern from.
     * @throws IllegalArgumentException if no valid pattern can be found.
     */
    public void apply(PlayerGUI gui, LinkedHashMap<ItemStack, ActionList> items, int startRow) {
        final List<List<ItemStack>> subList = subList(items);

        for (List<ItemStack> itemStacks : subList) {
            final byte[] bytes = getPattern(itemStacks.size());

            for (int i = 0; i < itemStacks.size(); i++) {
                final ItemStack item = itemStacks.get(i);

                int one = 0;
                for (int j = 0; j < bytes.length; j++) {
                    if (bytes[j] == 1) {
                        if (one == i) {
                            final int slot = j + (startRow * 9);
                            final ActionList click = items.get(item);

                            gui.setItem(slot, item, click);
                        }
                        one++;
                    }
                }
            }
            startRow++;
        }
    }

    public byte[][] getPattern() {
        return pattern;
    }

    private byte[] getPattern(int size) {
        byte[] pattern = compiled.get(size);
        if (pattern == null) {
            // Find the closest pattern.
            int index = 0;
            int closest = Integer.MAX_VALUE;

            for (int i = 0; i < this.pattern.length; i++) {
                final int ones = countOnes(this.pattern[i]);
                final int diff = Math.abs(ones - size);

                if (diff < closest) {
                    closest = diff;
                    index = i;
                }
            }

            pattern = this.pattern[index];
        }

        if (pattern == null) {
            throw new IllegalStateException("Could not find pattern for size %s!".formatted(size));
        }

        return pattern;
    }

    private List<List<ItemStack>> subList(LinkedHashMap<ItemStack, ActionList> items) {
        final List<List<ItemStack>> subList = Lists.newArrayList();
        final List<ItemStack> mapToList = Lists.newArrayList(items.keySet());

        for (int i = 0; i < mapToList.size(); i += maxPatternSize) {
            subList.add(mapToList.subList(i, Math.min(i + maxPatternSize, mapToList.size())));
        }

        return subList;
    }

    private void compile() {
        for (byte[] bytes : pattern) {
            final int ones = countOnes(bytes);

            if (maxPatternSize == 0 || ones > maxPatternSize) {
                maxPatternSize = ones;
            }

            if (compiled.containsKey(ones)) {
                throw new IllegalArgumentException("Pattern contains duplicate lines! (%s)".formatted(Arrays.toString(bytes)));
            }
            compiled.put(ones, bytes);
        }
    }

    private int countOnes(byte[] pattern) {
        int count = 0;
        for (byte b : pattern) {
            if (b == 1) {
                count++;
            }
        }
        return count;
    }

    // Converts Collection of ItemStack to slots depending on items size.
    @Deprecated
    public List<Integer> getSlots(Collection<ItemStack> items, int startLine) {
        return getSlots(calculateBytes(items), startLine);
    }

    // Converts bytes into list of slots to put items to.
    @Deprecated
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
    @Deprecated
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


}
