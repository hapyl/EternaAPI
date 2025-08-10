package me.hapyl.eterna.module.parkour;

import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.player.PlayerLib;
import me.hapyl.eterna.module.util.Formatter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public interface ParkourFormatter extends Formatter {

    ParkourFormatter EMPTY = new ParkourFormatter() {
        @Override
        public void sendCheckpointPassed(@Nonnull ParkourData data) {
        }

        @Override
        public void sendResetTime(@Nonnull Player player, @Nonnull Parkour parkour) {
        }

        @Override
        public void sendParkourStarted(@Nonnull Player player, @Nonnull Parkour parkour) {
        }

        @Override
        public void sendParkourFinished(@Nonnull ParkourData data) {
        }

        @Override
        public void sendParkourFailed(@Nonnull ParkourData data, @Nonnull FailType type) {
        }

        @Override
        public void sendHaventPassedCheckpoint(@Nonnull ParkourData data) {
        }

        @Override
        public void sendQuit(@Nonnull ParkourData data) {
        }

        @Override
        public void sendTickActionbar(@Nonnull ParkourData data) {
        }

        @Override
        public void sendCheckpointTeleport(@Nonnull ParkourData data) {
        }

        @Override
        public void sendErrorParkourNotStarted(@Nonnull Player player, @Nonnull Parkour parkour) {
        }

        @Override
        public void sendErrorMissedCheckpointCannotFinish(ParkourData data) {
        }

        @Override
        public void sendErrorMissedCheckpoint(ParkourData data) {
        }
    };

    ParkourFormatter DEFAULT = new ParkourFormatter() {

        @Override
        public void sendCheckpointPassed(@Nonnull ParkourData data) {
            Chat.sendMessage(
                    data.getPlayer(),
                    "&aCheckpoint! (%s/%s)".formatted(
                            data.passedCheckpointsCount(),
                            data.getParkour().getCheckpoints().size()
                    )
            );

            PlayerLib.playSound(data.getPlayer(), Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f);
        }

        @Override
        public void sendResetTime(@Nonnull Player player, @Nonnull Parkour parkour) {
            Chat.sendMessage(player, "&cReset time for %s!".formatted(parkour.getName()));
            PlayerLib.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f);
        }

        @Override
        public void sendParkourStarted(@Nonnull Player player, @Nonnull Parkour parkour) {
            Chat.sendMessage(player, "&aStarted %s!".formatted(parkour.getName()));
            PlayerLib.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f);
        }

        @Override
        public void sendParkourFinished(@Nonnull ParkourData data) {
            Chat.sendMessage(
                    data.getPlayer(),
                    "&aFinished &a%s&7 in &b%ss&7!".formatted(data.getParkour().getName(), data.getTimePassedFormatted())
            );
        }

        @Override
        public void sendParkourFailed(@Nonnull ParkourData data, @Nonnull FailType type) {
            final Player player = data.getPlayer();
            Chat.sendMessage(player, "&cParkour failed, &4%s&c!".formatted(type.getReason()));
            PlayerLib.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f);
        }

        @Override
        public void sendHaventPassedCheckpoint(@Nonnull ParkourData data) {
            final Player player = data.getPlayer();
            Chat.sendMessage(player, "&cYou haven't passed any checkpoints yet!");
            PlayerLib.endermanTeleport(player, 0.0f);
        }

        @Override
        public void sendQuit(@Nonnull ParkourData data) {
            Chat.sendMessage(data.getPlayer(), "&cQuit %s!".formatted(data.getParkour().getName()));
        }

        @Override
        public void sendTickActionbar(@Nonnull ParkourData data) {
            Chat.sendActionbar(data.getPlayer(), "&a&l%s: &b%ss".formatted(data.getParkour().getName(), data.getTimePassedFormatted()));
        }

        @Override
        public void sendCheckpointTeleport(@Nonnull ParkourData data) {
            PlayerLib.endermanTeleport(data.getPlayer(), 1.25f);
        }

        @Override
        public void sendErrorParkourNotStarted(@Nonnull Player player, @Nonnull Parkour parkour) {
            Chat.sendMessage(player, "&cYou must first start this parkour!");
            PlayerLib.endermanTeleport(player, 0.0f);
        }

        @Override
        public void sendErrorMissedCheckpointCannotFinish(ParkourData data) {
            Chat.sendMessage(data.getPlayer(), "&cYou missed &l%s&c checkpoints!".formatted(data.missedCheckpointsCount()));
        }

        @Override
        public void sendErrorMissedCheckpoint(ParkourData data) {
            final Player player = data.getPlayer();

            Chat.sendMessage(player, "&cYou missed a checkpoint!");
            PlayerLib.endermanTeleport(player, 0.0f);
        }
    };

    /**
     * Sent upon player passing a checkpoint.
     *
     * @param data - Parkour data.
     */
    void sendCheckpointPassed(@Nonnull ParkourData data);

    /**
     * Sent upon player resetting a parkour timer.
     *
     * @param player  - Player.
     * @param parkour - Parkour.
     */
    void sendResetTime(@Nonnull Player player, @Nonnull Parkour parkour);

    /**
     * Sent upon player starting a parkour.
     *
     * @param player  - Player.
     * @param parkour - Parkour.
     */
    void sendParkourStarted(@Nonnull Player player, @Nonnull Parkour parkour);

    /**
     * Sent upon player finishing a parkour.
     *
     * @param data - Parkour data.
     */
    void sendParkourFinished(@Nonnull ParkourData data);

    /**
     * Sent upon player failing a parkour.
     *
     * @param data - Parkour data.
     * @param type - Fail type.
     */
    void sendParkourFailed(@Nonnull ParkourData data, @Nonnull FailType type);

    /**
     * Sent upon player trying to teleport the checkpoint before reaching any.
     *
     * @param data - Parkour data.
     */
    void sendHaventPassedCheckpoint(@Nonnull ParkourData data);

    /**
     * Sent upon player quitting a parkour.
     *
     * @param data - Parkour data.
     */
    void sendQuit(@Nonnull ParkourData data);

    /**
     * Sent every parkour tick.
     *
     * @param data - Parkour data.
     */
    void sendTickActionbar(@Nonnull ParkourData data);

    /**
     * Sent upon player teleporting to a checkpoint.
     *
     * @param data - Parkour data.
     */
    void sendCheckpointTeleport(@Nonnull ParkourData data);

    /**
     * Sent upon player stepping on the finish pressure plate before starting a parkour.
     *
     * @param player  - Player.
     * @param parkour - Parkour data.
     */
    void sendErrorParkourNotStarted(@Nonnull Player player, @Nonnull Parkour parkour);

    /**
     * Sent upon player stepping on the finish pressure plate while not having all checkpoint passed.
     *
     * @param data - Parkour data.
     */
    void sendErrorMissedCheckpointCannotFinish(ParkourData data);

    /**
     * Sent upon player stepping on the checkpoint pressure plate while have missed a checkpoint.
     *
     * @param data - Parkour data.
     */
    void sendErrorMissedCheckpoint(ParkourData data);
}
