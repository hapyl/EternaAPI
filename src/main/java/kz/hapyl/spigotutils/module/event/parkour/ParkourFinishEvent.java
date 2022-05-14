package kz.hapyl.spigotutils.module.event.parkour;

import kz.hapyl.spigotutils.module.parkour.Data;
import org.bukkit.entity.Player;

/**
 * Called whenever player successfully finishes parkour.
 */
public class ParkourFinishEvent extends ParkourEvent {
    public ParkourFinishEvent(Player who, Data data) {
        super(who, data);
    }
}
