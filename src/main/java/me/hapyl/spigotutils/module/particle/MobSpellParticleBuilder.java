package me.hapyl.spigotutils.module.particle;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Displays a particle of a spell with the given color.
 */
public class MobSpellParticleBuilder extends ColoredParticleBuilder {

    private final double[] colors;

    MobSpellParticleBuilder(@Nonnull Color color, boolean ambient) {
        super(ambient ? Particle.SPELL_MOB_AMBIENT : Particle.SPELL_MOB, color, 0);

        colors = new double[] {
                magicColorNumber(color.getRed()),
                magicColorNumber(color.getGreen()),
                magicColorNumber(color.getBlue())
        };
    }

    @Override
    protected <T> void display0(@Nonnull Player player, @Nonnull Location location, int count, double x, double y, double z, float speed, @Nullable T particleData) {
        super.display0(
                player,
                location,
                0/*FIXME: Count must be 0 to display color, though I think 1.21 will break this*/,
                colors[0],
                colors[1],
                colors[2],
                speed,
                particleData
        );
    }

    private static double magicColorNumber(int v) {
        return v == 0 ? 0.0001f : v / 255f;
    }

}
