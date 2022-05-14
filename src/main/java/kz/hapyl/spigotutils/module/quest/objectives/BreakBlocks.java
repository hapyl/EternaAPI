package kz.hapyl.spigotutils.module.quest.objectives;

import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.quest.QuestObjective;
import kz.hapyl.spigotutils.module.quest.QuestObjectiveType;
import org.bukkit.Material;

public class BreakBlocks extends QuestObjective {

	private final Material material;

	public BreakBlocks(Material material, long goalTotal) {
		super(QuestObjectiveType.BREAK_BLOCKS, goalTotal,
				Chat.stringifyItemName(material) + " Wrecker",
				String.format("Mine %s %s.", goalTotal, Chat.stringifyItemName(material)));
		this.material = material;
	}

	@Override
	public double testQuestCompletion(Object... objects) {
		return super.validateArgument(0, this.material, objects);
//		return ((Block) objects[0]).getType() == this.material ? 1.0d : -1.0d;
	}
}
