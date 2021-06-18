package kz.hapyl.spigotutils.module.reflect.npc;

import org.bukkit.entity.Player;

public abstract class NPCEntry {

	private final int delay;

	public NPCEntry(int delay) {
		this.delay = delay;
	}

	public int getDelay() {
		return delay;
	}

	public abstract void invokeEntry(HumanNPC npc, Player player);

}
