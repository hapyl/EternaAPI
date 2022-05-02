package kz.hapyl.spigotutils.module.command;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommandCooldown {

	private final SimpleCommand   command;
	private final Map<UUID, Long> usedAt;

	public CommandCooldown(SimpleCommand command) {
		this.command = command;
		this.usedAt = new HashMap<>();
	}

	public boolean hasCooldown(Player player) {
		final long passed = System.currentTimeMillis() - getCooldown(player);
		if (passed >= (this.command.getCooldownTick() * 50L)) {
			this.stopCooldown(player);
			return false;
		}
		return true;
	}

	public long getTimeLeft(Player player) {
		return (this.command.getCooldownTick() * 50L) - (System.currentTimeMillis() - getCooldown(player));
	}

	private long getCooldown(Player player) {
		return this.usedAt.getOrDefault(player.getUniqueId(), 0L);
	}

	public void startCooldown(Player player) {
		this.usedAt.put(player.getUniqueId(), System.currentTimeMillis());
	}

	public void stopCooldown(Player player) {
		this.usedAt.remove(player.getUniqueId());
	}

}
