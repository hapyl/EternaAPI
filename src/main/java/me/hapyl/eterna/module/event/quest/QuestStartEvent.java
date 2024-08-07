package me.hapyl.eterna.module.event.quest;

import me.hapyl.eterna.module.quest.Quest;
import org.bukkit.entity.Player;

/**
 * Fires whenever player starts a quest.
 */
public class QuestStartEvent extends QuestEvent {
    public QuestStartEvent(Player player, Quest quest) {
        super(player, quest);
    }
}
