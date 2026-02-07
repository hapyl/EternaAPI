package me.hapyl.eterna.module.particle;

import me.hapyl.eterna.module.annotate.SelfReturn;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a {@link ParticleBuilder} builder-like holder of {@code type-based} {@link Particle}.
 *
 * <p>Intended to be used with particles that require an extra {@code data}, but also supports {@link #particle(Particle)}.</p>
 */
public class ParticleBuilder {
    
    protected final Particle particle;
    protected final Object data;
    
    private int count;
    private double offsetX;
    private double offsetY;
    private double offsetZ;
    private float speed;
    
    ParticleBuilder(@NotNull Particle particle, @Nullable Object data) {
        this.particle = particle;
        this.data = data;
        
        // Validate particle type
        final Class<?> particleDataType = particle.getDataType();
        final boolean hasParticleData = particleDataType == Void.class;
        
        if (hasParticleData && data != null) {
            throw new IllegalArgumentException("Particle `%s` does not require any data, but `%s` was given!".formatted(particle.name(), data.toString()));
        }
        else if (!hasParticleData && data != null && !particleDataType.isAssignableFrom(data.getClass())) {
            throw new IllegalArgumentException("Particle `%s` uses `%s` data type, not `%s`!".formatted(particle.name(), particleDataType.getSimpleName(), data.getClass().getSimpleName()));
        }
        
        this.count = 1;
        this.offsetX = 0;
        this.offsetY = 0;
        this.offsetZ = 0;
        this.speed = 0f;
    }
    
    /**
     * Sets the {@link Particle} count of this {@link ParticleBuilder}.
     *
     * <p>
     * Some particles, like {@link Particle#FLAME} may accept {@code 0} count as a magic value, which will change the {@code offset} values to be directional, rather than the offset.
     * </p>
     *
     * @param count - The new count to set.
     */
    @SelfReturn
    public ParticleBuilder count(final int count) {
        this.count = Math.max(0, count);
        return this;
    }
    
    /**
     * Sets the {@code x} offset of this {@link ParticleBuilder}.
     *
     * @param offsetX - The {@code x} offset.
     */
    @SelfReturn
    public ParticleBuilder offsetX(final double offsetX) {
        this.offsetX = offsetX;
        return this;
    }
    
    /**
     * Sets the {@code y} offset of this {@link ParticleBuilder}.
     *
     * @param offsetY - The {@code y} offset.
     */
    @SelfReturn
    public ParticleBuilder offsetY(final double offsetY) {
        this.offsetY = offsetY;
        return this;
    }
    
    /**
     * Sets the {@code y} offset of this {@link ParticleBuilder}.
     *
     * @param offsetZ - The {@code y} offset.
     */
    @SelfReturn
    public ParticleBuilder offsetZ(final double offsetZ) {
        this.offsetZ = offsetZ;
        return this;
    }
    
    /**
     * Sets the {@code speed} of this {@link ParticleBuilder}.
     *
     * @param speed - The new speed to set.
     */
    @SelfReturn
    public ParticleBuilder speed(final float speed) {
        this.speed = speed;
        return this;
    }
    
    /**
     * Displays the {@link Particle} at the given {@link Location} globally.
     *
     * @param location - The location to display the particles at.
     */
    public void display(@NotNull Location location) {
        location.getWorld().spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, speed, data);
    }
    
    /**
     * Displays the {@link Particle} at the given {@link Location} for the given {@link Player}.
     *
     * @param player   - The player for whom to display the particle.
     * @param location - The location to display the particle at.
     */
    public void display(@NotNull Player player, @NotNull Location location) {
        player.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, speed, data);
    }
    
    /**
     * A static factory method for creating a non-type based {@link ParticleBuilder}.
     *
     * @param particle - The non-type based particle.
     * @return a new {@link ParticleBuilder}.
     * @throws IllegalArgumentException if the given particle requires a type.
     */
    @NotNull
    public static ParticleBuilder particle(@NotNull Particle particle) {
        return new ParticleBuilder(particle, null);
    }
    
    /**
     * A static factory method for creating {@link Particle#EFFECT} {@link ParticleBuilder}.
     *
     * @param color - The particle color.
     * @param power - The power of the particle.
     *              <p>Positive values make the particle go down, while negative values make it go down.</p>
     * @return a new {@link ParticleBuilder}.
     */
    @NotNull
    public static ParticleBuilder effect(@NotNull Color color, final float power) {
        return new ParticleBuilder(Particle.EFFECT, new Particle.Spell(color, power));
    }
    
    /**
     * A static factory method for creating {@link Particle#INSTANT_EFFECT} {@link ParticleBuilder}.
     *
     * @param color - The particle color.
     * @param power - The power of the particle.
     *              <p>Positive values make the particle go down, while negative values make it go down.</p>
     * @return a new {@link ParticleBuilder}.
     */
    @NotNull
    public static ParticleBuilder instantEffect(@NotNull Color color, final float power) {
        return new ParticleBuilder(Particle.INSTANT_EFFECT, new Particle.Spell(color, power));
    }
    
    /**
     * A static factory method for creating {@link Particle#ENTITY_EFFECT} {@link ParticleBuilder}.
     *
     * @param color - The particle color.
     * @return a new {@link ParticleBuilder}.
     */
    @NotNull
    public static ParticleBuilder entityEffect(@NotNull Color color) {
        return new ParticleBuilder(Particle.ENTITY_EFFECT, color);
    }
    
    /**
     * A static factory method for creating {@link Particle#DUST} {@link ParticleBuilder}.
     *
     * @param color - The particle color.
     * @param size  - The size of the particle.
     * @return a new {@link ParticleBuilder}.
     */
    @NotNull
    public static ParticleBuilder dust(@NotNull Color color, final float size) {
        return new ParticleBuilder(Particle.DUST, new Particle.DustOptions(color, size));
    }
    
    /**
     * A static factory method for creating {@link Particle#ITEM} {@link ParticleBuilder}.
     *
     * @param itemStack - The item stack used for the particle.
     * @return a new {@link ParticleBuilder}.
     */
    @NotNull
    public static ParticleBuilder item(@NotNull ItemStack itemStack) {
        return new ParticleBuilder(Particle.ITEM, itemStack);
    }
    
    /**
     * A static factory method for creating {@link Particle#BLOCK} {@link ParticleBuilder}.
     *
     * @param material - The material used for the particle.
     * @return a new {@link ParticleBuilder}.
     * @throws IllegalArgumentException if the given material is not a block.
     */
    @NotNull
    public static ParticleBuilder block(@NotNull Material material) {
        return new ParticleBuilder(Particle.BLOCK, createBlockData(material));
    }
    
    /**
     * A static factory method for creating {@link Particle#DRAGON_BREATH} {@link ParticleBuilder}.
     *
     * @param power - The power of the particle.
     * @return a new {@link ParticleBuilder}.
     */
    @NotNull
    public static ParticleBuilder dragonBreath(final float power) {
        return new ParticleBuilder(Particle.DRAGON_BREATH, power);
    }
    
    /**
     * A static factory method for creating {@link Particle#FALLING_DUST} {@link ParticleBuilder}.
     *
     * @param material - The material used for the particle.
     * @return a new {@link ParticleBuilder}.
     * @throws IllegalArgumentException if the given material is not a block.
     */
    @NotNull
    public static ParticleBuilder fallingDust(@NotNull Material material) {
        return new ParticleBuilder(Particle.FALLING_DUST, createBlockData(material));
    }
    
    /**
     * A static factory method for creating {@link Particle#FLASH} {@link ParticleBuilder}.
     *
     * @param color - The particle color.
     * @return a new {@link ParticleBuilder}.
     */
    @NotNull
    public static ParticleBuilder flash(@NotNull Color color) {
        return new ParticleBuilder(Particle.FLASH, color);
    }
    
    /**
     * A static factory method for creating {@link Particle#DUST_COLOR_TRANSITION} {@link ParticleBuilder}.
     *
     * @param from - The {@code from} transition color.
     * @param to   - The {@code to} transition color.
     * @param size - The size of the particle.
     * @return a new {@link ParticleBuilder}.
     */
    @NotNull
    public static ParticleBuilder dustColorTransition(@NotNull Color from, @NotNull Color to, final float size) {
        return new ParticleBuilder(Particle.DUST_COLOR_TRANSITION, new Particle.DustTransition(from, to, Math.clamp(size, 0.01f, 4.0f)));
    }
    
    /**
     * A static factory method for creating {@link Particle#VIBRATION} {@link ParticleBuilder}.
     *
     * @param destination - The destination location.
     * @param arrivalTime - The arrival time, in ticks.
     * @return a new {@link ParticleBuilder}.
     */
    @NotNull
    public static ParticleBuilder vibration(@NotNull Location destination, final int arrivalTime) {
        return vibration0(new Vibration.Destination.BlockDestination(destination), arrivalTime);
    }
    
    /**
     * A static factory method for creating {@link Particle#VIBRATION} {@link ParticleBuilder}.
     *
     * @param entity      - The entity destination.
     * @param arrivalTime - The arrival time, in ticks.
     * @return a new {@link ParticleBuilder}.
     */
    @NotNull
    public static ParticleBuilder vibration(@NotNull Entity entity, final int arrivalTime) {
        return vibration0(new Vibration.Destination.EntityDestination(entity), arrivalTime);
    }
    
    /**
     * A static factory method for creating {@link Particle#SCULK_CHARGE} {@link ParticleBuilder}.
     *
     * @param angleRad - The angle in radians.
     * @return a new {@link ParticleBuilder}.
     */
    @NotNull
    public static ParticleBuilder sculkCharge(final float angleRad) {
        return new ParticleBuilder(Particle.SCULK_CHARGE, angleRad);
    }
    
    /**
     * A static factory method for creating {@link Particle#TINTED_LEAVES} {@link ParticleBuilder}.
     *
     * @param color - The particle color.
     * @return a new {@link ParticleBuilder}.
     */
    @NotNull
    public static ParticleBuilder tintedLeaves(@NotNull Color color) {
        return new ParticleBuilder(Particle.TINTED_LEAVES, color);
    }
    
    /**
     * A static factory method for creating {@link Particle#DUST_PILLAR} {@link ParticleBuilder}.
     *
     * @param material - The material used for the particle.
     * @return a new {@link ParticleBuilder}.
     * @throws IllegalArgumentException if the given material is not a block.
     */
    @NotNull
    public static ParticleBuilder dustPillar(@NotNull Material material) {
        return new ParticleBuilder(Particle.DUST_PILLAR, createBlockData(material));
    }
    
    /**
     * A static factory method for creating {@link Particle#BLOCK_CRUMBLE} {@link ParticleBuilder}.
     *
     * @param material - The material used for the particle.
     * @return a new {@link ParticleBuilder}.
     * @throws IllegalArgumentException if the given material is not a block.
     */
    @NotNull
    public static ParticleBuilder blockCrumble(@NotNull Material material) {
        return new ParticleBuilder(Particle.BLOCK_CRUMBLE, createBlockData(material));
    }
    
    /**
     * A static factory method for creating {@link Particle#TRAIL} {@link ParticleBuilder}.
     *
     * @param destination - The destination location.
     * @param color       - The particle color.
     * @param duration    - The duration, in ticks.
     * @return a new {@link ParticleBuilder}.
     */
    @NotNull
    public static ParticleBuilder trail(@NotNull Location destination, @NotNull Color color, final int duration) {
        return new ParticleBuilder(Particle.TRAIL, new Particle.Trail(destination, color, duration));
    }
    
    /**
     * A static factory method for creating {@link Particle#BLOCK_MARKER} {@link ParticleBuilder}.
     *
     * @param material - The material used for the particle.
     * @return a new {@link ParticleBuilder}.
     * @throws IllegalArgumentException if the given material is not a block.
     */
    @NotNull
    public static ParticleBuilder blockMarker(@NotNull Material material) {
        return new ParticleBuilder(Particle.BLOCK_MARKER, createBlockData(material));
    }
    
    @NotNull
    private static ParticleBuilder vibration0(@NotNull Vibration.Destination destination, final int arrivalTime) {
        return new ParticleBuilder(Particle.VIBRATION, new Vibration(destination, arrivalTime));
    }
    
    @NotNull
    private static BlockData createBlockData(@NotNull Material material) {
        if (!material.isBlock()) {
            throw new IllegalArgumentException("Material must be a block!");
        }
        
        return material.createBlockData();
    }
    
}
