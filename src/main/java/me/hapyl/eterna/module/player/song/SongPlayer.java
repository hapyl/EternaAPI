package me.hapyl.eterna.module.player.song;

import com.google.common.collect.Sets;
import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.module.util.EternaRunnable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Represents a {@link SongPlayer} responsible for managing and playing {@link Song}.
 */
public class SongPlayer extends EternaRunnable {
    
    /**
     * Defines the default played used by Eterna.
     */
    @NotNull
    public static final SongPlayer SERVER_PLAYER;
    
    static {
        SERVER_PLAYER = new SongPlayer(Eterna.getPlugin()) {
            @Override
            public void addListener(@NotNull Player player) {
            }
            
            @Override
            public void removeListener(Player player) {
            }
            
            @Override
            public boolean isListener(@NotNull Player player) {
                return true;
            }
            
            @NotNull
            @Override
            public Set<Player> getListeners() {
                return Set.copyOf(Bukkit.getOnlinePlayers());
            }
        };
    }
    
    private final Plugin plugin;
    private final SongQueue queue;
    private final Set<Player> listeners;
    
    @Nullable
    private SongInstance currentSong;
    
    @NotNull private Status status;
    @NotNull private SongFormatter formatter;
    
    @Range(from = 0, to = 1)
    private float volume;
    
    private float tickAccumulator;
    
    /**
     * Creates a new {@link SongPlayer} for the given {@link Plugin}.
     *
     * @param plugin - The plugin to delegate tasks to.
     */
    public SongPlayer(@NotNull Plugin plugin) {
        super(plugin);
        
        this.plugin = plugin;
        this.queue = new SongQueueImpl();
        this.listeners = Sets.newHashSet();
        this.volume = 1.0f;
        this.status = Status.NOT_PLAYING;
        this.formatter = SongFormatter.DEFAULT;
        
        // Schedule the task
        this.runTaskTimer(0, 1);
    }
    
    /**
     * Gets the {@link Plugin} this {@link SongPlayer} belongs to.
     *
     * @return the plugin this song player belongs to.
     */
    @NotNull
    public final Plugin getPlugin() {
        return plugin;
    }
    
    /**
     * Gets the current {@link SongInstance}, or {@code null} if nothing is currently playing.
     *
     * @return the current song instance, or {@code null} if nothing is currently playing.
     */
    @Nullable
    public SongInstance getCurrentSong() {
        return this.currentSong;
    }
    
    /**
     * Gets the {@link SongFormatter} for this {@link SongPlayer}.
     *
     * @return the formatter for this song player.
     * @see SongFormatter
     */
    @NotNull
    public SongFormatter getFormatter() {
        return formatter;
    }
    
    /**
     * Sets the {@link SongFormatter} for this {@link SongPlayer}.
     *
     * @param formatter - The formatter to set.
     * @throws NullPointerException if the given formatter is {@code null}.
     */
    public void setFormatter(@NotNull SongFormatter formatter) {
        this.formatter = Objects.requireNonNull(formatter, "Formatter cannot be null.");
    }
    
    /**
     * Gets the volume of this {@link SongPlayer}, from {@code 0} - {@code 1}.
     *
     * @return the volume of this song player, from {@code 0} - {@code 1}.
     */
    @Range(from = 0, to = 1)
    public float getVolume() {
        return volume;
    }
    
    /**
     * Sets the volume of this {@link SongPlayer}, from {@code 0} - {@code 1}.
     *
     * @param volume - The volume to set.
     */
    public void setVolume(@Range(from = 0, to = 1) final float volume) {
        this.volume = Math.clamp(volume, 0, 1);
    }
    
    /**
     * Gets the {@link SongQueue} for this {@link SongPlayer}.
     *
     * @return the song queue for this song player.
     */
    @NotNull
    public SongQueue getQueue() {
        return queue;
    }
    
    /**
     * Adds the given {@link Player} to listeners.
     *
     * @param player - The player to add to listeners.
     */
    public void addListener(@NotNull Player player) {
        this.listeners.add(player);
    }
    
    /**
     * Removes the given {@link Player} from listeners.
     *
     * @param player - The player to remove from listeners.
     */
    public void removeListener(Player player) {
        this.listeners.remove(player);
    }
    
    /**
     * Gets whether the given {@link Player} is a listener.
     *
     * @param player - The player to check.
     * @return {@code true} if the given player is a listener; {@code false} otherwise.
     */
    public boolean isListener(@NotNull Player player) {
        return this.listeners.contains(player);
    }
    
    /**
     * Gets an <b>immutable</b> {@link Set} of listeners.
     *
     * @return an <b>immutable</b> set of listeners.
     */
    @NotNull
    public Set<Player> getListeners() {
        return Set.copyOf(listeners);
    }
    
