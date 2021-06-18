package kz.hapyl.spigotutils.module.player.song;

import kz.hapyl.spigotutils.module.inventory.gui.GUI;
import org.bukkit.Instrument;
import org.bukkit.Note;

public class SongHelper {

	public static Instrument getInstrument(byte bit) {
		switch (bit) {
			case 1:
				return Instrument.BASS_GUITAR;
			case 2:
				return Instrument.BASS_DRUM;
			case 3:
				return Instrument.SNARE_DRUM;
			case 4:
				return Instrument.STICKS;
			case 5:
				return Instrument.GUITAR;
			case 6:
				return Instrument.FLUTE;
			case 7:
				return Instrument.BELL;
			case 8:
				return Instrument.CHIME;
			case 9:
				return Instrument.XYLOPHONE;
			default:
				return Instrument.PIANO;
		}
	}

	public static Note getNote(byte note) {
		return new Note(Math.min(Math.max(note - 33, 0), 24));
	}

}
