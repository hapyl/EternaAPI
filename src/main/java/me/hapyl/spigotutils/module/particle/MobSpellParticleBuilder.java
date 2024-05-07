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

    MobSpellParticleBuilder(@Nonnull Color color, boolean ambient) {
        super(Particle.ENTITY_EFFECT, ambient ? color.setAlpha(127) : color, 1);
    }

    @Override
    protected <T> void display0(@Nonnull Player player, @Nonnull Location location, int count, double x, double y, double z, float speed, @Nullable T particleData) {
        super.display0(
                player,
                location,
                count,
                x,
                y,
                z,
                speed,
                color
        );
    }

}
