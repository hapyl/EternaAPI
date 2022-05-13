package kz.hapyl.spigotutils.module.quest;

import org.bukkit.entity.Player;

public interface QuestFormatter {

    void sendObjectiveNew(Player player, QuestObjective quest);

    void sendObjectiveComplete(Player player, QuestObjective objective);

    void sendObjectiveFailed(Player player, QuestObjective quest);

    void sendQuestCompleteFormat(Player player, Quest quest);

    void sendQuestStartedFormat(Player player, Quest quest);

    void sendQuestFailedFormat(Player player, Quest quest);

}
