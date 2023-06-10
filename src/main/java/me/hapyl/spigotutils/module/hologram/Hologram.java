package me.hapyl.spigotutils.module.hologram;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.annotate.Super;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.util.BukkitUtils;
import me.hapyl.spigotutils.module.entity.LimitedVisibility;
import me.hapyl.spigotutils.registry.EternaRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * This class allows creating packet-holograms
 */
public class Hologram extends LimitedVisibility {

    /**
     * Vertical offset of the holograms.
     */
    public static final double HOLOGRAM_OFFSET = 0.3d;
    public static final double HOLOGRAM_HEIGHT = 1.75d;

    private final List<String> lines;
    private final List<HologramArmorStand> packets;
    private final Set<Player> showingTo;

    private BukkitTask task;
    private Location location;

    /**
     * Creates hologram with provided size.
     *
     * @param size - Lines size.
     */
    public Hologram(int size) {
        this.lines = new ArrayList<>(size);
        this.packets = Lists.newArrayList();
        this.showingTo = Sets.newConcurrentHashSet();

        setVisibility(25);
        EternaRegistry.getHologramRegistry().register(this);
    }

    /**
     * Creates hologram with size 1.
     */
    public Hologram() {
        this(1);
    }

    /**
     * Adds a line to the hologram.
     *
     * @param line - Line to add.
     */
    public Hologram addLine(@Nonnull String line) {
        this.lines.add(line);
        return this;
    }

    /**
     * Adds all lines to the hologram.
     *
     * @param lines - Line to add.
     */
    public Hologram addLines(@Nonnull String... lines) {
        this.lines.addAll(Arrays.asList(lines));
        return this;
    }

    /**
     * Removes the line at the given index, does nothing if index >= size().
     *
     * @param index - Index to remove at.
     */
    public Hologram removeLine(int index) {
        if (this.lines.size() - 1 < index) {
            return this;
        }
        else {
            this.lines.remove(index);
        }
        return this;
    }

    /**
     * Gets all lines of this hologram.
     *
     * @return string list for this hologram.
     */
    @Nonnull
    public List<String> getLines() {
        return lines;
    }

    /**
     * Sets the lines of this hologram. This will remove existing lines.
     * <h1>
     * Setting lines does not update them.
     * </h1>
     * <p>
     * Make sure to use {@link #updateLines()}.
     * Alternatively, you can use {@link #setLinesAndUpdate(String...)} to change lines and update them.
     *
     * @param lines - Lines to add.
     */
    public Hologram setLines(@Nonnull String... lines) {
        this.lines.clear();
        this.lines.addAll(Arrays.asList(lines));
        return this;
    }

    /**
     * Sets the lines and updates the stands.
     *
     * @param lines - Lines to add.
     */
    public Hologram setLinesAndUpdate(@Nonnull String... lines) {
        setLines(lines);
        updateLines(false);
        return this;
    }

    /**
     * Sets the line at the given index. This will add needed lines if index >= size()
     *
     * @param index - Index.
     * @param line  - Line to add.
     */
    public Hologram setLine(int index, @Nonnull String line) {
        if (this.lines.size() - 1 < index) {
            for (int i = 0; i < index; i++) {
                addLine("");
            }
        }
        else {
            this.lines.set(index, line);
        }
        return this;
    }

    /**
     * Clears the lines.
     */
    public Hologram clear() {
        this.lines.clear();
        return this;
    }

    /**
     * Returns the set of players and their status.
     *
     * @return the set of players and their status.
     */
    public Set<Player> getShowingTo() {
        return showingTo;
    }

    /**
     * Returns true if this hologram is showing to the given player.
     *
     * @param player - Player.
     * @return true if this hologram is showing to the given player.
     */
    public boolean isShowingTo(Player player) {
        return this.showingTo.contains(player);
    }

    /**
     * Gets the copy of this hologram origin location.
     *
     * @return the copy of this hologram origin location.
     */
    @Nonnull
    @Override
    public Location getLocation() {
        return BukkitUtils.newLocation(location);
    }

    /**
     * Completely destroys this hologram.
     */
    public void destroy() {
        this.removeStands();

        if (this.task != null) {
            this.task.cancel();
        }

        EternaRegistry.getHologramRegistry().unregister(this);
    }

    /**
     * Shows this hologram to all online players.
     */
    public Hologram showAll() {
        return this.show(BukkitUtils.onlinePlayersAsArray());
    }

    /**
     * Shows this hologram to the given player.
     *
     * @param players - Player to show to.
     */
    @Super
    public Hologram show(Player... players) {
        if (this.location == null) {
            for (final Player player : players) {
                Chat.sendMessage(player, "&4Could not spawn a hologram for you since is wasn't created yet!");
            }
            return this;
        }

        for (Player player : players) {
            if (isShowingTo(player)) {
                continue;
            }

            showingTo.add(player);
            packets.forEach(hologram -> {
                hologram.show(player);
                hologram.updateLocation(player);
            });
        }

        return this;
    }

    /**
     * Hides the hologram from the given players.
     *
     * @param players - Players.
     */
    @Super
    public Hologram hide(Player... players) {
        for (Player player : players) {
            showingTo.remove(player);
            packets.forEach(hologram -> hologram.hide(player));
        }

        return this;
    }

