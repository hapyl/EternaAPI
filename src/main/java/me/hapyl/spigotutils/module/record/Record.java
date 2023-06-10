package me.hapyl.spigotutils.module.record;

import com.google.common.collect.Maps;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.event.replay.ReplayFinishRecordingEvent;
import me.hapyl.spigotutils.module.event.replay.ReplayStartRecordingEvent;
import me.hapyl.spigotutils.module.math.Numbers;
import me.hapyl.spigotutils.module.util.BukkitUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

/**
 * Records any action of a player and allows to replay them using NPC.
 *
 * This includes:
 * - Location.
 * - Equipment.
 * - Poses, such as:
 * --- Sneaking
 * --- Sleeping
 * --- Swimming
 *
 * Not implemented: (mark as todo)
 * - Taking damage.
 * - Swinging hands.
 */
// FIXME (hapyl): 009, Jun 9: Crouching does not seem to work
public class Record {

    // only one instance of replays allowed per player
    private static final Map<UUID, Record> replays = Maps.newHashMap();

    private final Player player;
    private final Map<Long, Data> frames;

    private Replay replay;
    private boolean isRecordingFinished;
    private long maxFrames;
    private BukkitTask task;

    public Record(Player player) {
        this.player = player;
        this.frames = Maps.newHashMap();
        this.isRecordingFinished = false;
        this.maxFrames = 6000;

        // stop old replay
        final Record oldRecord = getReplay(player);
        if (oldRecord != null) {
            oldRecord.cleanup();
        }
        replays.put(player.getUniqueId(), this);

        startRecording();
    }

    /**
     * Stars recording of this replay is not already finished.
     * Cannot be called if {@link this#isRecordFinished()} is true.
     * Will override ongoing recording if exists.
     */
    public void startRecording() {
        if (isRecordFinished()) {
            return;
        }

        if (BukkitUtils.callCancellableEvent(new ReplayStartRecordingEvent(player, this))) {
            return;
        }

        if (task != null) {
            task.cancel();
        }

        frames.clear();

        task = new BukkitRunnable() {
            @Override
            public void run() {
                // finished recording
                if (isRecordingFinished || isMaxFrames()) {
                    isRecordingFinished = true;
                    this.cancel();
                    return;
                }

                // add step
                getDataAtNextFrame();
            }
        }.runTaskTimer(EternaPlugin.getPlugin(), 0L, 1L);

    }

    /**
     * Stops recording os this {@link Record}.
     * Recording must be stopped before {@link Replay} can to played.
     */
    public void stopRecording() {
        if (BukkitUtils.callCancellableEvent(new ReplayFinishRecordingEvent(player, this))) {
            return;
        }
        isRecordingFinished = true;
    }

    /**
     * Returns new {@link Replay} which is NOT linked to this {@link Record}.
     *
     * @return new replay.
     * @throws IllegalStateException if called before recording is finished.
     */
    public Replay newReplay() {
        if (!isRecordFinished()) {
            throw new IllegalStateException("replay must be finished before replay can be created");
        }
        return new Replay(this);
    }

    /**
     * Returns {@link Replay} of this {@link Record} which is linked
     * to this record and may only exist in one instance.
     *
     * @return replay of this record.
     */
    @Nonnull
    public Replay getReplay() {
        if (replay == null) {
            replay = newReplay();
        }
        return replay;
    }

    /**
     * Returns true recording is in progress, false otherwise.
     *
     * @return true recording is in progress, false otherwise.
     */
    public boolean isRecordingInProgress() {
        return !isRecordingFinished && frames.size() > 0 && frames.size() <= maxFrames;
    }

    /**
     * Returns player of this record.
     *
     * @return player of this record.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns current amount of frames recorded in ticks.
     *
     * @return current amount of frames recorded in ticks.
     */
    public int getFrames() {
        return frames.size();
    }

    /**
     * Returns true if amount of frames recorded is
     * equals or greater than maximum allowed, or maxFrames is 0, false otherwise.
     *
     * @return Returns true if amount of frames recorded is equals
     * or greater than maximum allowed, or maxFrames is 0, false otherwise.
     */
    public boolean isMaxFrames() {
        return getFrames() >= maxFrames || maxFrames == 0;
    }

    /**
     * Returns maximum amount of frames allowed for this record in ticks.
     *
     * @return maximum amount of frames allowed for this record in ticks.
     */
    public long getMaxFrames() {
        return maxFrames;
    }

    /**
     * Returns true if recording is finished.
     * Plugin should call this method before
     * calling {@link this#getReplay()} or {@link this#newReplay()}
     *
     * @return true if recording is finished.
     */
    public boolean isRecordFinished() {
        return isRecordingFinished;
    }

    /**
     * Changes maximum amount of frames allowed to this
     * record in ticks. Default is 6000, maximum is 72000.
     *
     * @param frames - new maximum amount.
     */
    public void setMaxFrames(long frames) {
        maxFrames = Numbers.clamp(frames, 6000, 72000);
    }

    /**
     * Returns data at provided frame, or creates new data if null.
     *
     * @param frame - frame (tick) of data.
     * @return data at provided frame, or creates new data if null.
     */
    @Nonnull
    public Data getData(long frame) {
        return frames.computeIfAbsent(frame, d -> new Data(player));
    }

    /**
     * Returns data of the first frame, or creates new data if null.
     *
     * @return data of the first frame, or creates new data if null.
     */
    @Nonnull
    public Data getDataAtFirstFrame() {
        return getData(0);
    }

    /**
     * Returns data of the last frame recorded, or creates new data if null.
     *
     * @return data of the last frame recorded, or creates new data if null.
     */
    @Nonnull
    public Data getDataAtCurrentFrame() {
        return getData(frames.size() - 1);
    }

    /**
     * Returns data of the next frame, or creates new data if null.
     *
     * @return data of the next frame, or creates new data if null.
     */
    @Nonnull
    public Data getDataAtNextFrame() {
        return getData(frames.size());
    }

    // static members

    /**
     * Returns stored replay of a player if exists, null otherwise.
     * Note that only one replay can be stored at the time, though
     * any amount of replaying can be recording at the same time.
     *
     * @param player - Player.
     * @return stored replay of a player if exists, null otherwise.
     */
    @Nullable
    public static Record getReplay(Player player) {
        return replays.get(player.getUniqueId());
    }

    private void cleanup() {
        maxFrames = 0;
        frames.clear();
    }

}
