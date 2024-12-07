package me.hapyl.eterna.module.reflect.border;

import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.util.Holder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

/**
 * Allows creating a per-player world border.
 * Note that this client is only change, and it will
 * be reset after player re-logins.
 */
public class PlayerBorder extends Holder<Player> {

    private final WorldBorder border;

    public PlayerBorder(Player player) {
        super(player);
        border = Bukkit.createWorldBorder();
        border.setWarningTime(0);
    }

    /**
     * Update world border.
     *
     * @param operation - Operation to perform update as.
     */
    public void update(Operation operation) {
        update(operation, 1000);
    }

    /**
     * Update world border with provided size.
     *
     * @param operation - Operation to perform update as.
     * @param size      - New border size.
     */
    public void update(Operation operation, double size) {
        final Player player = getPlayer();
        final Location location = player.getLocation();

        size = Math.clamp(size, 2.0d, Double.MAX_VALUE);
        border.setCenter(location.getX(), location.getZ());

        if (operation == Operation.REMOVE) {
            player.setWorldBorder(player.getWorld().getWorldBorder());
            return;
        }

        border.setSize(size);
        border.setWarningDistance(Integer.MAX_VALUE);

        switch (operation) {
            case BORDER_RED -> border.setSize(size - 1.0d, Long.MAX_VALUE);
            case BORDER_GREEN -> {
                final net.minecraft.world.level.border.WorldBorder netBorder = Reflect.getNetWorldBorder(border);
                if (netBorder != null) {
                    netBorder.lerpSizeBetween(size - 0.1d, size, Long.MAX_VALUE);
                }
            }
        }

        player.setWorldBorder(border);
    }

    /**
     * Resets players border.
     *
     * @param player - Player to reset border for.
     */
    public static void reset(Player player) {
        new PlayerBorder(player).update(Operation.REMOVE, 0);
    }

    /**
     * Shows red outline on players screen by setting
     * border at 1000 and warning at max value.
     *
     * @param player - Player to show outline to.
     */
    public static void showRedOutline(Player player) {
        new PlayerBorder(player).update(Operation.BORDER_RED);
    }

    public Player getPlayer() {
        return this.get();
    }

    public enum Operation {
        /**
         * Removes players border.
         */
        REMOVE,
        /**
         * Changes border color to green.
         */
        BORDER_GREEN,
        /**
         * Changes border color to red.
         */
        BORDER_RED
    }

}
