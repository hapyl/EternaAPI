package me.hapyl.eterna.module.locaiton;

import org.bukkit.Location;
import org.bukkit.util.BoundingBox;

import javax.annotation.Nonnull;

public class Position extends BoundingBox {

    public Position() {
        super();
    }

    public Position(double x1, double y1, double z1, double x2, double y2, double z2) {
        super(x1, y1, z1, x2, y2, z2);
    }

    public boolean contains(@Nonnull Location location) {
        return super.contains(location.toVector());
    }

    @Override
    public String toString() {
        return "%.0f, %.0f, %.0f".formatted(getCenterX(), getCenterY(), getCenterZ());
    }
}
