package me.hapyl.eterna.module.entity.packet;

import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * Represents a wrapper for an entity that is spawned via packets, rather than adding it to the world.
 */
public interface IPacketEntity {

    /**
     * Gets the NMS entity.
     *
     * @return NMS entity.
     */
    @Nonnull
    Entity getEntity();

    /**
     * Returns true if this entity is visible for the given player.
     *
     * @param player - Player.
     * @return true if this entity is visible for the given player.
     */
    boolean isShowingTo(@Nonnull Player player);

    /**
     * Shows this entity for the given player.
     *
     * @param player - Player to show for.
     */
    void show(@Nonnull Player player);

    /**
     * Shows this entity globally for each online player.
     */
    default void showGlobally() {
        Bukkit.getOnlinePlayers().forEach(this::show);
    }

    /**
     * Shows this entity for the given players.
     *
     * @param players - Players to show for.
     */
    default void show(@Nonnull Collection<Player> players) {
        for (Player player : players) {
            show(player);
        }
    }

    /**
     * Hides this entity from the given player.
     *
     * @param player - Player to hide from.
     */
    void hide(@Nonnull Player player);

    /**
     * Hides this entity globally for each online player.
     */
    default void hideGlobally() {
        Bukkit.getOnlinePlayers().forEach(this::hide);
    }

    /**
     * Hides this entity from the given players.
     *
     * @param players - Players to hide from.
     */
    default void hide(@Nonnull Collection<Player> players) {
        for (Player player : players) {
            hide(player);
        }
    }

    /**
     * Destroys this entity.
     */
    void destroy();

    /**
     * Sets if this entity is visible.
     *
     * @param visibility - Visibility.
     */
    void setVisible(boolean visibility);

    /**
     * Sets if this entity has a collision.
     *
     * @param collision - Collision.
     */
    void setCollision(boolean collision);

    /**
     * Sets if this entity is silent.
     *
     * @param silent - Silent.
     */
    void setSilent(boolean silent);

    /**
     * Sets if this entity has gravity.
     *
     * @param gravity - Gravity.
     */
    void setGravity(boolean gravity);

    /**
     * Makes this entity "marker", meaning:
     * <ul>
     *     <li>Makes it silent.
     *     <li>Removes gravity.
     *     <li>Makes it invisible.
     *     <li>Removes collision.
     * </ul>
     */
    void setMarker();

    /**
     * Teleports this entity to the given location.
     *
     * @param location - Location.
     */
    void teleport(@Nonnull Location location);

    /**
     * Gets the numeric Id of this entity.
     *
     * @return the entity's Id.
     */
    int getId();
}
