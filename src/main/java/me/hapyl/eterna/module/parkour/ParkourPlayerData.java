package me.hapyl.eterna.module.parkour;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.LinkedList;

/**
 * Represents a player-related {@link Parkour} data.
 */
public final class ParkourPlayerData {
    
    /**
     * Defines the constant for when a {@link Player} has not finished the {@link Parkour}.
     */
    public static final long HAS_NOT_FINISHED_CONSTANT = -1L;
    
    @NotNull
    public static SimpleDateFormat DATE_FORMAT_HOURS = new SimpleDateFormat("HH:mm:ss.SSS");
    
    @NotNull
    public static SimpleDateFormat DATE_FORMAT_MINUTES = new SimpleDateFormat("mm:ss.SSS");
    
    @NotNull
    public static SimpleDateFormat DATE_FORMAT_SECONDS = new SimpleDateFormat("ss.SSS");
    
    private final Player player;
    private final Parkour parkour;
    private final ParkourStatistics stats;
    private final PlayerInfo info;
    private final long firstStartedAt;
    private LinkedList<ParkourPosition> checkpoints;
    private ParkourPosition lastCheckpoint;
    private long startedAt;
    private long finishedAt;
    
    ParkourPlayerData(@NotNull Player player, @NotNull Parkour parkour) {
        this.player = player;
        this.parkour = parkour;
        this.stats = new ParkourStatistics();
        this.firstStartedAt = System.currentTimeMillis();
        this.startedAt = this.firstStartedAt;
        this.info = new PlayerInfo(player);
        
        this.reset();
    }
    
    /**
     * Gets the {@link Player} for whom this data belongs to.
     *
     * @return the player for whom this data belongs to.
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }
    
    /**
     * The {@link ParkourPosition} of the next checkpoint; or {@code null} if there aren't any checkpoints left.
     *
     * @return the parkour position of the next checkpoint; or {@code null} if there aren't any checkpoints left.
     */
    @Nullable
    public ParkourPosition getNextCheckpoint() {
        return checkpoints.peekFirst();
    }
    
    /**
     * Gets the {@link Parkour} associated with this {@link ParkourPlayerData}.
     *
     * @return the parkour associated with this data.
     */
    @NotNull
    public Parkour getParkour() {
        return parkour;
    }
    
    /**
     * Gets the {@link ParkourStatistics} for the {@link Player}.
     *
     * @return the parkour statistics for the player.
     */
    @NotNull
    public ParkourStatistics getParkourStatistics() {
        return stats;
    }
    
    /**
     * Gets the {@code milliseconds} at which the {@link Player} has first started this {@link Parkour}.
     *
     * <p>Unlike {@link #startedAt()}, this value cannot be reset via {@link #reset()} and will always define the actual start time.</p>
     *
     * @return the {@code milliseconds} at which the player has first started this parkour.
     */
    public long firstStartedAt() {
        return firstStartedAt;
    }
    
    /**
     * Gets the {@code milliseconds} at which the {@link Player} has started or restarted this {@link Parkour}.
     *
     * @return the {@code milliseconds} at which the player has started or restarted this parkour.
     */
    public long startedAt() {
        return this.startedAt;
    }
    
    /**
     * Gets the {@code milliseconds} at which the {@link Player} has finished this {@link Parkour}, or {@code -1} if haven't finished yet.
     *
     * @return the {@code milliseconds} at which the player has started or restarted this parkour, or {@code -1} if haven't finished yet.
     */
    public long finishedAt() {
        return this.finishedAt;
    }
    
    /**
     * Gets the number of checkpoint the {@link Player} has passed.
     *
     * @return the number of checkpoint the player has passed.
     */
    public int passedCheckpointsCount() {
        return this.parkour.checkpointCount() - this.checkpoints.size();
    }
    
    /**
     * Gets the number of checkpoints the {@link Player} has missed.
     *
     * @return the number of checkpoints the player has missed.
     */
    public int missedCheckpointsCount() {
        return this.parkour.checkpointCount() - passedCheckpointsCount();
    }
    
    /**
     * Gets the number of total checkpoints for this {@link Parkour}.
     *
     * @return the number of total checkpoints for this parkour.
     */
    public int totalCheckpoints() {
        return parkour.checkpointCount();
    }
    
