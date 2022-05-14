package kz.hapyl.spigotutils.module.parkour;

import kz.hapyl.spigotutils.EternaPlugin;
import kz.hapyl.spigotutils.module.player.PlayerLib;
import kz.hapyl.spigotutils.module.util.Holder;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Data extends Holder<Player> {

    private final Parkour parkour;
    private final Stats stats;

    private final PlayerInfo info;
    private List<Position> checkpoints;
    private Position previousCheckpoint;
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
    public Position getNextCheckpoint() {
        return checkpoints.size() > 0 ? checkpoints.get(0) : null;
    }

    @Nullable
    public Position getCurrentCheckpoint() {
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

    public boolean isNextChechpoint(Position position) {
        if (!hasNextCheckpoint()) {
            return false;
        }
        return Objects.requireNonNull(getNextCheckpoint()).compare(position);
    }

    public List<Position> getCheckpoints() {
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

        final Position currentCheckpoint = getCurrentCheckpoint();
        previousCheckpoint = currentCheckpoint;
        checkpoints.remove(currentCheckpoint);

        if (sendMessage && !parkour.isSilent()) {
            EternaPlugin
                    .getPlugin()
                    .getParkourManager()
                    .sendParkourMessage(
                            get(),
                            "Checkpoint! (%s/%s)",
                            passedCheckpointsCount(),
                            parkour.getCheckpoints().size()
                    );

            PlayerLib.playSound(get(), Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f);
        }
    }

    public int passedCheckpointsCount() {
        return (checkpoints.size() - parkour.getCheckpoints().size()) * -1;
    }

    public int missedCheckpointsCount() {
        return parkour.getCheckpoints().size() - passedCheckpointsCount();
    }

    @Nullable
    public Position getPreviousCheckpoint() {
        return previousCheckpoint;
    }

    public void setPreviousCheckpoint(Position previousCheckpoint) {
        this.previousCheckpoint = previousCheckpoint;
    }

    public Stats getStats() {
        return stats;
    }

    public void setFinished() {
        if (finishedAt == -1) {
            return;
        }

        finishedAt = System.currentTimeMillis();
    }

    public void setUnfinished() {
        finishedAt = -1;
    }

    public boolean isFinished() {
        return finishedAt != -1;
    }

    public long getTimePassed() {
        return System.currentTimeMillis() - this.startedAt;
    }

    public String getTimePassedFormatted() {
        final double timePassed = (double) (this.isFinished() ? this.getFinishedAt() : this.getTimePassed()) / 1000;
        return new DecimalFormat("#0.00").format(timePassed);
        // return Chat.format("&a&l%s &6&l%s", this.parkour.getName().toUpperCase(), format);
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

    // Checks if location is a valid checkpoint
    public boolean isFutureCheckpoint(Location clickedBlockLocation) {
        for (Position checkpoint : checkpoints) {
            if (checkpoint.compare(clickedBlockLocation)) {
                return true;
            }
        }
        return false;
    }
}
