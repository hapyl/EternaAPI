package me.hapyl.eterna.module.particle;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Displays a redstone dust particle transition from one color to another.
 */
public class DustTransitionParticleBuilder extends ColoredParticleBuilder {

    private final Particle.DustTransition dustTransition;

    DustTransitionParticleBuilder(@Nonnull Color from, @Nonnull Color to, float size) {
        super(Particle.DUST_COLOR_TRANSITION, from, size);

        this.dustTransition = new Particle.DustTransition(from, to, size);
    }

    @Override
    protected <T> void display0(@Nonnull Player player, @Nonnull Location location, int count, double x, double y, double z, float speed, @Nullable T particleData) {
        super.display0(player, location, count, x, y, z, speed, dustTransition);
    }
}
