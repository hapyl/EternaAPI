package kz.hapyl.spigotutils.module.quest.objectives;

import kz.hapyl.spigotutils.module.quest.QuestObjective;
import kz.hapyl.spigotutils.module.quest.QuestObjectiveType;

public class Jump extends QuestObjective {
    public Jump(int times) {
        super(QuestObjectiveType.JUMP, times, "Jumper", String.format("Jump %s times.", times));
    }

    @Override
    public double testQuestCompletion(Object... objects) {
        return 1.0f;
    }
}
