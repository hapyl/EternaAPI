package me.hapyl.spigotutils.module.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface Holo {

    static Holo create() {
        return new Hologram();
    }

    /**
     * Adds a line of hologram, lines are sorted from top bo bottom. (Revertible)
     *
     * @param line - String.
     */
    Hologram addLine(String line);

    /**
     * Adds all lines to hologram.
     *
     * @param lines - String[]
     */
    Hologram addLines(String... lines);

    /**
     * Clears the hologram lines, adds new ones and updates them.
     *
     * @param lines - Lines to set.
     */
    Hologram setLines(String... lines);

    /**
     * Removes line at the given index.
     *
     * @param index - Index.
     * @throws IndexOutOfBoundsException if there is on such line.
     */
    Hologram removeLine(int index) throws IndexOutOfBoundsException;

    /**
     * Sets string at provided index.
     *
     * @param index - Index.
     * @param line  - String.
     */
    Hologram setLine(int index, String line);

    /**
     * Clears the hologram lines, does not update them.
     */
    Hologram clear();

    /**
     * If hologram is persistent it will not be removed when far away.
     *
     * @param persistent - flag.
     */
    Hologram setPersistent(boolean persistent);

    /**
     * Returns true if hologram is persistent.
     * Persistent holograms are not automatically removed when far away.
     * Though minecraft will un render them still, the custom range will
     * not remove them.
     *
     * @return true if hologram is persistent.
     */
    boolean isPersistent();

    /**
     * Sets the range for which the hologram will be removed.
     *
     * @param hideWhenFurtherThan - Range.
     */
    Hologram setRemoveWhenFarAway(int hideWhenFurtherThan);

    /**
     * Returns the range from which the hologram will be removed.
     *
     * @return range.
     */
    int getRemoveWhenFarAway();

    /**
     * Returns true if hologram is showing to the player.
     *
     * @param player - Player.
     * @return true if hologram is showing to the player.
     */
    boolean isShowingTo(Player player);

    /**
     * Return actual location of the hologram.
     *
     * @return actual location of the hologram.
     */
    Location getLocation();

    /**
     * Removes the hologram.
     */
    boolean destroy();

    /**
     * Shows the hologram to all players.
     */
    Hologram showAll();

    /**
     * Shows the hologram to the provided players.
     *
     * @param players - Players.
     */
    Hologram show(Player... players);

    /**
     * @param flag true -> hide, false -> destroy
     */
    Hologram hide(boolean flag, Player... players);

    /**
     * Hides the hologram from the provided players.
     *
     * @param players - Players.
     */
    Hologram hide(Player... players);

    /**
     * Teleports the hologram to the provided location.
     *
     * @param location - Location.
     */
    Hologram teleport(Location location);

    /**
     * Updates the hologram lines.
     *
     * @param keepListSorted - If true, the list will be sorted bottom top to top.
     */
    Hologram updateLines(boolean keepListSorted);

    /**
     * Updates the hologram lines. The list will be sorted from top to bottom. (Default)
     */
    Hologram updateLines();

    /**
     * Creates a new hologram at the provided location.
     *
     * @param location - Location.
     * @throws IllegalStateException if hologram already exists.
     */
    Hologram create(Location location);

    /**
     * Moves the hologram to the provided location for the provided players.
     *
     * @param location - Location.
     * @param players  - Players.
     */
    Hologram move(Location location, Player... players);
}
