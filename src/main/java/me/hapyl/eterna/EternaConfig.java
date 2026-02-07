package me.hapyl.eterna;

import me.hapyl.eterna.module.npc.NpcRunnable;
import me.hapyl.eterna.module.parkour.ParkourRunnable;
import me.hapyl.eterna.module.util.Reloadable;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@code config.yml} file.
 */
public interface EternaConfig extends Reloadable {
    
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
     * Whether to allow quests journal.
     *
     * @return {@code true} if this server allows quest journal.
     */
    boolean allowQuestJournal();
    
    /**
     * Gets the {@link TickRate}.
     *
     * @return the tick rate.
     */
    @NotNull
    TickRate tickRate();
    
    /**
     * Represents a tick rate.
     */
    interface TickRate {
        
        /**
         * Gets the tick rate for {@link NpcRunnable}.
         *
         * @return the tick rate for {@link NpcRunnable}.
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
