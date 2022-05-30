package me.hapyl.spigotutils.module.math;

import me.hapyl.spigotutils.module.annotate.AsyncSafe;
import me.hapyl.spigotutils.module.math.gometry.Draw;
import me.hapyl.spigotutils.module.math.gometry.Quality;
import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * A geometry class that allows to draw simple stuff.
 */
public class Geometry {

    public static final Location CENTER = new Location(Bukkit.getWorlds().get(0), 0, Bukkit.getWorlds().get(0).getHighestBlockYAt(0, 0), 0);
    protected static final double TWO_PI = (Math.PI * 2);

    /**
     * Draws a circle around center.
     *
     * @param center  - Center of the circle.
     * @param radius  - Radius of the circle.
     * @param quality - Quality of the circle.
     * @param draw    - Drawable.
     * @throws NullPointerException if location, quality or drawable is null.
     */
    @AsyncSafe
    public static void drawCircle(@Nonnull Location center, double radius, @Nonnull Quality quality, @Nonnull Draw draw) {
        Validate.notNull(center, "location cannot be null");
        Validate.notNull(draw, "draw particle cannot be null");
        Validate.notNull(quality, "quality cannot be null");

        for (double i = 0.0d; i < TWO_PI; i += quality.getStep()) {
            double x = (radius * Math.sin(i));
            double z = (radius * Math.cos(i));
            center.add(x, 0, z);
            draw.draw(center);
            center.subtract(x, 0, z);
        }
    }

    /**
     * Draws a line between two points.
     *
     * @param start - Start of the line.
     * @param end   - End of the line.
     * @param step  - How much to move each move; smaller values results in higher quality.
     * @param draw  - Drawable.
     * @throws NullPointerException     if start, end or drawable is null.
     * @throws IllegalArgumentException if start and end is not in the same world.
     */
    @AsyncSafe
    public static void drawLine(@Nonnull Location start, @Nonnull Location end, double step, @Nonnull Draw draw) {
        Validate.notNull(start);
        Validate.notNull(end);
        Validate.notNull(draw);
        Validate.isTrue(Objects.equals(start.getWorld(), end.getWorld()), "start and end point must be in the same world");

        Location dynamic = start.clone();

        final double distance = start.distance(end);
        final Vector vector = end.toVector().subtract(start.toVector()).normalize().multiply(step);

        for (double i = 0; i < distance; i += step) {
            dynamic.add(vector);
            draw.draw(dynamic);
        }
    }

    /**
     * Draws a sphere around center.
     *
     * @param center      - Center of the sphere.
     * @param rings       - Amount of 'rings' sphere will have; higher value returns in higher quality. Usually (radius / 2) results in good quality.
     * @param radius      - Radius of the sphere.
     * @param draw        - Drawable.
     * @param drawOnlyTop - If true, only top of the sphere will be drawn to save on resources.
     * @throws NullPointerException if center or draw is null.
     */
    @AsyncSafe
    public static void drawSphere(@Nonnull Location center, double rings, double radius, @Nonnull Draw draw, boolean drawOnlyTop) {
        Validate.notNull(center);
        Validate.notNull(draw);

        for (double d = 0.0d; d < Math.PI; d += Math.PI / rings) {
            double rad = Math.sin(d) * radius;
            double y = Math.cos(d) * radius;
            if (drawOnlyTop && y < 0) {
                return;
            }
            for (double j = 0.0d; j < Math.PI * 2; j += Math.PI / (rings / 2)) {
                double x = rad * Math.sin(j);
                double z = rad * Math.cos(j);
                center.add(x, y, z);
                draw.draw(center);
                center.subtract(x, y, z);
            }
        }
    }

    /**
     * Draws a sphere around center.
     *
     * @param center - Center of the sphere.
     * @param rings  - Amount of 'rings' sphere will have; higher value returns in higher quality. Usually (radius / 2) results in good quality.
     * @param radius - Radius of the sphere.
     * @param draw   - Drawable.
     * @throws NullPointerException if center or draw is null.
     */
    @AsyncSafe
    public static void drawSphere(Location center, double rings, double radius, Draw draw) {
        drawSphere(center, rings, radius, draw, false);
    }

    /**
     * Draws a polygon around the center.
     *
     * @param center - Center of polygon.
     * @param points - How many points' polygon will have.
     * @param radius - Radius from the center to each point.
     * @param draw   - Drawable.
     * @throws NullPointerException     if center or draw is null.
     * @throws IllegalArgumentException if radius is negative.
     */
    public static void drawPolygon(@Nonnull Location center, int points, double radius, @Nonnull Draw draw) {
        Validate.notNull(center);
        Validate.notNull(draw);
        Validate.isTrue(radius > 0, "radius must be positive");

        for (int i = 0; i < points; i++) {
            double angle = 360.0d / points * i;
            double nextAngle = 360.0d / points * (i + 1);

            angle = Math.toRadians(angle);
            nextAngle = Math.toRadians(nextAngle);

            double x = Math.cos(angle);
            double z = Math.sin(angle);
            double nextX = Math.cos(nextAngle);
            double nextZ = Math.sin(nextAngle);

            x *= radius;
            z *= radius;
            nextX *= radius;
            nextZ *= radius;

            double deltaX = nextX - x;
            double deltaZ = nextZ - z;
            double distance = Math.sqrt(((deltaX - x) * (deltaX - x)) + ((deltaZ - z) * (deltaZ - z)));
            for (double d = 0.0d; d < distance - (distance + 1) * 0.1; d += 0.1d) {
                center.add(x + deltaX * d, 0, z + deltaZ * d);
                draw.draw(center);
                center.subtract(x + deltaX * d, 0, z + deltaZ * d);
            }
        }
    }

    /**
     * Draws a donut around center.
     *
     * @param center - Center of the donut.
     * @param layers - Amount of layers of the donut.
     * @param radius - Radius from the center.
     * @param draw   - Drawable.
     * @throws NullPointerException if center or draw is null.
     */
    public static void drawDonut(@Nonnull Location center, int layers, double radius, @Nonnull Draw draw) {
        Validate.notNull(center);
        Validate.notNull(draw);

        for (double d = 0.0d; d < Math.PI * 2; d += Math.PI / 15) {
            double x = Math.cos(d);
            double z = Math.sin(d);
            for (double j = 0.0d; j < layers; ++j) {
                double h = j * (Math.PI / layers);
                double y = Math.cos(h);

                double radiusEdit = Math.sin(h);
                double radiusInwards = radius - radiusEdit;
                double radiusOutwards = radiusEdit + radiusEdit;

                center.add(x * radiusInwards, y, z * radiusInwards);
                draw.draw(center);
                center.subtract(x * radiusInwards, y, z * radiusInwards);

                center.add(x * radiusOutwards, y, z * radiusOutwards);
                draw.draw(center);
                center.subtract(x * radiusOutwards, y, z * radiusOutwards);
            }
        }
    }

}
