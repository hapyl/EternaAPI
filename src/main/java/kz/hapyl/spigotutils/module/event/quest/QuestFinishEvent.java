package kz.hapyl.spigotutils.module.event.quest;

import kz.hapyl.spigotutils.module.quest.QuestProgress;
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
