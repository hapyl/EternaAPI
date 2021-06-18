package kz.hapyl.spigotutils.module.reflect.npc;

import kz.hapyl.spigotutils.module.quest.Quest;
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
