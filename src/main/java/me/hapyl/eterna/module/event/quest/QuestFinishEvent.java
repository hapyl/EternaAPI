package me.hapyl.eterna.module.event.quest;

import me.hapyl.eterna.module.quest.QuestProgress;
import org.bukkit.entity.Player;

/**
 * Fires whenever player finishes a quest.
 */
public class QuestFinishEvent extends QuestEvent {

    private final QuestProgress progress;

    public QuestFinishEvent(Player player, QuestProgress progress) {
        super(player, progress.getQuest());
        this.progress = progress;
    }

    public QuestProgress getProgress() {
        return progress;
    }
}
