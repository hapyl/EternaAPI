package me.hapyl.spigotutils.module.math.trace;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class RayTrace implements Traceable {

	private final Location startLocation;
	private final Properties properties;

	public RayTrace(Location location) {
		this.startLocation = location;
		this.properties = new Properties();
	}

	public Properties getProperties() {
		return properties;
	}

	/**
	 * Executes every time trace moves.
	 *
	 * @param location current location.
	 */
	public void onMove(Location location) {

	}

	/**
	 * Executes upon hitting an entity.
	 *
	 * @param entity hit entity.
	 */
	public void onHit(LivingEntity entity) {

	}

	/**
	 * Executes once upon staring trace.
	 *
	 * @param location start trace location.
	 */
	public void onTrace(Location location) {

	}

	@Override
	public void trace() {
		final Location location = startLocation;
		final Vector vector = location.getDirection().normalize();

		for (double i = 0; i < getProperties().getMaxDistance(); i += getProperties().getShift()) {

			final double x = vector.getX() * i;
			final double y = vector.getY() * i;
			final double z = vector.getZ() * i;
			location.add(x, y, z);

			// check for the hitting a block and an entity
			if (location.getBlock().getType().isOccluding()) {
				break;
			}

			Traceable.getNearbyLivingEntities(location, 0.5d, entity -> getProperties().predicateEntity(entity)).forEach(this::onHit);
			onMove(location);

			location.subtract(x, y, z);

		}
	}


}
