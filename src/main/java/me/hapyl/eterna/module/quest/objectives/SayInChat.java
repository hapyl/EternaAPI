package me.hapyl.eterna.module.quest.objectives;

import me.hapyl.eterna.module.quest.QuestObjective;
import me.hapyl.eterna.module.quest.QuestObjectiveType;

public class SayInChat extends QuestObjective {

    private final String toSay;
    private final boolean allowAnyCase;

    public SayInChat(String toSay, boolean allowAnyCase) {
        super(QuestObjectiveType.SAY_IN_CHAT, 1, "I love talking!", String.format("Say %s in chat.", toSay));
        this.toSay = toSay;
        this.allowAnyCase = allowAnyCase;
    }

    @Override
    public double testQuestCompletion(Object... objects) {
        this.getQuestType().validateAcceptingClasses(objects);
        String subject = objects[0].toString();
        return this.allowAnyCase ? (this.toSay.equalsIgnoreCase(subject) ? 1.0d : -1.0d) : (this.toSay.equals(subject) ? 1.0d : -1.0d);
    }
}
