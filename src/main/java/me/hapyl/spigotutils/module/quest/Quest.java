package me.hapyl.spigotutils.module.quest;

import me.hapyl.spigotutils.builtin.gui.QuestJournal;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.event.quest.QuestStartEvent;
import me.hapyl.spigotutils.module.quest.objectives.Objectives;
import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Bukkit;
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
    private boolean autoClaim;

    private long timeLimit;

    /**
     * Creates a new quest.
     *
     * @param questId     - ID of the quest; Will be lowercase.
     * @param displayName - Display name of the quest.
     * @param autoClaim   - If true, rewards will be auto claimed upon completion.
     */
    public Quest(String questId, String displayName, boolean autoClaim) {
        this.questId = questId.toLowerCase(Locale.ROOT);
        this.displayName = displayName;
        this.objectives = new HashMap<>();
        this.autoClaim = autoClaim;
        this.timeLimit = 0;
        this.formatter = QuestFormatter.DEFAULT;
    }

    /**
     * Creates a new quest.
     *
     * @param questId     - ID of the quest; Will be lowercase.
     * @param displayName - Display name of the quest.
     */
    public Quest(String questId, String displayName) {
        this(questId, displayName, true);
    }

    /**
     * Returns time limit of the quest or 0 if no limit.
     *
     * @return time limit of the quest or 0 if no limit.
     */
    public long getTimeLimit() {
        return timeLimit;
    }

    /**
     * Returns true if quest has time limit, false otherwise.
     *
     * @return true if quest has time limit, false otherwise.
     */
    public boolean hasTimeLimit() {
        return timeLimit > 0;
    }

    /**
     * Sets time limit of the quest to 0.
     */
    public void setNoTimeLimit() {
        setTimeLimit(0);
    }

    /**
     * Sets time limit of the quest to provided value in millis.
     *
     * @param millis - Time limit in milliseconds.
     */
    public void setTimeLimit(long millis) {
        this.timeLimit = millis;
    }

    /**
     * Sets time limit of the quest to provided value in ticks.
     *
     * @param ticks - Time limit in ticks.
     */
    public void setTimeLimitTicks(long ticks) {
        setTimeLimit(ticks * 50L);
    }

    /**
     * Sets the quest formatter.
     *
     * @param formatter - New formatter.
     * @throws NullPointerException if new formatter is null.
     */
    public void setFormatter(@Nonnull QuestFormatter formatter) {
        Validate.notNull(formatter, "quest formatter cannot be null, use QuestFormatter.EMPTY to remove formatter");
        this.formatter = formatter;
    }

    /**
     * Returns QuestFormatter of the quest.
     *
     * @return QuestFormatter of the quest.
     */
    @Nonnull
    public QuestFormatter getFormatter() {
        return formatter;
    }

    /**
     * Sets a reward to the quest.
     *
     * @param reward - New reward.
     */
    public void addReward(QuestReward reward) {
        this.reward = reward;
    }

    /**
     * Returns if quest has a reward, false otherwise.
     *
     * @return if quest has a reward, false otherwise.
     */
    public boolean hasReward() {
        return reward != null;
    }

    /**
     * Returns quest reward if exists, null otherwise.
     *
     * @return quest reward if exists, null otherwise.
     */
    @Nullable
    public QuestReward getReward() {
        return reward;
    }

    /**
     * Returns true if reward will be automatically claimed upon completing the quest.
     *
     * @return true if reward will be automatically claimed upon completing the quest.
     */
    public final boolean isAutoClaim() {
        return autoClaim;
    }

    /**
     * Sets automatic reward claiming.
     * <b>Note that if {@param autoClaim} is false, plugin must implement a way to
     * grant rewards if {@link QuestJournal} is disabled.</b>
     *
     * @param autoClaim - Should auto claim. Default is true.
     */
    public final void setAutoClaim(boolean autoClaim) {
        this.autoClaim = autoClaim;
    }

    /**
     * Attempts to start a quest for a player.
     *
     * @param player - Player.
     */
    public final void startQuest(Player player) {
        final QuestManager manager = QuestManager.current();
        if (!this.isValid()) {
            Chat.sendMessage(player, "&cCould not invalid quest (%s), please report this!", this.getQuestName());
            return;
        }

        if (manager.hasQuest(player, this)) {
            Chat.sendMessage(player, "&cYou already started this quest!");
            return;
        }

        final QuestStartEvent event = new QuestStartEvent(player, this);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        manager.startQuest(player, this);
        sendQuestInfo(player);
    }

    /**
     * Returns quest ID.
     *
     * @return quest ID.
     */
    public String getQuestId() {
        return questId;
    }

    /**
     * @return true if quest is valid.
     */
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

    /**
     * Adds a next objective for this quest.
     *
     * @param questObjective - Objective. {@link Objectives}
     */
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
