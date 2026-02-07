package me.hapyl.eterna.module.scheduler;

import com.google.common.collect.Lists;
import me.hapyl.eterna.module.annotate.SelfReturn;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a callback ({@link CompletableFuture})-based {@link Scheduler}.
 */
public class Scheduler {
    
    private final Plugin plugin;
    private final CompletableFuture<Void> future;
    private final LinkedList<SchedulerTask> tasks;
    
    /**
     * Creates a new {@link Scheduler}.
     *
     * @param plugin - The plugin delete.
     */
    public Scheduler(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.future = new CompletableFuture<>();
        this.tasks = Lists.newLinkedList();
    }
    
    /**
     * Appends the given {@link SchedulerTask} at the end of the {@link Scheduler}.
     *
     * @param task - The task to append.
     */
    @SelfReturn
    public Scheduler then(@NotNull SchedulerTask task) {
        this.tasks.add(task);
        return this;
    }
    
    /**
     * Executes the tasks in the exact order added.
     *
     * @return a {@link CompletableFuture} that will be completed when all tasks finished execution <b>successfully</b>, or completed exceptionally if any of the tasks throws an {@link Exception}.
     */
    @NotNull
    public CompletableFuture<Void> execute() {
        final SchedulerTask nextTask = this.tasks.pollFirst();
        
        // If no next task, means either finishes or no tasks, complete either way
        if (nextTask == null) {
            future.complete(null);
        }
        else {
            nextTask.run(this.plugin)
                    .thenRun(this::execute)
                    .exceptionally(ex -> {
                        future.completeExceptionally(ex);
                        return null;
                    });
        }
        
        return future;
    }
    
}
