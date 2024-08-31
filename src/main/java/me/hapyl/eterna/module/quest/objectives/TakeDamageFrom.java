package me.hapyl.eterna.module.quest.objectives;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.quest.QuestObjective;
import me.hapyl.eterna.module.quest.QuestObjectiveType;
import me.hapyl.eterna.module.util.Validate;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class TakeDamageFrom extends QuestObjective {

    private final EntityType type;

    public TakeDamageFrom(double amount, EntityType type) {
        super(QuestObjectiveType.TAKE_DAMAGE_FROM, amount, "Ouch", String.format("Take %s damage from %s.", amount, Chat.capitalize(type)));
        this.type = type;
    }

    @Override
    public double testQuestCompletion(Object... objects) {
        if (objects == null) {
            return -1.0d;
        }

        if (objects.length == 2) {
            if (objects[1] instanceof LivingEntity && ((LivingEntity) objects[1]).getType() == this.type) {
                return 0;
            }
        }
        return -1.0d;
    }
}
