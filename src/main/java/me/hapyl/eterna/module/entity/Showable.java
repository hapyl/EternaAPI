package me.hapyl.eterna.module.entity;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * Represents a showable {@link Entity} that may exist for a {@link Player}.
 */
public interface Showable {
    
    /**
     * Shows this {@link Entity} for the given {@link Player}.
     *
     * @param player - The player for whom this entity should be shown.
     */
    void show(@NotNull Player player);
    
    /**
     * Shows this {@link Entity} for each online {@link Player}.
     */
    default void showAll() {
        Bukkit.getOnlinePlayers().forEach(this::show);
    }
    
    /**
     * Hides this {@link Entity} for the given {@link Player}.
     *
     * @param player - The player for whom this entity should be hidden.
     */
    void hide(@NotNull Player player);
    
    /**
     * Gets an <b>immutable</b> view of all players who this entity is currently visible to.
     *
     * @return an <b>immutable</b> view of all players who this entity is currently visible to.
     */
    @NotNull
    Collection<Player> showingTo();
    
    /**
     * Gets whether this entity is shown for the given player.
     *
     * @param player - The player to check.
     * @return {@code true} if this entity is visible to the given player, {@code false} otherwise.
     */
    default boolean isShowingTo(@NotNull Player player) {
        return showingTo().contains(player);
    }
    
}
