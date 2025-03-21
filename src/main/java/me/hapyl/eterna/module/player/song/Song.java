package me.hapyl.eterna.module.player.song;

import com.google.common.collect.Lists;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.util.Named;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a parsed cached and playable .nbs song.
 */
public class Song implements Named {
    
    private final String fileName;
    private final String name;
    private final String author;
    private final String originalAuthor;
    
    private final long length;
    private final int tempo;
    
    private final Map<Integer, List<SongNote>> notes;
    
    private boolean okOctave;
    
    public Song(@Nonnull String fileName, @Nonnull String name, @Nonnull String author, @Nonnull String originalAuthor, long length, int tempo) {
        this.fileName = fileName;
        this.name = name;
        this.author = author;
        this.originalAuthor = originalAuthor;
        this.length = length;
        this.tempo = tempo;
        this.okOctave = true;
        this.notes = new HashMap<>();
    }
    
    /**
     * Gets the file name of this song.
     *
     * @return the file name of this song.
     */
    @Nonnull
    public String fileName() {
        return fileName;
    }
    
    /**
     * Returns {@code true} if all notes in the song within Minecraft limits.
     *
     * @return {@code true} if all notes in the song within Minecraft limits.
     */
    public boolean isOkOctave() {
        return okOctave;
    }
    
    /**
     * Returns {@code true} if the tempo of the song is divisible by {@code 20}, meaning the song is played in perfect tempo.
     *
     * @return {@code true} if the tempo of the song is divisible by {@code 20}, meaning the song is played in perfect tempo.
     */
    public boolean isOkTempo() {
        return 20 % tempo == 0;
    }
    
    /**
     * Returns {@code true} if the song is vanilla compatible; false otherwise.
     *
     * @return {@code true} if the song is vanilla compatible; false otherwise.
     */
    public boolean isPerfect() {
        return isOkOctave() && isOkTempo();
    }
    
    /**
     * Gets the length of the song in ticks.
     *
     * @return the length of the song in ticks.
     */
    public long getLength() {
        return length;
    }
    
    /**
     * Gets the tempo of the song, in ticks per second.
     *
     * @return the tempo of the song, in ticks per second.
     */
    public int tempo() {
        return tempo;
    }
    
    /**
     * Gets the author of the <b>.nbs</b> file.
     *
     * @return the author of the <b>.nbs</b> file.
     */
    @Nonnull
    public String getAuthor() {
        return author;
    }
    
    /**
     * Gets the original author of the song.
     *
     * @return the original author of the song.
     */
    @Nonnull
    public String getOriginalAuthor() {
        return originalAuthor;
    }
    
    /**
     * Gets either the original author of song, or the <b>.nbs</b> author with a {@code *} suffixes or Unknown if neither is known.
     *
     * @return either the original author of song, or the <b>.nbs</b> author with a {@code *} suffixes or Unknown if neither is known.
     */
    @Nonnull
    public String getEitherOriginalAuthorOrAuthorWithAsteriskOrUnknownIfAuthorNotSpecified() {
        return !originalAuthor.isEmpty() ? originalAuthor : !author.isEmpty() ? author + "*" : "Unknown";
    }
    
    /**
     * Gets the notes at the given index.
     *
     * @param index - The index.
     * @return the notes at the given index.
     */
    @Nullable
    public List<SongNote> getNotes(int index) {
        return this.notes.get(index);
    }
    
    /**
     * Gets all the notes of this song.
     *
     * @return all the notes of this song.
     */
    @Nonnull
    public Map<Integer, List<SongNote>> getNotes() {
        return notes;
    }
    
    /**
     * Gets the name of this song.
     *
     * @return the name of this song.
     */
    @Nonnull
    @Override
    public String getName() {
        return name;
    }
    
    /**
     * Gets the formatted name of this song, following the <i>Artist - Name</i> format.
     *
     * @return the formatted name of this song, following the <i>Artist - Name</i> format.
     */
    @Nonnull
    public String getNameFormatted() {
        return "&a%s - &6%s".formatted(getEitherOriginalAuthorOrAuthorWithAsteriskOrUnknownIfAuthorNotSpecified(), getName());
    }
    
    /**
     * Creates a new {@link SongInstance} of this song for the given plugin.
     *
     * @param plugin - The plugin.
     * @return a new song instance.
     */
    @Nonnull
    public SongInstance newInstance(@Nonnull JavaPlugin plugin) {
        return new SongInstance(this, plugin);
    }
    
    /**
     * Creates a new {@link SongInstance} of this song using {@link EternaPlugin}.
     *
     * @deprecated Prefer providing a plugin.
     */
    @Nonnull
    @Deprecated
    public SongInstance newInstance() {
        return newInstance(EternaPlugin.getPlugin());
    }
    
    protected void setNote(int index, @Nonnull SongNote note) {
        this.notes.computeIfAbsent(index, i -> Lists.newArrayList()).add(note);
    }
    
    protected void markInvalidOctave() {
        this.okOctave = false;
    }
    
    protected int taskPeriod() {
        return 20 / tempo;
    }
    
}
