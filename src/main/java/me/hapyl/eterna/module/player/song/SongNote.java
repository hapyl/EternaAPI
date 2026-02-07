package me.hapyl.eterna.module.player.song;

import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Represents a {@link SongNote} containing the {@link Instrument} and {@link Note}.
 */
public record SongNote(@NotNull Instrument instrument, @NotNull Note note) {
    
    /**
     * Defines the volume used for the sound.
     */
    public static final float VOLUME = 3.0f;
    
    /**
     * Plays this note to the give {@link Player}.
     *
     * @param player - The player to play the note to.
     * @param volume - The volume multiplier.
     */
    public void play(@NotNull Player player, @Range(from = 0, to = 1) final float volume) {
        final Sound sound = instrument.getSound();
        
        // Sound is `null` for player heads, which we don't support
        if (sound == null) {
            return;
        }
        
        player.playSound(player, sound, VOLUME * volume, note.getPitch());
    }
    
    
}
