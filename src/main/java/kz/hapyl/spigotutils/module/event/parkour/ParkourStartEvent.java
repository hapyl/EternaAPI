package kz.hapyl.spigotutils.module.event.parkour;

import kz.hapyl.spigotutils.module.parkour.Data;
import org.bukkit.entity.Player;

/**
 * Called whenever player starts a parkour (steps on pressure plate).
 * <b>This even is not triggered if player restarted parkour time!</b>
 */
public class ParkourStartEvent extends ParkourEvent {
    public ParkourStartEvent(Player who, Data data) {
        super(who, data);
    }
}
