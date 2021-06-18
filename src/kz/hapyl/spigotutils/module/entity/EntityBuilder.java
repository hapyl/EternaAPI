package kz.hapyl.spigotutils.module.entity;

import kz.hapyl.spigotutils.SpigotUtilsPlugin;
import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class EntityBuilder {

	private final EntityType type;

	private Entity finalEntity = null;
	private String name;

	private boolean nameVisibility = true;
	private boolean gravity;
	private boolean glowing;
	private boolean invulnerable;
	private boolean persistent;

	private Vector velocity;

	public EntityBuilder(EntityType type) {
		if (type.getEntityClass() == null) {
			throw new IllegalArgumentException("Entity class cannot be null");
		}
		this.type = type;
	}

	public EntityBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public EntityBuilder setNameVisible(boolean flag) {
		this.nameVisibility = flag;
		return this;
	}

	public EntityBuilder flipEntity() {
		this.name = "Dinnebone";
		return this;
	}

	public EntityBuilder setGlowing(boolean flag) {
		this.glowing = flag;
		return this;
	}

	public EntityBuilder setGravity(boolean flag) {
		this.gravity = flag;
		return this;
	}

	public EntityBuilder setInvulnerable(boolean flag) {
		this.invulnerable = flag;
		return this;
	}

	public EntityBuilder setPersistent(boolean flag) {
		this.persistent = flag;
		return this;
	}

	public EntityBuilder setVelocity(double x, double y, double z) {
		this.velocity = new Vector(x, y, z);
		return this;
	}

	public void push(double x, double y, double z) {
		this.validateSpawned();
		this.finalEntity.setVelocity(new Vector(x, y, z));
	}

	public void spawn(Location location) {
		this.validateNotSpawned();

		if (location.getWorld() == null) {
			throw new IllegalArgumentException(String.format("Cannot spawn %s since provided location is null", this.type));
		}

		this.finalEntity = location.getWorld().spawn(location, Validate.requireNotNull(this.type.getEntityClass()), me -> {
			me.setGravity(this.gravity);
			me.setGlowing(this.glowing);
			me.setPersistent(this.persistent);
			me.setInvulnerable(this.invulnerable);
			me.setCustomName(Chat.format(this.name));
			me.setCustomNameVisible(this.nameVisibility);
		});
	}

	public void despawn() {
		this.validateSpawned();
		this.finalEntity.remove();
	}

	public void spawn(Location location, int removeAfterTicks) {
		this.spawn(location);
		new BukkitRunnable() {
			@Override
			public void run() {
				despawn();
			}
		}.runTaskLater(SpigotUtilsPlugin.getPlugin(), removeAfterTicks);
	}

	public String getDisplayName() {
		return this.name.isEmpty() ? this.type.name() : this.name;
	}

	private void validateSpawned() {
		if (this.finalEntity == null) {
			throw new IllegalStateException("Entity must be spawned before you can use this method.");
		}
	}

	private void validateNotSpawned() {
		if (this.finalEntity != null) {
			throw new IllegalStateException("Entity must not be spawned to use this method.");
		}
	}

}
