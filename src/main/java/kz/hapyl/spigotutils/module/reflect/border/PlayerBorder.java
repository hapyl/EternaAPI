package kz.hapyl.spigotutils.module.reflect.border;

import kz.hapyl.spigotutils.module.EternaModule;
import kz.hapyl.spigotutils.module.annotate.TestedNMS;
import kz.hapyl.spigotutils.module.math.Numbers;
import kz.hapyl.spigotutils.module.reflect.Reflect;
import kz.hapyl.spigotutils.module.util.Holder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

@EternaModule
@TestedNMS(version = "1.18.2")
public class PlayerBorder extends Holder<Player> {

    private final WorldBorder border;

    public PlayerBorder(Player player) {
        super(player);
        border = Bukkit.createWorldBorder();
        border.setWarningTime(0);
    }

    public void update(Operation operation) {
        update(operation, 1000);
    }

    public void update(Operation operation, double size) {
        final Player player = getPlayer();
        final Location location = player.getLocation();

        size = Numbers.clamp(size, 2.0d, Double.MAX_VALUE);
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
                    netBorder.a(size - 0.1d, size, Long.MAX_VALUE);
                }
            }
        }

        player.setWorldBorder(border);
    }

    public static void reset(Player player) {
        new PlayerBorder(player).update(Operation.REMOVE, 0);
    }

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
