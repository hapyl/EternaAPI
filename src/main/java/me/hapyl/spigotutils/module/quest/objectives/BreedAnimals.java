package me.hapyl.spigotutils.module.quest.objectives;

import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.quest.QuestObjective;
import me.hapyl.spigotutils.module.quest.QuestObjectiveType;
import org.bukkit.entity.Breedable;
import org.bukkit.entity.EntityType;

public class BreedAnimals extends QuestObjective {

    private final EntityType type;

    public BreedAnimals(EntityType type, long goalTotal) {
        super(
                QuestObjectiveType.BREED_ANIMALS,
                goalTotal,
                String.format("%s Breeder", Chat.capitalize(type)),
                String.format("Bread %s %s time%s.", Chat.capitalize(type), goalTotal, goalTotal == 1 ? "" : "s")
        );
        this.type = type;
    }

    public double testQuestCompletion(Object... objects) {
        final Object obj = objects[0];
        if (obj instanceof Breedable) {
            return (((Breedable) obj).getType() == type) ? 1 : -1;
        }
        return -1;
    }
}
