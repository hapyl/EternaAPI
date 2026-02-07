package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.player.quest.QuestData;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link QuestObjective} for the completion of which the {@link Player} must jump.
 */
public class QuestObjectiveJump extends QuestObjective {
    
    /**
     * Creates a new {@link QuestObjectiveJump}.
     *
     * @param goal - The total number of times to jump.
     */
    public QuestObjectiveJump(final int goal) {
        super(Component.text("Jump %s times.".formatted(goal)), goal);
    }
    
    @NotNull
    @Override
    public Response test(@NotNull QuestData questData, @NotNull QuestObjectArray array) {
        return Response.testSucceeded();
    }
}
