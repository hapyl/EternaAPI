package me.hapyl.eterna.module.hologram;

import me.hapyl.eterna.module.component.ComponentListSupplier;
import me.hapyl.eterna.module.entity.Showable;
import me.hapyl.eterna.module.location.Located;
import me.hapyl.eterna.module.util.Disposable;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link Hologram} that can display multiple lines of text to a {@link Player}.
 *
 * <p>The lines are defines as a {@link ComponentListSupplier}, allowing each player to see personalized content.</p>
 */
public interface Hologram extends Showable, Located, Disposable {
    
    /**
     * Sets the lines of this {@link Hologram}.
     * <p>Each line is generated dynamically for each {@link Player} using the provided {@link ComponentListSupplier}.</p>
     * <p>Lines are always anchored at the bottom, meaning when there are more than one line, the bottom one will be at the {@link #getLocation()}.</p>
     *
     * @param supplier - The component list supplier.
     */
    void setLines(@NotNull ComponentListSupplier supplier);
    
    /**
     * Gets a copy of the {@link Location} of this {@link Hologram}.
     *
     * @return a copy of the location of this hologram.
     */
    @NotNull
    @Override
    Location getLocation();
    
    /**
     * Teleports this {@link Hologram} to the given {@link Location}.
     *
     * @param location - The designated location.
     */
    void teleport(@NotNull Location location);
    
    /**
     * Teleports this {@link Hologram} to the given {@link Location}.
     *
     * @param location - The designated location.
     */
    @Override
    default void setLocation(@NotNull Location location) {
        teleport(location);
    }
    
    /**
     * Shows this {@link Hologram} for the given {@link Player}.
     *
     * @param player - The player for whom this entity should be shown.
     */
    @Override
    void show(@NotNull Player player);
    
    /**
     * Hides this {@link Hologram} for the give {@link Player}.
     *
     * @param player - The player for whom this entity should be hidden.
     */
    @Override
    void hide(@NotNull Player player);
    
    /**
     * Destroys this {@link Hologram} for all players who can see it.
     */
    @Override
    void dispose();
    
    /**
     * Creates a new {@link Hologram} backed by {@link ArmorStand}
     *
     * @param location - The initial location of the hologram.
     * @return a new armor stand based hologram.
     */
    @NotNull
    static Hologram ofArmorStand(@NotNull Location location) {
        return new HologramImplArmorStand(location);
    }
    
    /**
     * Creates a new {@link Hologram} backed by {@link TextDisplay}.
     *
     * @param location - The initial location of the hologram.
     * @return a new text display based hologram.
     */
    @NotNull
    static Hologram ofTextDisplay(@NotNull Location location) {
        return new HologramImplTextDisplay(location);
    }
}
