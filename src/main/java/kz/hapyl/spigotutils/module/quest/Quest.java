package kz.hapyl.spigotutils.module.quest;

import kz.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Creates a new quest.
 */
public class Quest {

    private final String displayName;
    private final Map<Integer/*stage*/, QuestObjective> objectives;

    @Nullable private QuestReward reward;

    private QuestFormatter formatter;

    private final String questId;
    private final boolean autoClaim;
    private boolean allowParallel;

    public Quest(String questId, String displayName, boolean autoClaim) {
        this.questId = questId.toLowerCase(Locale.ROOT);
        this.displayName = displayName;
        this.objectives = new HashMap<>();
        this.autoClaim = autoClaim;
        this.formatter = QuestManager.formatter();
    }

    public Quest(String questId, String displayName) {
        this(questId, displayName, true);
    }

    public void setFormatter(@Nonnull QuestFormatter formatter) {
        this.formatter = formatter;
    }

    public void addReward(QuestReward reward) {
        this.reward = reward;
    }

    public void allowParallel(boolean allowParallel) {
        this.allowParallel = allowParallel;
    }

    public QuestFormatter getFormatter() {
        return formatter;
    }

    public boolean isAllowParallel() {
        return allowParallel;
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
        final QuestManager manager = QuestManager.current();
        if (!this.isValid()) {
            Chat.sendMessage(player, "&cCould not invalid quest (%s), please report this!", this.getQuestName());
            return;
        }

        if (!isAllowParallel() && manager.hasQuest(player, this)) {
            Chat.sendMessage(player, "&cYou already started this quest!");
            return;
        }

        manager.startQuest(player, this);
        this.sendQuestInfo(player);
    }

    public String getQuestId() {
        return questId;
    }

    public final boolean isValid() {
        return !questId.isBlank() && !this.displayName.isBlank() && this.objectives.size() != 0;
    }

    /**
     * Inserts QuestObjective to the next stage
     *
     * @param questObjective - Objective
     */
    public void addStage(QuestObjective questObjective) {
        this.objectives.put(this.objectives.size(), questObjective);
    }

    public void addObjective(QuestObjective questObjective) {
        addStage(questObjective);
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

    public void setObjective(int index, QuestObjective questObjective) {
        setStage(index, questObjective);
    }

    public Map<Integer, QuestObjective> getObjectives() {
        return objectives;
    }

    public QuestObjective getCurrentObjective() {
        return objectives.get(0);
    }

    public String getQuestName() {
        return displayName;
    }

    private void sendQuestInfo(Player player) {
        formatter.sendQuestStartedFormat(player, this);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Quest quest = (Quest) other;
        return questId.equalsIgnoreCase(quest.getQuestId()); // now using string id's to compare quests
    }

}
