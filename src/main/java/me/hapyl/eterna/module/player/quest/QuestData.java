package me.hapyl.eterna.module.player.quest;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.builtin.Debuggable;
import me.hapyl.eterna.builtin.manager.QuestManager;
import me.hapyl.eterna.module.util.Runnables;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * Represents the runtime quest data for the given player.
 */
@ApiStatus.Internal
public class QuestData implements Debuggable {
    
    private final Player player;
    private final Quest quest;
    
    private int currentStage;
    private double currentStageProgress;
    
    public QuestData(@Nonnull Player player, @Nonnull Quest quest) {
        this.player = player;
        this.quest = quest;
    }
    
    /**
     * Gets the player of this data.
     *
     * @return the player of this data.
     */
    @Nonnull
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Gets the quest of this data.
     *
     * @return the quest of this data.
     */
    @Nonnull
    public Quest getQuest() {
        return quest;
    }
    
    /**
     * Gets the current objective, or {@code null} if there is no objective.
     * <p>Realistically this will never returns null.</p>
     *
     * @return the current objective.
     */
    @Nullable
    public QuestObjective getCurrentObjective() {
        return quest.getObjective(currentStage);
    }
    
    /**
     * Gets the current stage of the quest.
     *
     * @return the current stage of the quest.
     */
    public int getCurrentStage() {
        return currentStage;
    }
    
    /**
     * Gets the current progress of the objective.
     * <p>The objective is considered as 'complete' if player's progress {@code >=} than the goal.</p>
     *
     * @return the current progress of the objective.
     */
    public double getCurrentStageProgress() {
        return currentStageProgress;
    }
    
    /**
     * Attempts to increment the progress for the current objective if the objective is {@code instanceof} the given {@link Class},
     * and the {@link QuestObjective#test(QuestData, QuestObjectArray)} returns {@link QuestObjective.Response#testSucceeded()}.
     *
     * @param clazz    - Objective class.
     * @param consumer - Consumer to accept.
     * @param objects  - Object parameters if needed.
     *                 Will be wrapped with {@link QuestObjectArray} before passing it to the objective.
     */
    @ApiStatus.Internal // Do not call this method manually
    public <T extends QuestObjective> void tryIncrementProgress(@Nonnull Class<T> clazz, @Nonnull BiConsumer<T, QuestObjective.Response> consumer, @Nullable Object[] objects) {
        final QuestObjective currentObjective = quest.getObjective(currentStage);
        final QuestFormatter formatter = quest.getFormatter();
        
        if (!clazz.isInstance(currentObjective)) {
            return;
        }
        
        final T objective = clazz.cast(currentObjective);
        final QuestObjective.Response response = objective.test(this, new QuestObjectArray(objects));
        
        consumer.accept(objective, response);
        
        if (response.isObjectiveFailed()) {
            currentStageProgress = 0.0d;
            
            objective.onFail(player);
            formatter.sendObjectiveFailed(player, objective);
            return;
        }
        else if (response.isTestFailed()) {
            return;
        }
        else {
            final double increment = response.getMagicNumber();
            
            objective.onIncrement(player, increment);
            currentStageProgress += increment;
        }
        
        // Objective complete, go next
        if (currentStageProgress >= objective.getGoal()) {
            // Calling nextObjective() forces the next objective, call onComplete() here
            objective.onComplete(player);
            quest.onObjectiveComplete(player, this, objective);
            
            nextObjective();
        }
    }
    
    public boolean isComplete() {
        return quest.getObjective(currentStage) == null;
    }
    
    @ApiStatus.Internal
    protected void nextObjective() { // Should not call manually
        final QuestFormatter formatter = quest.getFormatter();
        final QuestObjective currentObjective = quest.getObjective(currentStage);
        
        if (currentObjective == null) {
            return;
        }
        
        currentStage++;
        currentStageProgress = 0.0d;
        
        // Display the complete objective
        formatter.sendObjectiveComplete(player, currentObjective);
        
        final QuestObjective nextObjective = quest.getObjective(currentStage);
        
        // Quest complete
        if (nextObjective == null) {
            final QuestManager questManager = Eterna.getManagers().quest;
            
            quest.onComplete(player, this);
            
            // Call handler if not repeatable
            if (!quest.isRepeatable()) {
                questManager.workHandlers(handler -> handler.completeQuest(player, quest));
            }
            
            // Delay quest complete message
            Runnables.runLater(() -> formatter.sendQuestCompleteFormat(player, quest), 30);
            return;
        }
        
        // Call onStart() right away
        nextObjective.onStart(player);
        
        // Display the objective complete message and then the next objective with a little delay
        Runnables.runLater(() -> formatter.sendObjectiveNew(player, nextObjective), 20);
        
    }
    
    @Nonnull
    @Override
    public String toDebugString() {
        return "QuestData{" +
                "quest=" + quest.getName() +
                ", currentStage=" + currentStage +
                ", currentStageProgress=" + currentStageProgress +
                '}';
    }
    
    /**
     * A factory method for loading {@link QuestData}.
     * <p>This method should <b>only</b> be used to load quest data!</p>
     *
     * @param handler              - Quest handler, insures that unregistered loading isn't possible.
     * @param player               - Player to load the data for.
     * @param quest                - Quest.
     * @param currentStage         - Current stage.
     * @param currentStageProgress - Current progress.
     * @return a new {@link QuestData}.
     */
    @Nonnull
    public static QuestData load(@Nonnull QuestHandler handler, @Nonnull Player player, @Nonnull Quest quest, int currentStage, double currentStageProgress) {
        Objects.requireNonNull(handler, "Handler must not be null!"); // ensure existing handler
        
        final QuestData data = new QuestData(player, quest);
        data.currentStage = currentStage;
        data.currentStageProgress = currentStageProgress;
        
        return data;
    }
}
