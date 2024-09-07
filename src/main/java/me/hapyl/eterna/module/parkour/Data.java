package me.hapyl.eterna.module.parkour;

import me.hapyl.eterna.module.util.Holder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Stored all player's parkour-related data.
 */
public class Data extends Holder<Player> {

    private final Parkour parkour;
    private final Stats stats;

    private final PlayerInfo info;
    private List<ParkourPosition> checkpoints;
    private ParkourPosition previousCheckpoint;
    private long startedAt;
    private long finishedAt = -1;

    public Data(Player player, Parkour parkour) {
        super(player);
        this.parkour = parkour;
        this.stats = new Stats(player);
        this.startedAt = System.currentTimeMillis();
        this.info = new PlayerInfo(player);
        resetCheckpoints();
    }

    @Nullable // if last checkpoint
    public ParkourPosition getNextCheckpoint() {
        return checkpoints.size() > 0 ? checkpoints.get(0) : null;
    }

    @Nullable
    public ParkourPosition getCurrentCheckpoint() {
        return this.checkpoints.get(0);
    }

    public void resetTime() {
        this.startedAt = System.currentTimeMillis();
        this.stats.resetAll();
    }

    public PlayerInfo getPlayerInfo() {
        return info;
    }

    public void resetCheckpoints() {
        this.checkpoints = new ArrayList<>(this.parkour.getCheckpoints());
        previousCheckpoint = null;
    }

    public boolean hasNextCheckpoint() {
        return getNextCheckpoint() != null;
    }

    public boolean hasLastCheckpoint() {
        return previousCheckpoint != null;
    }

    public boolean isNextCheckpoint(ParkourPosition position) {
        if (!hasNextCheckpoint()) {
            return false;
        }
        return Objects.requireNonNull(getNextCheckpoint()).compare(position);
    }

    public List<ParkourPosition> getCheckpoints() {
        return checkpoints;
    }

    public Parkour getParkour() {
        return parkour;
    }

    public long getStartedAt() {
        return startedAt;
    }

    public long getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(long finishedAt) {
        this.finishedAt = finishedAt;
    }

    public boolean isPreviousCheckpoint(Location location) {
        return previousCheckpoint != null && previousCheckpoint.compare(location);
    }

    public void nextCheckpoint(boolean sendMessage) {
        if (!hasNextCheckpoint()) {
            return;
        }

        final ParkourPosition currentCheckpoint = getCurrentCheckpoint();
        previousCheckpoint = currentCheckpoint;
        checkpoints.remove(currentCheckpoint);

        if (sendMessage) {
            getParkour().getFormatter().sendCheckpointPassed(this);
        }
    }

    public int passedCheckpointsCount() {
        return (checkpoints.size() - parkour.getCheckpoints().size()) * -1;
    }

    public int missedCheckpointsCount() {
        return parkour.getCheckpoints().size() - passedCheckpointsCount();
    }

    @Nullable
    public ParkourPosition getPreviousCheckpoint() {
        return previousCheckpoint;
    }

    public void setPreviousCheckpoint(ParkourPosition previousCheckpoint) {
        this.previousCheckpoint = previousCheckpoint;
    }

    public Stats getStats() {
        return stats;
    }

    public void resetFinished() {
        this.finishedAt = -1;
    }

    public void setFinished() {
        if (isFinished()) {
            return;
        }

        finishedAt = System.currentTimeMillis();
    }

    public boolean isFinished() {
        return finishedAt != -1;
    }

    public long getTimePassed() {
        return System.currentTimeMillis() - this.startedAt;
    }

    public long getCompletionTime() {
        return this.finishedAt - this.startedAt;
    }

    public String formatTime(long millis) {
        if (millis >= 3600000) {
            return new SimpleDateFormat("HH:mm:ss.SSS").format(millis);
        }
        else if (millis >= 60000) {
            return new SimpleDateFormat("mm:ss.SSS").format(millis);
        }
        else {
            return new SimpleDateFormat("ss.SSS").format(millis);
        }
    }

    public String getTimePassedFormatted() {
        return formatTime(getTimePassed());
    }

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

    public boolean compareParkour(Parkour parkour) {
        return this.parkour != null && this.parkour.equals(parkour);
    }

    // Checks if location is a valid checkpoint
    public boolean isFutureCheckpoint(Location clickedBlockLocation) {
        for (ParkourPosition checkpoint : checkpoints) {
            if (checkpoint.compare(clickedBlockLocation)) {
                return true;
            }
        }
        return false;
    }
}
