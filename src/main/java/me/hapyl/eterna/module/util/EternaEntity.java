package me.hapyl.eterna.module.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;

/**
 * Represents an {@link me.hapyl.eterna.EternaAPI} 'entity', which can be shown, hidden to players.
 * <p>
 * It is not necessary an entity, just something that can be shown and hidden.
 */
public interface EternaEntity {

    /**
     * Shows this entity to the player.
     *
     * @param player - Player to show.
     */
    void show(@Nonnull Player player);

    /**
     * Shows this entity to the given players.
     *
     * @param players - Players to show.
     */
    default void show(@Nonnull Collection<? extends Player> players) {
        players.forEach(this::show);
    }

    /**
     * Shows this entity to all online players.
     */
    default void showAll() {
        show(Bukkit.getOnlinePlayers());
    }

    /**
     * Hides this entity from the player.
     *
     * @param player - Player to hide from.
     */
    void hide(@Nonnull Player player);

    /**
     * Hides this entity from the given players.
     *
     * @param players - Players to hide from.
     */
    default void hide(@Nonnull Collection<? extends Player> players) {
        players.forEach(this::hide);
    }

    /**
     * Hides this entity from all online players.
     */
    default void hideAll() {
        hide(Bukkit.getOnlinePlayers());
    }

    /**
     * Returns true if this entity is showing to the given player; false otherwise.
     *
     * @param player - Player to check.
     * @return true if the player is seeing this entity, false otherwise.
     */
    default boolean isShowing(@Nonnull Player player) {
        return getShowingTo().contains(player);
    }

    /**
     * Returns a copy of players who are seeing this entity.
     *
     * @return a copy of players who are seeing this entity.
     */
    @Nonnull
    Set<Player> getShowingTo();

}
