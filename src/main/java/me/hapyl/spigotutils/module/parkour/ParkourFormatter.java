package me.hapyl.spigotutils.module.parkour;

import me.hapyl.spigotutils.module.util.Formatter;
import org.bukkit.entity.Player;

public interface ParkourFormatter extends Formatter {

    ParkourFormatter EMPTY = new ParkourFormatter() {
        @Override
        public void sendCheckpointPassed(Data data) {
        }

        @Override
        public void sendResetTime(Player player, Parkour parkour) {
        }

        @Override
        public void sendParkourStarted(Player player, Parkour parkour) {
        }

        @Override
        public void sendParkourFinished(Data data) {
        }

        @Override
        public void sendParkourFailed(Data data, FailType type) {
        }

        @Override
        public void sendHaventPassedCheckpoint(Data data) {
        }

        @Override
        public void sendQuit(Data data) {
        }

        @Override
        public void sendTickActionbar(Data data) {
        }

        @Override
        public void sendCheckpointTeleport(Data data) {
        }

        @Override
        public void sendErrorParkourNotStarted(Player player, Parkour parkour) {
        }

        @Override
        public void sendErrorMissedCheckpointCannotFinish(Data data) {
        }

        @Override
        public void sendErrorMissedCheckpoint(Data data) {
        }
    };

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
