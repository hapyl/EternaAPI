package me.hapyl.eterna.module.player.quest;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.player.PlayerLib;
import me.hapyl.eterna.module.util.Formatter;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Represents a quest formatter.
 */
public interface QuestFormatter extends Formatter {

    QuestFormatter EMPTY = new QuestFormatter() {
        @Override
        public void sendObjectiveNew(@Nonnull Player player, @Nonnull QuestObjective objective) {
        }

        @Override
        public void sendObjectiveComplete(@Nonnull Player player, @Nonnull QuestObjective objective) {
        }

        @Override
        public void sendObjectiveFailed(@Nonnull Player player, @Nonnull QuestObjective objective) {
        }

        @Override
        public void sendQuestCompleteFormat(@Nonnull Player player, @Nonnull Quest quest) {
        }

        @Override
        public void sendQuestStartedFormat(@Nonnull Player player, @Nonnull Quest quest) {
        }

        @Override
        public void sendCannotStartQuestAlreadyCompleted(@Nonnull Player player, @Nonnull Quest quest) {
        }

        @Override
        public void sendPreRequirementNotMet(@Nonnull Player player, @Nonnull QuestPreRequirement preRequirement) {
        }

        @Override
        public void sendCannotStartQuestAlreadyStarted(@Nonnull Player player, @Nonnull Quest quest) {
        }

        @Override
        public void sendQuestFailedFormat(@Nonnull Player player, @Nonnull Quest quest) {
        }
    };

    QuestFormatter DEFAULT = new QuestFormatter() {

        @Override
        public void sendObjectiveNew(@Nonnull Player player, @Nonnull QuestObjective objective) {
            sendLine(player, ChatColor.GOLD);
            Chat.sendCenterMessage(player, "&e&lɴᴇᴡ ᴏʙᴊᴇᴄᴛɪᴠᴇ");
            Chat.sendCenterMessage(player, "&7" + objective.getDescription());
            sendLine(player, ChatColor.GOLD);

            PlayerLib.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.25f);
        }

