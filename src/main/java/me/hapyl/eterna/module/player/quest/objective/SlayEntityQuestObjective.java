package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.player.quest.QuestObjectArray;
import me.hapyl.eterna.module.player.quest.QuestData;
import me.hapyl.eterna.module.player.quest.QuestObjective;
import org.bukkit.entity.EntityType;

import javax.annotation.Nonnull;

/**
 * A {@link QuestObjective} for the completion of which the player must slay an entity.
 */
public class SlayEntityQuestObjective extends QuestObjective {

    public final EntityType entityType;

    /**
     * Creates a new objective for the completion of which the player must slay an entity.
     *
     * @param entityType - The entity type to slay.
     * @param goal       - The total number of times to slay the entity.
     */
    public SlayEntityQuestObjective(@Nonnull EntityType entityType, double goal) {
        super("Slayers", "Slay %s %s times.".formatted(Chat.capitalize(entityType.getKey().getKey()), goal), goal);

        this.entityType = entityType;
    }

    @Nonnull
    @Override
    public Response test(@Nonnull QuestData data, @Nonnull QuestObjectArray object) {
        return object.compareAs(EntityType.class, entityType -> {
            return this.entityType == entityType;
        });
    }
}
