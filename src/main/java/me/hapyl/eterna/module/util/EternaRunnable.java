package me.hapyl.eterna.module.util;

import me.hapyl.eterna.module.annotate.EventLike;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an {@link EternaRunnable}, which can run via the {@link BukkitScheduler}.
 *
 * <p>
 * This implementation is exception safe, compared to the {@link BukkitRunnable}.
 * </p>
 */
public abstract class EternaRunnable implements Runnable {
    
    private final Plugin plugin;
    private BukkitTask task;
    
    /**
     * Creates a new {@link EternaRunnable}.
     *
     * @param plugin - The plugin delegate.
     */
    public EternaRunnable(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Gets whether this {@link EternaRunnable} is cancelled.
     *
     * @return {@code true} if this runnable is cancelled; {@code false} otherwise.
     */
    public synchronized boolean isCancelled() {
        return task != null && task.isCancelled();
    }
    
    /**
     * Gets whether this {@link EternaRunnable} is scheduled.
     *
     * @return {@code true} if this runnable is scheduled; {@code false} otherwise.
     */
    public synchronized boolean isScheduled() {
        return task != null && !task.isCancelled();
    }
    
    /**
     * An event-like method which is called whenever this {@link EternaRunnable} is cancelled.
     */
    @EventLike
    public void onCancel() {
    }
    
    /**
     * Cancels this {@link EternaRunnable}.
     */
    public synchronized void cancel() {
        if (task == null || task.isCancelled()) {
            return;
        }
        
        onCancel();
        Bukkit.getScheduler().cancelTask(getTaskId());
    }
    
    /**
     * Runs this {@link EternaRunnable} on the next game tick.
     *
     * @return the underlying bukkit task.
     */
    @NotNull
    public synchronized BukkitTask runTask() {
        return setupTask(Bukkit.getScheduler().runTask(plugin, this));
    }
    
    /**
     * Runs this {@link EternaRunnable} asynchronously at the next game tick.
     *
     * @return the underlying bukkit task.
     */
    @NotNull
    public synchronized BukkitTask runTaskAsynchronously() {
        return setupTask(Bukkit.getScheduler().runTaskAsynchronously(plugin, this));
    }
    
    /**
     * Runs this {@link EternaRunnable} after the given delay.
     *
     * @param delay - The delay.
     * @return the underlying bukkit task.
     */
    @NotNull
    public synchronized BukkitTask runTaskLater(long delay) {
        return setupTask(Bukkit.getScheduler().runTaskLater(plugin, this, delay));
    }
    
    /**
     * Runs this {@link EternaRunnable} asynchronously after the given delay.
     *
     * @param delay - The delay.
     * @return the underlying bukkit task.
     */
    @NotNull
    public synchronized BukkitTask runTaskLaterAsynchronously(long delay) {
        return setupTask(Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, this, delay));
    }
    
    /**
     * Runs this {@link EternaRunnable} repeatedly.
     *
     * @param delay  - The delay before the first execution.
     * @param period - The period of the task.
     * @return the underlying bukkit task.
     */
    @NotNull
    public synchronized BukkitTask runTaskTimer(long delay, long period) {
        return setupTask(Bukkit.getScheduler().runTaskTimer(plugin, this, delay, period));
    }
    
    /**
     * Runs this {@link EternaRunnable} asynchronously repeatedly.
     *
     * @param delay  - The delay before the first execution.
     * @param period - The period of the task.
     * @return the underlying bukkit task.
     */
    @NotNull
    public synchronized BukkitTask runTaskTimerAsynchronously(long delay, long period) {
        return setupTask(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this, delay, period));
    }
    
    /**
     * Gets the id of this {@link EternaRunnable}, or {@code -1} is not scheduled yet.
     *
     * @return the id of this runnable, or {@code -1} is not scheduled yet.
     */
    public synchronized int getTaskId() {
        return task != null ? task.getTaskId() : -1;
    }
    
    @ApiStatus.Internal
    private BukkitTask setupTask(final BukkitTask task) {
        return this.task != null ? this.task : (this.task = task);
    }
    
}
