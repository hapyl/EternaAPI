package me.hapyl.eterna.module.player.quest;

import me.hapyl.eterna.module.chat.Chat;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Represents quest pre-requirements.
 */
public interface QuestPreRequirement {

    /**
     * Returns {@code true} if the given player has completed the pre-requirements to start the quest, {@code false} otherwise.
     *
     * @param player - Player to check.
     * @return {@code true} if the given player has completed the pre-requirements to start the quest, {@code false} otherwise.
     */
    boolean isMet(@Nonnull Player player);

    /**
     * Gets the message that will be sent if the player fails pre-requirement check.
     *
     * @return the message that will be sent if the player fails pre-requirement check.
     * @see QuestFormatter#sendPreRequirementNotMet(Player, QuestPreRequirement)
     */
    @Nonnull
    String getMessage();

    @Nonnull
    static QuestPreRequirement quests(@Nonnull QuestHandler handler, @Nonnull Quest... quests) {
        return new QuestPreRequirement() {
            @Override
            public boolean isMet(@Nonnull Player player) {
                for (Quest quest : quests) {
                    if (!handler.hasCompleted(player, quest)) {
                        return false;
                    }
                }

                return true;
            }

            @Nonnull
            @Override
            public String getMessage() {
                return "You must complete %s before starting this quest!".formatted(Chat.makeStringCommaAnd(quests, Quest::getName));
            }
        };
    }

}
