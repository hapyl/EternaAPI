package me.hapyl.spigotutils.module.event.parkour;

import me.hapyl.spigotutils.module.parkour.Data;
import me.hapyl.spigotutils.module.parkour.Position;
import org.bukkit.entity.Player;

/**
 * Called whenever player steps on checkpoint pressure plate.
 */
@Deprecated
public class ParkourCheckpointEvent extends ParkourEvent {
    private final Position position;
    private final Type type;

    public ParkourCheckpointEvent(Player who, Data data, Position checkpoint, Type type) {
        super(who, data);
        this.position = checkpoint;
        this.type = type;
    }

    /**
     * Returns a position of checkpoint.
     *
     * @return a position of checkpoint.
     */
    public Position getCheckpoint() {
        return position;
    }

    /**
     * Returns a type of checkpoint action.
     *
     * @return a type of checkpoint action.
     */
    public Type getType() {
        return type;
    }

    public enum Type {
        TELEPORT_TO, // player teleported to checkpoint using an item or command.
        REACH // player reached (stepped) on a checkpoint plate, no matter if it's already reached or not.
    }
}
