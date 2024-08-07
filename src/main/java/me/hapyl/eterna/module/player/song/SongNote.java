package me.hapyl.eterna.module.player.song;

import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * Represents a note in a song.
 */
public class SongNote {

    private final Instrument instrument;
    private final Note note;
    private boolean okOctave;

    public SongNote(Instrument instrument, Note note) {
        this.instrument = instrument;
        this.note = note;
        this.okOctave = true;
    }

    public boolean isOkOctave() {
        return okOctave;
    }

    public void markInvalidOctave() {
        this.okOctave = false;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public Note getNote() {
        return note;
    }

    public void play(@Nonnull Collection<? extends Player> players) {
        for (final Player player : players) {
            this.play(player);
        }
    }

    public void play(@Nonnull Player player) {
        player.playNote(player.getLocation(), this.instrument, this.note);
    }

}
