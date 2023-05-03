package me.hapyl.spigotutils.module.player.song;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a parsed, cached and playable .nbs song.
 */
public class Song {

    private final String devName;
    private final String name;
    private final String author;
    private final String originalAuthor;
    private final long length;
    private final int tempo;
    private final Map<Long, List<SongNote>> notes;
    private boolean okOctave;

    public Song(String devName, String name, String author, String originalAuthor, long length, int tempo) {
        this.devName = devName;
        this.name = name;
        this.author = author;
        this.originalAuthor = originalAuthor;
        this.length = length;
        this.tempo = 20 / tempo;
        this.okOctave = true;
        this.notes = new HashMap<>();
    }

    public String getDevName() {
        return devName;
    }

    public void markInvalidOctave() {
        this.okOctave = false;
    }

    /**
     * Returns true is octave is in Minecraft bounds; false otherwise.
     * If false, the song might sound a little weird.
     *
     * @return true if octave is in Minecraft bounds; false otherwise.
     */
    public boolean isOkOctave() {
        return okOctave;
    }

    /**
     * Returns total length of the song.
     *
     * @return total length of the song.
     */
    public long getLength() {
        return length;
    }

    /**
     * Returns author of the song, who made the NBS file.
     *
     * @return author of the song, who made the NBS file.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Returns original author of the song, who made the song.
     *
     * @return original author of the song, who made the song.
     */
    public String getOriginalAuthor() {
        return originalAuthor;
    }

    /**
     * Adds a note to the song.
     *
     * @param note - note to add.
     */
    public void addNote(SongNote note) {
        this.notes.put((long) this.notes.size(), List.of(note));
    }

    /**
     * Puts a note to the song at provided index.
     *
     * @param index - index to put the note at.
     * @param note  - note to put.
     */
    public void putNote(long index, SongNote note) {
        final List<SongNote> list = this.notes.getOrDefault(index, new ArrayList<>());
        list.add(note);
        this.notes.put(index, list);
    }

    /**
     * Returns list of notes at provided index.
     *
     * @param index - index to get notes from.
     * @return list of notes at provided index.
     */
    @Nullable
    public List<SongNote> getNotes(long index) {
        return this.notes.getOrDefault(index, null);
    }

    /**
     * Returns all notes in the song.
     *
     * @return all notes in the song.
     */
    public Map<Long, List<SongNote>> getNotes() {
        return notes;
    }

    /**
     * Returns the name of this song.
     *
     * @return the name of this song.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the tempo of this song.
     *
     * @return the tempo of this song.
     */
    public int getTempo() {
        return tempo;
    }

}
