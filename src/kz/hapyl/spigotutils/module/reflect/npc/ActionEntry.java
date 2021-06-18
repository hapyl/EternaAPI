package kz.hapyl.spigotutils.module.reflect.npc;

import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class ActionEntry extends NPCEntry {

	private final Consumer<Player> action;

	public ActionEntry(Consumer<Player> action, int delay) {
		super(delay);
		this.action = action;
	}

	@Override
	public void invokeEntry(HumanNPC npc, Player player) {
		this.action.accept(player);
	}
}
