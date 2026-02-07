package me.hapyl.eterna.module.scheduler;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Represents a {@link SchedulerTask} that may be scheduled in {@link Scheduler}.
 */
public class SchedulerTask {
    
    protected final CompletableFuture<Void> callback;
    
    private final Runnable runnable;
    private final SchedulerExecutor executor;
    
    private SchedulerTask(@NotNull Runnable runnable, @NotNull SchedulerExecutor executor) {
        this.runnable = runnable;
        this.executor = executor;
        this.callback = new CompletableFuture<>();
    }
    
    /**
     * Runs this task.
     *
     * @param plugin - The plugin delegate.
     * @return a {@link CompletableFuture} that will be completed when the task finished execution <b>successfully</b>, or completed exceptionally if the task throws an {@link Exception}.
     */
    @NotNull
    public CompletableFuture<Void> run(@NotNull Plugin plugin) {
        executor.execute(
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            runnable.run();
                            callback.complete(null);
                        }
                        catch (Exception ex) {
                            callback.completeExceptionally(ex);
                        }
                    }
                }, plugin
        );
        
        return callback;
    }
    
    /**
     * A static factory method for creating a {@link SchedulerTask} that runs the given {@link Runnable}.
     *
     * @param runnable - The runnable to run.
     * @return a new scheduler task.
     */
    @NotNull
    public static SchedulerTask run(@NotNull Runnable runnable) {
        return new SchedulerTask(runnable, BukkitRunnable::runTask);
    }
    
    /**
     * A static factory method for creating a {@link SchedulerTask} that runs the given {@link Runnable} with the given {@code delay}.
     *
     * @param runnable - The runnable to run.
     * @param delay    - The run delay, in ticks.
     * @return a new scheduler task.
     */
    @NotNull
    public static SchedulerTask later(@NotNull Runnable runnable, int delay) {
        return new SchedulerTask(runnable, (_run, _plugin) -> _run.runTaskLater(_plugin, delay));
    }
    
    /**
     * A static factory method for creating a {@link SchedulerTask} that runs the {@link Runnable} which must <b>manually</b> complete the {@code await} future to advance the tasks.
     *
     * @param await - The completable future to complete.
     */
    @NotNull
    public static SchedulerTask await(@NotNull Consumer<CompletableFuture<Void>> await) {
        return new SchedulerTask(() -> {}, BukkitRunnable::runTask) {
            @Override
            @NotNull
            public CompletableFuture<Void> run(@NotNull Plugin plugin) {
                await.accept(callback);
                return callback;
            }
        };
    }
}
