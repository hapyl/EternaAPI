package me.hapyl.spigotutils.module.quest;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

/**
 * This used to store quest object for quest
 */
public abstract class QuestObjective {

    // backwards reference to quest?

    private String objectiveName;
    private String objectiveShortInfo;

    private final QuestObjectiveType type;
    private final double goalTotal;

    private boolean hidden;

    /**
     * Creates new Quest Objective.
     *
     * @param type      - Type of objective. Use CUSTOM for custom objectives.
     * @param goalTotal - Total goal of the objective.
     * @param name      - Name of the objective.
     * @param info      - Short info about the objective.
     */
    public QuestObjective(QuestObjectiveType type, double goalTotal, String name, String info) {
        this.type = type;
        this.goalTotal = goalTotal;
        this.objectiveName = name;
        this.objectiveShortInfo = info;
        this.hidden = false;
    }

    /**
     * Creates new Quest Objective.
     *
     * @param type      - Type of objective. Use CUSTOM for custom objectives.
     * @param goalTotal - Total goal of the objective.
     * @param name      - Name of the objective.
     * @param info      - Short info about the objective.
     * @param format    - Formatter.
     */
    public QuestObjective(QuestObjectiveType type, double goalTotal, String name, String info, Object... format) {
        this(type, goalTotal, name, String.format(info, format));
    }

    /**
     * Creates new Quest Objective.
     *
     * @param type      - Type of objective. Use CUSTOM for custom objectives.
     * @param goalTotal - Total goal of the objective.
     */
    public QuestObjective(QuestObjectiveType type, long goalTotal) {
        this(type, goalTotal, "Debug Objective", "This is a debug message!");
        Bukkit.getLogger().warning("Called debug constructor of QuestObjective!");
    }

    /**
     * Sets if objective should be should as ??? instead of name and info.
     *
     * @param hidden - New value.
     */
    public QuestObjective setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    /**
     * Returns true if objective is hidden and will be shown as ??? in quest journal.
     *
     * @return true if objective is hidden and will be shown as ??? in quest journal.
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Implement a test for completion.
     *
     * @param objects - Extra arguments. Nullable.
     * @return Amount of completed objective.
     */
    public abstract double testQuestCompletion(@Nullable Object... objects);

    /**
     * Executed after objective is completed.
     *
     * @param player - Player.
     * @param value  - Value of completion, always >= total goal.
     */
    public void afterObjectiveCompletion(Player player, double value) {
    }

    /**
     * Executed after objective is incremented.
     *
     * @param player - Player.
     * @param value  - Value of increment.
     */
    public void afterObjectiveIncrement(Player player, double value) {
    }

    /**
     * Validates argument for {@link QuestObjective#testQuestCompletion(Object...)} safely.
     *
     * @param index   - Index of arguments.
     * @param value   - Value to except.
     * @param objects - Arguments.
     * @return true if (value.equals(objects[index])).
     */
    protected double validateArgument(int index, Object value, Object... objects) {
        if (objects.length >= index) {
            if (value.equals(objects[index])) {
                return 1.0d;
            }
        }
        return -1.0d;
    }

    /**
     * Sets objectives new name.
     *
     * @param string - New name.
     */
    public QuestObjective setName(String string) {
        this.objectiveName = string;
        return this;
    }

    /**
     * Sets objectives new short info.
     *
     * @param string - New short info.
     */
    public QuestObjective setShortInfo(String string) {
        this.objectiveShortInfo = string;
        return this;
    }

    /**
     * Returns objective name.
     *
     * @return objective name.
     */
    public String getObjectiveName() {
        return objectiveName;
    }

    /**
     * Returns objective short info.
     *
     * @return objective short info.
     */
    public String getObjectiveShortInfo() {
        return objectiveShortInfo;
    }

    /**
     * Returns objective type.
     *
     * @return objective type.
     */
    public QuestObjectiveType getQuestType() {
        return type;
    }

    /**
     * Returns objective total goal.
     *
     * @return objective total goal.
     */
    public double getCompletionGoal() {
        return goalTotal;
    }

    protected final PlayerQuestObjective cloneAsPlayerObjective(QuestProgress progress) {
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
