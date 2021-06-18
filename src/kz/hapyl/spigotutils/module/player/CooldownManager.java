package kz.hapyl.spigotutils.module.player;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownManager {

	private static final CooldownManager classInstance = new CooldownManager();

	private final Map<UUID, Set<Cooldown>> byUuid;

	private CooldownManager() {
		this.byUuid = new ConcurrentHashMap<>();
	}

	public void startCooldown(Cooldown cooldown, boolean cancelIfActive) {
		final UUID uuid = cooldown.getOwner();
		final String name = cooldown.getName();
		if (hasCooldown(uuid, name) && cancelIfActive) {
			stopCooldown(uuid, name);
		}
		final Set<Cooldown> set = getCooldowns(uuid);
		set.add(cooldown);
		setCooldowns(uuid, set);
	}

	public void stopCooldown(UUID uuid, String name) {

		final Set<Cooldown> set = this.getCooldowns().get(uuid);
		final Iterator<Cooldown> iterator = set.iterator();

		iterator.forEachRemaining(item -> {
			if (item.getName().equals(name)) {
				set.remove(item);
			}
		});

		setCooldowns(uuid, set);

	}

	public boolean hasCooldown(UUID who, String name) {
		if (this.getCooldowns(who).isEmpty()) {
			return false;
		}
		for (final Cooldown cooldown : this.getCooldowns(who)) {
			if (cooldown.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public static CooldownManager current() {
		return classInstance;
	}

	private void setCooldowns(UUID uuid, Set<Cooldown> cooldowns) {
		this.getCooldowns().put(uuid, cooldowns);
	}

	public Set<Cooldown> getCooldowns(UUID uuid) {
		return this.getCooldowns().getOrDefault(uuid, new HashSet<>());
	}

	public Map<UUID, Set<Cooldown>> getCooldowns() {
		return byUuid;
	}

	@Nullable
	public Cooldown getCooldown(UUID uniqueId, String name) {
		if (!hasCooldown(uniqueId, name)) {
			return null;
		}
		for (final Cooldown cooldown : getCooldowns(uniqueId)) {
			if (cooldown.getName().equals(name)) {
				return cooldown;
			}
		}
		return null;
	}
}
