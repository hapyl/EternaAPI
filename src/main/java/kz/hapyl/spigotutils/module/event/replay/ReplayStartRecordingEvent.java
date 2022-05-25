package kz.hapyl.spigotutils.module.event.replay;

import kz.hapyl.spigotutils.module.record.Record;
import org.bukkit.entity.Player;

public class ReplayStartRecordingEvent extends ReplayEvent {

    public ReplayStartRecordingEvent(Player player, Record record) {
        super(player, record);
    }

}
