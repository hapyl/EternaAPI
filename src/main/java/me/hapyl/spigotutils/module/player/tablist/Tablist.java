package me.hapyl.spigotutils.module.player.tablist;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import me.hapyl.spigotutils.module.annotate.Range;
import me.hapyl.spigotutils.module.math.Numbers;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * A four column customizable tab list.
 * <br>
 * Keep in mind this relies on player's scoreboard!
 * Changing it with {@link Player#setScoreboard(Scoreboard)} will <b>break</b> the tablist!
 */
public class Tablist {

    private static final Map<UUID, Tablist> PLAYER_TAB_LIST_MAP = Maps.newHashMap();
    private static final int MAX_ENTRIES = 80;
    private static final int MAX_COLUMN = 3;
    private static final int MAX_ENTRIES_PER_COLUMN = 20;
    private static final TablistEntry DUMMY_ENTRY = new TablistEntry(0);

    private final Map<Integer, TablistEntry> entries;
    private final Set<Player> viewers;

    public Tablist() {
        this.viewers = Sets.newHashSet();
        this.entries = Maps.newLinkedHashMap();

        for (int i = 0; i < MAX_ENTRIES; i++) {
            this.entries.put(i, new TablistEntry(i));
        }
    }

    /**
     * Gets the {@link TablistEntry} at the given index; or {@link #DUMMY_ENTRY} if there is no entry at that index.
     *
     * @param index - Index, from <code>0</code> to <code>79</code>.
     * @return tablist entry.
     */
    @Nonnull
    public TablistEntry getEntry(int index) {
        return this.entries.getOrDefault(index, DUMMY_ENTRY);
    }

    /**
     * Gets the {@link TablistEntry} at the given column and line; or {@link #DUMMY_ENTRY} if there is no entry at that column and line.
     *
     * @param column - Column, from <code>0</code> to <code>3</code>.
     * @param line   - Index, from <code>0</code> to <code>19</code>.
     * @return tablist entry.
     */
    @Nonnull
    public TablistEntry getEntry(int column, int line) {
        return getEntry(line + (column * 20));
    }

    /**
     * Sets the column to the given {@link EntryList}.
     *
     * @param column - Column, from <code>0</code> to <code>3</code>.
     * @param list   - {@link EntryList}.
     * @return tablist entry.
     * @see TablistEntry
     */
    public Tablist setColumn(int column, @Nonnull EntryList list) {
        int index = Numbers.clamp(column, 0, MAX_COLUMN) * MAX_ENTRIES_PER_COLUMN;

        for (String string : list) {
            getEntry(index++).setText(string);
        }

        return this;
    }

    /**
     * Sets the column to the given {@link String} {@link List}.
     *
     * @param column - Column, from <code>0</code> to <code>3</code>.
     * @param lines  - List of lines, max 20 lines.
     * @return tablist entry.
     */
    public Tablist setColumn(int column, @Range(max = 20) @Nonnull List<String> lines) {
        return setColumn(column, new EntryList(lines));
    }

    /**
     * Sets the column to the given {@link String} array.
     *
     * @param column - Column, from <code>0</code> to <code>3</code>.
     * @param lines  - Array of lines, max 20 lines.
     * @return tablist entry.
     */
    public Tablist setColumn(int column, @Range(max = 20) @Nonnull String... lines) {
        return setColumn(column, new EntryList(lines));
    }

    /**
     * Shows this {@link Tablist} to the given {@link Player}.
     *
     * @param player - Player.
     */
    public void show(@Nonnull Player player) {
        if (viewers.contains(player)) {
            return;
        }

        viewers.add(player);

        entries.forEach((index, entry) -> {
            entry.show(player);
        });

        PLAYER_TAB_LIST_MAP.put(player.getUniqueId(), this);
    }

    /**
     * Hides this {@link Tablist} from the given {@link Player}.
     *
     * @param player - Player.
     */
    public void hide(@Nonnull Player player) {
        if (!viewers.contains(player)) {
            return;
        }

        entries.forEach((index, entry) -> {
            entry.hide(player);
        });

        viewers.remove(player);
        PLAYER_TAB_LIST_MAP.remove(player.getUniqueId(), this);
    }

    /**
     * Completely destroys this {@link Tablist}, clearing all entries.
     */
    public void destroy() {
        viewers.forEach(player -> {
            entries.forEach((index, entry) -> entry.hide(player));
        });

        viewers.clear();
        entries.clear();
    }

    /**
     * Gets the current {@link Tablist} for the given {@link Player}.
     *
     * @param player - Player.
     * @return player's tablist, or null.
     */
    @Nullable
    public static Tablist getPlayerTabList(@Nonnull Player player) {
        return PLAYER_TAB_LIST_MAP.get(player.getUniqueId());
    }

    /**
     * Iterates over all {@link Tablist}.
     *
     * @param consumer - Consumer to apply.
     */
    public static void forEach(@Nonnull Consumer<Tablist> consumer) {
        PLAYER_TAB_LIST_MAP.values().forEach(consumer);
    }
}