package me.hapyl.eterna.module.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;

/**
 * A {@link BukkitRunnable} implementation without throwing random errors.
 */
public abstract class EternaRunnable implements Runnable {
    
    private final JavaPlugin plugin;
    private BukkitTask task;
    
    public EternaRunnable(@Nonnull JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Returns true if this task is not yet scheduled or cancelled, false otherwise.
     *
     * @return true if this task is not yet scheduled or cancelled, false otherwise.
     */
    public synchronized boolean isCancelled() {
        return task != null && task.isCancelled();
    }
    
    /**
     * Gets whether this task is currently scheduled.
     *
     * @return {@code true} if this task is currently scheduled, {@code false} otherwise.
     */
    public synchronized boolean isScheduled() {
        return task != null && !task.isCancelled();
    }
    
    /**
     * Called whenever this task is cancelled via {@link #cancel()}.
     */
    public void onCancel() {
    }
    
    /**
     * Cancels this task if it's not already.
     */
    public synchronized void cancel() {
        if (task == null || task.isCancelled()) {
            return;
        }
        
        onCancel();
        Bukkit.getScheduler().cancelTask(getTaskId());
    }
    
    /**
     * Runs this task.
     *
     * @return the {@link BukkitTask}.
     */
    @Nonnull
    public synchronized BukkitTask runTask() {
        return setupTask(Bukkit.getScheduler().runTask(plugin, this));
    }
    
    /**
     * Runs this task asynchronously.
     *
     * @return the {@link BukkitTask}.
     */
    @Nonnull
    public synchronized BukkitTask runTaskAsynchronously() {
        return setupTask(Bukkit.getScheduler().runTaskAsynchronously(plugin, this));
    }
    
    /**
     * Runs this task after the given delay.
     *
     * @param delay - Delay.
     * @return the {@link BukkitTask}.
     */
    @Nonnull
    public synchronized BukkitTask runTaskLater(long delay) {
        return setupTask(Bukkit.getScheduler().runTaskLater(plugin, this, delay));
    }
    
    /**
     * Runs this task asynchronously after the given delay.
     *
     * @param delay - Delay.
     * @return the {@link BukkitTask}.
     */
    @Nonnull
    public synchronized BukkitTask runTaskLaterAsynchronously(long delay) {
        return setupTask(Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, this, delay));
    }
    
    /**
     * Runs this task repeatedly after the given delay with the given period.
     *
     * @param delay  - Delay.
     * @param period - Period.
     * @return the {@link BukkitTask}.
     */
    @Nonnull
    public synchronized BukkitTask runTaskTimer(long delay, long period) {
        return setupTask(Bukkit.getScheduler().runTaskTimer(plugin, this, delay, period));
    }
    
    /**
     * Runs this task asynchronously repeatedly after a given delay with the given period.
     *
     * @param delay  - Delay.
     * @param period - Period.
     * @return the {@link BukkitTask}.
     */
    @Nonnull
    public synchronized BukkitTask runTaskTimerAsynchronously(long delay, long period) {
        return setupTask(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this, delay, period));
    }
    
    /**
     * Gets this task <code>Id</code>, or <code>-1</code> if not scheduled yet.
     *
     * @return the task Id, or -1 if not scheduled yet.
     */
    public synchronized int getTaskId() {
        return task == null ? -1 : task.getTaskId();
    }
    
    private BukkitTask setupTask(final BukkitTask task) {
        if (this.task != null) {
            return this.task;
        }
        
        return this.task = task;
    }
    
}
