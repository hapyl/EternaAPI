package kz.hapyl.spigotutils.module.event.parkour;

import kz.hapyl.spigotutils.module.parkour.Data;
import org.bukkit.entity.Player;

public class ParkourStartEvent extends ParkourEvent {
    public ParkourStartEvent(Player who, Data data) {
        super(who, data);
    }
}
