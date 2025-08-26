package me.hapyl.eterna.module.hologram;

import me.hapyl.eterna.module.entity.Showable;
import me.hapyl.eterna.module.locaiton.Located;
import me.hapyl.eterna.module.util.list.StringList;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Represents a hologram that can display multiple lines of text to players.
 * <p>
 * The lines are defined as a {@link LineSupplier}, allowing each player to see personalized content.
 */
public interface Hologram extends Showable, Located {
    
    /**
     * Sets the lines of this {@link Hologram}.
     * Each line is generated dynamically for each {@link Player} using the provided {@link LineSupplier}.
     * <p>
     * For example:
     * <pre>{@code
     * hologram.setLines(player -> StringArray.of("Hello, " + player.getName()));
     * }</pre>
     * This will show "Hello, PLAYER_NAME" for each player individually.
     * </p>
     *
     * @param supplier - The supplier.
     */
    void setLines(@Nonnull LineSupplier supplier);
    
    /**
     * Sets static lines for this {@link Hologram}.
     * <p>
     * This is a convenience method that wraps the given {@link StringList} into a function,
     * so the same lines are shown to all players.
     *
     * @param lines the lines to display
     */
    default void setLines(@Nonnull StringList lines) {
        setLines(player -> lines);
    }
    
    /**
     * Sets static lines for this {@link Hologram}.
     * <p>
     * This is a convenience method that wraps the given varargs {@link String} into a function,
     * so the same lines are shown to all players.
     *
     * @param lines the lines to display
     */
    default void setLines(@Nonnull String... lines) {
        setLines(player -> StringList.of(lines));
    }
    
    /**
     * Gets a copy of the location of this {@link Hologram}.
     *
     * @return a copy of the location of this {@link Hologram}.
     */
    @Nonnull
    @Override
    Location getLocation();
    
    /**
     * Teleports this {@link Hologram} to the given destination.
     *
     * @param location - The new location.
     */
    void teleport(@Nonnull Location location);
    
    /**
     * Shows this {@link Hologram} for the given player.
     *
     * @param player - The player for whom this entity should be shown.
     */
    @Override
    void show(@Nonnull Player player);
    
    /**
     * Hides this {@link Hologram} for the give player.
     *
     * @param player - The player for whom this entity should be hidden.
     */
    @Override
    void hide(@Nonnull Player player);
    
    /**
     * Destroys this {@link Hologram} for all players who can see it.
     */
    void destroy();
    
    /**
     * Creates a new {@link Hologram} backed by armor stands.
     *
     * @param location - The base {@link Location} of the hologram.
     * @return a new armor stand based hologram.
     */
    @Nonnull
    static Hologram ofArmorStand(@Nonnull Location location) {
        return new HologramImplArmorStand(location);
    }
    
    /**
     * Creates a new {@link Hologram} backed by text display entities.
     *
     * @param location - The base {@link Location} of the hologram.
     * @return a new text display based hologram.
     */
    @Nonnull
    static Hologram ofTextDisplay(@Nonnull Location location) {
        return new HologramImplTextDisplay(location);
    }
}
