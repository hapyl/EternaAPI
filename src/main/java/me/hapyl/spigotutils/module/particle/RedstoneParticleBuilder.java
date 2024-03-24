package me.hapyl.spigotutils.module.particle;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Displays a redstone dust particle of the given color.
 */
public class RedstoneParticleBuilder extends ColoredParticleBuilder {

    private final Particle.DustOptions dustOptions;

    RedstoneParticleBuilder(@Nonnull Color color, float size) {
        super(Particle.REDSTONE, color, size);

        this.dustOptions = new Particle.DustOptions(color, size);
    }

    @Override
    protected <T> void display0(@Nonnull Player player, @Nonnull Location location, int count, double x, double y, double z, float speed, @Nullable T particleData) {
        super.display0(player, location, count, x, y, z, speed, dustOptions);
    }

}
