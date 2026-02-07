package me.hapyl.eterna.module.math.geometry;

import me.hapyl.eterna.module.annotate.UtilityClass;
import me.hapyl.eterna.module.location.LocationHelper;
import me.hapyl.eterna.module.math.PiHelper;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Represents a geometry related utility class, allowing drawing different shaped in the world via {@link Particle}.
 *
 * <p>Most of the code here is from <a href="https://www.spigotmc.org/members/finnbon.37739/">finnbon</a>, check them to understand how it works!</p>
 */
@UtilityClass
public final class Geometry {
    
    private Geometry() {
        UtilityClass.Validator.throwIt();
    }
    
    /**
     * Draws a circle around the given {@link Location}.
     *
     * @param centre   - The centre of the circle.
     * @param radius   - The radius of the circle.
     * @param quality  - The quality of the circle.
     * @param drawable - The drawable
     */
    public static void drawCircle(@NotNull Location centre, final double radius, @NotNull Quality quality, @NotNull Drawable drawable) {
        for (double i = 0.0; i < PiHelper.TWO_PI; i += quality.getQuality()) {
            final double x = Math.sin(i) * radius;
            final double z = Math.cos(i) * radius;
            
            LocationHelper.offset(centre, x, 0, z, drawable::draw);
        }
    }
    
    /**
     * Draws a circle around the given {@link Location}, anchoring it to the lowest block below it.
     *
     * @param centre   - The centre of the circle.
     * @param radius   - The radius of the circle.
     * @param quality  - The quality of the circle.
     * @param drawable - The drawable.
     * @param yOffset  - The optional {@code y} offset to add after anchoring the location.
     */
    public static void drawCircleAnchored(@NotNull Location centre, final double radius, @NotNull Quality quality, @NotNull Drawable drawable, final double yOffset) {
        for (double i = 0.0; i < PiHelper.TWO_PI; i += quality.getQuality()) {
            final double x = Math.sin(i) * radius;
            final double z = Math.cos(i) * radius;
            
            drawable.draw(LocationHelper.anchor(LocationHelper.copyOf(centre).add(x, 0, z)).add(0, yOffset, 0));
        }
    }
    
    /**
     * Draws a circle around the given {@link Location}, anchoring it to the lowest block below it.
     *
     * @param centre   - The centre of the circle.
     * @param radius   - The radius of the circle.
     * @param quality  - The quality of the circle.
     * @param drawable - The drawable.
     */
    public static void drawCircleAnchored(@NotNull Location centre, final double radius, @NotNull Quality quality, @NotNull Drawable drawable) {
        drawCircleAnchored(centre, radius, quality, drawable, 0.0);
    }
    
    /**
     * Draws a line between the two {@link Location}.
     *
     * @param start    - The first location.
     * @param end      - The second location.
     * @param step     - The step of each particle.
     * @param drawable - The drawable.
     */
    public static void drawLine(@NotNull Location start, @NotNull Location end, final double step, @NotNull Drawable drawable) {
        final double distance = start.distance(end);
        final Vector vector = end.toVector().subtract(start.toVector()).normalize();
        
        for (double i = 0; i < distance; i += step) {
            final double x = vector.getX() * i;
            final double y = vector.getY() * i;
            final double z = vector.getZ() * i;
            
            LocationHelper.offset(start, x, y, z, drawable::draw);
        }
    }
    
    /**
     * Draws a sphere around the given {@link Location}.
     *
     * @param centre     - The centre of the sphere.
     * @param radius     - The radius of the sphere.
     * @param quality    - The quality of the sphere.
     * @param drawable   - The drawable.
     * @param sphereType - The sphere type.
     */
    public static void drawSphere(@NotNull Location centre, double radius, @NotNull Quality quality, @NotNull Drawable drawable, @NotNull SphereType sphereType) {
        for (double phi = sphereType.from(); phi <= sphereType.to(); phi += quality.getQuality()) {
            for (double theta = 0; theta <= PiHelper.TWO_PI; theta += quality.getQuality()) {
                final double x = radius * Math.sin(phi) * Math.cos(theta);
                final double y = radius * Math.cos(phi);
                final double z = radius * Math.sin(phi) * Math.sin(theta);
                
                LocationHelper.offset(centre, x, y, z, drawable::draw);
            }
        }
    }
    
    /**
     * Draws a sphere around the given {@link Location}.
     *
     * @param centre   - The centre of the sphere.
     * @param radius   - The radius of the sphere.
     * @param quality  - The quality of the sphere.
     * @param drawable - The drawable.
     */
    public static void drawSphere(@NotNull Location centre, final double radius, @NotNull Quality quality, @NotNull Drawable drawable) {
        drawSphere(centre, radius, quality, drawable, SphereType.FULL);
    }
    
    /**
     * Draws a polygon around the given {@link Location}.
     * <p>The polygon must consist of at least {@code 3} points!</p>
     *
     * @param center   - The centre of the polygon.
     * @param points   - The number of polygon points.
     * @param radius   - The radius of the polygon.
     * @param step     - The step of each particle.
     * @param drawable - The drawable.
     * @throws IllegalArgumentException if {@code points < 3}.
     */
    public static void drawPolygon(@NotNull Location center, @Range(from = 3, to = Byte.MAX_VALUE) final int points, final double radius, final double step, @NotNull Drawable drawable) {
        if (points < 3) {
            throw new IllegalArgumentException("The polygon must consist of a least 3 points!");
        }
        
        final Location[] vertices = new Location[points];
        final double angleStep = PiHelper.TWO_PI / points;
        
        for (int i = 0; i < points; i++) {
            final double angle = i * angleStep;
            final double x = center.getX() + Math.sin(angle) * radius;
            final double z = center.getZ() + Math.cos(angle) * radius;
            
            vertices[i] = new Location(center.getWorld(), x, center.getY(), z);
        }
        
        for (int i = 0; i < points; i++) {
            drawLine(vertices[i], vertices[(i + 1) % points], step, drawable);
        }
    }
    
}
