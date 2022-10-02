package me.hapyl.spigotutils.module.player.synthesizer;

import me.hapyl.spigotutils.module.math.Numbers;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Collection;

public class Tune {

    private final Sound sound;
    private final float pitch;

    public Tune(Sound sound, float pitch) {
        this.sound = sound;
        this.pitch = Numbers.clamp(pitch, 0.0f, 2.0f);
    }

    public void play(Player player, Player... other) {
        play(player);
        for (Player player1 : other) {
            play(player1);
        }
    }

    public void play(Player player) {
        player.playSound(player, sound, 2, pitch);
    }

    public void play(Collection<Player> players) {
        players.forEach(this::play);
    }

    public Sound getSound() {
        return sound;
    }

    public float getPitch() {
        return pitch;
    }
}
