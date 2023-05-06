package me.hapyl.spigotutils.module.player.song;

import me.hapyl.spigotutils.EternaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents
 */
public class SongPlayerQueue implements SongQueue {

    private final Queue<Song> queue;
    private Song current;

    public SongPlayerQueue() {
        this.queue = new LinkedList<>();
    }

    @Override
    @Nullable
    public Song current() {
        return current;
    }

    @Override
    public boolean hasNext() {
        return this.queue.peek() != null;
    }

    @Override
    @Nullable
    public Song getNext() {
        return this.queue.peek();
    }

    @Override
    public void playNext() {
        if (this.hasNext()) {
            this.current = this.queue.poll();
            final SongPlayer player = EternaPlugin.getPlugin().getSongPlayer();
            player.setCurrentSong(this.current);
            player.startPlaying();
        }
    }

    @Override
    public void skip() {
        this.playNext();
    }

    @Override
    public void addSong(Song song) {
        this.queue.add(song);
    }

    @Override
    public void removeSong() {
        this.queue.poll();
    }

    @Nonnull
    public Queue<Song> getQueue() {
        return queue;
    }
}
