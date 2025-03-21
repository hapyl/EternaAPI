package me.hapyl.eterna.module.player.song;

import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a note in a song.
 */
public record SongNote(@Nonnull Instrument instrument, @Nonnull Note note) {
    
    public static final float DEFAULT_VOLUME = 1.0f;
    
    /**
     * Plays this note to the given players at the default volume.
     *
     * @param players - The players to play the note to.
     */
    public void play(@Nonnull Collection<? extends Player> players) {
        play(players, DEFAULT_VOLUME);
    }
    
    /**
     * Plays this note to the given players at the default volume.
     *
     * @param players - The players to play the note to.
     * @param volume  - The volume to play the note at.
     */
    public void play(@Nonnull Collection<? extends Player> players, float volume) {
        players.forEach(player -> play(player, volume));
    }
    
    /**
     * Plays this note to the given player at the default volume.
     *
     * @param player - The player to play the note to.
     */
    public void play(@Nonnull Player player) {
        play(player, DEFAULT_VOLUME);
    }
    
    /**
     * Plays this note to the given player at the given volume.
     *
     * @param player - The player to play the note to.
     * @param volume - The volume to play the note at.
     */
    public void play(@Nonnull Player player, float volume) {
        final Sound sound = Objects.requireNonNull(instrument.getSound(), "Cannot use heads for notes!");
        
        player.playSound(player.getLocation(), sound, volume, note.getPitch());
    }
    
}
