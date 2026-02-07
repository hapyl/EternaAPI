package me.hapyl.eterna.module.math.geometry;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link Drawable} that displays the given {@link Particle} globally.
 *
 * @see Drawable#worldParticle(Particle)
 */
public class WorldParticle extends AbstractDrawable {
    
    WorldParticle(@NotNull Particle particle) {
        super(particle);
    }
    
    @Override
    public void draw(@NotNull Location location) {
        location.getWorld().spawnParticle(super.particle, location, super.count, super.offsetX, super.offsetY, super.offsetZ, super.speed, super.data);
    }
}
