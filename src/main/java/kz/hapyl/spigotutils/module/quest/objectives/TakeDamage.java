package kz.hapyl.spigotutils.module.quest.objectives;

import kz.hapyl.spigotutils.module.quest.QuestObjective;
import kz.hapyl.spigotutils.module.quest.QuestObjectiveType;
import kz.hapyl.spigotutils.module.util.Validate;

public class TakeDamage extends QuestObjective {
    public TakeDamage(long goalTotal) {
        super(QuestObjectiveType.TAKE_DAMAGE, goalTotal, "Ouch!", String.format("Take %s damage from any source.", goalTotal));
    }

    @Override
    public double testQuestCompletion(Object... objects) {
        return Validate.getDouble(objects[0]);
    }
}
