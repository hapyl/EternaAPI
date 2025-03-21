package me.hapyl.eterna.module.player.song;

import me.hapyl.eterna.module.util.CallbackHandler;
import me.hapyl.eterna.module.util.EternaRunnable;
import me.hapyl.eterna.module.util.Validate;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Range;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * Represents a playable instance of a given {@link Song}.
 * <p>Instances are designed as <i>one-time use and throw away</i> objects, so they <b>do not</b> support restarting or changing the listeners.</p>
 * <p>However pausing is supported using {@link #pause()}.</p>
 */
public final class SongInstance extends EternaRunnable implements CallbackHandler<SongCallback> {
    
    private final Song song;
    
    @Nonnull private Status status;
    @Nonnull private Collection<Player> players;
    @Nonnull private SongCallback callback;
    
    private int tick;
    private float volume;
    
    SongInstance(@Nonnull Song song, @Nonnull JavaPlugin plugin) {
        super(plugin);
        
        this.song = song;
        this.status = Status.AWAITING;
        this.players = List.of();
        this.volume = SongNote.DEFAULT_VOLUME;
        
        this.callback = new SongCallback() {
            @Override
            public void onStartPlaying() {
            }
            
            @Override
            public void onFinishedPlaying() {
            }
            
            @Override
            public void onPause(boolean pause) {
            }
        };
    }
    
    /**
     * Plays the instance for the given player.
     *
     * @param player - The player to play to.
     * @throws IllegalStateException If the instance is already playing.
     */
    public void play(@Nonnull Player player) {
        play(List.of(player));
    }
    
    /**
     * Plays the instance for the given players.
     *
     * @param players - The players to play to.
     * @throws IllegalStateException If the instance is already playing.
     */
    public void play(@Nonnull Collection<Player> players) throws IllegalStateException {
        Validate.isTrue(!isScheduled(), "Instance already scheduled!");
        
        this.status = Status.PLAYING;
        this.players = players;
        
        callback.onStartPlaying();
        runTaskTimer(0, song.taskPeriod());
    }
    
    /**
     * Either pauses or resumed the instance.
     *
     * @return the current state {@code true} if paused {@code false} otherwise.
     */
    public boolean pause() {
        if (isAwaitingOrFinishedPlaying()) {
            return false;
        }
        
        final boolean isPaused = isPaused();
        
        this.status = isPaused ? Status.PLAYING : Status.PAUSED;
        this.callback.onPause(!isPaused);
        
        return !isPaused;
    }
    
    /**
     * Gracefully stops the instance at the next Minecraft tick.
     */
    public void stop() {
        this.tick = (int) song.getLength();
    }
    
    /**
     * Returns true is the instance is awaiting to be played.
     *
     * @return true is the instance is awaiting to be played.
     */
    public boolean isAwaiting() {
        return status == Status.AWAITING;
    }
    
    /**
     * Returns true if the instance has finished playing.
     *
     * @return true if the instance has finished playing.
     */
    public boolean isFinishedPlaying() {
        return status == Status.FINISHED_PLAYING;
    }
    
    /**
     * Returns true if the instance is either awaiting to be played or has finished playing.
     *
     * @return true if the instance is either awaiting to be played or has finished playing.
     */
    public boolean isAwaitingOrFinishedPlaying() {
        return isAwaiting() || isFinishedPlaying();
    }
    
    /**
     * Returns true if the instance is currently playing.
     *
     * @return true if the instance is currently playing.
     */
    public boolean isPlaying() {
        return status == Status.PLAYING;
    }
    
    /**
     * Returns true if the instance is currently paused.
     *
     * @return true if the instance is currently paused.
     */
    public boolean isPaused() {
        return status == Status.PAUSED;
    }
    
    /**
     * Gets the song of this instance.
     *
     * @return the song of this instance.
     */
    @Nonnull
    public Song song() {
        return song;
    }
    
    /**
     * Gets the status of this instance.
     *
     * @return the status of this instance.
     */
    @Nonnull
    public Status status() {
        return status;
    }
    
    /**
     * Gets the volume of this instance.
     *
     * @return the volume of this instance.
     */
    public float volume() {
        return volume;
    }
    
    /**
     * Sets the volume of this instance.
     *
     * @param volume - The new volume, will be clamped between {@code 0}-{@code 1}.
     */
    public void volume(@Range(from = 0, to = 1) float volume) {
        this.volume = Math.clamp(volume, 0, 1);
    }
    
    /**
     * Gets the current tick of the instance.
     *
     * @return the current tick of the instance.
     */
    public int tick() {
        return tick;
    }
    
    /**
     * Gets the progress of the instance.
     *
     * @return the progress of the instance.
     */
    public float progress() {
        return (float) tick() / maxTick();
    }
    
    /**
     * Gets the max tick of the song.
     *
     * @return the max tick of the song.
     */
    public int maxTick() {
        return (int) song.getLength();
    }
    
    /**
     * Sets the callback of the instance.
     *
     * @param callback - The callback.
     */
    @Override
    public SongInstance callback(@Nonnull SongCallback callback) {
        this.callback = callback;
        return this;
    }
    
    /**
     * The implementation method of the instance.
     */
    @Override
    public void run() {
        if (isPaused()) {
            return;
        }
        
        // Finished playing
        if (tick++ >= song.getLength()) {
            callback.onFinishedPlaying();
            status = Status.FINISHED_PLAYING;
            super.cancel(); // Make sure to delegate cancel to super because we don't support force stop
            return;
        }
        
        final List<SongNote> notesAtTick = song.getNotes(tick);
        
        if (notesAtTick != null) {
            notesAtTick.forEach(note -> note.play(players, volume));
        }
    }
    
    /**
     * Forcefully stops the instance.
     * <p><b>Does NOT call the callback!</b></p>
     * <p>
     * It is not recommended to forcefully stop the instance, instead {@link #stop()} should be used.
     */
    @Override
    public synchronized void cancel() {
        this.status = Status.FINISHED_PLAYING;
        super.cancel();
    }
    
    /**
     * Denotes the status of the instance.
     */
    public enum Status {
        /**
         * The instance is awaiting to be {@link #play(Player)}.
         */
        AWAITING,
        
        /**
         * The instance is currently playing the song.
         */
        PLAYING,
        
        /**
         * The instance is currently paused.
         */
        PAUSED,
        
        /**
         * The instance has finished playing.
         */
        FINISHED_PLAYING
    }
    
}
