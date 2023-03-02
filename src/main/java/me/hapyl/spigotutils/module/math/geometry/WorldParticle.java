package me.hapyl.spigotutils.module.math.geometry;

import org.bukkit.Location;
import org.bukkit.Particle;

import javax.annotation.Nonnull;

public class WorldParticle extends Draw {

    private final int amount;
    private final double x, y, z;
    private final float speed;

    public WorldParticle(Particle particle) {
        this(particle, 0.0d, 0.0d, 0.0d, 0.0f);
    }

    public WorldParticle(Particle particle, double x, double y, double z, float speed) {
        this(particle, 1, x, y, z, speed);
    }

    public WorldParticle(Particle particle, int amount, double x, double y, double z, float speed) {
        super(particle);
        this.amount = amount;
        this.x = x;
        this.y = y;
        this.z = z;
        this.speed = speed;
    }

    @Override
    public void draw(@Nonnull Location location) {
        if (location.getWorld() == null) {
            return;
        }
        location.getWorld().spawnParticle(this.getParticle(), location, amount, x, y, z, speed);
    }
}
