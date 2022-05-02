package kz.hapyl.spigotutils.module.quest;

import kz.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class Quest {

	private final String displayName;
	private final Map<Integer/*stage*/, QuestObjective> objectives;

	@Nullable
	private QuestReward reward;

	private int questId;
	private final boolean autoClaim;

	public Quest(String displayName, boolean autoClaim) {
		this.displayName = displayName;
		this.objectives = new HashMap<>();
		this.autoClaim = autoClaim;
	}

	public Quest(String displayName) {
		this(displayName, true);
	}

	public void addReward(QuestReward reward) {
		this.reward = reward;
	}

	public void grantRewardIfExist(Player player) {
		if (this.reward == null) {
			return;
		}
		this.reward.grantReward(player);
	}

	public final boolean isAutoClaim() {
		return autoClaim;
	}

	public final void startQuest(Player player) {
		if (!this.isValid()) {
			Chat.sendMessage(player, "&cCould not invalid quest (%s), please report this!", this.getQuestName());
			return;
		}
		QuestManager.current().startQuest(player, this);
		this.sendQuestInfo(player);
	}

	public void setQuestId(int questId) {
		this.questId = questId;
	}

	public int getQuestId() {
		return questId;
	}

	public final boolean isValid() {
		return !this.displayName.equalsIgnoreCase("") && this.objectives.size() != 0;
	}

	/**
	 * Inserts QuestObjective to the next stage
	 *
	 * @param questObjective - Objective
	 */
	public void addStage(QuestObjective questObjective) {
		this.objectives.put(this.objectives.size(), questObjective);
	}

	/**
	 * Inserts QuestObjective to the given stage
	 *
	 * @param stage          - Stage to put in
	 * @param questObjective - Objective
	 */
	public void setStage(int stage, QuestObjective questObjective) {
		this.objectives.put(stage, questObjective);
	}

	public Map<Integer, QuestObjective> getObjectives() {
		return objectives;
	}

	public String getQuestName() {
		return displayName;
	}

	private void sendQuestInfo(Player player) {
		Chat.sendCenterMessage(player, "&e&m]                                      [");
		Chat.sendCenterMessage(player, "&6&lQuest Started");
		Chat.sendCenterMessage(player, "&f" + this.getQuestName());
		Chat.sendCenterMessage(player, "");
		Chat.sendCenterMessage(player, "&aCurrent Objective:");
		Chat.sendCenterMessage(player, "&7&o" + this.getObjectives().get(0).getObjectiveName());
		Chat.sendCenterMessage(player, "&7&o" + this.getObjectives().get(0).getObjectiveShortInfo());
		Chat.sendCenterMessage(player, "&e&m]                                      [");
	}

}
