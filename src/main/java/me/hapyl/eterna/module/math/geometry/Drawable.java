package me.hapyl.eterna.module.math.geometry;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an object that may draw something at a {@link Location}.
 */
public interface Drawable {
    
    /**
     * Draws at the given {@link Location}.
     *
     * @param location - The location to draw at.
     */
    void draw(@NotNull Location location);
    
    
    /**
     * A static factory method for creating a {@link WorldParticle}, which is displayed globally.
     *
     * @param particle - The particle to display.
     * @return a new {@link WorldParticle}.
     */
    @NotNull
    static WorldParticle worldParticle(@NotNull Particle particle) {
        return new WorldParticle(particle);
    }
    
    /**
     * A static factory method for creating a {@link PlayerParticle}, which displays the given {@link Particle} for the specified {@link Player}.
     *
     * @param particle - The particle to display.
     * @param player   - The player for whom to display the particle.
     * @return a new {@link PlayerParticle}.
     */
    @NotNull
    static PlayerParticle playerParticle(@NotNull Particle particle, @NotNull Player player) {
        return new PlayerParticle(particle, player);
    }
}
