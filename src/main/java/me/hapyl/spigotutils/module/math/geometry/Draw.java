package me.hapyl.spigotutils.module.math.geometry;

import org.bukkit.Particle;
import org.bukkit.entity.Player;

public abstract class Draw implements Drawable {

    private final Particle particle;

    public static WorldParticle worldParticle(Particle particle, float offsetX, float offsetY, float offsetZ, float speed) {
        return new WorldParticle(particle, offsetX, offsetY, offsetZ, speed);
    }

    public static PlayerParticle playerParticle(Particle particle, Player player) {
        return new PlayerParticle(particle, player);
    }

    public static MultiPlayerParticle multiPlayerParticle(Particle forPlayer, Player player, Particle forOthers) {
        return new MultiPlayerParticle(forPlayer, player, forOthers);
    }

    public Draw(Particle particle) {
        this.particle = particle;
    }

    public Particle getParticle() {
        return particle;
    }

}
