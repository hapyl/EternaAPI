package me.hapyl.eterna.module.player.sound;

import org.bukkit.Sound;

import javax.annotation.Nonnull;

public class SoundQueueSound {

    private final Sound sound;
    private final float pitch;
    private final int delay;

    SoundQueueSound(@Nonnull Sound sound, float pitch, int delay) {
        this.sound = sound;
        this.pitch = pitch;
        this.delay = delay;
    }

    @Nonnull
    public Sound getSound() {
        return sound;
    }

    public float getPitch() {
        return pitch;
    }

    public int getDelay() {
        return delay;
    }
}
