package me.hapyl.spigotutils.module.player.song;

import org.bukkit.Instrument;
import org.bukkit.Note;

/**
 * Helper for song parsing.
 */
public final class SongHelper {

    private SongHelper() {
    }

    /**
     * Returns the instrument based on a byte.
     *
     * @param bit - The byte.
     * @return The instrument.
     */
    public static Instrument getInstrument(byte bit) {
        return switch (bit) {
            case 1 -> Instrument.BASS_GUITAR;
            case 2 -> Instrument.BASS_DRUM;
            case 3 -> Instrument.SNARE_DRUM;
            case 4 -> Instrument.STICKS;
            case 5 -> Instrument.GUITAR;
            case 6 -> Instrument.FLUTE;
            case 7 -> Instrument.BELL;
            case 8 -> Instrument.CHIME;
            case 9 -> Instrument.XYLOPHONE;
            default -> Instrument.PIANO;
        };
    }

    /**
     * Returns the note from a byte.
     *
     * @param note - The byte.
     * @return The note.
     */
    public static Note getNote(byte note) {
        return new Note(Math.min(Math.max(note - 33, 0), 24));
    }

    /**
     * Returns true if the octave is valid in Minecraft.
     *
     * @param note - The note.
     * @return True if the octave is valid.
     */
    public static boolean isInvalidOctave(byte note) {
        return note < 0 || note > 24;
    }

}
