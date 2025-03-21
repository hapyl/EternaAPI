package me.hapyl.eterna.module.player.song;

import me.hapyl.eterna.module.annotate.UtilityClass;
import org.bukkit.Instrument;
import org.bukkit.Note;

import javax.annotation.Nonnull;

/**
 * Helper for song parsing.
 */
@UtilityClass
public final class SongHelper {

    private SongHelper() {
        UtilityClass.Validator.throwIt();
    }

    /**
     * Returns the instrument based on a byte.
     *
     * @param bit - The byte.
     * @return The instrument.
     */
    @Nonnull
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
            case 10 -> Instrument.IRON_XYLOPHONE;
            case 11 -> Instrument.COW_BELL;
            case 12 -> Instrument.DIDGERIDOO;
            case 13 -> Instrument.BIT;
            case 14 -> Instrument.BANJO;
            case 15 -> Instrument.PLING;
            default -> Instrument.PIANO;
        };
    }

    /**
     * Returns the note from a byte.
     *
     * @param note - The byte.
     * @return The note.
     */
    @Nonnull
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
