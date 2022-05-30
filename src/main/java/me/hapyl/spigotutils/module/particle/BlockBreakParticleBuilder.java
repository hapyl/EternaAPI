package me.hapyl.spigotutils.module.particle;

import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Display particle of a block breaking.
 */
public class BlockBreakParticleBuilder extends ParticleBuilder {

    private final Material block;

    /**
     * Display particle of a block breaking.
     *
     * @param block - Material.
     * @throws IllegalArgumentException if material is not a block.
     */
    public BlockBreakParticleBuilder(Material block) {
        super(Particle.BLOCK_CRACK);
        if (!block.isBlock()) {
            throw new IllegalArgumentException("Material must be block");
        }
        this.block = block;
    }

    @Override
    public void display(@Nonnull Location location) {
        Validate.notNull(location.getWorld());

        location.getWorld().spawnParticle(
                this.getParticle(),
                location,
                this.getAmount(),
                this.getOffX(),
                this.getOffY(),
                this.getOffZ(),
                this.getSpeed(),
                this.block.createBlockData()
        );
    }

    @Override
    public void display(@Nonnull Location location, @Nonnull Player... players) {
        Validate.notNull(location);
        Validate.notNull(players);

        for (final Player player : players) {
            player.spawnParticle(
                    this.getParticle(),
                    player.getLocation(),
                    this.getAmount(),
                    this.getOffX(),
                    this.getOffY(),
                    this.getOffZ(),
                    this.getSpeed(),
                    this.block.createBlockData()
            );
        }
    }


}
