package me.hapyl.spigotutils.module.math.gometry;

import org.bukkit.Location;
import org.bukkit.Particle;

public abstract class Draw {

    private final Particle particle;

    public Draw(Particle particle) {
        this.particle = particle;
    }

    public abstract void draw(Location location);

    public Particle getParticle() {
        return particle;
    }

}
