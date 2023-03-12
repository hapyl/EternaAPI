package me.hapyl.spigotutils.module.event.parkour;

import me.hapyl.spigotutils.module.parkour.Data;
import org.bukkit.entity.Player;

/**
 * Called whenever player starts a parkour (steps on pressure plate).
 * <b>This even is not triggered if player restarted parkour time!</b>
 */
@Deprecated
public class ParkourStartEvent extends ParkourEvent {
    public ParkourStartEvent(Player who, Data data) {
        super(who, data);
    }
}
