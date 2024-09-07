package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.locaiton.Position;
import me.hapyl.eterna.module.player.quest.QuestData;
import me.hapyl.eterna.module.player.quest.QuestObjectArray;
import me.hapyl.eterna.module.player.quest.QuestObjective;
import org.bukkit.Location;

import javax.annotation.Nonnull;

/**
 * A {@link QuestObjective} for the completion of which the player must travel to the given {@link Position}.
 */
public class TravelToQuestObjective extends QuestObjective {

    public final Position position;

    /**
     * Creates a new objective for the completion of which the player must travel to the given {@link Position}.
     *
     * @param position - The position to travel to.
     */
    public TravelToQuestObjective(@Nonnull Position position) {
        super("Travel to around %s.".formatted(position.toString()), 1);

        this.position = position;
    }

    @Nonnull
    @Override
    public Response test(@Nonnull QuestData data, @Nonnull QuestObjectArray object) {
        return object.compareAs(Location.class, location -> {
            return this.position.contains(location.toVector());
        });
    }
}
