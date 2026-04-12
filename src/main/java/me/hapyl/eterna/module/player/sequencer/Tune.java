package me.hapyl.eterna.module.player.sequencer;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Represents a {@link Tune} that is played on a {@link Sequencer}.
 */
public interface Tune {
    
    /**
     * Plays this {@link Tune} to the given {@link Player}.
     *
     * @param player - The player to whom to play the tune.
     * @param volume - The volume of the sound.
     */
    void play(@NotNull Player player, float volume);
    
    /**
     * A static factory method for creating a {@link Tune}.
     *
     * <p>
     * This method plays a sound directly to a player, meaning it will follow player's location over the sound duration.
     * </p>
     *
     * @param sound - The sound from which to create a tune.
     * @param pitch - The pitch of the sound.
     * @return a new tune.
     */
    @NotNull
    static Tune create(@NotNull Sound sound, @Range(from = 0, to = 2) float pitch) {
        return new TuneImpl(sound, Math.clamp(pitch, 0.0f, 2.0f));
    }
    
}
