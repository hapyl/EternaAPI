package me.hapyl.eterna.module.parkour;

import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public interface ParkourHandler {

    @Nullable
    Response onStart(Player player, Data data);

    @Nullable
    Response onFinish(Player player, Data data);

    @Nullable
    Response onFail(Player player, Data data, FailType failType);

    @Nullable
    Response onCheckpoint(Player player, Data data, Position position, Type type);

    enum Type {
        TELEPORT_TO, // player teleported to checkpoint using an item or command.
        REACH        // player reached (stepped) on a checkpoint plate, no matter if it's already reached or not.
    }

    enum Response {
        CANCEL, // cancel the event
        ALLOW   // allow event (default)
    }

}
