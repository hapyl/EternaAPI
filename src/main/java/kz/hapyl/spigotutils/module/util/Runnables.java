package kz.hapyl.spigotutils.module.util;

import kz.hapyl.spigotutils.SpigotUtilsPlugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Runnables {

    private final static JavaPlugin plugin = SpigotUtilsPlugin.getPlugin();

    public static void runLater(Runnable runnable, long delay) {
        newBukkitRunnable(runnable).runTaskLater(plugin, delay);
    }

    public static void runAsync(Runnable runnable) {
        newBukkitRunnable(runnable).runTaskAsynchronously(plugin);
    }

    public static void runSync(Runnable runnable) {
        newBukkitRunnable(runnable).runTask(plugin);
    }

    public static void runLaterAsync(Runnable runnable, long delay) {
        newBukkitRunnable(runnable).runTaskLaterAsynchronously(plugin, delay);
    }

    public static void runTimer(Runnable runnable, long delay, long period) {
        newBukkitRunnable(runnable).runTaskTimer(plugin, delay, period);
    }

    public static void runTimer(Runnable runnable, long period) {
        runTimer(runnable, 0, period);
    }

    public static void runTimerAsync(Runnable runnable, long delay, long period) {
        newBukkitRunnable(runnable).runTaskTimerAsynchronously(plugin, delay, period);
    }

    public static void runTimerAsync(Runnable runnable, long period) {
        runTimerAsync(runnable, 0, period);
    }

    private static BukkitRunnable newBukkitRunnable(Runnable runnable) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

}
