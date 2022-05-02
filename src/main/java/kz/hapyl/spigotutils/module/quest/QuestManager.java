package kz.hapyl.spigotutils.module.quest;

import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;

public class QuestManager {

	private static final QuestManager classInstance = new QuestManager();

	private final Map<UUID, Set<QuestProgress>> playerQuests;
	private final Map<Integer, Quest> byIdQuests;

	private int freeId = 0;

	private QuestManager() {
		this.playerQuests = new HashMap<>();
		this.byIdQuests = new HashMap<>();
	}

	public void registerQuest(Quest quest) {
		quest.setQuestId(this.freeId++);
		this.byIdQuests.put(quest.getQuestId(), quest);
	}

	@Nullable
	public Quest getById(int id) {
		return this.byIdQuests.get(id);
	}

	public Map<Integer, Quest> getRegisteredQuests() {
		return byIdQuests;
	}

	public void startQuest(Player player, Quest quest) {
		addQuest(player, quest);
	}

	public void completeQuest(QuestProgress quest) {
		// TODO: 023. 03/23/2021 - Add ability to claiming quests instead of auto claiming when finishing (configurable)
		removeQuest(quest);
	}

	private void addQuest(Player player, Quest quest) {
		final Set<QuestProgress> hash = getProgress(player);
		hash.add(new QuestProgress(player, quest));
		this.playerQuests.put(player.getUniqueId(), hash);
	}

	private void removeQuest(QuestProgress quest) {
		final Player player = quest.getPlayer();
		final Set<QuestProgress> hash = getProgress(player);
		if (hash.isEmpty()) {
			return;
		}
		hash.remove(quest);
		this.playerQuests.put(player.getUniqueId(), hash);
	}

	public Set<QuestProgress> getActiveQuests(Player player) {
		return playerQuests.getOrDefault(player.getUniqueId(), new HashSet<>());
	}

	public boolean hasQuestsOfType(Player player, QuestObjectiveType type) {
		return !getActiveQuestsOfType(player, type).isEmpty();
	}

	public Set<QuestProgress> getActiveQuestsOfType(Player player, QuestObjectiveType type) {
		final Set<QuestProgress> hash = new HashSet<>();
		final Set<QuestProgress> activeQuests = getActiveQuests(player);
		if (activeQuests.isEmpty()) {
			return hash;
		}
		for (final QuestProgress active : activeQuests) {
			// not active since complete
			if (active.isComplete()) {
				continue;
			}
			if (active.getCurrentObjective().getQuestType() == type) {
				hash.add(active);
			}
		}
		return hash;
	}

	public Set<PlayerQuestObjective> getActiveObjectivesOfType(Player player, QuestObjectiveType type) {
		final Set<PlayerQuestObjective> hash = new HashSet<>();
		for (final QuestProgress questProgress : getActiveQuestsOfType(player, type)) {
			final PlayerQuestObjective current = questProgress.getCurrentObjective();
			if (current.getQuestType() == type) {
				hash.add(current);
			}
		}
		return hash;
	}

	public void checkActiveQuests(Player player, QuestObjectiveType type, Object... checkParams) {
		if (hasQuestsOfType(player, type)) {
			for (final PlayerQuestObjective active : getActiveObjectivesOfType(player, type)) {
				final double boolAsDouble = active.testQuestCompletion(checkParams);
				if (boolAsDouble > 0.0d) {
					active.incrementGoal(boolAsDouble, true);
					// check moved into incrementGoal
				}
			}
		}
	}

	private Set<QuestProgress> getProgress(Player player) {
		return this.playerQuests.getOrDefault(player.getUniqueId(), new HashSet<>());
	}

	public static QuestManager current() {
		return classInstance;
	}
}
