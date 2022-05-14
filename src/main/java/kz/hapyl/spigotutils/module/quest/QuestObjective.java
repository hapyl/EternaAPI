package kz.hapyl.spigotutils.module.quest;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * This used to store quest object for quest
 */
public abstract class QuestObjective {

	// backwards reference to quest?

	private String objectiveName;
	private String objectiveShortInfo;

	private final QuestObjectiveType type;
	private final double             goalTotal;

	private boolean hidden;

	public QuestObjective(QuestObjectiveType type, double goalTotal, String name, String info) {
		this.type = type;
		this.goalTotal = goalTotal;
		this.objectiveName = name;
		this.objectiveShortInfo = info;
		this.hidden = false;
	}

	public QuestObjective(QuestObjectiveType type, double goalTotal, String name, String info, Object... infoRepl) {
		this(type, goalTotal, name, String.format(info, infoRepl));
	}

	public QuestObjective setHidden(boolean hidden) {
		this.hidden = hidden;
		return this;
	}

	public boolean isHidden() {
		return hidden;
	}

	public QuestObjective(QuestObjectiveType type, long goalTotal) {
		this(type, goalTotal, "Debug Objective", "This is a debug message!");
		Bukkit.getLogger().warning("Called debug constructor of QuestObjective!");
	}

	public abstract double testQuestCompletion(Object... objects);

	public void afterObjectiveCompletion(Player player, double value) {

	}

	public void afterObjectiveIncrement(Player player, double value) {
	}

	protected double validateArgument(int index, Object value, Object... objects) {
		if (objects.length >= index) {
			if (value.equals(objects[index])) {
				return 1.0d;
			}
		}
		return -1.0d;
	}


	public QuestObjective setName(String string) {
		this.objectiveName = string;
		return this;
	}

	public QuestObjective setShortInfo(String string) {
		this.objectiveShortInfo = string;
		return this;
	}

	public String getObjectiveName() {
		return objectiveName;
	}

	public String getObjectiveShortInfo() {
		return objectiveShortInfo;
	}

	public QuestObjectiveType getQuestType() {
		return type;
	}

	public double getCompletionGoal() {
		return goalTotal;
	}

	public PlayerQuestObjective cloneAsPlayerObjective(QuestProgress progress) {
		return new PlayerQuestObjective(progress, this) {
			@Override
			public double testQuestCompletion(Object... objects) {
				return QuestObjective.this.testQuestCompletion(objects);
			}

			@Override
			public void afterObjectiveCompletion(Player player, double value) {
				QuestObjective.this.afterObjectiveCompletion(player, value);
			}

			@Override
			public void afterObjectiveIncrement(Player player, double value) {
				QuestObjective.this.afterObjectiveIncrement(player, value);
			}
		};
	}
}
