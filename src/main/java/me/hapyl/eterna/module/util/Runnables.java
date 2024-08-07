package me.hapyl.eterna.module.util;

import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.annotate.AsyncNotSafe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * An easy way to run bukkit tasks, without having to create a new BukkitRunnable every time.
 * Used for testing purposes. The running plugin is always Eterna.
 */
public class Runnables {

    private final static JavaPlugin plugin = EternaPlugin.getPlugin();

    /**
     * Runs the runnable with a delay.
     *
     * @param runnable - runnable to run.
     * @param delay    - delay before running.
     */
    public static void runLater(Runnable runnable, long delay) {
        newBukkitRunnable(runnable).runTaskLater(plugin, delay);
    }

    /**
     * Runs the runnable asynchronously. Keep in mind that this is not thread-safe.
     *
     * @param runnable - runnable to run.
     */
    @AsyncNotSafe
    public static void runAsync(Runnable runnable) {
        newBukkitRunnable(runnable).runTaskAsynchronously(plugin);
    }

    /**
     * Runs the runnable synchronously.
     *
     * @param runnable - runnable to run.
     */
    public static void runSync(Runnable runnable) {
        newBukkitRunnable(runnable).runTask(plugin);
    }

    /**
     * Runs the runnable asynchronously with a delay. Keep in mind that this is not thread-safe.
     *
     * @param runnable - runnable to run.
     * @param delay    - delay before running.
     */
    @AsyncNotSafe
    public static void runLaterAsync(Runnable runnable, long delay) {
        newBukkitRunnable(runnable).runTaskLaterAsynchronously(plugin, delay);
    }

    /**
     * Runs the runnable repeatedly.
     *
     * @param runnable - runnable to run.
     * @param delay    - delay before running.
     * @param period   - period between each run.
     */
    public static void runTimer(Runnable runnable, long delay, long period) {
        newBukkitRunnable(runnable).runTaskTimer(plugin, delay, period);
    }

    /**
     * Runs the runnable repeatedly.
     *
     * @param runnable - runnable to run.
     * @param period   - period between each run.
     */
    public static void runTimer(Runnable runnable, long period) {
        runTimer(runnable, 0, period);
    }

    /**
     * Runs the runnable repeatedly asynchronously. Keep in mind that this is not thread-safe.
     *
     * @param runnable - runnable to run.
     * @param delay    - delay before running.
     * @param period   - period between each run.
     */
    public static void runTimerAsync(Runnable runnable, long delay, long period) {
        newBukkitRunnable(runnable).runTaskTimerAsynchronously(plugin, delay, period);
    }

    /**
     * Runs the runnable repeatedly asynchronously. Keep in mind that this is not thread-safe.
     *
     * @param runnable - runnable to run.
     * @param period   - period between each run.
     */
    public static void runTimerAsync(Runnable runnable, long period) {
        runTimerAsync(runnable, 0, period);
    }

    // Worker
    private static BukkitRunnable newBukkitRunnable(Runnable runnable) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

}
