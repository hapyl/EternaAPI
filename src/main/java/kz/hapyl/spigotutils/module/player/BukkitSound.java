package kz.hapyl.spigotutils.module.player;

import org.bukkit.Sound;

class BukkitSound {

    private final Sound sound;
    private final float pitch;
    private final int delay;

    BukkitSound(Sound sound, float pitch, int delay) {
        this.sound = sound;
        this.pitch = pitch;
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }

    public float getPitch() {
        return pitch;
    }

    public Sound getSound() {
        return sound;
    }
}
