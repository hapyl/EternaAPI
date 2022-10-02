package test;

import me.hapyl.spigotutils.module.reflect.Laser;
import org.bukkit.entity.Player;

public class LaserTest {

    private static Laser laser;

    public static void test(Player player) {
        if (laser == null) {
            laser = new Laser(player.getLocation(), player.getLocation().add(0.0d, 2.0d, 0.0d));
            laser.spawn(player);
            player.sendMessage("§aSpawned.");
        }
        else {
            laser.remove(player);
            laser = null;
            player.sendMessage("§aRemoved.");
        }
    }

}
