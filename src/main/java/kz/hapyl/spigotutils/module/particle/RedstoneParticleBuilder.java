package kz.hapyl.spigotutils.module.particle;

import kz.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Creates a Redstone dust particle with certain color.
 */
public class RedstoneParticleBuilder extends ParticleBuilder {

    private final Color color;

    public RedstoneParticleBuilder(Color color) {
        super(Particle.REDSTONE);
        this.color = color;
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
                this.fetchColor()
        );
    }

    @Override
    public void display(@Nonnull Location location, @Nonnull Player... players) {
        Validate.notNull(location);
        Validate.notNull(players);

        for (final Player player : players) {
            player.spawnParticle(
                    this.getParticle(),
                    location,
                    this.getAmount(),
                    this.getOffX(),
                    this.getOffY(),
                    this.getOffZ(),
                    this.getSpeed(),
                    this.fetchColor()
            );
        }
    }

    private Particle.DustOptions fetchColor() {
        return new Particle.DustOptions(this.color, this.getAmount());
    }

    public Color getColor() {
        return color;
    }

}
