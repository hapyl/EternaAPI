package kz.hapyl.spigotutils.module.quest.objectives;

import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.quest.QuestObjective;
import kz.hapyl.spigotutils.module.quest.QuestObjectiveType;
import kz.hapyl.spigotutils.module.util.Validate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class DealDamageTo extends QuestObjective {

    private final EntityType type;

    public DealDamageTo(double goalTotal, EntityType type) {
        super(
                QuestObjectiveType.DEAL_DAMAGE_TO,
                goalTotal,
                String.format("%s Damager", Chat.capitalize(type)),
                String.format("Deal %s damage to %s.", goalTotal, type.getKey().getKey())
        );
        this.type = type;
    }

    @Override
    public double testQuestCompletion(Object... objects) {
        if (objects.length == 2) {
            final double damage = Validate.getDouble(objects[0]);
            if (!(objects[1] instanceof Entity)) {
                return -1.0d;
            }
            if (damage > 0.0d && ((Entity) objects[1]).getType() == this.type) {
                return damage;
            }
        }
        return -1.0d;
    }
}
