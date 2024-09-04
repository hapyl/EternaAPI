package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.player.quest.QuestObjectArray;
import me.hapyl.eterna.module.player.quest.QuestData;
import me.hapyl.eterna.module.player.quest.QuestObjective;

import javax.annotation.Nonnull;

/**
 * A {@link QuestObjective} for the completion of which the player must jump.
 */
public class JumpQuestObjective extends QuestObjective {

    /**
     * Creates a new objective for the completion of which the player must jump.
     *
     * @param goal - The total number of times to jump.
     */
    public JumpQuestObjective(double goal) {
        super("Jumper", "Jump %s times.".formatted(goal), goal);
    }

    @Nonnull
    @Override
    public Response test(@Nonnull QuestData data, @Nonnull QuestObjectArray object) {
        return Response.testSucceeded();
    }
}
