package me.hapyl.eterna.module.entity;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * Represents a showable entity that may exist for a given player.
 */
public interface Showable {
    
    /**
     * Shows this entity for the given player.
     *
     * @param player - The player for whom this entity should be shown.
     */
    void show(@Nonnull Player player);
    
    /**
     * Shows this entity for each online player.
     */
    default void showAll() {
        Bukkit.getOnlinePlayers().forEach(this::show);
    }
    
    /**
     * Hides this entity for the given player.
     *
     * @param player - The player for whom this entity should be hidden.
     */
    void hide(@Nonnull Player player);
    
    /**
     * Gets an <b>immutable</b> view of all players who this entity is currently visible to.
     *
     * @return an <b>immutable</b> view of all players who this entity is currently visible to.
     */
    @Nonnull
    Collection<Player> showingTo();
    
    /**
     * Gets whether this entity is shown for the given player.
     *
     * @param player - The player to check.
     * @return {@code true} if this entity is visible to the given player, {@code false} otherwise.
     */
    default boolean isShowingTo(@Nonnull Player player) {
        return showingTo().contains(player);
    }
    
}
