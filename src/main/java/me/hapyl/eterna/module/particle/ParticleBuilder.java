package me.hapyl.eterna.module.particle;

import me.hapyl.eterna.module.player.PlayerLib;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * Allows displaying particles.
 * <br>
 * Mainly used for "special" particles, such as {@link Particle#BLOCK_DUST} or {@link Particle#SPELL_MOB}.
 *
 * @see #normal(Particle)
 * @see #redstoneDust(Color, int)
 * @see #blockBreak(Material)
 * @see #blockDust(Material)
 * @see #mobSpell(Color, boolean)
 * @see #itemBreak(ItemStack)
 */
public class ParticleBuilder {

    @Nonnull
    protected Particle particle;

    public ParticleBuilder(@Nonnull Particle particle) {
        this.particle = particle;
    }

    /**
     * Displays the particle at the given {@link Location} for each online player.
     *
     * @param location - Location to display at.
     */
    public void display(@Nonnull Location location) {
        forEach(player -> display(player, location, 1, 0, 0, 0, 1.0f));
    }

    /**
     * Displays a particle at the given {@link Location} for each online player.
     *
     * @param location - Location to display at.
     * @param count    - Count or number of particles.
     */
    public void display(@Nonnull Location location, int count) {
        forEach(player -> display(player, location, count, 0, 0, 0, 1.0f));
    }

    /**
     * Displays a particle at the given {@link Location} for each online player.
     *
     * @param location - Location to display at.
     * @param count    - Count or number of particles.
     * @param speed    - Speed.
     */
    public void display(@Nonnull Location location, int count, float speed) {
        forEach(player -> display(player, location, count, 0, 0, 0, speed));
    }

    /**
     * Displays a particle at the given {@link Location} for each online player.
     *
     * @param location - Location to display at.
     * @param count    - Count or number of particles.
     * @param x        - X offset.
     * @param y        - Y offset.
     * @param z        - Z offset.
     * @param speed    - Speed.
     */
    public void display(@Nonnull Location location, int count, double x, double y, double z, float speed) {
        forEach(player -> display(player, location, count, x, y, z, speed, null));
    }

    /**
     * Displays a particle at the given {@link Location} for each online player.
     *
     * @param location - Location to display at.
     * @param count    - Count or number of particles.
     * @param x        - X offset.
     * @param y        - Y offset.
     * @param z        - Z offset.
     * @param speed    - Speed.
     * @param data     - Extra data if applicable to this particle.
     */
    public <T> void display(@Nonnull Location location, int count, double x, double y, double z, float speed, @Nullable T data) {
        forEach(player -> display0(player, location, count, x, y, z, speed, data));
    }

    /**
     * Displays a particle at the given {@link Location} for the given {@link Player}.
     *
     * @param player   - Player.
     * @param location - Location to display at.
     */
    public void display(@Nonnull Player player, @Nonnull Location location) {
        display(player, location, 1, 0, 0, 0, 1.0f);
    }

    /**
     * Displays a particle at the given {@link Location} for the given {@link Player}.
     *
     * @param player   - Player.
     * @param location - Location to display at.
     * @param count    - Count or number of particles.
     */
    public void display(@Nonnull Player player, @Nonnull Location location, int count) {
        display(player, location, count, 0, 0, 0, 1.0f);
    }

    /**
     * Displays a particle at the given {@link Location} for the given {@link Player}.
     *
     * @param player   - Player.
     * @param location - Location to display at.
     * @param count    - Count or number of particles.
     * @param speed    - Speed.
     */
    public void display(@Nonnull Player player, @Nonnull Location location, int count, float speed) {
        display(player, location, count, 0, 0, 0, speed);
    }

    /**
     * Displays a particle at the given {@link Location} for the given {@link Player}.
     *
     * @param player   - Player.
     * @param location - Location to display at.
     * @param count    - Count or number of particles.
     * @param x        - X offset.
     * @param y        - Y offset.
     * @param z        - Z offset.
     * @param speed    - Speed.
     */
    public void display(@Nonnull Player player, @Nonnull Location location, int count, double x, double y, double z, float speed) {
        display(player, location, count, x, y, z, speed, null);
    }

    /**
     * Displays a particle at the given {@link Location} for the given {@link Player}.
     *
     * @param player   - Player.
     * @param location - Location to display at.
     * @param count    - Count or number of particles.
     * @param x        - X offset.
     * @param y        - Y offset.
     * @param z        - Z offset.
     * @param speed    - Speed.
     * @param data     - Extra data if applicable to this particle.
     */
    public <T> void display(@Nonnull Player player, @Nonnull Location location, int count, double x, double y, double z, float speed, @Nullable T data) {
        display0(player, location, count, x, y, z, speed, data);
    }

    /**
     * Super display method.
     *
     * @implNote Implementation must override this method to accurately spawn the particle.
     * One example of this is {@link MobSpellParticleBuilder}.
     */
    protected <T> void display0(@Nonnull Player player, @Nonnull Location location, int count, double x, double y, double z, float speed, @Nullable T particleData) {
        PlayerLib.spawnParticle(player, location, particle, count, 0, 0, 0, speed, particleData);
    }

    /**
     * Creates a normal particle builder.
     *
     * @param particle - Particle.
     */
    public static ParticleBuilder normal(@Nonnull Particle particle) {
        return new ParticleBuilder(particle);
    }

    /**
     * Creates a {@link RedstoneParticleBuilder}.
     *
     * @param color - Color of a particle.
     * @param size  - Size.
     */
    public static ParticleBuilder redstoneDust(@Nonnull Color color, int size) {
        return new RedstoneParticleBuilder(color, size);
    }

    /**
     * Creates a {@link BlockBreakParticleBuilder}.
     *
     * @param material - Block to display the break particle of.
     * @throws IllegalArgumentException if the given material is not a block.
     */
    public static ParticleBuilder blockBreak(@Nonnull Material material) {
        return new BlockBreakParticleBuilder(material);
    }

    /**
     * Creates a {@link BlockDustParticleBuilder}.
     *
     * @param material - Block to display the dust particle of.
     * @throws IllegalArgumentException if the given material is not a block.
     */
    public static ParticleBuilder blockDust(@Nonnull Material material) {
        return new BlockDustParticleBuilder(material);
    }

    /**
     * Creates a {@link MobSpellParticleBuilder}.
     *
     * @param color   - Color of a particle.
     * @param ambient - Is ambient?
     *                Ambient particles are half-transparent.
     */
    public static ParticleBuilder mobSpell(@Nonnull Color color, boolean ambient) {
        return new MobSpellParticleBuilder(color, ambient);
    }

    /**
     * Creates a {@link ItemBreakParticleBuilder}.
     *
     * @param stack - Item to display the break of.
     */
    public static ParticleBuilder itemBreak(@Nonnull ItemStack stack) {
        return new ItemBreakParticleBuilder(stack);
    }

    /**
     * Creates a {@link DustTransitionParticleBuilder}.
     *
     * @param fromColor - Color to transition from.
     * @param toColor   - Color to transition to.
     * @param size      - Size of the particle.
     */
    public static ParticleBuilder dustTransition(@Nonnull Color fromColor, @Nonnull Color toColor, float size) {
        return new DustTransitionParticleBuilder(fromColor, toColor, size);
    }

    /**
     * Creates a {@link VibrationParticleBuilder}.
     *
     * @param from        - Origin.
     * @param to          - Destination.
     * @param arrivalTime - Arrival time in ticks.
     */
    public static ParticleBuilder vibration(@Nonnull Location from, @Nonnull Location to, int arrivalTime) {
        return VibrationParticleBuilder.of(from, to, arrivalTime);
    }

    /**
     * Creates a {@link VibrationParticleBuilder}.
     *
     * @param from        - Origin.
     * @param to          - Destination.
     * @param arrivalTime - Arrival time in ticks.
     */
    public static ParticleBuilder vibration(@Nonnull Location from, @Nonnull Entity to, int arrivalTime) {
        return VibrationParticleBuilder.of(from, to, arrivalTime);
    }

    /**
     * Creates a {@link BlockMarkerParticleBuilder}.
     *
     * @param material - Block to display the dust particle of.
     * @throws IllegalArgumentException if the given material is not a block.
     */
    public static ParticleBuilder blockMarker(@Nonnull Material material) {
        return new BlockMarkerParticleBuilder(material);
    }

    private static void forEach(Consumer<Player> consumer) {
        Bukkit.getOnlinePlayers().forEach(consumer);
    }

}
