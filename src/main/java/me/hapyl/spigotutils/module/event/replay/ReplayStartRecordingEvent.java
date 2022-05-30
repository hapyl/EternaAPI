package me.hapyl.spigotutils.module.event.replay;

import me.hapyl.spigotutils.module.record.Record;
import org.bukkit.entity.Player;

public class ReplayStartRecordingEvent extends ReplayEvent {

    public ReplayStartRecordingEvent(Player player, Record record) {
        super(player, record);
    }

}
