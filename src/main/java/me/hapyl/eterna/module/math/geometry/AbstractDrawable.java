package me.hapyl.eterna.module.math.geometry;

import me.hapyl.eterna.module.annotate.SelfReturn;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Represents an {@link AbstractDrawable} builder-like implementation of {@link Drawable}.
 *
 * @see Drawable#worldParticle(Particle)
 * @see Drawable#playerParticle(Particle, Player)
 */
public abstract class AbstractDrawable implements Drawable {
    
    protected final Particle particle;
    
    protected int count;
    protected double offsetX;
    protected double offsetY;
    protected double offsetZ;
    protected float speed;
    protected Object data;
    
    AbstractDrawable(@NotNull Particle particle) {
        this.particle = particle;
        this.count = 1;
    }
    
    /**
     * Sets the {@code x} offset of the {@link Particle}.
     *
     * @param offsetX - The {@code x} to set.
     */
    @SelfReturn
    public Drawable setOffsetX(double offsetX) {
        this.offsetX = offsetX;
        return this;
    }
    
    /**
     * Sets the {@code y} offset of the {@link Particle}.
     *
     * @param offsetY - The {@code y} to set.
     */
    @SelfReturn
    public Drawable setOffsetY(double offsetY) {
        this.offsetY = offsetY;
        return this;
    }
    
    /**
     * Sets the {@code z} offset of the {@link Particle}.
     *
     * @param offsetZ - The {@code z} to set.
     */
    @SelfReturn
    public Drawable setOffsetZ(double offsetZ) {
        this.offsetZ = offsetZ;
        return this;
    }
    
    /**
     * Sets the {@code speed} of the {@link Particle}.
     *
     * @param speed - The {@code speed} to set.
     */
    @SelfReturn
    public Drawable setSpeed(@Range(from = 0, to = Long.MAX_VALUE) float speed) {
        this.speed = Math.max(speed, 0.0f);
        return this;
    }
    
    /**
     * Sets an optional {@code data} for the {@link Particle}.
     * <p>
     * Some particles, such as {@link Particle#EFFECT}, require an additional data to be displayed and will throw an exception if no data was passed.
     * </p>
     *
     * @param data - The {@code data} to set.
     */
    @SelfReturn
    public Drawable setData(Object data) {
        this.data = data;
        return this;
    }
}
