package me.hapyl.eterna.module.quest.objectives;

import me.hapyl.eterna.module.quest.QuestObjective;
import me.hapyl.eterna.module.quest.QuestObjectiveType;

public class UseCustomItem extends QuestObjective {

    private final String itemId;

    public UseCustomItem(String itemId, int times) {
        super(QuestObjectiveType.USE_CUSTOM_ITEM, times, "Work It!", String.format("Use custom item with Id %s %s times!", itemId, times));
        this.itemId = itemId;
    }

    @Override
    public double testQuestCompletion(Object... objects) {
        return objects[0].toString().equals(itemId) ? 1 : -1;
    }
}
