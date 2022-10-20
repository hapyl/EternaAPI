package me.hapyl.spigotutils.module.quest;

import com.google.common.collect.Sets;
import me.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;

// Quest manager.
public class QuestManager {

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

    private Set<QuestProgress> getProgress(Player player) {
        return this.playerQuests.getOrDefault(player.getUniqueId(), Sets.newConcurrentHashSet());
    }

    public static QuestFormatter formatter() {
        return FORMATTER;
    }

    public static QuestManager current() {
        return classInstance;
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

    private static final QuestManager classInstance = new QuestManager();
    private static final QuestFormatter FORMATTER = new QuestFormatter() {

        @Override
        public void sendObjectiveNew(Player player, QuestObjective objective) {
            Chat.sendMessage(player, "");
            Chat.sendCenterMessage(player, "&e&lNEW OBJECTIVE!");
            Chat.sendCenterMessage(player, "&6" + objective.getObjectiveName());
            Chat.sendCenterMessage(player, "&7" + objective.getObjectiveShortInfo());
            Chat.sendMessage(player, "");
        }

        @Override
        public void sendObjectiveComplete(Player player, QuestObjective objective) {
            Chat.sendMessage(player, "");
            Chat.sendCenterMessage(player, "&2&lOBJECTIVE COMPLETE!");
            Chat.sendCenterMessage(player, "&a✔ " + objective.getObjectiveName());
            Chat.sendMessage(player, "");
        }

        @Override
        public void sendObjectiveFailed(Player player, QuestObjective objective) {
            Chat.sendMessage(player, "");
            Chat.sendCenterMessage(player, "&c&lOBJECTIVE FAILED!");
            Chat.sendCenterMessage(player, "&7It's ok! Try again.");
            Chat.sendMessage(player, "");
        }

        @Override
        public void sendQuestCompleteFormat(Player player, Quest quest) {
            sendLine(player);
            Chat.sendCenterMessage(player, "&6&lQUEST COMPLETE");
            Chat.sendCenterMessage(player, "&f" + quest.getQuestName());

            if (!quest.isAutoClaim()) {
                Chat.sendMessage(player, "");
                Chat.sendCenterMessage(player, "&7Navitage to Quest Journal to claim reward!");
            }

            sendLine(player);
        }

        @Override
        public void sendQuestStartedFormat(Player player, Quest quest) {
            sendLine(player);
            Chat.sendCenterMessage(player, "&6&lQuest Started");
            Chat.sendCenterMessage(player, "&f" + quest.getQuestName());
            Chat.sendCenterMessage(player, "");
            Chat.sendCenterMessage(player, "&aCurrent Objective:");
            Chat.sendCenterMessage(player, "&7&o" + quest.getCurrentObjective().getObjectiveName());
            Chat.sendCenterMessage(player, "&7&o" + quest.getCurrentObjective().getObjectiveShortInfo());
            sendLine(player);
        }

        @Override
        public void sendQuestFailedFormat(Player player, Quest quest) {
            sendLine(player);
            Chat.sendCenterMessage(player, "&c&lQUEST FAILED");
            Chat.sendCenterMessage(player, "&f" + quest.getQuestName());
            sendLine(player);
        }

        private void sendLine(Player player) {
            Chat.sendCenterMessage(player, "&e&m]                                      [");
        }
    };

}
