package me.hapyl.spigotutils.module.event.parkour;

import me.hapyl.spigotutils.module.parkour.Data;
import me.hapyl.spigotutils.module.parkour.FailType;
import org.bukkit.entity.Player;

/**
 * Called whenever player fails a parkour.
 */
@Deprecated
public class ParkourFailEvent extends ParkourEvent {
    private final FailType type;

    public ParkourFailEvent(Player who, Data data, FailType type) {
        super(who, data);
        this.type = type;
    }

    /**
     * Returns readable fail reason.
     *
     * @return readable fail reason.
     */
    public String getReason() {
        return type.getReason();
    }

    /**
     * Returns fail type.
     *
     * @return fail type.
     */
    public FailType getFailType() {
        return type;
    }
}
