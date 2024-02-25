package me.hapyl.spigotutils.module.particle.wrapped;

import me.hapyl.spigotutils.module.util.Nulls;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WrappedDataTypeParticle<T> {

    private final Particle particle;

    protected WrappedDataTypeParticle(Particle particle) {
        this.particle = particle;
    }


    /**
     * Display the particle.
     *
     * @param location - Location to display at.
     * @param amount   - Number of particles to display.
     * @param x        - X Offset.
     * @param y        - Y Offset.
     * @param z        - Z Offset.
     * @param speed    - Speed of the particle if animated.
     * @param t        - Data of the particles.
     * @param player   - Players who will see particles. Keep null or empty for global particle.
     */
    public void display(Location location, int amount, double x, double y, double z, float speed, @Nonnull T t, @Nullable Player player) {
        if (player == null) {
            Nulls.runIfNotNull(location.getWorld(), world -> world.spawnParticle(particle, location, amount, x, y, z, speed, t));
        }
        else {
            player.spawnParticle(particle, location, amount, x, y, z, speed, t);
        }
    }

    public void display(Location location, int amount, double x, double y, double z, @Nonnull T t, @Nullable Player player) {
        display(location, amount, x, y, z, 1.0f, t, player);
    }

    public void display(Location location, int amount, float speed, @Nonnull T t, @Nullable Player player) {
        display(location, amount, 0.0d, 0.0d, 0.0d, speed, t, player);
    }

    public void display(Location location, int amount, @Nonnull T t, @Nullable Player player) {
        display(location, amount, 0.0d, 0.0d, 0.0d, 1.0f, t, player);
    }

    public void display(Location location, @Nonnull T t, @Nullable Player player) {
        display(location, 1, 0.0d, 0.0d, 0.0d, 1.0f, t, player);
    }

    public Particle getParticle() {
        return particle;
    }

}
