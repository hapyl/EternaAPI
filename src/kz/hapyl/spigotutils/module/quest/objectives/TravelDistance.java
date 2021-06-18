package kz.hapyl.spigotutils.module.quest.objectives;

import kz.hapyl.spigotutils.module.quest.QuestObjective;
import kz.hapyl.spigotutils.module.quest.QuestObjectiveType;
import kz.hapyl.spigotutils.module.quest.TravelType;
import kz.hapyl.spigotutils.module.util.Validate;

public class TravelDistance extends QuestObjective {

	private final TravelType type;

	public TravelDistance(TravelType type, double distance) {
		super(QuestObjectiveType.TRAVEL_DISTANCE, distance, "Traveller",
				String.format("Travel %s blocks %s.", distance, type.getString()));
		this.type = type;
	}

	@Override
	public double testQuestCompletion(Object... objects) {
		// type , dist
		if (objects[0].equals(type)) {
			return Validate.getDouble(objects[1]);
		}
		return -1.0d;
	}
}
