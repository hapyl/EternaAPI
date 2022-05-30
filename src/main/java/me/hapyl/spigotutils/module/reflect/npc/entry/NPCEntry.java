package me.hapyl.spigotutils.module.reflect.npc.entry;

import me.hapyl.spigotutils.module.reflect.npc.HumanNPC;
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
