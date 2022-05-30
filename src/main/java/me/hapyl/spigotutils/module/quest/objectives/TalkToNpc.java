package me.hapyl.spigotutils.module.quest.objectives;

import me.hapyl.spigotutils.module.quest.QuestObjective;
import me.hapyl.spigotutils.module.quest.QuestObjectiveType;
import me.hapyl.spigotutils.module.reflect.npc.HumanNPC;

public class TalkToNpc extends QuestObjective {

    private final HumanNPC npc;

    public TalkToNpc(HumanNPC npc, int times) {
        super(QuestObjectiveType.TALK_TO_NPC, times, "Talker", String.format("Talk to %s.", npc.getName()));
        this.npc = npc;
    }

    @Override
    public double testQuestCompletion(Object... objects) {
        return super.validateArgument(0, this.npc, objects);
    }
}
