package me.hapyl.spigotutils.module.particle;

import me.hapyl.spigotutils.module.util.BukkitUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Vibration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Displays a vibration particle.
 */
public class VibrationParticleBuilder extends ParticleBuilder {

    private final Vibration vibration;

    VibrationParticleBuilder(@Nonnull Location origin, @Nonnull Vibration.Destination destination, int arrivalTime) {
        super(Particle.VIBRATION);

        this.vibration = new Vibration(origin, destination, arrivalTime);
    }

    @Override
    protected <T> void display0(@Nonnull Player player, @Nonnull Location location, int count, double x, double y, double z, float speed, @Nullable T particleData) {
        super.display0(player, location, count, x, y, z, speed, vibration);
    }

    static ParticleBuilder of(@Nonnull Location from, @Nonnull Location to, int arrivalTime) {
        return new VibrationParticleBuilder(from, new Vibration.Destination.BlockDestination(to), arrivalTime);
    }

    static ParticleBuilder of(@Nonnull Location from, @Nonnull Entity to, int arrivalTime) {
        return new VibrationParticleBuilder(from, new Vibration.Destination.EntityDestination(to), arrivalTime);
    }

}
