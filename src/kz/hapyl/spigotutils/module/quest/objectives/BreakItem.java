package kz.hapyl.spigotutils.module.quest.objectives;

import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.quest.QuestObjective;
import kz.hapyl.spigotutils.module.quest.QuestObjectiveType;
import org.bukkit.Material;

public class BreakItem extends QuestObjective {

	private final Material material;

	public BreakItem(Material material) {
		super(QuestObjectiveType.BREAK_ITEM, 1,
				"I didn't mean to, I swear!",
				String.format("Break %s.", Chat.capitalize(material)));
		this.material = material;
	}

	@Override
	public double testQuestCompletion(Object... objects) {
		return super.validateArgument(0, this.material, objects);
	}
}
