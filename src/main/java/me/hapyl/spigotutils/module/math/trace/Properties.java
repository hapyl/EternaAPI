package me.hapyl.spigotutils.module.math.trace;

import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

/**
 * Delegates to properties of a RayTrace.
 */
public class Properties {

    private double maxDistance;
    private double shift;
    private double searchRadius;

    public Properties(double maxDistance, double shift, double searchRadius) {
        this.maxDistance = maxDistance;
        this.shift = shift;
        this.searchRadius = searchRadius;
    }

    public Properties(double maxDistance, double shift) {
        this(maxDistance, shift, 0.5d);
    }

    public Properties(double maxDistance) {
        this(maxDistance, 0.5d, 0.5d);
    }

    public Properties() {
        this(24.0d, 0.5d, 0.5d);
    }

    public final void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public final void setShift(double shift) {
        this.shift = shift;
    }

    public final double getSearchRadius() {
        return searchRadius;
    }

    public final void setSearchRadius(double searchRadius) {
        this.searchRadius = searchRadius;
    }

    public final double getMaxDistance() {
        return maxDistance;
    }

    public final double getShift() {
        return shift;
    }

    public boolean predicateEntity(LivingEntity entity) {
        return entity != null && !entity.isDead();
    }

    public boolean predicateBlock(Block block) {
        return !block.getType().isOccluding();
    }

}
