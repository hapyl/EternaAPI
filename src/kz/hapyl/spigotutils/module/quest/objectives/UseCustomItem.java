package kz.hapyl.spigotutils.module.quest.objectives;

import kz.hapyl.spigotutils.module.quest.QuestObjective;
import kz.hapyl.spigotutils.module.quest.QuestObjectiveType;

public class UseCustomItem extends QuestObjective {

	private final String itemId;

	public UseCustomItem(String itemId, int times) {
		super(QuestObjectiveType.USE_CUSTOM_ITEM, times, "Work It!", String.format("Use custom item with Id %s %s times!", itemId, times));
		this.itemId = itemId;
	}

	@Override
	public double testQuestCompletion(Object... objects) {
		return objects[0].toString().equals(itemId) ? 1 : -1;
	}
}
