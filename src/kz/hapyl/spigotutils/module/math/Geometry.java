package kz.hapyl.spigotutils.module.math;

import kz.hapyl.spigotutils.module.annotate.AsyncSafe;
import kz.hapyl.spigotutils.module.math.gometry.Draw;
import kz.hapyl.spigotutils.module.math.gometry.Quality;
import kz.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.Objects;

public class Geometry {

	public static final Location CENTER = new Location(Bukkit.getWorlds().get(0), 0, Bukkit.getWorlds().get(0).getHighestBlockYAt(0, 0), 0);
	protected static final double TWO_PI = (Math.PI * 2);

	@AsyncSafe
	public static void drawCircle(Location center, double radius, Quality quality, Draw draw) {
		Validate.notNull(center, "location cannot be null");
		Validate.notNull(draw, "draw particle cannot be null");

		for (double i = 0.0d; i < TWO_PI; i += quality.getStep()) {
			double x = (radius * Math.sin(i));
			double z = (radius * Math.cos(i));
			center.add(x, 0, z);
			draw.draw(center);
			center.subtract(x, 0, z);
		}

	}

	@AsyncSafe
	public static void drawLine(Location start, Location end, double step, Draw draw) {
		Validate.notNull(start);
		Validate.notNull(end);
		Validate.notNull(draw);
		Validate.isTrue(Objects.equals(start.getWorld(), end.getWorld()), "start and end point must be in the same world");

		Location dynamic = end.clone();

		final double distance = start.distance(end);
		final Vector vector = end.toVector().subtract(start.toVector()).normalize().multiply(step);

		for (double i = 0; i < distance; i += step) {
			dynamic.add(vector);
			draw.draw(dynamic);
		}

	}

	@AsyncSafe
	public static void drawSphere(Location center, double rings, double radius, Draw draw, boolean drawOnlyTop) {
		// TODO: 028. 02/28/2021 Quality impls
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

	@AsyncSafe
	public static void drawSphere(Location center, double rings, double radius, Draw draw) {
		drawSphere(center, rings, radius, draw, false);
	}

	public static void drawPolygon(Location center, int points, double radius, Draw draw) {
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

	public static void drawDonut(Location center, int layers, double radius, Draw draw) {
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
