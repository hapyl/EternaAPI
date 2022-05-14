package kz.hapyl.spigotutils.module.event.parkour;

import kz.hapyl.spigotutils.module.parkour.Data;
import kz.hapyl.spigotutils.module.parkour.FailType;
import org.bukkit.entity.Player;

public class ParkourFailEvent extends ParkourEvent {
    private final FailType type;

    public ParkourFailEvent(Player who, Data data, FailType type) {
        super(who, data);
        this.type = type;
    }

    public FailType getFailType() {
        return type;
    }
}
