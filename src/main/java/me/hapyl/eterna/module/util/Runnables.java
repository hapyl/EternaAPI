package me.hapyl.eterna.module.util;

import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.annotate.Asynchronous;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@ApiStatus.Internal
public final class Runnables {
    
    private final static JavaPlugin ETERNA = EternaPlugin.getPlugin();
    
    private Runnables() {
    }
    
    @ApiStatus.Internal
    @Nonnull
    public static CompletableFuture<Void> runLater(@Nonnull Runnable runnable, long delay) {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        
        newBukkitRunnable(() -> {
            runnable.run();
            future.complete(null);
        }).runTaskLater(ETERNA, delay);
        
        return future;
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
    public static CompletableFuture<Void> runLaterAsync(@Nonnull Runnable runnable, long delay) {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        
        newBukkitRunnable(() -> {
            runnable.run();
            future.complete(null);
        }).runTaskLaterAsynchronously(ETERNA, delay);
        
        return future;
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
    
    @ApiStatus.Internal
    public static BukkitRunnable makeTask(@Nonnull Runnable runnable, @Nonnull Consumer<BukkitRunnable> consumer) {
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
    @Nonnull
    public static <E extends Enum<E>> CompletableFuture<Void> iterateEnum(@Nonnull Class<E> enumClass, int delayEach, @Nonnull Consumer<E> consumer) {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        int delay = 0;
        
        for (E enumConstant : enumClass.getEnumConstants()) {
            runLater(() -> consumer.accept(enumConstant), delay += delayEach);
        }
        
        // Complete future
        runLater(() -> future.complete(null), delay);
        
        return future;
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
