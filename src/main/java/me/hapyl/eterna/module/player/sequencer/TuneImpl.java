package me.hapyl.eterna.module.player.sequencer;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public final class TuneImpl implements Tune {
    
    private final Sound sound;
    private final float pitch;
    
    TuneImpl(@NotNull Sound sound, float pitch) {
        this.sound = sound;
        this.pitch = pitch;
    }
    
    @Override
    public void play(@NotNull Player player, float volume) {
        player.playSound(player, sound, volume, pitch);
    }
}
