package me.hapyl.spigotutils.module.particle;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Display a particle of a block breaking.
 */
public class BlockBreakParticleBuilder extends ParticleBuilder {

    private final BlockData block;

    BlockBreakParticleBuilder(@Nonnull Material material) {
        super(Particle.BLOCK);

        if (!material.isBlock()) {
            throw new IllegalArgumentException("Material must be a block, %s isn't!".formatted(material));
        }

        this.block = material.createBlockData();
    }

    @Override
    protected <T> void display0(@Nonnull Player player, @Nonnull Location location, int count, double x, double y, double z, float speed, @Nullable T particleData) {
        super.display0(player, location, count, x, y, z, speed, block);
    }
}
