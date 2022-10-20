package me.hapyl.spigotutils.module.quest;

import me.hapyl.spigotutils.module.util.Formatter;
import org.bukkit.entity.Player;

public interface QuestFormatter extends Formatter {

    QuestFormatter EMPTY = new QuestFormatter() {
        @Override
        public void sendObjectiveNew(Player player, QuestObjective quest) {
        }

        @Override
        public void sendObjectiveComplete(Player player, QuestObjective objective) {
        }

        @Override
        public void sendObjectiveFailed(Player player, QuestObjective quest) {
        }

        @Override
        public void sendQuestCompleteFormat(Player player, Quest quest) {
        }

        @Override
        public void sendQuestStartedFormat(Player player, Quest quest) {
        }

        @Override
        public void sendQuestFailedFormat(Player player, Quest quest) {
        }
    };

    /**
     * Sent upon started a new OBJECTIVE, not QUEST.
     *
     * @param player - Player.
     * @param quest  - Quest that objective belongs to.
     */
    void sendObjectiveNew(Player player, QuestObjective quest);

    /**
     * Sent upon completing an OBJECTIVE, not QUEST.
     *
     * @param player    - Player.
     * @param objective - Completed objective.
     */
    void sendObjectiveComplete(Player player, QuestObjective objective);

    /**
     * Sent upon failing an OBJECTIVE, not QUEST.
     *
     * @param player - Player.
     * @param quest  - Quest that objective belongs to.
     */
    void sendObjectiveFailed(Player player, QuestObjective quest);

    /**
     * Sent upon completing a QUEST.
     *
     * @param player - Player.
     * @param quest  - Completed quest.
     */
    void sendQuestCompleteFormat(Player player, Quest quest);

    /**
     * Sent upon started a QUEST.
     *
     * @param player - Player.
     * @param quest  - Started quest.
     */
    void sendQuestStartedFormat(Player player, Quest quest);

    /**
     * Sent upon failing a QUEST.
     *
     * @param player - Player.
     * @param quest  - Failed quest.
     */
    void sendQuestFailedFormat(Player player, Quest quest);

}
