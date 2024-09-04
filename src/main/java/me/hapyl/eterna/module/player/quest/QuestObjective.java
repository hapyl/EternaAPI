package me.hapyl.eterna.module.player.quest;

import me.hapyl.eterna.module.ai.goal.MoveToGoal;
import me.hapyl.eterna.module.annotate.EventLike;
import me.hapyl.eterna.module.player.dialog.NPCDialog;
import me.hapyl.eterna.module.player.quest.objective.BreakBlockQuestObjective;
import me.hapyl.eterna.module.player.quest.objective.TalkInChatQuestObjective;
import me.hapyl.eterna.module.player.quest.objective.TalkToNpcQuestObjective;
import me.hapyl.eterna.module.reflect.npc.HumanNPC;
import me.hapyl.eterna.module.util.Described;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Range;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * The base objective class.
 * <p>If plugin wants to add an objective, they must implement the usage and execution of it.
 * See literally any objective implementation, it's pretty simple.</p>
 *
 * @see BreakBlockQuestObjective
 * @see TalkInChatQuestObjective
 * @see MoveToGoal
 */
public abstract class QuestObjective implements Described {

    private final double goal;
    private final Map<HumanNPC, NPCDialog> dialogs; // hash map is faster

    private String name;
    private String description;

    public QuestObjective(@Nonnull String name, @Nonnull String description, double goal) {
        this.name = name;
        this.description = description;
        this.goal = goal;
        this.dialogs = new HashMap<>();
    }

    /**
     * Sets the {@link NPCDialog} for the given {@link HumanNPC}.
     * <p>This is a 'magical' method - it stores the dialog for the given {@link HumanNPC},
     * and if the current objective of the {@link Quest} is {@link TalkToNpcQuestObjective},
     * the dialog is automatically started upon player clicking at the NPC! <i>(very magic, I know ✨)</i></p>
     *
     * @param dialog - The dialog to set.
     */
    public void setDialog(@Nonnull NPCDialog dialog) {
        this.dialogs.put(dialog.getNpc(), dialog);
    }

    /**
     * Gets the {@link NPCDialog} for the given {@link HumanNPC}, or {@code null} if it doesn't exist.
     *
     * @param npc - The npc to get the dialog for.
     * @return the dialog for the given npc, or null if it doesn't exist.
     */
    @Nullable
    public NPCDialog getDialog(@Nonnull HumanNPC npc) {
        return this.dialogs.get(npc);
    }

    /**
     * Gets the name of this quest.
     *
     * @return the name of this quest.
     */
    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this quest.
     *
     * @param name - The name to set.
     */
    @Override
    public void setName(@Nonnull String name) {
        this.name = name;
    }

    /**
     * Gets the description of this quest.
     *
     * @return the description of this quest.
     */
    @Nonnull
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this quest.
     *
     * @param description - The description to set.
     */
    @Override
    public void setDescription(@Nonnull String description) {
        this.description = description;
    }

    /**
     * Gets the goal of this objective.
     * <p>The objective is considered as 'complete' if player's progress {@code >=} than the goal.</p>
     *
     * @return the goal of this objective.
     */
    public final double getGoal() {
        return goal;
    }

    /**
     * Tests the given {@link QuestObjectArray} and returns a {@link Response}.
     *
     * @return the response of this test.
     * @see Response
     */
    @Nonnull
    public abstract Response test(@Nonnull QuestData data, @Nonnull QuestObjectArray object);

    /**
     * Called whenever this objective progress is incremented.
     *
     * @param player - Player for whom the objective progress is incremented.
     * @param value  - The value by which the progress is incremented.
     */
    @EventLike
    public void onIncrement(@Nonnull Player player, double value) {
    }

    /**
     * Called whenever this objective is started.
     *
     * @param player - Player for whom the objective was started.
     */
    @EventLike
    public void onStart(@Nonnull Player player) {
    }

    /**
     * Called whenever this objective is completed.
     *
     * @param player - Player who completed the objective.
     */
    @EventLike
    public void onComplete(@Nonnull Player player) {
    }

    /**
     * Called whenever this objective is failed.
     *
     * @param player - Player who failed the objective.
     */
    @EventLike
    public void onFail(@Nonnull Player player) {
    }

    /**
     * Represents the {@link #test(QuestData, QuestObjectArray)} response.
     */
    public static final class Response {

        /**
         * Indicates that the {@link QuestObjective#test(QuestData, QuestObjectArray)} has failed,
         * and the objective progress should <b>not</b> be incremented.
         */
        public static final Response TEST_FAILED = new Response(0.0d);

        /**
         * Indicates that the {@link QuestObjective#test(QuestData, QuestObjectArray)} has failed,
         * and the objective should be considered as 'failed' and the progress of it is reset.
         */
        public static final Response OBJECTIVE_FAILED = new Response(-1.0d);

        private final double magicNumber;

        private Response(double magicNumber) {
            this.magicNumber = magicNumber;
        }

        /**
         * Gets the magic number.
         *
         * @return the magic number.
         */
        @ApiStatus.Internal
        public double getMagicNumber() {
            return magicNumber;
        }

        /**
         * Returns {@code true} if the response means 'test failed', {@code false} otherwise.
         *
         * @return {@code true} if the response means 'test failed', {@code false} otherwise.
         */
        public boolean isTestFailed() {
            return magicNumber == TEST_FAILED.magicNumber;
        }

        /**
         * Returns {@code true} if the response means 'objective failed', {@code false} otherwise.
         *
         * @return {@code true} if the response means 'objective failed', {@code false} otherwise.
         */
        public boolean isObjectiveFailed() {
            return magicNumber == OBJECTIVE_FAILED.magicNumber;
        }

        /**
         * Indicates that the {@link QuestObjective#test(QuestData, QuestObjectArray)} has succeeded,
         * and the objective progress should be incremented by the given value.
         *
         * @param value - The value by which to increment the objective progress.
         */
        @Nonnull
        public static Response testSucceeded(@Range(from = 1, to = Integer.MAX_VALUE) @Nonnull Double value) {
            return new Response(value);
        }

        /**
         * Indicates that the {@link QuestObjective#test(QuestData, QuestObjectArray)} has succeeded,
         * and the objective progress should be incremented by {@code 1}.
         */
        @Nonnull
        public static Response testSucceeded() {
            return new Response(1.0d);
        }

        /**
         * Indicates that the {@link QuestObjective#test(QuestData, QuestObjectArray)} has failed,
         * and the objective progress should <b>not</b> be incremented.
         */
        @Nonnull
        public static Response testFailed() {
            return TEST_FAILED;
        }

        /**
         * Indicates that the {@link QuestObjective#test(QuestData, QuestObjectArray)} has failed,
         * and the objective should be considered as 'failed' and the progress of it is reset.
         */
        @Nonnull
        public static Response objectiveFailed() {
            return OBJECTIVE_FAILED;
        }

    }
}
