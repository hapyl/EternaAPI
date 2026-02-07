package me.hapyl.eterna.module.player.quest;

import me.hapyl.eterna.module.player.quest.objective.QuestObjective;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Optional;

/**
 * Represents a {@link QuestData} for a given {@link Player} and {@link Quest}.
 */
public class QuestData {
    
    private final Player player;
    private final Quest quest;
    private final long startedAt;
    
    private int currentStage;
    private double currentStageProgress;
    
    private long completedAt;
    
    QuestData(@NotNull Player player, @NotNull Quest quest, final long startedAt) {
        this.player = player;
        this.quest = quest;
        this.startedAt = Math.max(0, startedAt);
        this.completedAt = 0L;
    }
    
    /**
     * Gets the {@link Player} this data belongs to.
     *
     * @return the player this data belongs to.
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Gets the {@link Quest} for this data.
     *
     * @return the quest for this data.
     */
    @NotNull
    public Quest getQuest() {
        return quest;
    }
    
    /**
     * Gets the time, in millis, when the {@link Quest} was started.
     *
     * @return the time, in millis, when the quest was started.
     */
    public long getStartedAt() {
        return startedAt;
    }
    
    /**
     * Gets whether the {@link Quest} is completed.
     *
     * @return {@code true} if the quest is completed; {@code false} otherwise.
     */
    public boolean isCompleted() {
        return completedAt > 0L;
    }
    
    /**
     * Gets the time, in millis, when this {@link Quest} was completed.
     *
     * <p>
     * This method returns {@code 0L} if the quest is not completed yet.
     * </p>
     *
     * @return the time, in millis, when this quest was completed.
     */
    public long getCompletedAt() {
        return completedAt;
    }
    
    /**
     * Gets the current stage (index).
     *
     * @return the current stage (index).
     */
    public int getCurrentStage() {
        return this.currentStage;
    }
    
    /**
     * Sets the current stage, which will be clamped between {@code 0} - {@code objectiveCount - 1}.
     *
     * @param currentStage - The stage to set.
     */
    public void setCurrentStage(int currentStage) {
        this.currentStage = Math.clamp(currentStage, 0, quest.getObjectiveCount() - 1);
    }
    
    /**
     * Gets the current stage progress.
     *
     * @return the current stage progress.
     */
    public double getCurrentStageProgress() {
        return this.currentStageProgress;
    }
    
    /**
     * Sets the current stage progress.
     *
     * @param currentStageProgress - The progress to set.
     */
    public void setCurrentStageProgress(double currentStageProgress) {
        this.currentStageProgress = Math.max(0, currentStageProgress);
    }
    
    /**
     * Gets the current {@link QuestObjective} wrapped in an {@link Optional}.
     *
     * @return the current quest objective wrapped in an optional.
     */
    @NotNull
    public Optional<QuestObjective> getCurrentObjective() {
        return getCurrentObjective(QuestObjective.class);
    }
    
    /**
     * Gets the current {@link QuestObjective} wrapped in an {@link Optional}.
     *
     * <p>
     * If the current objective is not of the {@code expectedClass} type, an empty {@link Optional} will be returned.
     * </p>
     *
     * @param expectedClass - The expected objective class.
     * @return the current quest objective wrapped in an optional.
     */
    @NotNull
    public <Q extends QuestObjective> Optional<Q> getCurrentObjective(@NotNull Class<Q> expectedClass) {
        final QuestObjective objective = quest.getObjective(currentStage);
        
        if (expectedClass.isInstance(objective)) {
            return Optional.of(expectedClass.cast(objective));
        }
        
        return Optional.empty();
    }
    
    /**
     * Gets the next {@link QuestObjective} wrapped in an {@link Optional}.
     *
     * <p>
     * If there is no next objective, an empty {@link Optional} will be returned.
     * </p>
     *
     * @return the next objective wrapped in an optional.
     */
    @NotNull
    public Optional<QuestObjective> getNextObjective() {
        return Optional.ofNullable(this.quest.getObjective(this.currentStage + 1));
    }
    
    /**
     * Gets the {@link String} representation of this {@link QuestData}.
     *
     * @return the string representation of this quest data.
     */
    @Override
    public String toString() {
        return "{player = %s, quest = %s, stage = %s, progress = %s, startedAt = %s, completedAt = %s}".formatted(
                player.getName(),
                quest.getKey(),
                currentStage,
                currentStageProgress,
                startedAt,
                completedAt
        );
    }
    
    /**
     * Marks this {@link QuestData} as complete.
     */
    @ApiStatus.Internal
    void markComplete() {
        if (!isCompleted()) {
            this.completedAt = System.currentTimeMillis();
        }
    }
    
    /**
     * A static factory method for creating {@link QuestData}.
     *
     * <p>
     * This method is designed to be used at {@link QuestRegistry#load(Player)} to load a runtime {@link QuestData} from a database of some sorts.
     * </p>
     *
     * @param player               - The player to load the data for.
     * @param quest                - The quest to load the data for.
     * @param currentStage         - The current stage.
     * @param currentStageProgress - The current stage progress.
     * @param startedAt            - The timestamp at when the quest was started.
     * @param completedAt          - The timestamp at when the quest was started, or {@code 0L} if not completed.
     * @return a new {@link QuestData}.
     */
    @NotNull
    public static QuestData load(
            @NotNull Player player,
            @NotNull Quest quest,
            final int currentStage,
            final double currentStageProgress,
            @Range(from = 0, to = Long.MAX_VALUE) final long startedAt,
            @Range(from = 0, to = Long.MAX_VALUE) final long completedAt
    ) {
        final QuestData data = new QuestData(player, quest, startedAt);
        
        data.currentStage = currentStage;
        data.currentStageProgress = currentStageProgress;
        data.completedAt = completedAt;
        
        return data;
    }
}
