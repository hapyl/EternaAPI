package me.hapyl.eterna.module.quest;

import com.google.common.collect.Sets;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;

// Quest manager.
public class QuestManager {

    private static final QuestManager classInstance = new QuestManager();
    private final Map<UUID, Set<QuestProgress>> playerQuests;
    private final Map<String, Quest> byIdQuests;

    private QuestManager() {
        this.playerQuests = new HashMap<>();
        this.byIdQuests = new HashMap<>();
    }

    /**
     * Registers a quest.
     *
     * @param quest - Quest to register.
     * @throws IllegalArgumentException - If Quest is already registered.
     */
    public void registerQuest(Quest quest) {
        final String questId = quest.getQuestId();
        if (getById(questId) != null) {
            throw new IllegalArgumentException("Quest with ID %s is already registered!".formatted(questId));
        }
        this.byIdQuests.put(questId, quest);
    }

    public void unregisterQuest(Quest quest) {
        final String questId = quest.getQuestId();
        if (getById(questId) != null) {
            byIdQuests.remove(questId);
        }
    }

    @Nullable
    public Quest getById(String id) {
        return this.byIdQuests.get(id.toLowerCase(Locale.ROOT));
    }

    public Map<String, Quest> getRegisteredQuests() {
        return byIdQuests;
    }

    public void startQuest(Player player, Quest quest) {
        addQuest(player, quest);
    }

    public void completeQuest(QuestProgress quest) {
        if (quest.isAutoClaimReward() || quest.isClaimedReward()) {
            removeQuest(quest);
        }
    }

    /**
     * Adds a QuestProgress to player current quest progress if not present.
     *
     * @param player   - Player.
     * @param progress - Progress.
     */
    public void addQuestProgress(Player player, QuestProgress progress) {
        if (hasQuest(player, progress.getQuest())) {
            return;
        }

        final Set<QuestProgress> set = getProgress(player);
        set.add(progress);
        playerQuests.put(player.getUniqueId(), set);
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

    // Check for exact quest
    public boolean hasQuest(Player player, Quest quest) {
        final Set<QuestProgress> quests = getActiveQuests(player);
        for (QuestProgress progress : quests) {
            if (progress.getQuest().equals(quest)) {
                return true;
            }
        }
        return false;
    }

    protected void addQuest(Player player, Quest quest) {
        final Set<QuestProgress> hash = getProgress(player);
        hash.add(new QuestProgress(player, quest));
        this.playerQuests.put(player.getUniqueId(), hash);
    }

    protected void removeQuest(QuestProgress quest) {
        final Player player = quest.getPlayer();
        final Set<QuestProgress> hash = getProgress(player);
        if (hash.isEmpty()) {
            return;
        }
        hash.remove(quest);
        this.playerQuests.put(player.getUniqueId(), hash);
    }

    private Set<QuestProgress> getProgress(Player player) {
        return this.playerQuests.getOrDefault(player.getUniqueId(), Sets.newConcurrentHashSet());
    }

    public static QuestManager current() {
        return classInstance;
    }

}
