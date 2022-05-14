package test;

import kz.hapyl.spigotutils.module.reflect.Laser;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class LaserTest {

    private static Laser laser;
    private static BukkitTask task;

    public static void test(Player player) {
        if (laser == null) {
            laser = new Laser(player.getLocation(), player.getLocation().add(0.0d, 2.0d, 0.0d));
            laser.spawn(player);
            player.sendMessage("§aSpawned.");
        }
        else {
            laser.remove(player);
            laser = null;
            task.cancel();
            player.sendMessage("§aRemoved.");
        }
    }

}
