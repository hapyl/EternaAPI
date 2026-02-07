package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.player.quest.QuestData;
import me.hapyl.eterna.module.text.Capitalizable;
import me.hapyl.eterna.module.util.Validate;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link QuestObjective} for the completion of which the {@link Player} must breed {@link Animals}.
 */
public class QuestObjectiveBreedAnimal extends QuestObjective {
    
    private final EntityType entityType;
    
    /**
     * Creates a new {@link QuestObjectiveBreedAnimal}.
     *
     * @param entityType - The type of the animal to breed.
     * @param goal       - The total number of times to breed.
     * @throws IllegalArgumentException if the given entity type is not {@link Breedable}.
     */
    public QuestObjectiveBreedAnimal(@NotNull EntityType entityType, final int goal) {
        super(Component.text("Breed %s %s times.".formatted(Capitalizable.capitalize(entityType.getKey().getKey()), goal)), goal);
        
        this.entityType = Validate.requireValid(entityType, type -> {
            final Class<? extends Entity> entityClass = type.getEntityClass();
            
            return entityClass != null && Breedable.class.isAssignableFrom(entityClass);
        }, "Entity must be breedable!");
    }
    
    /**
     * Gets the {@link EntityType} of the entity to breed.
     *
     * @return the entity type of the entity to breed.
     */
    @NotNull
    public EntityType getEntityType() {
        return entityType;
    }
    
    @NotNull
    @Override
    public Response test(@NotNull QuestData questData, @NotNull QuestObjectArray array) {
        return Response.ofBoolean(array.get(0, EntityType.class) == this.entityType);
    }
}
