package me.hapyl.spigotutils.module.particle;

import org.bukkit.Color;
import org.bukkit.Particle;

import javax.annotation.Nonnull;


public class ColoredParticleBuilder extends ParticleBuilder {

    protected final Color color;
    protected final float size;

    ColoredParticleBuilder(@Nonnull Particle particle, @Nonnull Color color, float size) {
        super(particle);

        this.color = color;
        this.size = size;
    }
}
