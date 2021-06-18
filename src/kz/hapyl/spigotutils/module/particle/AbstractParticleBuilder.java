package kz.hapyl.spigotutils.module.particle;

import kz.hapyl.spigotutils.module.annotate.NotEmpty;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public abstract class AbstractParticleBuilder {

	private Particle particle;
	private int amount = 1;
	private double offX;
	private double offY;
	private double offZ;
	private double speed;

	public AbstractParticleBuilder(Particle particle) {
		this.particle = particle;
	}

	public abstract void display(Location location);

	public abstract void display(Location location, Player... players);

	public abstract void displayAsync(@NotEmpty Player... players);

	public AbstractParticleBuilder setParticle(Particle particle) {
		this.particle = particle;
		return this;
	}

	public AbstractParticleBuilder setAmount(int amount) {
		this.amount = amount;
		return this;
	}

	public AbstractParticleBuilder setOffX(double offX) {
		this.offX = offX;
		return this;
	}

	public AbstractParticleBuilder setOffY(double offY) {
		this.offY = offY;
		return this;
	}

	public AbstractParticleBuilder setOffZ(double offZ) {
		this.offZ = offZ;
		return this;
	}

	public AbstractParticleBuilder resetOffset() {
		this.offX = 0.0d;
		this.offY = 0.0d;
		this.offZ = 0.0d;
		return this;
	}

	public Particle getParticle() {
		return particle;
	}

	public AbstractParticleBuilder setSpeed(float speed) {
		this.speed = speed;
		return this;
	}

	public int getAmount() {
		return amount;
	}

	public double getOffX() {
		return offX;
	}

	public double getOffY() {
		return offY;
	}

	public double getOffZ() {
		return offZ;
	}

	public double getSpeed() {
		return speed;
	}

	public void compareDataTypes(Class<?> other) {
		if (!this.particle.getDataType().equals(other)) {
			throw new IllegalArgumentException(String.format("%s is not applicable to %s, expected %s.", other.getSimpleName(), this.particle.name(),
					this.particle.getDataType().getSimpleName()));
		}
	}

	public final void worldNotNull(Location location) {
		if (location == null || location.getWorld() == null) {
			throw new IllegalArgumentException("Either location or world location is null!");
		}
	}

	public final void viewersNotEmpty(Player[] array) {
		if (array == null || array.length == 0) {
			throw new IllegalArgumentException("There must be at least one viewer!");
		}
	}
}
