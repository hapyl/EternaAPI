package me.hapyl.spigotutils.module.player.song;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Queue;

/**
 * Represents a SongQueue for SongPlayer.
 */
public interface SongQueue {

    /**
     * Returns the current song.
     *
     * @return current song
     */
    @Nullable
    Song current();

    /**
     * Returns next song to play or null if none.
     *
     * @return next song to play or null if none.
     */
    @Nullable
    Song getNext();

    /**
     * Returns true if there is next song to play; false otherwise.
     *
     * @return true if there is next song to play; false otherwise.
     */
    boolean hasNext();

    /**
     * Plays the next song if there is one.
     */
    void playNext();

    /**
     * Skips the currently playing song.
     */
    void skip();

    /**
     * Adds a song to a queue.
     *
     * @param song - Song to add.
     */
    void addSong(Song song);

    /**
     * Removes the last song from queue.
     */
    void removeSong();

    /**
     * Gets the queue of songs.
     *
     * @return queue of songs.
     */
    @Nonnull
    Queue<Song> getQueue();

}
