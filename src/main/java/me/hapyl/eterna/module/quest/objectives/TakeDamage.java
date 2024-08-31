package me.hapyl.eterna.module.quest.objectives;

import me.hapyl.eterna.module.quest.QuestObjective;
import me.hapyl.eterna.module.quest.QuestObjectiveType;
import me.hapyl.eterna.module.util.Validate;

public class TakeDamage extends QuestObjective {
    public TakeDamage(double goalTotal) {
        super(QuestObjectiveType.TAKE_DAMAGE, goalTotal, "Ouch!", String.format("Take %s damage from any source.", goalTotal));
    }

    @Override
    public double testQuestCompletion(Object... objects) {
        return 0;
    }
}