    /**
     * Runs this {@link SongPlayer}.
     */
    @Override
    public void run() {
        if (currentSong == null || status == Status.PAUSED) {
            return;
        }
        
        // Because tempo is not guaranteed to be divisible by 20, we have to use an
        // accumulator to play the songs at the right tick.
        // It is technically better to calculate the delta between this tick and the
        // previous one, but we're assuming it's 20 tps (1000ms), so that's what we use.
        tickAccumulator += 50 / (1000.0f / currentSong.getSong().getTempo());
        
        while (tickAccumulator >= 1.0f) {
            final boolean hasFinished = currentSong.playNextNote(this);
            
            if (hasFinished) {
                stopPlaying(true);
                return;
            }
            
            tickAccumulator -= 1.0f;
        }
    }
    
    /**
     * Pauses the current {@link Song}.
     *
     * <p>
     * If the song is currently paused, it will be resumed.
     * </p>
     */
    public void pause() {
        if (this.currentSong == null) {
            return;
        }
        
        this.status = this.status == Status.PLAYING ? Status.PAUSED : Status.PLAYING;
        
        formatterAll((formatter, player) -> formatter.pausedPlaying(player, currentSong.getSong(), status == Status.PAUSED));
    }
    
    /**
     * Forcefully plays the given {@link Song}.
     *
     * <p>
     * If any song was already playing, it is forcefully stopped.
     * </p>
     *
     * @param song - The song to play.
     */
    public void startPlaying(@NotNull Song song) {
        this.status = Status.PLAYING;
        this.currentSong = song.newInstance();
        
        this.tickAccumulator = 0.0f;
        
        formatterAll((formatter, player) -> formatter.nowPlaying(player, song));
    }
    
    /**
     * Stops playing the current {@link Song}.
     *
     * @param playNext - {@code true} to play the next song in queue; {@code false} otherwise.
     */
    public void stopPlaying(boolean playNext) {
        if (this.currentSong == null) {
            return;
        }
        
        final Song song = currentSong.getSong();
        
        this.status = Status.NOT_PLAYING;
        this.currentSong = null;
        
        this.tickAccumulator = 0.0f;
        
        formatterAll((formatter, player) -> formatter.finishedPlaying(player, song));
        
        if (playNext) {
            final Song nextSong = queue.pollNext();
            
            if (nextSong != null) {
                startPlaying(nextSong);
            }
        }
    }
    
    /**
     * Gets the {@link Status} of this {@link SongPlayer}.
     *
     * @return the status of this song player.
     */
    @NotNull
    public Status getStatus() {
        return status;
    }
    
    /**
     * Plays the next song in {@link SongQueue}, if any.
     */
    public void playNextInQueue() {
        final Song song = this.queue.pollNext();
        
        if (song != null) {
            startPlaying(song);
        }
    }
    
    /**
     * Adds the given {@link Song} to this {@link SongQueue}.
     *
     * @param player - The player who added the song.
     * @param song   - The song to add.
     */
    public void addToQueue(@NotNull Player player, @NotNull Song song) {
        if (this.queue.hasSong(song)) {
            this.formatter.songCannotBeAddedToQueueDuplicate(player, song);
        }
        else {
            this.queue.add(song);
            this.formatter.songAddedToQueue(player, song, this.queue.size() - 1);
            
            // If nothing is currently playing, start playing the song
            if (this.currentSong == null) {
                this.playNextInQueue();
            }
        }
    }
    
    /**
     * Removes the given {@link Song} from this {@link SongQueue}.
     *
     * @param player - The player who removed the song.
     * @param song   - The song to remove.
     */
    public void removeFromQueue(@NotNull Player player, @NotNull Song song) {
        if (!this.queue.hasSong(song)) {
            this.formatter.songCannotBeRemovedFromQueueNotInQueue(player, song);
        }
        else {
            this.queue.remove(song);
            this.formatter.songRemovedFromQueue(player, song);
        }
    }
    
    /**
     * Advances the current {@link Song} by the given number of seconds.
     *
     * <p>
     * If there is no song currently playing, this method is silently ignored.
     * </p>
     *
     * @param seconds - The number of seconds to advance; use negative value to advance backwards.
     */
    public void advanceCurrentSong(final float seconds) {
        if (this.currentSong == null) {
            return;
        }
        
        final int tickIncrement = (int) (seconds * currentSong.getSong().getTempo());
        this.currentSong.advanceTick(tickIncrement);
    }
    
    private void formatterAll(@NotNull BiConsumer<SongFormatter, Player> consumer) {
        getListeners().forEach(player -> consumer.accept(formatter, player));
    }
    
    /**
     * Represents the {@link Status} for {@link SongPlayer}.
     */
    public enum Status {
        
        /**
         * Defines that nothing is currently playing.
         */
        NOT_PLAYING,
        
        /**
         * Defines that a {@link Song} is present and is currently playing.
         */
        PLAYING,
        
        /**
         * Defines that a {@link Song} is present, but currently paused.
         */
        PAUSED
    }
    
}
