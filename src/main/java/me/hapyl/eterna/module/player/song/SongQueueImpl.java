package me.hapyl.eterna.module.player.song;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Stream;

/**
 * Represents an internal song queue impl.
 */
@ApiStatus.Internal
public class SongQueueImpl implements SongQueue {
    
    private final Queue<Song> queue;
    
    SongQueueImpl() {
        this.queue = new LinkedList<>();
    }
    
    @Override
    @Nullable
    public Song peekNext() {
        return this.queue.peek();
    }
    
    @Nullable
    @Override
    public Song pollNext() {
        return this.queue.poll();
    }
    
    @Override
    public boolean hasNext() {
        return this.queue.peek() != null;
    }
    
    @Override
    public void add(@NotNull Song song) {
        this.queue.add(song);
    }
    
    @Override
    public boolean hasSong(@NotNull Song song) {
        return queue.contains(song);
    }
    
    @Override
    public void clear() {
        queue.clear();
    }
    
    @Override
    public int size() {
        return queue.size();
    }
    
    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }
    
    @Override
    @NotNull
    public Stream<Song> stream() {
        return queue.stream();
    }
    
    @Override
    public int indexOf(@NotNull Song song) {
        int index = 0;
        
        for (Song queueSong : queue) {
            if (queueSong.equals(song)) {
                return index;
            }
            
            index++;
        }
        
        return -1;
    }
    
    @Override
    public void remove(@NotNull Song song) {
        this.queue.remove(song);
    }
}
