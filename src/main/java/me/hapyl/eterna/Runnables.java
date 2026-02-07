package me.hapyl.eterna;

import me.hapyl.eterna.module.annotate.Asynchronous;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Represents an internal runnable handler.
 */
@ApiStatus.Internal
public final class Runnables {
    
    private final static Plugin ETERNA = Eterna.getPlugin();
    
    private Runnables() {
    }
    
    @ApiStatus.Internal
    @NotNull
    public static CompletableFuture<Void> later(@NotNull Runnable runnable, long delay) {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        
        newBukkitRunnable(() -> {
            runnable.run();
            future.complete(null);
        }).runTaskLater(ETERNA, delay);
        
        return future;
    }
    
    @Asynchronous
    @ApiStatus.Internal
    public static void async(@NotNull Runnable runnable) {
        newBukkitRunnable(runnable).runTaskAsynchronously(ETERNA);
    }
    
    @ApiStatus.Internal
    public static void sync(@NotNull Runnable runnable) {
        newBukkitRunnable(runnable).runTask(ETERNA);
    }
    
    @Asynchronous
    @ApiStatus.Internal
    public static CompletableFuture<Void> laterAsync(@NotNull Runnable runnable, long delay) {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        
        newBukkitRunnable(() -> {
            runnable.run();
            future.complete(null);
        }).runTaskLaterAsynchronously(ETERNA, delay);
        
        return future;
    }
    
    @ApiStatus.Internal
    public static void timer(@NotNull Runnable runnable, long delay, long period) {
        newBukkitRunnable(runnable).runTaskTimer(ETERNA, delay, period);
    }
    
    @ApiStatus.Internal
    public static void timer(@NotNull Runnable runnable, long period) {
        timer(runnable, 0, period);
    }
    
    @ApiStatus.Internal
    public static void timerAsync(@NotNull Runnable runnable, long delay, long period) {
        newBukkitRunnable(runnable).runTaskTimerAsynchronously(ETERNA, delay, period);
    }
    
    @ApiStatus.Internal
    public static void timerAsync(@NotNull Runnable runnable, long period) {
        timerAsync(runnable, 0, period);
    }
    
    @ApiStatus.Internal
    public static BukkitRunnable makeTask(@NotNull Runnable runnable, @NotNull Consumer<BukkitRunnable> consumer) {
        final BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        };
        
        consumer.accept(bukkitRunnable);
        return bukkitRunnable;
    }
    
    @ApiStatus.Internal
    @NotNull
    public static <E extends Enum<E>> CompletableFuture<Void> iterateEnum(@NotNull Class<E> enumClass, int delayEach, @NotNull Consumer<E> consumer) {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        int delay = 0;
        
        for (E enumConstant : enumClass.getEnumConstants()) {
            later(() -> consumer.accept(enumConstant), delay += delayEach);
        }
        
        // Complete future
        later(() -> future.complete(null), delay);
        
        return future;
    }
    
    @NotNull
    private static BukkitRunnable newBukkitRunnable(Runnable runnable) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }
    
}
