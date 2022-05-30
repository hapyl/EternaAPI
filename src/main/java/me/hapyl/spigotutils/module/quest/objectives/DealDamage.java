package me.hapyl.spigotutils.module.quest.objectives;

import me.hapyl.spigotutils.module.quest.QuestObjective;
import me.hapyl.spigotutils.module.quest.QuestObjectiveType;
import me.hapyl.spigotutils.module.util.Validate;

public class DealDamage extends QuestObjective {
    public DealDamage(long goalTotal) {
        super(QuestObjectiveType.DEAL_DAMAGE, goalTotal, "Damager", String.format("Deal %s damage to any mob.", goalTotal));
    }

    @Override
    public double testQuestCompletion(Object... objects) {
        if (objects.length == 1) {
            return Validate.getDouble(objects[0]);
        }
        return -1.0d;
    }
}
