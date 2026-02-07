package me.hapyl.eterna.module.player.song;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.eterna.module.component.Components;
import me.hapyl.eterna.module.component.Described;
import me.hapyl.eterna.module.component.Named;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a parsed {@code .nbs} (Note Block Studio) song instance, that may be played to players.
 */
public class Song implements Named, Described {
    
    private final String fileName;
    
    private final Component name;
    private final Component description;
    
    private final Component author;
    private final Component originalAuthor;
    
    private final Map<Integer, List<SongNote>> notes;
    
    private final long duration;
    private final int tempo;
    
    private boolean okOctave;
    
    Song(
            @NotNull String fileName,
            @NotNull String name,
            @NotNull String description,
            @NotNull String author,
            @NotNull String originalAuthor,
            final long duration,
            final int tempo
    ) {
        this.fileName = fileName;
        this.name = Component.text(name);
        this.description = Component.text(description);
        this.author = Components.textOrDefault(author, "Unknown");
        this.originalAuthor = Components.textOrDefault(originalAuthor, "Unknown");
        this.notes = Maps.newHashMap();
        this.duration = duration;
        this.tempo = tempo;
        this.okOctave = true;
    }
    
    /**
     * Gets the file name of this {@link Song}.
     *
     * @return the file name of this song.
     */
    @NotNull
    public String getFileName() {
        return fileName;
    }
    
    /**
     * Gets the name of this {@link Song}.
     *
     * @return the name of this song.
     */
    @Override
    @NotNull
    public Component getName() {
        return name;
    }
    
    /**
     * Gets the description of this {@link Song}.
     *
     * @return the description of this song.
     */
    @NotNull
    @Override
    public Component getDescription() {
        return description;
    }
    
    /**
     * Gets the author of this {@link Song}.
     *
     * <p>
     * Author refers to whoever made the {@code .nbs} file, for song author, use {@link #getOriginalAuthor()}.
     * </p>
     *
     * @return the author of this {@link Song}.
     */
    @NotNull
    public Component getAuthor() {
        return author;
    }
    
    /**
     * Gets the original author of this {@link Song}.
     *
     * <p>
     * Original author refers to whoever made the actual song.
     * </p>
     *
     * @return the original author of this song.
     */
    @NotNull
    public Component getOriginalAuthor() {
        return originalAuthor;
    }
    
    /**
     * Gets whether all notes in this {@link Song} are within minecraft limits.
     *
     * @return {@code true} if all notes in this song are within minecraft limits; {@code false} otherwise.
     */
    public boolean isOkOctave() {
        return okOctave;
    }
    
    /**
     * Gets the duration of this {@link Song}, in ticks.
     *
     * @return the duration of the song in ticks.
     */
    public long getDuration() {
        return duration;
    }
    
    /**
     * Gets the duration of this {@link Song}, in seconds.
     *
     * @return the duration of this song, in seconds.
     */
    public float getDurationIsSeconds() {
        return (float) duration / tempo;
    }
    
    /**
     * Gets the tempo of the song, in ticks per second.
     *
     * @return the tempo of the song, in ticks per second.
     */
    public int getTempo() {
        return tempo;
    }
    
    /**
     * Gets an <b>immutable</b> {@link List} copy of {@link SongNote} for the given index.
     *
     * @param index - The index to retrieve the notes at.
     * @return an <b>immutable</b> list copy of song notes for the given index.
     */
    @Nullable
    public List<SongNote> notesAt(int index) {
        return List.copyOf(notes.get(index));
    }
    
    /**
     * Creates a new {@link SongInstance}.
     *
     * @return a new song instance.
     */
    @NotNull
    public SongInstance newInstance() {
        return new SongInstance(this);
    }
    
    /**
     * Gets the hash code of this {@link Song}, which is its {@code fileName}.
     *
     * @return the hash code of this song.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(this.fileName);
    }
    
    /**
     * Gets whether the given object is a {@link Song} and their {@code fileName} matches.
     *
     * @param object - The object to compare to.
     * @return {@code true} if the given {@code object} is a song and their {@code fileName} matches; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        
        final Song that = (Song) object;
        return Objects.equals(this.fileName, that.fileName);
    }
    
    @Nullable
    @ApiStatus.Internal
    List<SongNote> notesAt0(int tick) {
        return notes.get(tick);
    }
    
    @ApiStatus.Internal
    void setNote(int index, @NotNull SongNote note) {
        this.notes.computeIfAbsent(index, i -> Lists.newArrayList()).add(note);
    }
    
    @ApiStatus.Internal
    void markInvalidOctave() {
        this.okOctave = false;
    }
}
