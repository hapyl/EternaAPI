package me.hapyl.eterna.module.player.song;

import me.hapyl.eterna.module.util.Streamable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

/**
 * Represents a {@link SongQueue}.
 *
 * <p>
 * Does not permit duplicate songs.
 * </p>
 */
public interface SongQueue extends Streamable<Song> {
    
    /**
     * Retrieves, but does not remove, the next {@link Song} in the queue.
     *
     * @return the next song in the queue.
     */
    @Nullable
    Song peekNext();
    
    /**
     * Retrieves and removes the next {@link Song} in the queue.
     *
     * @return the next song in the queue.
     */
    @Nullable
    Song pollNext();
    
    /**
     * Gets whether this queue has a next {@link Song}.
     *
     * @return {@code true} if there is a next song in this queue; {@code false} otherwise.
     */
    boolean hasNext();
    
    /**
     * Adds the given {@link Song} to this queue.
     *
     * @param song - The song to add.
     */
    void add(@NotNull Song song);
    
    /**
     * Removes the given {@link Song} from this queue.
     *
     * @param song - The song to remove.
     */
    void remove(@NotNull Song song);
    
    /**
     * Gets whether this queue has the given {@link Song}.
     *
     * @param song - The song to check.
     * @return {@code true} if this queue has the given song; {@code false} otherwise.
     */
    boolean hasSong(@NotNull Song song);
    
    /**
     * Gets the index of the given {@link Song} in this queue, or {@code -1} if not in the queue.
     *
     * @param song - The song which index to get.
     * @return the index of the given song in this queue, or {@code -1} if not in the queue.
     */
    int indexOf(@NotNull Song song);
    
    /**
     * Clears this queue.
     */
    void clear();
    
    /**
     * Gets the size of this queue.
     *
     * @return the size of this queue.
     */
    int size();
    
    /**
     * Gets whether this queue is empty.
     *
     * @return {@code true} if this queue is empty; {@code false} otherwise.
     */
    default boolean isEmpty() {
        return size() == 0;
    }
    
    /**
     * Creates a {@link Stream} of {@link Song} in this queue.
     *
     * @return a stream of songs in this queue.
     */
    @Override
    @NotNull
    Stream<Song> stream();
}
