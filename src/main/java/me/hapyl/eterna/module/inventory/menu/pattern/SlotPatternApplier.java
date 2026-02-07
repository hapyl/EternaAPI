package me.hapyl.eterna.module.inventory.menu.pattern;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.eterna.module.annotate.SelfReturn;
import me.hapyl.eterna.module.inventory.menu.ChestSize;
import me.hapyl.eterna.module.inventory.menu.PlayerMenu;
import me.hapyl.eterna.module.inventory.menu.action.PlayerMenuAction;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Represents an applier for {@link SlotPattern}.
 */
public class SlotPatternApplier {
    
    private final PlayerMenu playerMenu;
    private final SlotPattern pattern;
    private final ChestSize from;
    private final ChestSize to;
    private final LinkedHashMap<ItemStack, PlayerMenuAction> actions;
    
    SlotPatternApplier(@NotNull PlayerMenu playerMenu, @NotNull SlotPattern patter, @NotNull ChestSize from, @NotNull ChestSize to) {
        if (from.compareTo(to) > 0) {
            throw new IllegalArgumentException("`from` cannot be higher than `to`! (%s, %s)".formatted(from, to));
        }
        
        this.playerMenu = playerMenu;
        this.pattern = patter;
        this.from = from;
        this.to = to;
        this.actions = Maps.newLinkedHashMap();
    }
    
    /**
     * Adds the given {@link ItemStack} to this {@link SlotPatternApplier}.
     *
     * @param item - The item to add.
     */
    @SelfReturn
    public SlotPatternApplier add(@NotNull ItemStack item) {
        return add(item, (PlayerMenuAction) null);
    }
    
    /**
     * Adds the given {@link ItemStack} and {@link PlayerMenuAction} to this {@link SlotPatternApplier}.
     *
     * @param item   - The item to add.
     * @param action - The action to add, or {@code null} for no action.
     */
    @SelfReturn
    public SlotPatternApplier add(@NotNull ItemStack item, @Nullable PlayerMenuAction action) {
        this.actions.put(item, action);
        return this;
    }
    
    /**
     * Adds the given {@link ItemStack} and {@link PlayerMenuAction.Builder} to this {@link SlotPatternApplier}.
     *
     * @param item   - The item to add.
     * @param action - The action to add.
     */
    @SelfReturn
    public SlotPatternApplier add(@NotNull ItemStack item, @NotNull PlayerMenuAction.Builder action) {
        this.actions.put(item, action.build());
        return this;
    }
    
    /**
     * Applies the given {@link SlotPattern} to the given {@link PlayerMenu}.
     */
    public void apply() {
        final List<List<ItemStack>> sublists = Lists.newArrayList();
        final List<ItemStack> keyList = Lists.newArrayList(actions.keySet());
        
        final int width = pattern.maxWidth();
        final int size = keyList.size();
        
        for (int i = 0; i < size; i += width) {
            sublists.add(keyList.subList(i, Math.min(i + width, size)));
        }
        
        int startRow = from.getRowIndex();
        
        for (List<ItemStack> sublist : sublists) {
            final byte[] patternBytes = pattern.patternFor(sublist.size());
            
            int index = 0;
            
            for (int i = 0; i < patternBytes.length; i++) {
                final byte value = patternBytes[i];
                
                if (value != 0) {
                    final int slot = i + (startRow * ChestSize.CONTAINER_WIDTH);
                    final ItemStack item = sublist.get(index++);
                    
                    playerMenu.setItem0(slot, item, actions.get(item));
                }
            }
            
            // Can't fit anymore, should have used PlayerPageMenu ¯\_(ツ)_/¯
            if (++startRow > to.getRowIndex()) {
                break;
            }
        }
    }
    
    /**
     * A static factory method for creating {@link SlotPatternApplier}.
     *
     * @param playerMenu - The menu to apply the pattern to.
     * @param pattern    - The pattern to apply.
     * @param from       - The starting size, inclusive.
     * @param to         - The ending size, inclusive.
     * @return a new {@link SlotPatternApplier}.
     */
    @NotNull
    public static SlotPatternApplier of(@NotNull PlayerMenu playerMenu, @NotNull SlotPattern pattern, @NotNull ChestSize from, @NotNull ChestSize to) {
        return new SlotPatternApplier(playerMenu, pattern, from, to);
    }
    
    /**
     * A static factory method for creating {@link SlotPatternApplier}.
     *
     * <p>This method assumes the range is {@code from} - {@link ChestSize#SIZE_6}.</p>
     *
     * @param playerMenu - The menu to apply the pattern to.
     * @param pattern    - The pattern to apply.
     * @param from       - The starting size, inclusive.
     * @return a new {@link SlotPatternApplier}.
     */
    @NotNull
    public static SlotPatternApplier of(@NotNull PlayerMenu playerMenu, @NotNull SlotPattern pattern, @NotNull ChestSize from) {
        return new SlotPatternApplier(playerMenu, pattern, from, ChestSize.SIZE_6);
    }
    
    /**
     * A static factory method for creating {@link SlotPatternApplier}.
     *
     * <p>This method assumes the range is {@link ChestSize#SIZE_1} - {@link ChestSize#SIZE_6}.</p>
     *
     * @param playerMenu - The menu to apply the pattern to.
     * @param pattern    - The pattern to apply.
     * @return a new {@link SlotPatternApplier}.
     */
    @NotNull
    public static SlotPatternApplier of(@NotNull PlayerMenu playerMenu, @NotNull SlotPattern pattern) {
        return new SlotPatternApplier(playerMenu, pattern, ChestSize.SIZE_1, ChestSize.SIZE_6);
    }
    
}
