package me.hapyl.eterna.module.player.sequencer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.eterna.module.annotate.EventLike;
import me.hapyl.eterna.module.annotate.SelfReturn;
import me.hapyl.eterna.module.util.Buildable;
import me.hapyl.eterna.module.util.Compute;
import me.hapyl.eterna.module.util.EternaRunnable;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

/**
 * Represents a {@link Sequencer} which is a sound sequence composed of {@link Track}, which represent a linear
 * {@link String} where individual characters map to specific {@link Sound}.
 */
public class Sequencer {
    
    /**
     * Defines the empty note character.
     *
     * <p>
     * This character must be used for silent notes and placeholders.
     * </p>
     */
    public static final char EMPTY_NOTE = '-';
    
    /**
     * Defines the default volume used for playing the sounds.
     */
    public static final float DEFAULT_VOLUME = 3.0f;
    
    private final Plugin plugin;
    private final TreeMap<Integer, List<Tune>> compiled;
    
    private final float volume;
    
    /**
     * Creates a new {@link Sequencer}.
     *
     * @param plugin - The plugin to delegate task to.
     */
    private Sequencer(@NotNull Plugin plugin, @NotNull List<? extends Track> tracks, float volume) {
        this.plugin = plugin;
        this.compiled = compile(tracks);
        this.volume = volume;
    }
    
    /**
     * Plays the sound sequence to the given {@link Player}.
     *
     * @param players - The players for whom to play the sequence.
     */
    public void play(@NotNull Collection<? extends Player> players) {
        this.onStartPlaying();
        
        final int maxNote = compiled.lastKey();
        
        new EternaRunnable(plugin) {
            private int note;
            
            @Override
            public void run() {
                if (note > maxNote) {
                    this.cancel();
                    Sequencer.this.onStopPlaying();
                    return;
                }
                
                final List<Tune> tunes = compiled.get(note);
                
                // null tunes means there is nothing on that note
                if (tunes != null) {
                    players.forEach(player -> tunes.forEach(tune -> tune.play(player, volume)));
                }
                
                note++;
            }
        }.runTaskTimer(0, 1);
    }
    
    /**
     * Plays the sound sequence to the given {@link Player}.
     *
     * @param player - The player for whom to play the sequence.
     */
    public void play(@NotNull Player player) {
        this.play(List.of(player));
    }
    
    /**
     * An event-like method which is called whenever the sequence starts playing.
     */
    @EventLike
    public void onStartPlaying() {
    }
    
    /**
     * An event-like method which is called whenever the sequence stops playing.
     */
    @EventLike
    public void onStopPlaying() {
    }
    
    /**
     * A static factory method of creating {@link Sequencer}.
     *
     * @param plugin - The plugin delegate.
     * @param track  - The track to add.
     * @return a new sequencer.
     */
    public static @NotNull Sequencer singleTrack(@NotNull Plugin plugin, @NotNull Track track) {
        return new Sequencer(plugin, List.of(track), DEFAULT_VOLUME);
    }
    
    /**
     * Creates a builder for {@link Sequencer}.
     *
     * @param plugin - The plugin delegate.
     * @return a new builder.
     */
    public static @NotNull Builder builder(@NotNull Plugin plugin) {
        return new Builder(plugin);
    }
    
    @ApiStatus.Internal
    private static @NotNull TreeMap<Integer, List<Tune>> compile(@NotNull List<? extends Track> tracks) {
        final TreeMap<Integer, List<Tune>> compiled = Maps.newTreeMap();
        
        for (final Track track : tracks) {
            for (int i = 0; i < track.track.length(); i++) {
                final char ch = track.track.charAt(i);
                final Tune tune = track.characterTuneMap.get(ch);
                
                if (ch != EMPTY_NOTE && tune != null) {
                    compiled.compute(i, Compute.listAdd(tune));
                }
            }
        }
        
        return compiled;
    }
    
    /**
     * Represents a {@link Builder} for {@link Sequencer}.
     */
    public static class Builder implements Buildable<Sequencer> {
        
        private final Plugin plugin;
        private final List<Track> tracks;
        
        private float volume;
        
        Builder(@NotNull Plugin plugin) {
            this.plugin = plugin;
            this.tracks = Lists.newArrayList();
            this.volume = DEFAULT_VOLUME;
        }
        
        /**
         * Adds the given {@link Track}.
         *
         * @param track - The track to add.
         */
        @SelfReturn
        public Builder track(@NotNull Track track) {
            this.tracks.add(track);
            return this;
        }
        
        /**
         * Sets the volume of sounds.
         *
         * @param volume - The new volume.
         */
        @SelfReturn
        public Builder volume(@Range(from = 0, to = Integer.MAX_VALUE) float volume) {
            this.volume = volume;
            return this;
        }
        
        /**
         * Builds a new {@link Sequencer}.
         *
         * @return a new sequencer instance.
         */
        @Override
        public @NonNull Sequencer build() {
            return new Sequencer(plugin, tracks, volume);
        }
    }
    
}