        @Override
        public void sendObjectiveComplete(@Nonnull Player player, @Nonnull QuestObjective objective) {
            sendLine(player, ChatColor.GREEN);
            Chat.sendCenterMessage(player, "&2&lᴏʙᴊᴇᴄᴛɪᴠᴇ ᴄᴏᴍᴘʟᴇᴛᴇ");
            Chat.sendCenterMessage(player, "&a✔ " + objective.getDescription());
            sendLine(player, ChatColor.GREEN);

            PlayerLib.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f);
        }

        @Override
        public void sendObjectiveFailed(@Nonnull Player player, @Nonnull QuestObjective objective) {
            sendLine(player, ChatColor.DARK_RED);
            Chat.sendCenterMessage(player, "&c&lᴏʙᴊᴇᴄᴛɪᴠᴇ ꜰᴀɪʟᴇᴅ");
            Chat.sendCenterMessage(player, "&c" + objective.getDescription());
            Chat.sendCenterMessage(player, "&7It's ok! Try again.");
            sendLine(player, ChatColor.DARK_RED);

            PlayerLib.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 0.0f);
        }

        @Override
        public void sendQuestCompleteFormat(@Nonnull Player player, @Nonnull Quest quest) {
            sendLine(player, ChatColor.YELLOW);
            Chat.sendCenterMessage(player, "&6&lQUEST COMPLETE");
            Chat.sendCenterMessage(player, "&a" + quest.getName());
            sendLine(player, ChatColor.YELLOW);

            PlayerLib.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 2.0f);
        }

        @Override
        public void sendQuestStartedFormat(@Nonnull Player player, @Nonnull Quest quest) {
            sendLine(player, ChatColor.YELLOW);
            Chat.sendCenterMessage(player, "&6&lQUEST STARTED!");
            Chat.sendCenterMessage(player, "&a" + quest.getName());
            Chat.sendCenterMessage(player, "");
            Chat.sendCenterMessage(player, "&a&lᴄᴜʀʀᴇɴᴛ ᴏʙᴊᴇᴄᴛɪᴠᴇ");
            Chat.sendCenterMessage(player, "&7&o" + quest.getFirstObjective().getDescription());
            sendLine(player, ChatColor.YELLOW);
        }

        @Override
        public void sendQuestFailedFormat(@Nonnull Player player, @Nonnull Quest quest) {
            sendLine(player, ChatColor.DARK_RED);
            Chat.sendCenterMessage(player, "&c&lQUEST FAILED");
            Chat.sendCenterMessage(player, "&f" + quest.getName());
            sendLine(player, ChatColor.DARK_RED);
        }

        @Override
        public void sendCannotStartQuestAlreadyCompleted(@Nonnull Player player, @Nonnull Quest quest) {
            Chat.sendMessage(player, "&4You have already completed this quest!");
        }

        @Override
        public void sendPreRequirementNotMet(@Nonnull Player player, @Nonnull QuestPreRequirement preRequirement) {
            Chat.sendMessage(player, "&4" + preRequirement.getMessage());
        }

        @Override
        public void sendCannotStartQuestAlreadyStarted(@Nonnull Player player, @Nonnull Quest quest) {
            Chat.sendMessage(player, "&4This quest is already started!");
        }

        private void sendLine(Player player, ChatColor color) {
            Chat.sendCenterMessage(player, color + "&m]                                      [");
        }
    };

    /**
     * Sent whenever the player's objective changes to a new one.
     *
     * @param player    - Player for whom the objective has changed.
     * @param objective - The new objective.
     */
    void sendObjectiveNew(@Nonnull Player player, @Nonnull QuestObjective objective);

    /**
     * Sent whenever the player completes an objective.
     *
     * @param player    - Player who has completed the objective.
     * @param objective - The completed the objective.
     */
    void sendObjectiveComplete(@Nonnull Player player, @Nonnull QuestObjective objective);

    /**
     * Sent whenever the player fails an objective.
     *
     * @param player    - Player who has failed the objective.
     * @param objective - The failed objective.
     */
    void sendObjectiveFailed(@Nonnull Player player, @Nonnull QuestObjective objective);

    /**
     * Sent whenever the player starts a quest.
     *
     * @param player - Player who has started the quest.
     * @param quest  - The started quest.
     */
    void sendQuestStartedFormat(@Nonnull Player player, @Nonnull Quest quest);

    /**
     * Sent whenever the player completes a quest.
     *
     * @param player - Player who has completed the quest.
     * @param quest  - The completed quest.
     */
    void sendQuestCompleteFormat(@Nonnull Player player, @Nonnull Quest quest);

    /**
     * Sent whenever the player fails a quest.
     *
     * @param player - Player who has failed the quest.
     * @param quest  - The failed quest.
     * @deprecated Failing quests is currently not implemented.
     */
    @Deprecated
    void sendQuestFailedFormat(@Nonnull Player player, @Nonnull Quest quest);

    /**
     * Sent whenever the player attempts to start a quest that they have already completed.
     *
     * @param player - Player who tried to start the quest.
     * @param quest  - The quest.
     */
    void sendCannotStartQuestAlreadyCompleted(@Nonnull Player player, @Nonnull Quest quest);

    /**
     * Sent whenever the player attempts to start a quest but doesn't have pre-requirements met.
     *
     * @param player         - Player who tried to start the quest.
     * @param preRequirement - Pre-requirements.
     */
    void sendPreRequirementNotMet(@Nonnull Player player, @Nonnull QuestPreRequirement preRequirement);

    /**
     * Sent whenever the player attempts to start a quest while it's already active.
     *
     * @param player - The player who attempted to start the quest.
     * @param quest  - The quest.
     */
    void sendCannotStartQuestAlreadyStarted(@Nonnull Player player, @Nonnull Quest quest);
}
