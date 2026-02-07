package me.hapyl.eterna.module.player.tablist;

import me.hapyl.eterna.module.annotate.Size;
import me.hapyl.eterna.module.reflect.Skin;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Represents an {@code list} of {@link Tablist#MAX_ENTRIES_PER_COLUMN} lines exactly (which is how many fits on one tablist column),
 * that defines what changed are made to a {@link TablistEntry}.
 */
public class EntryList {
    
    protected final Entry[] values;
    
    private EntryList(@NotNull Entry[] values) {
        this.values = values;
    }
    
    /**
     * Appends the given text {@link Component} to this {@link EntryList}.
     *
     * @param text - The text to append.
     */
    public void append(@NotNull Component text) {
        append0(text, null, null);
    }
    
    /**
     * Appends the given text {@link Component} and {@link EntryTexture} to this {@link EntryList}.
     *
     * @param text    - The text to append.
     * @param texture - The texture to append.
     * @see EntryTexture
     */
    public void append(@NotNull Component text, @NotNull EntryTexture texture) {
        append0(text, texture, null);
    }
    
    /**
     * Appends the given text {@link Component} and {@link PingBars} to this {@link EntryList}.
     *
     * @param text - The text to append.
     * @param ping - The ping bars to append.
     */
    public void append(@NotNull Component text, @NotNull PingBars ping) {
        append0(text, null, ping);
    }
    
    /**
     * Appends the given text {@link Component}, {@link EntryTexture} and {@link PingBars} to this {@link EntryList}.
     *
     * @param text    - The text to append.
     * @param texture - The texture to append.
     * @param ping    - The ping bars to append.
     */
    public void append(@NotNull Component text, @Nullable EntryTexture texture, @Nullable PingBars ping) {
        append0(text, texture, ping);
    }
    
    /**
     * Appends an empty text {@link Component} to this {@link EntryList}.
     */
    public void append() {
        append0(Component.empty(), null, null);
    }
    
    /**
     * Gets the size of non-{@code null} elements in this list.
     *
     * @return the size of non-{@code null} elements in this list.
     */
    public int size() {
        int size = 0;
        
        for (Entry entry : values) {
            if (entry != null) {
                size++;
            }
        }
        
        return size;
    }
    
    /**
     * Clears this list.
     */
    public void clear() {
        Arrays.fill(values, null);
    }
    
    private void append0(@NotNull Component component, @Nullable Skin skin, @Nullable PingBars ping) {
        for (int i = 0; i < values.length; i++) {
            final Entry val = values[i];
            
            // Since we're appending to a nullable list, just set the
            // first null value to a new tablist entry
            if (val == null) {
                values[i] = entry -> {
                    // Always set text
                    entry.setText(component);
                    
                    // Don't override skin
                    if (skin != null) {
                        entry.setTexture(skin);
                    }
                    
                    // Don't override ping
                    if (ping != null) {
                        entry.setPing(ping);
                    }
                };
                
                return;
            }
        }
    }
    
    /**
     * A static factory method for creating an empty {@link EntryList}.
     *
     * @return a new empty entry list.
     */
    @NotNull
    public static EntryList ofEmpty() {
        return new EntryList(new Entry[Tablist.MAX_ENTRIES_PER_COLUMN]);
    }
    
    /**
     * A static factory method for creating {@link EntryList} from the given {@link Component} list.
     *
     * @param entries - The entries to set.
     * @return a new entry list.
     */
    @NotNull
    public static EntryList ofList(@NotNull @Size(from = 0, to = Tablist.MAX_ENTRIES_PER_COLUMN) List<Component> entries) {
        return new EntryList(
                entries.stream()
                       .limit(Tablist.MAX_ENTRIES_PER_COLUMN)
                       .map(Entry.mapper())
                       .toArray(Entry[]::new)
        );
    }
    
    /**
     * A static factory method for creating {@link EntryList} from the given {@link Component} array.
     *
     * @param entries - The entries to set.
     * @return a new entry list.
     */
    @NotNull
    public static EntryList ofArray(@NotNull @Size(from = 0, to = Tablist.MAX_ENTRIES_PER_COLUMN) Component[] entries) {
        return new EntryList(
                Arrays.stream(entries)
                      .limit(Tablist.MAX_ENTRIES_PER_COLUMN)
                      .map(Entry.mapper())
                      .toArray(Entry[]::new)
        );
    }
    
    /**
     * Represents a {@link TablistEntry} consumer used in {@link EntryList}.
     *
     * @see EntryList
     */
    @ApiStatus.NonExtendable
    public interface Entry {
        
        /**
         * Applies this {@link Entry} to the given {@link TablistEntry}.
         *
         * @param entry - The entry to apply to.
         */
        void apply(@NotNull TablistEntry entry);
        
        /**
         * Represents an internal mapped for {@link EntryList}.
         *
         * @return the mapping function.
         */
        @NotNull
        @ApiStatus.Internal
        static Function<Component, Entry> mapper() {
            return component -> (Entry) entry -> entry.setText(component);
        }
        
    }
}
