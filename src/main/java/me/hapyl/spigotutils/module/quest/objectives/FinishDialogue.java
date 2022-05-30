package me.hapyl.spigotutils.module.quest.objectives;

import me.hapyl.spigotutils.module.quest.QuestObjective;
import me.hapyl.spigotutils.module.quest.QuestObjectiveType;
import me.hapyl.spigotutils.module.reflect.npc.HumanNPC;

public class FinishDialogue extends QuestObjective {

    private final HumanNPC npc;

    public FinishDialogue(HumanNPC npc) {
        super(QuestObjectiveType.FINISH_DIALOGUE, 1, "Listener", String.format("Talk and Listen to %s.", npc.getName()));
        this.npc = npc;
    }

    @Override
    public double testQuestCompletion(Object... objects) {
        return super.validateArgument(0, this.npc, objects);
    }
}