    /**
     * Gets the last checkpoint passed, to which the {@link Player} will be teleported upon using the {@code Teleport to Checkpoint} item.
     *
     * @return the last checkpoint passed, to which the player will be teleported upon using the {@code Teleport to Checkpoint} item.
     */
    @Nullable
    public ParkourPosition getLastCheckpoint() {
        return lastCheckpoint;
    }
    
    /**
     * Gets whether the {@link Player} has finished the {@link Parkour}.
     *
     * @return {@code true} if the player has finished the parkour; {@code false} otherwise.
     */
    public boolean hasFinished() {
        return this.finishedAt != -1L;
    }
    
    /**
     * Gets the time elapsed from the first starting this {@link Parkour} until this instant, in milliseconds.
     *
     * @return the time elapsed from the first starting this parkour until this instant, in milliseconds.
     */
    public long getElapsedTimeSinceFirstStarted() {
        return System.currentTimeMillis() - this.firstStartedAt;
    }
    
    /**
     * Gets the time elapsed from starting this {@link Parkour} until this instant, in milliseconds.
     *
     * @return the time elapsed from starting this parkour until this instant, in milliseconds.
     */
    public long getElapsedTime() {
        return System.currentTimeMillis() - this.startedAt;
    }
    
    /**
     * Gets the completion duration of this {@link Parkour} in milliseconds, or {@code -1} if haven't completed yet.
     *
     * @return the completion duration of this parkour in milliseconds, or {@code -1} if haven't completed yet.
     */
    public long getCompletionDuration() {
        return hasFinished() ? this.finishedAt - this.startedAt : HAS_NOT_FINISHED_CONSTANT;
    }
    
    /**
     * Gets the formatted time elapsed from starting this {@link Parkour} until this instant.
     *
     * @return the formatted time elapsed from starting this parkour until this instant.
     */
    @NotNull
    public String getElapsedTimeSinceFirstStartedFormatted() {
        return formatTime(getElapsedTimeSinceFirstStarted());
    }
    
    /**
     * Gets the formatted time elapsed from starting this {@link Parkour} until this instant.
     *
     * @return the formatted time elapsed from starting this parkour until this instant.
     */
    @NotNull
    public String getElapsedTimeFormatted() {
        return formatTime(getElapsedTime());
    }
    
    /**
     * Gets the formatted completion duration of this {@link Parkour} in milliseconds.
     *
     * @return the formatted completion duration of this parkour in milliseconds.
     */
    @NotNull
    public String getCompletionDurationFormatted() {
        return formatTime(getCompletionDuration());
    }
    
    /**
     * Formats the given {@code milliseconds} into a {@link String} based on the duration.
     *
     * @param millis - The milliseconds to format.
     * @return the formatted time.
     */
    @NotNull
    public String formatTime(long millis) {
        if (millis == HAS_NOT_FINISHED_CONSTANT) {
            return "No Time!";
        }
        
        final String formatted = millis >= 3_600_000
                                 ? DATE_FORMAT_HOURS.format(millis)
                                 : millis >= 60_000
                                   ? DATE_FORMAT_MINUTES.format(millis)
                                   : DATE_FORMAT_SECONDS.format(millis);
        
        return formatted + "s";
    }
    
    @ApiStatus.Internal
    public void markFinished() {
        if (hasFinished()) {
            return;
        }
        
        this.finishedAt = System.currentTimeMillis();
    }
    
    @ApiStatus.Internal
    public boolean isFutureCheckpoint(@NotNull Location location) {
        for (ParkourPosition checkpoint : checkpoints) {
            if (checkpoint.compare(location)) {
                return true;
            }
        }
        
        return false;
    }
    
    @ApiStatus.Internal
    void restorePlayer() {
        this.info.restore();
    }
    
    @ApiStatus.Internal
    void reset() {
        this.startedAt = System.currentTimeMillis();
        this.finishedAt = HAS_NOT_FINISHED_CONSTANT;
        this.stats.reset();
        
        this.checkpoints = Lists.newLinkedList(this.parkour.checkpoints);
        this.lastCheckpoint = null;
    }
    
    @ApiStatus.Internal
    boolean nextCheckpoint() {
        final ParkourPosition checkpoint = checkpoints.pollFirst();
        
        // No more checkpoints
        if (checkpoint == null) {
            return false;
        }
        
        this.lastCheckpoint = checkpoint;
        return true;
    }
    
    @ApiStatus.Internal
    @NotNull
    LinkedList<ParkourPosition> checkpoints() {
        return checkpoints;
    }
    
}
