package me.hapyl.eterna.module.player.song;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents a default song queue.
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
        this.current = this.queue.poll();
        
        if (this.current != null) {
            SongPlayer.DEFAULT_PLAYER.startPlaying(this.current);
        }
    }
    
    @Override
    public void addSong(@Nonnull Song song) {
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
    
    @Override
    public void clear() {
        queue.clear();
    }
}
