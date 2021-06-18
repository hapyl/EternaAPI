package kz.hapyl.spigotutils.module.quest.objectives;

import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.quest.QuestObjective;
import kz.hapyl.spigotutils.module.quest.QuestObjectiveType;
import org.bukkit.entity.EntityType;

public class SlayEntity extends QuestObjective {

	private final EntityType entityType;

	public SlayEntity(EntityType type, long goalTotal) {
		super(QuestObjectiveType.SLAY_ENTITY, goalTotal, Chat.capitalize(type) + " Slayer",
				String.format("Slay %s %s.", goalTotal, Chat.capitalize(type)));
		// TODO: 023. 03/23/2021 - check for living entity
		this.entityType = type;
	}

	@Override
	public double testQuestCompletion(Object... objects) {
		return super.validateArgument(0, this.entityType, objects);
	}
}
