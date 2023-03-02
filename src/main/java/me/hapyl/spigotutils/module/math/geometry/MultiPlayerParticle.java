package me.hapyl.spigotutils.module.math.geometry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

/**
 * Displays one particle for player A and other for everyone else.
 */
public class MultiPlayerParticle extends PlayerParticle {

    private final Particle forOthers;

    public MultiPlayerParticle(Particle forPlayer, Player player, Particle forOthers) {
        super(forPlayer, player);
        this.forOthers = forOthers;
    }

    @Override
    public void draw(Location location) {
        for (final Player online : Bukkit.getOnlinePlayers()) {
            if (online == this.getPlayer()) {
                online.spawnParticle(this.getParticle(), location, 1, 0, 0, 0, 0);
            }
            else {
                online.spawnParticle(this.forOthers, location, 1, 0, 0, 0, 0);
            }
        }
    }

}
