package me.hapyl.spigotutils.module.reflect.npc.entry;

import me.hapyl.spigotutils.module.quest.Quest;
import me.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import org.bukkit.entity.Player;

public class QuestEntry extends NPCEntry {

	private final Quest quest;

	public QuestEntry(Quest quest, int delay) {
		super(delay);
		this.quest = quest;
	}

	@Override
	public void invokeEntry(HumanNPC npc, Player player) {
		this.quest.startQuest(player);
	}
}
