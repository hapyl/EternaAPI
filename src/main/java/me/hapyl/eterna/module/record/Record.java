package me.hapyl.eterna.module.record;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.reflect.npc.HumanNPC;
import me.hapyl.eterna.module.util.EternaRunnable;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

/**
 * Records any action of a {@link Player} and allows to replay them using a {@link HumanNPC}.
 * <br>
 * <b>Actions:</b>
 * <ul>
 *     <li>Movement
 *     <li>Equipment
 *     <li>Poses:
 *     <ul>
 *         <li>Sneaking
 *         <li>Sleeping
 *         <li>Swimming
 *     </ul>
 * </ul>
 */
public class Record extends EternaRunnable {

    public static final long MAX_RECORDING_FRAMES = 6000L;

    private static final Map<Integer, Record> cachedRecords = Maps.newHashMap();
    private static final Map<UUID, Record> runningRecords = Maps.newHashMap();

    private final Player player;
    private final Map<Long, ReplayData> frames;
    private final Set<RecordAction> ignoredActions;

    private boolean status;

    public Record(@Nonnull Player player) {
        super(EternaPlugin.getPlugin());

        this.player = player;
        this.frames = Maps.newHashMap();
        this.ignoredActions = Sets.newHashSet();
        this.status = false;

        final Record previousRecord = runningRecords.put(player.getUniqueId(), this);

        if (previousRecord != null) {
            previousRecord.stopRecording();
        }
    }

    public void addIgnoredAction(@Nonnull RecordAction action) {
        ignoredActions.add(action);
    }

    @Override
    public void run() {
        if (status || frames.size() >= MAX_RECORDING_FRAMES) {
            stopRecording();
            return;
        }

        // Add step
        getData(frames.size());
    }

    /**
     * Starts the recording of the replay.
     */
    public void startRecording() {
        runTaskTimer(1, 1);
    }

    /**
     * Stops the recoding and saves it in <b>memory</b>.
     *
     * @return true if the recording was stopped, false otherwise.
     */
    public boolean stopRecording() {
        if (isCancelled()) {
            return false;
        }

        cancel();
        status = true;

        runningRecords.remove(player.getUniqueId());
        cachedRecords.put(cachedRecords.size(), this);

        return true;
    }

    public boolean isComplete() {
        return status;
    }

    /**
     * Returns true if this recording is in progress, false otherwise.
     *
     * @return true if this recording is in progress, false otherwise.
     */
    public boolean isRecordingInProgress() {
        return !status && !frames.isEmpty() && frames.size() <= MAX_RECORDING_FRAMES;
    }

    /**
     * Gets the player of this record.
     *
     * @return the player of this record.
     */
    @Nonnull
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns current number of frames recorded in ticks.
     *
     * @return current number of frames recorded in ticks.
     */
    public int getFrames() {
        return frames.size();
    }

    /**
     * Returns true if the number of frames recorded is equals or greater than the maximum allowed, false otherwise.
     *
     * @return true if the number of frames recorded is equals or greater than the maximum allowed, false otherwise.
     */
    public boolean isMaxFrames() {
        return getFrames() >= MAX_RECORDING_FRAMES;
    }

    /**
     * Get the {@link ReplayData} at the given frame.
     * <br>
     * This method will always compute new data if it doesn't exist already.
     *
     * @param frame - Frame.
     * @return the replay data at the given frame.
     */
    @Nonnull
    public ReplayData getData(long frame) {
        if (frame < 0 || frame > MAX_RECORDING_FRAMES) {
            throw new IllegalArgumentException("Frame cannot negative not greater than %s!".formatted(MAX_RECORDING_FRAMES));
        }

        return frames.computeIfAbsent(frame, d -> new ReplayData(this));
    }

    /**
     * Get the {@link ReplayData} at the first frame.
     *
     * @return the replay data at the first frame.
     */
    @Nonnull
    public ReplayData getDataAtFirstFrame() {
        return getData(0);
    }

    /**
     * Gets the {@link ReplayData} at the current frame.
     *
     * @return the replay data at the current frame.
     */
    @Nonnull
    public ReplayData getDataAtCurrentFrame() {
        return getData(frames.size() - 1);
    }

    /**
     * Creates a {@link SimpleReplay} of this {@link Record}.
     *
     * @return a replay.
     */
    @Nonnull
    public Replay replay() {
        return replay(SimpleReplay::new);
    }

    /**
     * Creates a {@link Replay}.
     *
     * @param fn - Function on how to create {@link Replay}.
     *           Since {@link Replay} has 'events', this is the way it should be created.
     * @return a replay.
     */
    @Nonnull
    public Replay replay(@Nonnull Function<Record, Replay> fn) {
        if (!status) {
            stopRecording();
        }

        return fn.apply(this);
    }

    public boolean isIgnoredAction(@Nonnull RecordAction action) {
        return ignoredActions.contains(action);
    }

    /**
     * Gets the currently running {@link Record} for the given player if it exists, <code>null</code> otherwise.
     *
     * @param player - Player.
     * @return the currently running record for the given player, or null.
     */
    @Nullable
    public static Record getRunningRecord(@Nonnull Player player) {
        return runningRecords.get(player.getUniqueId());
    }

    /**
     * Gets a <b>copy</b> of all the cached {@link Record}s.
     *
     * @return the copy of all the cached records.
     */
    @Nonnull
    public static Map<Integer, Record> getCachedRecords() {
        return Maps.newHashMap(cachedRecords);
    }

    /**
     * Fetches the current running {@link Record} for the players and adds a {@link RecordAction} at the current frame.
     * <br>
     * Does nothing if:
     * <ul>
     *     <li>Player has not running {@link Record}.
     *     <li>The {@link Record} is finished.
     * </ul>
     *
     * @param player - Player.
     */
    public static void fetchRecordAction(@Nonnull Player player, @Nonnull RecordAction action) {
        final Record record = getRunningRecord(player);

        if (record == null || record.status) {
            return;
        }

        record.getDataAtCurrentFrame().addAction(action);
    }

}
