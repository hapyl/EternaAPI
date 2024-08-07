package me.hapyl.eterna.module.player.synthesizer;

import me.hapyl.eterna.module.math.Numbers;
import me.hapyl.eterna.module.player.PlayerLib;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public record Tune(Sound sound, float pitch) {

    public Tune(Sound sound, float pitch) {
        this.sound = sound;
        this.pitch = Numbers.clamp(pitch, 0.0f, 2.0f);
    }

    public void play(@Nonnull Player player) {
        play(player, player.getLocation());
    }

    public void play(@Nonnull Player player, @Nonnull Location location) {
        PlayerLib.playSound(player, location, sound, pitch);
    }

}
