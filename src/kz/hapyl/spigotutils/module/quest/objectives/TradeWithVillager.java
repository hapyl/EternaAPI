package kz.hapyl.spigotutils.module.quest.objectives;

import kz.hapyl.spigotutils.module.quest.QuestObjective;
import kz.hapyl.spigotutils.module.quest.QuestObjectiveType;
import kz.hapyl.spigotutils.module.util.Validate;
import org.bukkit.entity.Villager;

public class TradeWithVillager extends QuestObjective {

	private final Villager villager;

	public TradeWithVillager(Villager villager, int times) {
		super(QuestObjectiveType.TRADE_WITH_VILLAGER, times, "Trader",
				String.format("Trade with %s.", Validate.ifNull(villager.getCustomName(), "Villager")));
		this.villager = villager;
	}

	@Override
	public double testQuestCompletion(Object... objects) {
		return -1.0d;
	}
}
