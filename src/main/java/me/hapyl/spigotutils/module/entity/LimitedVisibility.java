package me.hapyl.spigotutils.module.entity;

import com.google.common.collect.Sets;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * A visibility limiter for entities.
 */
public abstract class LimitedVisibility {

    private final Set<Player> hidden;
    private double distance;

    public LimitedVisibility() {
        hidden = Sets.newHashSet();
        distance = -1;
    }

    /**
     * Gets the visibility, or -1 if no limit.
     *
     * @return the visibility, or -1 if no limit.
     */
    public double getVisibility() {
        return distance;
    }

    /**
     * Sets the visibility.
     *
     * @param distance - New visibility. Use -1 to remove limit.
     */
    public LimitedVisibility setVisibility(double distance) {
        this.distance = distance;
        return this;
    }

    /**
     * Returns true if limited; false otherwise.
     *
     * @return true if limited; false otherwise.
     */
    public boolean isLimited() {
        return distance > 0;
    }

    public final void hideVisibility0(@Nonnull Player player) {
        hidden.add(player);
        hideVisibility(player);
    }

    public final void showVisibility0(@Nonnull Player player) {
        hidden.remove(player);
        showVisibility(player);
    }

    /**
     * Gets the location for the distance check.
     *
     * @return the location for the distance check.
     */
    @Nonnull
    public abstract Location getLocation();

    /**
     * Hide impl.
     *
     * @param player - Player.
     */
    public abstract void hideVisibility(@Nonnull Player player);

    /**
     * Show impl.
     *
     * @param player - Player.
     */
    public abstract void showVisibility(@Nonnull Player player);

    /**
     * Returns true if player can see this entity; false otherwise.
     *
     * @param player - Player.
     * @return true if a player can see this entity; false otherwise.
     */
    public final boolean canSee(@Nonnull Player player) {
        if (distance <= 0) {
            return true;
        }

        return player.getLocation().distance(getLocation()) <= distance;
    }

    /**
     * Returns true if this entity is not hidden for a player.
     *
     * @param player - Player.
     * @return true if this entity is not hidden for a player.
     */
    public final boolean isShownTo(@Nonnull Player player) {
        return !hidden.contains(player);
    }

    // performs a static check and hides/shows when needed.
    public static void check(@Nonnull Player player, LimitedVisibility visibility) {
        if (!visibility.isLimited()) {
            return;
        }

        if (visibility.canSee(player)) {
            if (!visibility.isShownTo(player)) {
                visibility.showVisibility0(player);
            }
        }
        else {
            if (visibility.isShownTo(player)) {
                visibility.hideVisibility0(player);
            }
        }
    }

}
