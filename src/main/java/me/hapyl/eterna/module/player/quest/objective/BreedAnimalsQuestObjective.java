package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.player.quest.QuestObjectArray;
import me.hapyl.eterna.module.player.quest.QuestData;
import me.hapyl.eterna.module.player.quest.QuestObjective;
import me.hapyl.eterna.module.util.Validate;
import org.bukkit.entity.Breedable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import javax.annotation.Nonnull;

/**
 * A {@link QuestObjective} for the completion of which the player must breed animals.
 */
public class BreedAnimalsQuestObjective extends QuestObjective {

    public final EntityType entityType;

    /**
     * Creates a new objective for the completion of which the player must breed animals.
     *
     * @param entityType - The type of the animal to breed.
     * @param goal       - The total number of times to breed.
     * @throws IllegalArgumentException If the given entity type is not {@link Breedable}.
     */
    public BreedAnimalsQuestObjective(@Nonnull EntityType entityType, double goal) {
        super("Breed %s %s times.".formatted(Chat.capitalize(entityType.getKey().getKey()), goal), goal);

        this.entityType = Validate.isTrue(entityType, type -> {
            final Class<? extends Entity> entityClass = type.getEntityClass();

            return entityClass != null && Breedable.class.isAssignableFrom(entityClass);
        }, "Entity must be breedable!");
    }

    @Nonnull
    @Override
    public Response test(@Nonnull QuestData data, @Nonnull QuestObjectArray object) {
        return object.compareAs(EntityType.class, entityType -> {
            return this.entityType == entityType;
        });
    }
}
