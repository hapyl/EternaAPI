package me.hapyl.spigotutils.module.particle.wrapped;

import me.hapyl.spigotutils.module.player.PlayerLib;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public class WrappedParticle {

    private final Particle particle;

    protected WrappedParticle(Particle particle) {
        this.particle = particle;
    }

    /**
     * Display the particle.
     *
     * @param location - Location to display at.
     * @param amount   - Amount of particles to display.
     * @param x        - X Offset.
     * @param y        - Y Offset.
     * @param z        - Z Offset.
     * @param speed    - Speed of the particle if animated.
     * @param players  - Players who will see particles. Keep null or empty for global particle.
     */
    public void display(Location location, int amount, double x, double y, double z, float speed, @Nullable Player... players) {
        if (players == null || players.length == 0) {
            PlayerLib.spawnParticle(location, particle, amount, x, y, z, speed);
        }
        else {
            for (Player player : players) {
                PlayerLib.spawnParticle(player, location, particle, amount, x, y, z, speed);
            }
        }
    }

    public void display(Location location, int amount, double x, double y, double z, @Nullable Player... players) {
        display(location, amount, x, y, z, 1.0f, players);
    }

    public void display(Location location, int amount, float speed, @Nullable Player... players) {
        display(location, amount, 0.0d, 0.0d, 0.0d, speed, players);
    }

    public void display(Location location, int amount, @Nullable Player... players) {
        display(location, amount, 0.0d, 0.0d, 0.0d, 1.0f, players);
    }

    public void display(Location location, @Nullable Player... players) {
        display(location, 1, 0.0d, 0.0d, 0.0d, 1.0f, players);
    }

    public Particle getParticle() {
        return particle;
    }
}
