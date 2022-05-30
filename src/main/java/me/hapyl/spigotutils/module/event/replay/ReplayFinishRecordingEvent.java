package me.hapyl.spigotutils.module.event.replay;

import me.hapyl.spigotutils.module.record.Record;
import org.bukkit.entity.Player;

public class ReplayFinishRecordingEvent extends ReplayEvent {
    public ReplayFinishRecordingEvent(Player player, Record record) {
        super(player, record);
    }
}
