package me.hapyl.eterna.module.player.quest;

import org.bukkit.entity.Player;

/**
 * Defines a quest join behaviour whenever a {@link Player} joins the server.
 */
public enum QuestJoinBehaviour {

    /**
     * Do nothing when a {@link Player} joins.
     */
    DO_NOTHING,

    /**
     * Start the quest normally when a {@link Player} joins.
     * <p>Completed quests will not be started.</p>
     */
    START,

    /**
     * Start the quest when a {@link Player} joins, but don't send the message.
     * <p>Completed quests will not be started.</p>
     */
    START_WITHOUT_NOTIFYING

}
