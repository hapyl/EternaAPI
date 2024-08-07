package me.hapyl.eterna.module.player;

import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public final class Cooldown {

	private final String name;
	private final UUID owner;
	private final long startedAt;
	private final long duration;

	public Cooldown(String name, UUID owner, long durationMillis) {
		this.name = name;
		this.owner = owner;
		this.startedAt = System.currentTimeMillis();
		this.duration = durationMillis;
		CooldownManager.current().startCooldown(this, true);
	}

	public Cooldown(String name, Player owner, long durationMillis) {
		this(name, owner.getUniqueId(), durationMillis);
	}

	public long getTimeLeft() {
		return this.getDuration() - (System.currentTimeMillis() - this.getStartedAt());
	}

	public boolean isFinished() {
		return (System.currentTimeMillis() - this.getStartedAt()) >= this.getDuration();
	}

	// static members
	public static boolean hasCooldown(Player player, String name) {
		return CooldownManager.current().hasCooldown(player.getUniqueId(), name);
	}

	/**
	 * @return time left in millis or -1 if invalid cooldown
	 */
	public static long getTimeLeft(Player player, String name) {
		final Cooldown cooldown = CooldownManager.current().getCooldown(player.getUniqueId(), name);
		if (cooldown == null) {
			return -1;
		}
		return cooldown.getTimeLeft();
	}

	public String getName() {
		return name;
	}

	public UUID getOwner() {
		return owner;
	}

	public long getStartedAt() {
		return startedAt;
	}

	public long getDuration() {
		return duration;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final Cooldown cooldown = (Cooldown)o;
		return startedAt == cooldown.startedAt && duration == cooldown.duration && Objects.equals(name,
				cooldown.name) && Objects.equals(owner, cooldown.owner);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, owner, startedAt, duration);
	}
}
