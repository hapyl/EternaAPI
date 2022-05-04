package kz.hapyl.spigotutils.module.event.parkour;

import kz.hapyl.spigotutils.module.parkour.Data;
import kz.hapyl.spigotutils.module.parkour.Position;
import org.bukkit.entity.Player;

public class ParkourCheckpointEvent extends ParkourEvent {
    private final Position position;
    private final Type type;

    public ParkourCheckpointEvent(Player who, Data data, Position checkpoint, Type type) {
        super(who, data);
        this.position = checkpoint;
        this.type = type;
    }

    public Position getCheckpoint() {
        return position;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        TELEPORT_TO,
        REACH
    }
}
