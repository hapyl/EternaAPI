package me.hapyl.eterna.module.parkour;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

/**
 * Stores player-related {@link Parkour} data.
 */
public class ParkourData {
    
    private final Player player;
    private final Parkour parkour;
    private final ParkourStatistics stats;
    private final PlayerInfo info;
    
    private LinkedList<ParkourPosition> checkpoints;
    private ParkourPosition previousCheckpoint;
    
    private long startedAt;
    private long finishedAt;
    
    public ParkourData(@Nonnull Player player, @Nonnull Parkour parkour) {
        this.player = player;
        this.parkour = parkour;
        this.stats = new ParkourStatistics();
        this.startedAt = System.currentTimeMillis();
        this.info = new PlayerInfo(player);
        
        this.reset();
    }
    
    @Nonnull
    public Player getPlayer() {
        return player;
    }
    
    @Nullable
    public ParkourPosition getNextCheckpoint() {
        return checkpoints.peekFirst();
    }
    
    public void reset() {
        this.startedAt = System.currentTimeMillis();
        this.finishedAt = -1L;
        this.stats.reset();
        
        this.checkpoints = this.parkour.getCheckpoints();
        this.previousCheckpoint = null;
    }
    
    @Nonnull
    public Parkour getParkour() {
        return parkour;
    }
    
    @Nonnull
    public PlayerInfo getPlayerInfo() {
        return info;
    }
    
    @Nonnull
    public ParkourStatistics getStats() {
        return stats;
    }
    
    public long startedAt() {
        return this.startedAt;
    }
    
    public long finishedAt() {
        return this.finishedAt;
    }
    
    public void nextCheckpoint() {
        final ParkourPosition checkpoint = checkpoints.pollFirst();
        
        // No more checkpoints
        if (checkpoint != null) {
            this.previousCheckpoint = checkpoint;
            this.parkour.getFormatter().sendCheckpointPassed(this);
        }
    }
    
    public int passedCheckpointsCount() {
        return parkour.checkpointCount() - checkpoints.size();
    }
    
    public int missedCheckpointsCount() {
        return parkour.checkpointCount() - passedCheckpointsCount();
    }
    
    @Nullable
    public ParkourPosition getPreviousCheckpoint() {
        return previousCheckpoint;
    }
    
    public void resetFinished() {
        this.finishedAt = -1L;
    }
    
    public void setFinished() {
        if (isFinished()) {
            return;
        }
        
        this.finishedAt = System.currentTimeMillis();
    }
    
    public boolean isFinished() {
        return finishedAt != -1L;
    }
    
    public long getTimePassed() {
        return System.currentTimeMillis() - this.startedAt;
    }
    
    public long getCompletionTime() {
        return this.finishedAt - this.startedAt;
    }
    
    @Nonnull
    public String formatTime(long millis) {
        return parkour.formatTime(millis);
    }
    
    @Nonnull
    public String getTimePassedFormatted() {
        return formatTime(getTimePassed());
    }
    
    @Nonnull
    public String getCompletionTimeFormatted() {
        return formatTime(getCompletionTime());
    }
    
    @Override
    public String toString() {
        return "Data{" +
               "parkour=" + parkour.getName() +
               ", stats=" + stats.toString() +
               ", info=" + info.toString() +
               ", passedCheckpoints=" + checkpoints +
               ", lastCheckpoint=" + previousCheckpoint +
               ", startedAt=" + startedAt +
               ", finishedAt=" + finishedAt +
               '}';
    }
    
    public boolean isFutureCheckpoint(Location clickedBlockLocation) {
        for (ParkourPosition checkpoint : checkpoints) {
            if (checkpoint.compare(clickedBlockLocation)) {
                return true;
            }
        }
        return false;
    }
    
    @Nonnull
    protected List<ParkourPosition> getCheckpoints() {
        return checkpoints;
    }
}
