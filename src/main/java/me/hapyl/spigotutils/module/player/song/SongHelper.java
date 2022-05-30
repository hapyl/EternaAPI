package me.hapyl.spigotutils.module.player.song;

import org.bukkit.Instrument;
import org.bukkit.Note;

public class SongHelper {

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

	public static Note getNote(byte note) {
		return new Note(Math.min(Math.max(note - 33, 0), 24));
	}

	public static boolean isInvalidOctave(byte note) {
		return note < 0 || note > 24;
	}

}
