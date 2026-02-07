package me.hapyl.eterna.module.math.geometry;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link Drawable} that displays the given {@link Particle} for the specified {@link Player}.
 *
 * @see Drawable#playerParticle(Particle, Player)
 */
public class PlayerParticle extends AbstractDrawable {
    
    private final Player player;
    
    PlayerParticle(@NotNull Particle particle, @NotNull Player player) {
        super(particle);
        this.player = player;
    }
    
    @Override
    public void draw(@NotNull Location location) {
        this.player.spawnParticle(super.particle, location, super.count, super.offsetX, super.offsetY, super.offsetZ, super.speed, super.data);
    }
}
