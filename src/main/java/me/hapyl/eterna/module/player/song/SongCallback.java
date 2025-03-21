package me.hapyl.eterna.module.player.song;

import me.hapyl.eterna.module.util.Callback;

/**
 * Represents a callback for {@link Song}.
 */
public interface SongCallback extends Callback {
    
    /**
     * Called whenever a {@link Song} has started playing.
     */
    void onStartPlaying();
    
    /**
     * Called whenever a {@link Song} has finished playing.
     */
    void onFinishedPlaying();
    
    /**
     * Called whenever a {@link Song} is paused/resumed.
     *
     * @param pause - Is the song currently paused.
     */
    void onPause(boolean pause);
    
}
