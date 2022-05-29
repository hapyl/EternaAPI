package kz.hapyl.spigotutils.module.particle;

import kz.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Display mob spell particle of certain color.
 */
public class MobSpellParticleBuilder extends RedstoneParticleBuilder {

    public MobSpellParticleBuilder(Color color, boolean ambient) {
        super(color);
        this.setParticle(ambient ? Particle.SPELL_MOB_AMBIENT : Particle.SPELL_MOB);
    }

    @Override
    public void display(@Nonnull Location location) {
        Validate.notNull(location.getWorld());

        location.getWorld().spawnParticle(this.getParticle(),
                                          location.getX(),
                                          location.getY(),
                                          location.getZ(),
                                          0,
                                          this.getColor().getRed() / 255f,
                                          this.getColor().getGreen() / 255f,
                                          this.getColor().getBlue() / 255f, null
        );
    }

    @Override
    public void display(@Nonnull Location location, @Nonnull Player... players) {
        Validate.notNull(location);
        Validate.notNull(players);

        for (final Player player : players) {
            player.spawnParticle(this.getParticle(),
                                 location.getX(),
                                 location.getY(),
                                 location.getZ(),
                                 0,
                                 this.getColor().getRed() / 255f,
                                 this.getColor().getGreen() / 255f,
                                 this.getColor().getBlue() / 255f, null
            );
        }
    }
}
