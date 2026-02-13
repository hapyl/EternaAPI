package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.annotate.EventLike;
import me.hapyl.eterna.module.annotate.SelfReturn;
import me.hapyl.eterna.module.component.Described;
import me.hapyl.eterna.module.player.quest.Quest;
import me.hapyl.eterna.module.player.quest.QuestData;
import me.hapyl.eterna.module.util.Buildable;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Represents a base {@link QuestObjective}.
 *
 * <p>
 * Note that the constructor has packet-private access to limit creating of objectives to Eterna.
 * </p>
 */
public abstract class QuestObjective implements Described {
    
    protected final double goal;
    
    @NotNull private Component description;
    @NotNull private Component detailedDescription;
    
    @NotNull private FailType failType;
    
    // Force package-private to delegate objectives creation to Eterna
    QuestObjective(@NotNull Component description, final double goal) {
        this.description = description;
        this.detailedDescription = Component.empty();
        this.goal = goal;
        this.failType = FailType.RESTART_OBJECTIVE;
    }
    
    /**
     * Gets the {@link FailType} for this {@link QuestObjective}.
     *
     * @return the fail type for this objective.
     */
    @NotNull
    public FailType getFailType() {
        return failType;
    }
    
    /**
     * Sets the {@link FailType} for this {@link QuestObjective}.
     *
     * @param failType - The fail type to set.
     */
    public void setFailType(@NotNull FailType failType) {
        this.failType = failType;
    }
    
    /**
     * Gets the description of this {@link QuestObjective}.
     *
     * @return the description of this objective.
     */
    @Override
    @NotNull
    public Component getDescription() {
        return description;
    }
    
    /**
     * Sets the description of this {@link QuestObjective}.
     *
     * @param description - The description to set.
     */
    @Override
    public void setDescription(@NotNull Component description) {
        this.description = description;
    }
    
    /**
     * Gets the optional detailed description of this {@link QuestObjective}, or {@link Component#empty()} if not set.
     *
     * @return the optional detailed description of this objective, or an empty component if not set.
     */
    @NotNull
    public Component getDetailedDescription() {
        return detailedDescription;
    }
    
    /**
     * Sets the optional detailed description of this {@link QuestObjective}.
     *
     * @param detailedDescription - The detailed description to set, or {@link Component#empty()} to unset.
     */
    public void setDetailedDescription(@NotNull Component detailedDescription) {
        this.detailedDescription = detailedDescription;
    }
    
    /**
     * Gets the {@code goal} of this {@link QuestObjective}.
     *
     * @return the {@code goal} of this objective.
     */
    public final double getGoal() {
        return goal;
    }
    
    /**
     * Tests the given {@link QuestObjectArray} and gets a {@link Response}.
     *
     * @param questData - The quest data.
     * @param array     - The object array.
     * @return the response on how to progress the objective.
     */
    @NotNull
    public abstract Response test(@NotNull QuestData questData, @NotNull QuestObjectArray array);
    
    /**
     * An event-like method that is called whenever this {@link QuestObjective} progress is incremented.
     *
     * @param player - The player who progressed this objective.
     * @param value  - The value by which the objective is progressed.
     */
    @EventLike
    public void onProgress(@NotNull Player player, final double value) {
    }
    
    /**
     * An event-like method that is called whenever this {@link QuestObjective} is started.
     *
     * @param player - The player who started the objective.
     */
    @EventLike
    public void onStart(@NotNull Player player) {
    }
    
    /**
     * An event-like method that is called whenever this {@link QuestObjective} is completed.
     *
     * @param player - The player who completed the objective.
     */
    @EventLike
    public void onComplete(@NotNull Player player) {
    }
    
    /**
     * An event-like method that is called whenever this {@link QuestObjective} is failed.
     *
     * @param player   - The player who failed the objective.
     * @param failType - The fail type of this objective.
     */
    @EventLike
    public void onFail(@NotNull Player player, @NotNull FailType failType) {
    }
    
