package me.hapyl.eterna.module.player.sequencer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.eterna.module.annotate.EventLike;
import me.hapyl.eterna.module.annotate.SelfReturn;
import me.hapyl.eterna.module.util.Compute;
import me.hapyl.eterna.module.util.EternaRunnable;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

/**
 * Represents a {@link Sequencer} which is a sound sequence composed of {@link Track}, which represent a
 * linear {@link String} where individual characters map to specific {@link Sound}.
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
    private final List<Track> tracks;
    
    private int bpt;
    private float volume;
    
    /**
     * Creates a new {@link Sequencer}.
     *
     * @param plugin - The plugin to delegate task to.
     */
    public Sequencer(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.tracks = Lists.newArrayList();
        this.bpt = 1;
        this.volume = DEFAULT_VOLUME;
    }
    
    /**
     * Plays the sound sequence to the given {@link Player}.
     *
     * @param players - The players for whom to play the sequence.
     */
    public void play(@NotNull Collection<? extends Player> players) {
        // Map the tracks
        final TreeMap<Integer, List<Track.Tune>> mappedTunes = Maps.newTreeMap();
        
        for (final Track track : this.tracks) {
            for (int i = 0; i < track.track.length(); i++) {
                final char ch = track.track.charAt(i);
                final Track.Tune tune = track.characterTuneMap.get(ch);
                
                if (ch != EMPTY_NOTE && tune != null) {
                    mappedTunes.compute(i, Compute.listAdd(tune));
                }
            }
        }
        
        this.onStartPlaying();
        
        final int maxNote = mappedTunes.lastKey();
        
        new EternaRunnable(plugin) {
            private int note;
            
            @Override
            public void run() {
                if (note > maxNote) {
                    this.cancel();
                    Sequencer.this.onStopPlaying();
                    return;
                }
                
                final List<Track.Tune> tunes = mappedTunes.get(note);
                
                // null tunes means there is nothing on that note
                if (tunes != null) {
                    players.forEach(player -> tunes.forEach(tune -> tune.play(player, volume)));
                }
                
                note++;
            }
        }.runTaskTimer(0, bpt);
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
     * Sets the bpt (Beats per Tick) of this {@link Sequencer}.
     *
     * @param bpt - The bpt to set; will be clamped between {@code 1} - {@code 20}.
     */
    @SelfReturn
    public Sequencer bpt(final int bpt) {
        this.bpt = Math.clamp(bpt, 1, 20);
        return this;
    }
    
    /**
     * Sets the volume of this {@link Sequencer}.
     *
     * @param volume - The volume to set; will be clamped between {@code 0} - {@code 10}.
     */
    @SelfReturn
    public Sequencer volume(final float volume) {
        this.volume = Math.clamp(volume, 0, 10);
        return this;
    }
    
    /**
     * Adds the given {@link Track} to this {@link Sequencer}.
     *
     * @param track - The track to add.
     */
    @SelfReturn
    public Sequencer addTrack(@NotNull Track track) {
        this.tracks.add(track);
        return this;
    }
    
    /**
     * A static factory method of creating {@link Sequencer}.
     *
     * @param plugin - The plugin delegate.
     * @param track  - The track to add.
     * @return a new sequencer.
     */
    @NotNull
    public static Sequencer singleTrack(@NotNull Plugin plugin, @NotNull Track track) {
        return new Sequencer(plugin).addTrack(track);
    }
    
}
