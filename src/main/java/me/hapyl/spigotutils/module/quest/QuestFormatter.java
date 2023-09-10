package me.hapyl.spigotutils.module.quest;

import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.util.Formatter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public interface QuestFormatter extends Formatter {

    QuestFormatter EMPTY = new QuestFormatter() {
        @Override
        public void sendObjectiveNew(@Nonnull Player player, @Nonnull QuestObjective quest) {
        }

        @Override
        public void sendObjectiveComplete(@Nonnull Player player, @Nonnull QuestObjective objective) {
        }

        @Override
        public void sendObjectiveFailed(@Nonnull Player player, @Nonnull QuestObjective quest) {
        }

        @Override
        public void sendQuestCompleteFormat(@Nonnull Player player, @Nonnull Quest quest) {
        }

        @Override
        public void sendQuestStartedFormat(@Nonnull Player player, @Nonnull Quest quest) {
        }

        @Override
        public void sendQuestFailedFormat(@Nonnull Player player, @Nonnull Quest quest) {
        }
    };

    QuestFormatter DEFAULT = new QuestFormatter() {

        @Override
        public void sendObjectiveNew(@Nonnull Player player, @Nonnull QuestObjective objective) {
            Chat.sendMessage(player, "");
            Chat.sendCenterMessage(player, "&e&lNEW OBJECTIVE!");
            Chat.sendCenterMessage(player, "&6" + objective.getObjectiveName());
            Chat.sendCenterMessage(player, "&7" + objective.getObjectiveShortInfo());
            Chat.sendMessage(player, "");
        }

        @Override
        public void sendObjectiveComplete(@Nonnull Player player, @Nonnull QuestObjective objective) {
            Chat.sendMessage(player, "");
            Chat.sendCenterMessage(player, "&2&lOBJECTIVE COMPLETE!");
            Chat.sendCenterMessage(player, "&a✔ " + objective.getObjectiveName());
            Chat.sendMessage(player, "");
        }

        @Override
        public void sendObjectiveFailed(@Nonnull Player player, @Nonnull QuestObjective objective) {
            Chat.sendMessage(player, "");
            Chat.sendCenterMessage(player, "&c&lOBJECTIVE FAILED!");
            Chat.sendCenterMessage(player, "&7It's ok! Try again.");
            Chat.sendMessage(player, "");
        }

        @Override
        public void sendQuestCompleteFormat(@Nonnull Player player, @Nonnull Quest quest) {
            sendLine(player);
            Chat.sendCenterMessage(player, "&6&lQUEST COMPLETE");
            Chat.sendCenterMessage(player, "&f" + quest.getQuestName());

            if (!quest.isAutoClaim()) {
                Chat.sendMessage(player, "");
                Chat.sendCenterMessage(player, "&7Navitage to Quest Journal to claim reward!");
            }

            sendLine(player);
        }

        @Override
        public void sendQuestStartedFormat(@Nonnull Player player, @Nonnull Quest quest) {
            sendLine(player);
            Chat.sendCenterMessage(player, "&6&lQuest Started");
            Chat.sendCenterMessage(player, "&f" + quest.getQuestName());
            Chat.sendCenterMessage(player, "");
            Chat.sendCenterMessage(player, "&aCurrent Objective:");
            Chat.sendCenterMessage(player, "&7&o" + quest.getCurrentObjective().getObjectiveName());
            Chat.sendCenterMessage(player, "&7&o" + quest.getCurrentObjective().getObjectiveShortInfo());
            sendLine(player);
        }

        @Override
        public void sendQuestFailedFormat(@Nonnull Player player, @Nonnull Quest quest) {
            sendLine(player);
            Chat.sendCenterMessage(player, "&c&lQUEST FAILED");
            Chat.sendCenterMessage(player, "&f" + quest.getQuestName());
            sendLine(player);
        }

        private void sendLine(Player player) {
            Chat.sendCenterMessage(player, "&e&m]                                      [");
        }
    };

    /**
     * Sent upon started a new OBJECTIVE, not QUEST.
     *
     * @param player - Player.
     * @param quest  - Quest that objective belongs to.
     */
    void sendObjectiveNew(@Nonnull Player player, @Nonnull QuestObjective quest);

    /**
     * Sent upon completing an OBJECTIVE, not QUEST.
     *
     * @param player    - Player.
     * @param objective - Completed objective.
     */
    void sendObjectiveComplete(@Nonnull Player player, @Nonnull QuestObjective objective);

    /**
     * Sent upon failing an OBJECTIVE, not QUEST.
     *
     * @param player - Player.
     * @param quest  - Quest that objective belongs to.
     */
    void sendObjectiveFailed(@Nonnull Player player, @Nonnull QuestObjective quest);

    /**
     * Sent upon completing a QUEST.
     *
     * @param player - Player.
     * @param quest  - Completed quest.
     */
    void sendQuestCompleteFormat(@Nonnull Player player, @Nonnull Quest quest);

    /**
     * Sent upon started a QUEST.
     *
     * @param player - Player.
     * @param quest  - Started quest.
     */
    void sendQuestStartedFormat(@Nonnull Player player, @Nonnull Quest quest);

    /**
     * Sent upon failing a QUEST.
     *
     * @param player - Player.
     * @param quest  - Failed quest.
     * @deprecated Failing is not yet implemented.
     */
    @Deprecated
    void sendQuestFailedFormat(@Nonnull Player player, @Nonnull Quest quest);

}
