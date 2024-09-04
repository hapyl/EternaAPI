package me.hapyl.eterna.module.hologram;

import com.google.common.collect.Maps;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * A per-player hologram.
 * Automatically manager creation, updating and removing of a hologram.
 */
public class PlayerHologram {

    private final Map<Player, Hologram> playerHolograms;

    private Location location;
    private Function<Player, StringArray> function;

    public PlayerHologram(@Nonnull Location location) {
        this.location = location;
        this.playerHolograms = Maps.newHashMap();
    }

    /**
     * Sets the lines of this hologram and updates for all players.
     *
     * @param function - Lines to set.
     */
    public void setLines(@Nonnull HologramFunction function) {
        this.function = function;
        updateAll();
    }

    /**
     * Creates hologram for the give player.
     *
     * @param player - Player.
     */
    public void create(@Nonnull Player player) {
        destroy(player);

        final Hologram hologram = new Hologram();
        hologram.create(location).show(player);

        if (function != null) {
            hologram.setLinesAndUpdate(function.apply(player).toArray());
        }

        playerHolograms.put(player, hologram);
    }

    /**
     * Destroys the armor stand for the given player.
     *
     * @param player - Player.
     */
    public void destroy(@Nonnull Player player) {
        final Hologram hologram = playerHolograms.remove(player);

        if (hologram != null) {
            hologram.destroy();
        }
    }

    /**
     * Moves holograms to the given location.
     *
     * @param location - Location to move to.
     */
    public void move(@Nonnull Location location) {
        this.location = location;
        forEach((player, hologram) -> hologram.teleport(location));
    }

    /**
     * Performs a for each iteration.
     * <p>
     * This method will hide the holograms for player is they're not {@link Player#isOnline()}.
     *
     * @param consumer - Player >< Hologram relationship.
     */
    public void forEach(@Nonnull BiConsumer<Player, Hologram> consumer) {
        if (playerHolograms.isEmpty()) {
            return;
        }

        // Actually remove the player
        playerHolograms.keySet().removeIf(player -> !player.isOnline());

        // Only then accept the consumer
        playerHolograms.forEach(consumer);
    }

    /**
     * Gets the actual map of {@link Player} and their respective {@link Hologram}.
     *
     * @return the map.
     */
    @Nonnull
    public Map<Player, Hologram> getPlayerHolograms() {
        return playerHolograms;
    }

    /**
     * Hides all holograms for their players.
     */
    public void hideAll() {
        forEach((player, hologram) -> hologram.hide(player));
    }

    /**
     * Shows all holograms for their players.
     * Note that hologram must be created for a player before it can be shown.
     */
    public void showAll() {
        forEach((player, hologram) -> hologram.show(player));
    }

    private void updateAll() {
        forEach((player, hologram) -> hologram.setLinesAndUpdate(function.apply(player).toArray()));
    }

}