package me.hapyl.eterna.module.math.geometry;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Displays particle for specific player.
 */
public class PlayerParticle extends Draw {
    private final Player player;

    public PlayerParticle(Particle particle, Player player) {
        super(particle);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void draw(@Nonnull Location location) {
        this.player.spawnParticle(this.getParticle(), location, 1, 0, 0, 0, 0);
    }
}