    /**
     * Executed every tick this hologram exists.
     *
     * @param action - Action to perform.
     * @param tick   - Tick.
     */
    public Hologram onTick(HologramAction<?> action, int tick) {
        return onTick(action, tick, Bukkit.getOnlinePlayers().toArray(new Player[] {}));
    }

    public Hologram onTick(HologramAction<?> action, int tick, Player... receivers) {
        if (this.task != null) {
            this.task.cancel();
        }

        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                action.consume(receivers);
            }
        }.runTaskTimer(EternaPlugin.getPlugin(), 0, tick);
        return this;
    }

    /**
     * Moves this hologram to the provided location.
     * This will only move the hologram for players who this hologram is showing to.
     *
     * @param location - Location to move to.
     */
    public Hologram teleport(@Nonnull Location location) {
        return move(location);
    }

    /**
     * Removes the holograms and creates them again with updated values.
     * This will only affect players who this hologram is showing to.
     *
     * @param keepListSorted - To whenever to keep the lines sorted (Reverse add order).
     *                       Default to false for TOP to BOTTOM order.
     */
    public Hologram updateLines(boolean keepListSorted) {
        final Location location = getLocation();

        // If the size didn't change, just update the armor stand lines.

        // FIXME (hapyl): 010, Jun 10: A little bit (VERY) ugly,
        //  but the keepListSorted is very annoying, maybe split into different hologram class.
        if (nonEmptyLinesSizes() == packets.size()) {
            if (keepListSorted) {
                int index = nonEmptyLinesSizes() - 1;
                for (int i = lines.size() - 1; i >= 0; i--) {
                    final String line = lines.get(i);
                    if (line.isBlank() || line.isEmpty()) {
                        continue;
                    }

                    packets.get(index--).setLine(line);
                }
            }
            else {
                int index = 0;
                for (String line : lines) {
                    if (line.isBlank() || line.isEmpty()) {
                        continue;
                    }

                    packets.get(index++).setLine(Chat.format(line));
                }
            }

            for (Player player : showingTo) {
                packets.forEach(stand -> stand.update(player));
            }
        }
        else {
            // have to use a copy here, since removing stands clears the 'showingTo' set
            final HashSet<Player> players = Sets.newHashSet(showingTo);

            removeStands();

            if (keepListSorted) {
                for (final String line : lines) {
                    location.add(0.0d, HOLOGRAM_OFFSET, 0.0d);
                    createStand(location, ChatColor.translateAlternateColorCodes('&', line));
                }
            }
            else {
                for (final String line : lines) {
                    createStand(location, ChatColor.translateAlternateColorCodes('&', line));
                    location.subtract(0.0d, HOLOGRAM_OFFSET, 0.0d);
                }
            }

            players.forEach(this::show);
            players.clear();
        }

        return this;
    }

    /**
     * Updates lines with unsorted (TOP to BOTTOM) order.
     */
    public Hologram updateLines() {
        return this.updateLines(false);
    }

    /**
     * Creates the hologram at the given location.
     * The hologram must be created before showing it to the players.
     *
     * @param location - Location.
     * @throws IllegalStateException if the hologram is already created.
     */
    public Hologram create(@Nonnull Location location) {
        if (this.location != null) {
            throw new IllegalStateException("This hologram was already created!");
        }

        this.location = location.clone();
        this.location.add(0.0d, HOLOGRAM_HEIGHT, 0.0); // Fix marker location

        // Move the current location up so holograms stop at the start position
        this.location.add(0.0d, HOLOGRAM_OFFSET * (double) lines.size(), 0.0d);

        for (String string : lines) {
            createStand(this.location, Chat.format(string));
            this.location.subtract(0.0d, HOLOGRAM_OFFSET, 0.0d);
        }
        return this;
    }

    /**
     * Moves this hologram to the given location.
     *
     * @param location - Location.
     */
    public Hologram move(Location location) {
        location.add(0.0d, HOLOGRAM_OFFSET * (double) lines.size(), 0.0d);
        packets.forEach(hologram -> {
            hologram.setLocation(location);
            hologram.updateLocation(showingTo);
            location.subtract(0.0d, HOLOGRAM_OFFSET, 0.0d);
        });
        return this;
    }

    /**
     * Returns the string representation of this hologram lines.
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("[");

        for (int i = 0; i < lines.size(); i++) {
            if (i != 0) {
                builder.append(", ");
            }

            builder.append(lines.get(i));
        }

        return builder.append("]").toString();
    }

    /**
     * Returns true if this hologram was created; false otherwise.
     *
     * @return true if this hologram was created; false otherwise.
     */
    public boolean isCreated() {
        return this.location != null;
    }

    @Override
    public final void hideVisibility(@Nonnull Player player) {
        for (HologramArmorStand packet : packets) {
            packet.hide(player);
        }
    }

    @Override
    public final void showVisibility(@Nonnull Player player) {
        for (HologramArmorStand packet : packets) {
            packet.show(player);
        }
    }

    protected void removeStands() {
        showingTo.forEach(this::hide);
        packets.clear();
    }

    private int nonEmptyLinesSizes() {
        int size = 0;
        for (String line : lines) {
            if (line.isBlank() || line.isEmpty()) {
                continue;
            }

            size++;
        }
        return size;
    }

    private void createStand(Location location, String name) {
        // skip line if empty instead of | thing
        if (name.isEmpty()) {
            return;
        }

        packets.add(new HologramArmorStand(location, name));
    }

}
