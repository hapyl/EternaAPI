package me.hapyl.eterna.module.player.song;

import me.hapyl.eterna.module.util.Progressible;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a {@link SongInstance} that handles playing notes.
 */
public final class SongInstance implements Progressible {
    
    private final Song song;
    
    private int tick;
    
    SongInstance(@NotNull Song song) {
        this.song = song;
    }
    
    /**
     * Gets the {@link Song} that belongs to this {@link SongInstance}.
     *
     * @return the song that belongs to this song instance.
     */
    @NotNull
    public Song getSong() {
        return song;
    }
    
    /**
     * Gets the current progress of the {@link Song}, in ticks.
     *
     * @return the current progress of the song, in ticks.
     */
    @Override
    public double currentProgress() {
        return tick;
    }
    
    /**
     * Gets the max progress of the {@link Song}, in ticks.
     *
     * <p>
     * The max progress defines the duration of the {@link Song}.
     * </p>
     *
     * @return the max progress of the song, in ticks.
     */
    @Override
    public double maxProgress() {
        return song.getDuration();
    }
    
    @ApiStatus.Internal
    void advanceTick(final int tick) {
        this.tick = Math.clamp(this.tick + tick, 0, (int) maxProgress());
    }
    
    @ApiStatus.Internal
    boolean playNextNote(@NotNull SongPlayer songPlayer) {
        final List<SongNote> notes = song.notesAt0(tick);
        
        if (notes != null) {
            for (Player listener : songPlayer.getListeners()) {
                for (SongNote songNote : notes) {
                    songNote.play(listener, songPlayer.getVolume());
                }
            }
        }
        
        return tick++ >= song.getDuration();
    }
    
}
