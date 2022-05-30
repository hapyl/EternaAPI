package me.hapyl.spigotutils.module.parkour;

import me.hapyl.spigotutils.module.util.Formatter;
import org.bukkit.entity.Player;

public interface ParkourFormatter extends Formatter {

    ParkourFormatter EMPTY = null;

    void sendCheckpointPassed(Data data);

    void sendResetTime(Player player, Parkour parkour);

    void sendParkourStarted(Player player, Parkour parkour);

    void sendParkourFinished(Data data);

    void sendParkourFailed(Data data, FailType type);

    void sendHaventPassedCheckpoint(Data data);

    void sendQuit(Data data);

    void sendTickActionbar(Data data);

    void sendCheckpointTeleport(Data data);

    void sendErrorParkourNotStarted(Player player, Parkour parkour);

    void sendErrorMissedCheckpointCannotFinish(Data data);

    void sendErrorMissedCheckpoint(Data data);
}
