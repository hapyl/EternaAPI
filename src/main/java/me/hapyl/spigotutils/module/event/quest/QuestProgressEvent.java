package me.hapyl.spigotutils.module.event.quest;

import me.hapyl.spigotutils.module.quest.QuestProgress;
import org.bukkit.entity.Player;

/**
 * Fires whenever player progresses a quest.
 */
public class QuestProgressEvent extends QuestEvent {

    private final QuestProgress progress;

    public QuestProgressEvent(Player player, QuestProgress progress) {
        super(player, progress.getQuest());
        this.progress = progress;
    }

    public QuestProgress getProgress() {
        return progress;
    }
}
