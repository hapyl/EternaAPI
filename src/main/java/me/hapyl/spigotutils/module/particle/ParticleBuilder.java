package me.hapyl.spigotutils.module.particle;

import me.hapyl.spigotutils.module.particle.wrapped.Particles;
import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * -> {@link Particles}
 */
public class ParticleBuilder extends AbstractParticleBuilder {

    public ParticleBuilder(Particle particle) {
        super(particle);
    }

    public static ParticleBuilder general(Particle particle) {
        return new ParticleBuilder(particle);
    }

    public static ParticleBuilder redstoneDust(Color color) {
        return new RedstoneParticleBuilder(color);
    }

    public static ParticleBuilder blockBreak(Material block) {
        return new BlockBreakParticleBuilder(block);
    }

    public static ParticleBuilder blockDust(Material block) {
        return new BlockDustParticleBuilder(block);
    }

    public static ParticleBuilder mobSpell(Color color, boolean ambient) {
        return new MobSpellParticleBuilder(color, ambient);
    }

    public static ParticleBuilder itemBreak(ItemStack stack) {
        return new ItemBreakParticleBuilder(stack);
    }

    @Override
    public void display(@Nonnull Location location) {
        Validate.notNull(location.getWorld());

        location.getWorld()
                .spawnParticle(
                        this.getParticle(),
                        location,
                        this.getAmount(),
                        this.getOffX(),
                        this.getOffY(),
                        this.getOffZ(),
                        this.getSpeed()
                );
    }

    @Override
    public void display(@Nonnull Location location, @Nonnull Player player) {
        Validate.notNull(location);
        Validate.notNull(player);

        player.spawnParticle(
                this.getParticle(),
                location,
                this.getAmount(),
                this.getOffX(),
                this.getOffY(),
                this.getOffZ(),
                this.getSpeed()
        );
    }

}
