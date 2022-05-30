package me.hapyl.spigotutils.module.player.song;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Song {

    private final String name;
    private final String author;
    private final String originalAuthor;
    private final long length;
    private final int tempo;
    private final Map<Long, List<SongNote>> notes;
    private boolean okOctave;

    public Song(String name, String author, String originalAuthor, long length, int tempo) {
        this.name = name;
        this.author = author;
        this.originalAuthor = originalAuthor;
        this.length = length;
        this.tempo = 20 / tempo;
        this.okOctave = true;
        this.notes = new HashMap<>();
    }

    public void markInvalidOctave() {
        this.okOctave = false;
    }

    public boolean isOkOctave() {
        return okOctave;
    }

    public long getLength() {
        return length;
    }

    public String getAuthor() {
        return author;
    }

    public String getOriginalAuthor() {
        return originalAuthor;
    }

    public void addNote(SongNote note) {
        this.notes.put((long) this.notes.size(), List.of(note));
    }

    public void putNote(long index, SongNote note) {
        final List<SongNote> list = this.notes.getOrDefault(index, new ArrayList<>());
        list.add(note);
        this.notes.put(index, list);
    }

    @Nullable
    public List<SongNote> getNotes(long index) {
        return this.notes.getOrDefault(index, null);
    }

    public Map<Long, List<SongNote>> getNotes() {
        return notes;
    }

    public String getName() {
        return name;
    }

    public int getTempo() {
        return tempo;
    }

}
