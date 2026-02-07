package me.hapyl.eterna.module.scheduler;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a functional interface for a {@link SchedulerTask}.
 */
@FunctionalInterface
public interface SchedulerExecutor {
    
    /**
     * Executes the given {@link BukkitRunnable} with the given {@link Plugin} delegate.
     *
     * @param runnable - The runnable to execute.
     * @param plugin   - The plugin delegate.
     */
    void execute(@NotNull BukkitRunnable runnable, @NotNull Plugin plugin);
    
}