    /**
     * Creates a {@link Builder} wrapper for {@link QuestObjective}.
     *
     * <p>
     * Can be used to modify this objective via builder-like chain calls.
     * </p>
     *
     * @param supplier - The supplier of the objective.
     * @param <O>      - The objective type.
     * @return a new builder.
     */
    @NotNull
    public static <O extends QuestObjective> Builder<O> builder(@NotNull Supplier<O> supplier) {
        return new Builder<>(supplier.get());
    }
    
    /**
     * Represents a {@link FailType} for {@link QuestObjective}.
     */
    public enum FailType {
        /**
         * Denotes that the {@link QuestObjective} progress is restarted upon failing it.
         */
        RESTART_OBJECTIVE,
        
        /**
         * Denotes that the {@link Quest} the {@link QuestObjective} belongs is failed upon failing the objective.
         */
        FAIL_QUEST
    }
    
    /**
     * Represents a {@link QuestObjective} builder to be used for chain calls.
     *
     * @param <O> - The objective type.
     */
    public static class Builder<O extends QuestObjective> implements Buildable<O> {
        
        private final O objective;
        
        Builder(@NotNull O objective) {
            this.objective = objective;
        }
        
        /**
         * Sets the description of this {@link QuestObjective}.
         *
         * @param description - The description to set.
         */
        @SelfReturn
        public Builder<O> description(@NotNull Component description) {
            this.objective.setDescription(description);
            return this;
        }
        
        /**
         * Sets the detailed description of this {@link QuestObjective}.
         *
         * @param detailedDescription - The detailed description to set.
         */
        @SelfReturn
        public Builder<O> detailedDescription(@NotNull Component detailedDescription) {
            this.objective.setDetailedDescription(detailedDescription);
            return this;
        }
        
        /**
         * Sets the {@link FailType} of this {@link QuestObjective}.
         *
         * @param failType - The fail type to set.
         */
        @SelfReturn
        public Builder<O> failType(@NotNull FailType failType) {
            this.objective.setFailType(failType);
            return this;
        }
        
        /**
         * Edits the underlying {@code O} {@link QuestObjective} via the given {@link Consumer}.
         *
         * @param consumer - The consumer to apply.
         */
        @SelfReturn
        public Builder<O> edit(@NotNull Consumer<O> consumer) {
            consumer.accept(objective);
            return this;
        }
        
        /**
         * Gets the {@code O} {@link QuestObjective}.
         *
         * @return the {@code O} objective.
         */
        @NotNull
        @Override
        public O build() {
            return objective;
        }
    }
    
    public static final class Response {
        
        private static final Response TEST_SUCCEEDED = new Response(1.0);
        private static final Response TEST_FAILED = new Response(0.0);
        
        private static final Response OBJECTIVE_FAILED = new Response(-1.0);
        
        private final double value;
        
        private Response(final double value) {
            this.value = value;
        }
        
        public double getValue() {
            return value;
        }
        
        public boolean isTestSucceeded() {
            return value > 0;
        }
        
        public boolean isTestFailed() {
            return Double.compare(value, TEST_FAILED.value) == 0;
        }
        
        public boolean isObjectiveFailed() {
            return Double.compare(value, OBJECTIVE_FAILED.value) == 0;
        }
        
        @NotNull
        public static Response testSucceeded(@Range(from = 1, to = Integer.MAX_VALUE) final double value) {
            return new Response(value);
        }
        
        @NotNull
        public static Response testSucceeded() {
            return TEST_SUCCEEDED;
        }
        
        @NotNull
        public static Response testFailed() {
            return TEST_FAILED;
        }
        
        @NotNull
        public static Response objectiveFailed() {
            return OBJECTIVE_FAILED;
        }
        
        @NotNull
        public static Response ofBoolean(boolean condition) {
            return condition ? testSucceeded() : testFailed();
        }
        
        @NotNull
        public static Response ofBoolean(boolean condition, double value) {
            return condition ? testSucceeded(value) : testFailed();
        }
        
        @NotNull
        public static Response ofBooleanOrFailObjective(boolean condition) {
            return condition ? testSucceeded() : objectiveFailed();
        }
    }
}
