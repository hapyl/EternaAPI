package me.hapyl.spigotutils.module.math.geometry;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

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
    public void draw(Location location) {
        this.player.spawnParticle(this.getParticle(), location, 1, 0, 0, 0, 0);
    }
}
