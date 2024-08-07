package me.hapyl.eterna.module.quest.objectives;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.quest.QuestObjective;
import me.hapyl.eterna.module.quest.QuestObjectiveType;
import me.hapyl.eterna.module.util.Validate;
import org.bukkit.entity.EntityType;

public class SlayEntity extends QuestObjective {

    private final EntityType entityType;

    public SlayEntity(EntityType type, long goalTotal) {
        super(
                QuestObjectiveType.SLAY_ENTITY,
                goalTotal,
                Chat.capitalize(type) + " Slayer",
                String.format("Slay %s %s.", goalTotal, Chat.capitalize(type))
        );
        Validate.isTrue(type.isAlive(), "entity must be living entity");
        this.entityType = type;
    }

    @Override
    public double testQuestCompletion(Object... objects) {
        return super.validateArgument(0, this.entityType, objects);
    }
}
