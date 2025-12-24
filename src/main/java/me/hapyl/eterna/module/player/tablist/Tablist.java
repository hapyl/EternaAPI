package me.hapyl.eterna.module.player.tablist;

import com.google.common.collect.Maps;
import me.hapyl.eterna.module.annotate.SelfReturn;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.util.Destroyable;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Range;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Represents a customizable four-column tablist.
 */
public class Tablist implements Destroyable {
    
    /**
     * Defines the default entry name.
     */
    public static final Component DEFAULT_ENTRY_NAME = Component.text("                 ");
    
    /**
     * Defines the maximum total entries.
     */
    public static final int MAX_ENTRIES = 80;
    
    /**
     * Defines the maximum entries per column.
     */
    public static final int MAX_ENTRIES_PER_COLUMN = 20;
    
    private static final Map<Player, Tablist> PLAYER_TAB_LIST_MAP = Maps.newHashMap();
    private static final EnumSet<ClientboundPlayerInfoUpdatePacket.Action> ACTION_SET_INIT = EnumSet.of(
            ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER,
            ClientboundPlayerInfoUpdatePacket.Action.INITIALIZE_CHAT,
            ClientboundPlayerInfoUpdatePacket.Action.UPDATE_GAME_MODE,
            ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED,
            ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LATENCY,
            ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME,
            ClientboundPlayerInfoUpdatePacket.Action.UPDATE_HAT,
            ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LIST_ORDER
    );
    
    protected final Player player;
    protected final Map<Integer, TablistEntry> entries;
    
    /**
     * Creates a new {@link Tablist} for the given {@link Player}.
     *
     * @param player - The player for whom to create the tablist.
     */
    public Tablist(@Nonnull Player player) {
        this.player = player;
        this.entries = Maps.newLinkedHashMap();
        
        for (int i = 0; i < MAX_ENTRIES; i++) {
            this.entries.put(i, new TablistEntry(i, this));
        }
    }
    
    /**
     * Gets the {@link TablistEntry} at the given {@code index}.
     *
     * @param index - The index to retrieve the entry, must be within {@code 0} - {@link #MAX_ENTRIES}.
     * @return tablist entry.
     * @throws IndexOutOfBoundsException if the given {@code index} is out of bounds.
     */
    @Nonnull
    public TablistEntry getEntry(@Range(from = 0, to = MAX_ENTRIES) int index) {
        return this.entries.get(index);
    }
    
    /**
     * Gets the {@link TablistEntry} at the given {@link TablistColumn} and {@code line}.
     *
     * @param column - The column.
     * @param line   - The index of the column, must be within {@code 0} - {@code 19}.
     * @return tablist entry.
     * @throws IndexOutOfBoundsException if the given {@code index} is out of bounds.
     */
    @Nonnull
    public TablistEntry getEntry(@Nonnull TablistColumn column, int line) {
        return getEntry(line + column.ordinal() * MAX_ENTRIES_PER_COLUMN);
    }
    
    /**
     * Sets the {@link TablistColumn} to the given {@link EntryList}.
     *
     * @param column - The column.
     * @param list   - The entry list to set.
     */
    @SelfReturn
    public Tablist setColumn(@Nonnull TablistColumn column, @Nonnull EntryList list) {
        int index = column.ordinal() * MAX_ENTRIES_PER_COLUMN;
        
        for (EntryConsumer entryConsumer : list.array) {
            final TablistEntry entry = getEntry(index++);
            
            if (entryConsumer == null) {
                entry.setText(DEFAULT_ENTRY_NAME);
                entry.setTexture(EntryTexture.DARK_GRAY);
            }
            else {
                entryConsumer.apply(entry);
            }
        }
        
        return this;
    }
    
    /**
     * Sets the {@link TablistColumn} to the given {@link Component} list.
     *
     * @param column - The column.
     * @param lines  - The list of {@link Component}; must be within range, as any out of bounds components are silently ignored.
     */
    @SelfReturn
    public Tablist setColumn(@Nonnull TablistColumn column, @Range(from = 0, to = MAX_ENTRIES_PER_COLUMN) @Nonnull List<Component> lines) {
        return setColumn(column, new EntryList(lines));
    }
    
    /**
     * Sets the column to the given {@link String} array.
     *
     * @param column - The column.
     * @param lines  - The list of {@link Component}; must be within range, as any out of bounds components are silently ignored.
     */
    @SelfReturn
    public Tablist setColumn(@Nonnull TablistColumn column, @Range(from = 0, to = MAX_ENTRIES_PER_COLUMN) @Nonnull Component... lines) {
        return setColumn(column, new EntryList(lines));
    }
    
    /**
     * Shows this {@link Tablist}.
     * <p>If the player had an existing {@link Tablist}, it will be removed.</p>
     */
    public void show() {
        // We must put the tablist in the map before showing entries because we're intercepting a packet
        final Tablist previousTablist = PLAYER_TAB_LIST_MAP.put(player, this);
        
        if (previousTablist != null) {
            previousTablist.destroy();
        }
        
        Reflect.sendPacket(player, new ClientboundPlayerInfoUpdatePacket(ACTION_SET_INIT, new HashSet<>(entries.values())));
    }
    
    /**
     * Hides this {@link Tablist}.
     */
    public void hide() {
        entries.forEach((index, entry) -> entry.hide(player));
        
        PLAYER_TAB_LIST_MAP.remove(player, this);
    }
    
    /**
     * Completely destroys this {@link Tablist}, clearing all entries.
     */
    @Override
    public void destroy() {
        entries.forEach((index, entry) -> entry.hide(player));
        entries.clear();
    }
    
    @Nullable
    protected TablistEntry getEntryByProfileUuid(@Nonnull UUID uuid) {
        return entries.values()
                      .stream().filter(entry -> entry.getUUID() == uuid)
                      .findFirst()
                      .orElse(null);
    }
    
    /**
     * Gets the current {@link Tablist} for the given {@link Player}.
     *
     * @param player - Player.
     * @return player's tablist, or null.
     */
    @Nullable
    public static Tablist getPlayerTabList(@Nonnull Player player) {
        return PLAYER_TAB_LIST_MAP.get(player);
    }
    
}