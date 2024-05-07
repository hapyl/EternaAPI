package me.hapyl.spigotutils.module.particle;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Displays a particle of a block dust falling down.
 */
public class BlockDustParticleBuilder extends BlockBreakParticleBuilder {

    BlockDustParticleBuilder(@Nonnull Material material) {
        super(material);
        particle = Particle.FALLING_DUST;
    }

    @Override
    protected <T> void display0(@Nonnull Player player, @Nonnull Location location, int count, double x, double y, double z, float speed, @Nullable T particleData) {
        super.display0(player, location, count, x, y, z, speed, particleData);
    }
}
