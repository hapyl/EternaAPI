package me.hapyl.eterna.module.util;

import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.annotate.Asynchronous;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;

@ApiStatus.Internal
public class Runnables {

    private final static JavaPlugin ETERNA = EternaPlugin.getPlugin();

    @ApiStatus.Internal
    public static void runLater(@Nonnull Runnable runnable, long delay) {
        newBukkitRunnable(runnable).runTaskLater(ETERNA, delay);
    }

    @Asynchronous
    @ApiStatus.Internal
    public static void runAsync(@Nonnull Runnable runnable) {
        newBukkitRunnable(runnable).runTaskAsynchronously(ETERNA);
    }

    @ApiStatus.Internal
    public static void runSync(@Nonnull Runnable runnable) {
        newBukkitRunnable(runnable).runTask(ETERNA);
    }

    @Asynchronous
    @ApiStatus.Internal
    public static void runLaterAsync(@Nonnull Runnable runnable, long delay) {
        newBukkitRunnable(runnable).runTaskLaterAsynchronously(ETERNA, delay);
    }

    @ApiStatus.Internal
    public static void runTimer(@Nonnull Runnable runnable, long delay, long period) {
        newBukkitRunnable(runnable).runTaskTimer(ETERNA, delay, period);
    }

    @ApiStatus.Internal
    public static void runTimer(@Nonnull Runnable runnable, long period) {
        runTimer(runnable, 0, period);
    }

    @ApiStatus.Internal
    public static void runTimerAsync(@Nonnull Runnable runnable, long delay, long period) {
        newBukkitRunnable(runnable).runTaskTimerAsynchronously(ETERNA, delay, period);
    }

    @ApiStatus.Internal
    public static void runTimerAsync(@Nonnull Runnable runnable, long period) {
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
