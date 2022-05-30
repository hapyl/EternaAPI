package me.hapyl.spigotutils.module.quest.objectives;

import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.quest.QuestObjective;
import me.hapyl.spigotutils.module.quest.QuestObjectiveType;
import me.hapyl.spigotutils.module.util.Validate;
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
        if (objects.length == 2) {
            if (objects[1] instanceof LivingEntity && ((LivingEntity) objects[1]).getType() == this.type) {
                return Validate.getDouble(objects[0]);
            }
        }
        return -1.0d;
    }
}
