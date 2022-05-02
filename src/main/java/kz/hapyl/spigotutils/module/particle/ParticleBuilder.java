package kz.hapyl.spigotutils.module.particle;

import kz.hapyl.spigotutils.module.annotate.NOTNULL;
import kz.hapyl.spigotutils.module.annotate.NotEmpty;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
    public void display(Location location) {
        super.worldNotNull(location);
        location.getWorld().spawnParticle(this.getParticle(),
                                          location,
                                          this.getAmount(),
                                          this.getOffX(),
                                          this.getOffY(),
                                          this.getOffZ(),
                                          this.getSpeed());
    }

    @Override
    public void display(Location location, @NOTNULL @NotEmpty Player... players) {
        super.worldNotNull(location);
        super.viewersNotEmpty(players);
        for (final Player player : players) {
            player.spawnParticle(this.getParticle(),
                                 location,
                                 this.getAmount(),
                                 this.getOffX(),
                                 this.getOffY(),
                                 this.getOffZ(),
                                 this.getSpeed());
        }
    }

    @Override
    public void displayAsync(Player... players) {
        throw new NotImplementedException("not yet implemented");
    }

}
