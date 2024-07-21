package me.hapyl.spigotutils.module.hologram;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import me.hapyl.spigotutils.Eterna;
import me.hapyl.spigotutils.EternaLogger;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.annotate.Super;
import me.hapyl.spigotutils.module.util.BukkitUtils;
import me.hapyl.spigotutils.module.entity.LimitedVisibility;
import me.hapyl.spigotutils.registry.EternaRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * This class allows creating packet-holograms
 */
public class Hologram extends LimitedVisibility {

    /**
     * Vertical offset of the holograms.
     */
    public static final double HOLOGRAM_OFFSET = 0.25d;
    public static final double ABSOLUTE_CENTER_OFFSET = 0.2d;
    protected final Set<Player> showingTo;
    private final List<String> lines;
    private final List<HologramArmorStand> packets;
    private Location location;

    /**
     * Creates hologram with provided size.
     *
     * @param size - Size of this hologram.
     */
    public Hologram(int size) {
        this.lines = new ArrayList<>(size);
        this.packets = Lists.newArrayList();
        this.showingTo = Sets.newConcurrentHashSet();

        setVisibility(25);
        Eterna.getRegistry().hologramRegistry.register(this);
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
        updateLines();
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

        Eterna.getRegistry().hologramRegistry.unregister(this);
    }

    /**
     * Shows this hologram to all online players.
     */
    public Hologram showAll() {
        Bukkit.getOnlinePlayers().forEach(this::show);
        return this;
    }

    /**
     * Shows this hologram to the given player.
     *
     * @param player - Player to show to.
     */
    @Super
    public Hologram show(@Nonnull Player player) {
        if (this.location == null) {
            throw new IllegalStateException("Cannot spawn a hologram because it's not created yet!");
        }

        if (isShowingTo(player)) {
            return this;
        }

        showingTo.add(player);
        packets.forEach(hologram -> {
            hologram.show(player);
            hologram.updateLocation(player);
        });

        return this;
    }

    /**
     * Hides the hologram from the given player.
     *
     * @param player - Player to hide from.
     */
    @Super
    public Hologram hide(@Nonnull Player player) {
        showingTo.remove(player);
        packets.forEach(hologram -> hologram.hide(player));

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

    public Hologram updateLines() {
        return updateLines(LineFit.DEFAULT);
    }

    public Hologram updateLines(@Nonnull LineFit lineFit) {
        final Location location = getLocation();
        final List<String> fit = lineFit.fit(lines);

        if (lines.size() != fit.size()) {
            throw new IllegalArgumentException("Illegal fit, size must match! lines=%s, fit=%s".formatted(lines.size(), fit.size()));
        }

        // Update
        if (nonEmptyLinesSizes() == packets.size()) {
            int index = 0;
            for (int i = 0; i < lines.size(); i++) {
                final String line = fit.get(i);

                if (line.isBlank() || line.isEmpty()) {
                    continue;
                }

                packets.get(index++).setLine(line);
            }

            for (Player player : showingTo) {
                packets.forEach(stand -> stand.update(player));
            }
        }
        // Create
        else {
            final HashSet<Player> players = Sets.newHashSet(showingTo);
            removeStands();

            for (int i = 0; i < lines.size(); i++) {
                createStand(location.add(0, HOLOGRAM_OFFSET, 0), fit.get(i));
            }

            players.forEach(this::show);
            players.clear();
        }

        return this;
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

        this.location = location.clone().subtract(0.0d, ABSOLUTE_CENTER_OFFSET, 0.0);

        updateLines();
        return this;
    }

    /**
     * Moves this hologram to the given location.
     *
     * @param newLocation - Location.
     */
    public Hologram move(@Nonnull Location newLocation, @Nonnull LineFit lineFit) {
        this.location = newLocation; // actually update location lol

        final Location location = getLocation();
        final List<String> fit = lineFit.fit(lines);

        if (lines.size() != fit.size()) {
            throw new IllegalStateException("Lines and fit size must match!");
        }

        int index = 0;
        for (int i = 0; i < lines.size(); i++) {
            final String line = fit.get(i);

            if (!line.isBlank() && !line.isEmpty()) {
                final HologramArmorStand packet = packets.get(index++);
                packet.setLocation(location);
                packet.updateLocation(showingTo);
            }

            location.add(0, HOLOGRAM_OFFSET, 0);
        }

        return this;
    }

    /**
     * Moves this hologram to the given location.
     *
     * @param newLocation - Location.
     */
    public Hologram move(@Nonnull Location newLocation) {
        return move(newLocation, LineFit.DEFAULT);
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
            packet.updateLocation(player);
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
