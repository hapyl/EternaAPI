package me.hapyl.eterna.module.player.quest.objective;

import me.hapyl.eterna.module.player.quest.QuestData;
import me.hapyl.eterna.module.text.Capitalizable;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link QuestObjective} for the completion of which the {@link Player} must kill an {@link Entity}.
 */
public class QuestObjectiveKillEntity extends QuestObjective {
    
    private final EntityType entityType;
    
    /**
     * Creates a new {@link QuestObjectiveKillEntity}.
     *
     * @param entityType - The entity type to kill.
     * @param goal       - The total number of times to slay the entity.
     */
    public QuestObjectiveKillEntity(@NotNull EntityType entityType, final int goal) {
        super(Component.text("Slay %s %s times.".formatted(Capitalizable.capitalize(entityType.getKey().getKey()), goal)), goal);
        
        this.entityType = entityType;
    }
    
    @NotNull
    @Override
    public Response test(@NotNull QuestData questData, @NotNull QuestObjectArray array) {
        return Response.ofBoolean(this.entityType == array.get(0, EntityType.class));
    }
}
