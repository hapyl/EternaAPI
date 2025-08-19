package me.hapyl.eterna;

import me.hapyl.eterna.module.parkour.ParkourRunnable;
import me.hapyl.eterna.module.reflect.npc.HumanNpcRunnable;

import javax.annotation.Nonnull;

/**
 * Represents a {@code config.yml} file.
 */
public interface EternaConfig {
    
    /**
     * Whether to check for updates.
     *
     * @return {@code true} if this server should check for updates.
     */
    boolean checkForUpdates();
    
    /**
     * Whether to keep test commands.
     *
     * @return {@code true} if this server keeps test commands.
     */
    boolean keepTestCommands();
    
    /**
     * Whether to print stack traces.
     *
     * @return {@code ture} if this server prints stack traces.
     */
    boolean printStackTraces();
    
    /**
     * Gets the {@link TickRate}.
     *
     * @return the tick rate.
     */
    @Nonnull
    TickRate tickRate();
    
    /**
     * Represents a tick rate.
     */
    interface TickRate {
        
        /**
         * Gets the tick rate for {@link HumanNpcRunnable}.
         *
         * @return the tick rate for {@link HumanNpcRunnable}.
         */
        int npc();
        
        /**
         * Gets the tick rate for {@link ParkourRunnable}.
         *
         * @return the tick rate for {@link ParkourRunnable}.
         */
        int parkour();
        
    }
    
}
