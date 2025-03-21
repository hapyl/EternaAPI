package me.hapyl.eterna.module.player.song;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Queue;

/**
 * Represents a queue of songs.
 */
public interface SongQueue {
    
    /**
     * Gets the current song in the queue.
     *
     * @return the current song in the queue.
     */
    @Nullable
    Song current();
    
    /**
     * Gets the next song in the queue without advancing the queue.
     *
     * @return the next song in the queue without advancing the queue.
     */
    @Nullable
    Song getNext();
    
    /**
     * Returns {@code true} if the queue has a next song.
     *
     * @return {@code true} if the queue has a next song.
     */
    boolean hasNext();
    
    /**
     * Plays the next song in the queue or does nothing if there is none.
     */
    void playNext();
    
    /**
     * Adds a song to the queue.
     *
     * @param song - The song to add.
     */
    void addSong(@Nonnull Song song);
    
    /**
     * Removes the next song from the queue.
     */
    void removeSong();
    
    /**
     * Gets the actual queue object.
     *
     * @return the actual queue object.
     */
    @Nonnull
    Queue<Song> getQueue();
    
    /**
     * Clears the queue.
     */
    void clear();
    
    /**
     * Gets the size of the queue.
     *
     * @return the size of the queue.
     */
    default int size() {
        return getQueue().size();
    }
}
