package kz.hapyl.spigotutils.module.particle;

import kz.hapyl.spigotutils.module.annotate.NotEmpty;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class RedstoneParticleBuilder extends ParticleBuilder {

    private final Color color;

    public RedstoneParticleBuilder(Color color) {
        super(Particle.REDSTONE);
        this.color = color;
    }

    @Override
    public void display(Location location) {
        super.worldNotNull(location);
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
    public void display(Location location, @Nonnull @NotEmpty Player... players) {
        super.worldNotNull(location);
        super.viewersNotEmpty(players);
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

    @Override
    public void displayAsync(Player... players) {
        super.displayAsync(players);
    }

    private Particle.DustOptions fetchColor() {
        return new Particle.DustOptions(this.color, this.getAmount());
    }

    public Color getColor() {
        return color;
    }

}
