package kz.hapyl.spigotutils.module.event.replay;

import kz.hapyl.spigotutils.module.record.Record;
import org.bukkit.entity.Player;

public class ReplayFinishRecordingEvent extends ReplayEvent {
    public ReplayFinishRecordingEvent(Player player, Record record) {
        super(player, record);
    }
}
