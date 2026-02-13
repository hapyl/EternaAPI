package me.hapyl.eterna.module.parkour;

import me.hapyl.eterna.EternaColors;
import me.hapyl.eterna.module.component.Formatter;
import me.hapyl.eterna.module.player.PlayerLib;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link ParkourFormatter}.
 */
public interface ParkourFormatter extends Formatter {
    
    /**
     * Defines the default formatter used.
     */
    @NotNull
    ParkourFormatter DEFAULT = new ParkourFormatter() {
        @Override
        public void resetTime(@NotNull ParkourPlayerData playerData) {
            final Player player = playerData.getPlayer();
            
            player.sendMessage(
                    Component.empty()
                             .append(Component.text("Reset time for ", EternaColors.GREEN))
                             .append(playerData.getParkour().getName().color(EternaColors.AMBER))
                             .append(Component.text("!", EternaColors.GREEN))
            );
            
            PlayerLib.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f);
        }
        
        @Override
        public void parkourStarted(@NotNull ParkourPlayerData parkourData) {
            final Player player = parkourData.getPlayer();
            final Parkour parkour = parkourData.getParkour();
            
            player.sendMessage(
                    Component.empty()
                             .append(Component.text("Started ", EternaColors.GREEN))
                             .append(parkour.getName().color(EternaColors.AMBER))
                             .append(Component.text("!", EternaColors.GREEN))
            );
            
            PlayerLib.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f);
        }
        
        @Override
        public void parkourFinished(@NotNull ParkourPlayerData parkourData) {
            final Player player = parkourData.getPlayer();
            final Parkour parkour = parkourData.getParkour();
            
            player.sendMessage(
                    Component.empty()
                             .append(Component.text("Finished ", EternaColors.GREEN))
                             .append(parkour.getName().color(EternaColors.AMBER))
                             .append(Component.text(" in ", EternaColors.GREEN))
                             .append(Component.text(parkourData.getCompletionDurationFormatted(), EternaColors.AQUA, TextDecoration.UNDERLINED))
                             .append(Component.text("!", EternaColors.GREEN))
            );
        }
        
        @Override
        public void parkourFailed(@NotNull ParkourPlayerData parkourData, @NotNull FailType failType) {
            final Player player = parkourData.getPlayer();
            final Parkour parkour = parkourData.getParkour();
            
            player.sendMessage(
                    Component.empty()
                             .append(Component.text("Parkour failed! ", EternaColors.RED))
                             .append(failType.getName().color(EternaColors.DARK_RED))
            );
            
            PlayerLib.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f);
        }
        
        @Override
        public void checkpointPassed(@NotNull ParkourPlayerData parkourData) {
            final Player player = parkourData.getPlayer();
            
            player.sendMessage(
                    Component.empty()
                             .append(Component.text("Checkpoint! ", EternaColors.GREEN))
                             .append(Component.text("(%s/%s)".formatted(parkourData.passedCheckpointsCount(), parkourData.totalCheckpoints()), EternaColors.GRAY))
            );
            
            PlayerLib.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f);
        }
        
        @Override
        public void cannotTeleportToCheckpointHaveNotPassedAny(@NotNull ParkourPlayerData parkourData) {
            final Player player = parkourData.getPlayer();
            
            player.sendMessage(Component.text("You haven't passed any checkpoints yet!", EternaColors.RED));
            
            PlayerLib.endermanTeleport(player, 0.0f);
        }
        
        @Override
        public void quit(@NotNull ParkourPlayerData parkourData) {
            final Player player = parkourData.getPlayer();
            final Parkour parkour = parkourData.getParkour();
            
            player.sendMessage(
                    Component.empty()
                             .append(Component.text("Quit ", EternaColors.RED))
                             .append(parkour.getName().color(EternaColors.AMBER))
                             .append(Component.text("!", EternaColors.RED))
            );
        }
        
        @Override
        public void actionbar(@NotNull ParkourPlayerData parkourData) {
            final Player player = parkourData.getPlayer();
            final Parkour parkour = parkourData.getParkour();
            
            player.sendActionBar(
                    Component.empty()
                             .append(Component.text(parkourData.getElapsedTimeFormatted(), EternaColors.AQUA, TextDecoration.UNDERLINED))
                             .append(Component.text("  ⪻ ", EternaColors.GRAY))
                             .append(parkour.getName().style(Style.style(EternaColors.AMBER, EternaColors.AMBER)))
                             .append(Component.text(" ⪼  ", EternaColors.GRAY))
                             .append(parkourData.getParkourStatistics().createActionbar())
            );
        }
        
        @Override
        public void checkpointTeleport(@NotNull ParkourPlayerData parkourData) {
            PlayerLib.endermanTeleport(parkourData.getPlayer(), 1.25f);
        }
        
        @Override
        public void cannotSetCheckpointMissingPreviousCheckpoint(@NotNull ParkourPlayerData parkourData) {
            final Player player = parkourData.getPlayer();
            
            player.sendMessage(Component.text("You have missing a checkpoint!", EternaColors.DARK_RED));
            
            PlayerLib.endermanTeleport(player, 0.0f);
        }
        
        @Override
        public void cannotFinishParkourNotStarted(@NotNull Player player, @NotNull Parkour parkour) {
            player.sendMessage(Component.text("You haven't started this parkour!", EternaColors.DARK_RED));
            
            PlayerLib.endermanTeleport(player, 0.0f);
        }
        
        @Override
        public void cannotFinishParkourMissedCheckpoint(@NotNull ParkourPlayerData parkourData) {
            final Player player = parkourData.getPlayer();
            
            player.sendMessage(
                    Component.empty()
                             .append(Component.text("Unable to finish this parkour because you have missed ", EternaColors.DARK_RED))
                             .append(Component.text(parkourData.missedCheckpointsCount(), EternaColors.RED))
                             .append(Component.text(" checkpoints!", EternaColors.DARK_RED))
            );
        }
        
        @Override
        public void cannotBreakParkourBlocks(@NotNull Player player) {
            player.sendMessage(Component.text("Cannot break parkour blocks!", EternaColors.DARK_RED));
            
            PlayerLib.playSound(player, Sound.ITEM_SHIELD_BREAK, 2.0f);
        }
    };
    
    @NotNull
    @Override
    default Component defineFormatter() {
        return Component.text("parkour");
    }
    
    /**
     * Sends the message for when a {@link Player} reset a {@link Parkour} time.
     *
     * @param playerData - The parkour data.
     */
    void resetTime(@NotNull ParkourPlayerData playerData);
    
    /**
     * Sends the message for when a {@link Player} starts a {@link Parkour}.
     *
     * @param parkourData - The parkour data.
     */
    void parkourStarted(@NotNull ParkourPlayerData parkourData);
    
    /**
     * Sends a message for when a {@link Player} finished a {@link Parkour}.
     *
     * @param parkourData - The player data.
     */
    void parkourFinished(@NotNull ParkourPlayerData parkourData);
    
    /**
     * Sends a message for when a {@link Player} fails a {@link Parkour}.
     *
     * @param parkourData - The parkour data.
     * @param type        - The fail type.
     */
    void parkourFailed(@NotNull ParkourPlayerData parkourData, @NotNull FailType type);
    
    /**
     * Sends a message for when a {@link Player} passes a checkpoint.
     *
     * @param parkourData - The parkour data.
     */
    void checkpointPassed(@NotNull ParkourPlayerData parkourData);
    
    /**
     * Sends a message for when a {@link Player} attempts to teleport to a checkpoint but haven't passed any yet.
     *
     * @param parkourData - The parkour data.
     */
    void cannotTeleportToCheckpointHaveNotPassedAny(@NotNull ParkourPlayerData parkourData);
    
    /**
     * Sends a message for when a {@link Player} quits a {@link Parkour}.
     *
     * @param parkourData - The parkour data.
     */
    void quit(@NotNull ParkourPlayerData parkourData);
    
    /**
     * Sends an actionbar message every tick, detailing the current time, {@link Parkour} name and {@link ParkourStatistics}.
     *
     * @param parkourData - The parkour data.
     */
    void actionbar(@NotNull ParkourPlayerData parkourData);
    
    /**
     * Sends a message for when a {@link Player} teleports to a checkpoint.
     *
     * @param parkourData - The parkour data.
     */
    void checkpointTeleport(@NotNull ParkourPlayerData parkourData);
    
    /**
     * Sends a message for when a {@link Player} attempts to set a new checkpoint but have missing a previous checkpoint.
     *
     * @param parkourData - The player data.
     */
    void cannotSetCheckpointMissingPreviousCheckpoint(@NotNull ParkourPlayerData parkourData);
    
    /**
     * Sends a message for when a {@link Player} attempts to finish a {@link Parkour} that they haven't started.
     *
     * @param player  - The player who attempted to finish the parkour.
     * @param parkour - The parkour.
     */
    void cannotFinishParkourNotStarted(@NotNull Player player, @NotNull Parkour parkour);
    
    /**
     * Sends a message for when a {@link Player} attempts to finish a {@link Parkour} but have missed checkpoints.
     *
     * @param parkourData - The parkour data.
     */
    void cannotFinishParkourMissedCheckpoint(@NotNull ParkourPlayerData parkourData);
    
    /**
     * Sends a message for when a {@link Player} attempts to break a {@link Parkour} block.
     *
     * @param player - The player who attempted to break a parkour block.
     */
    void cannotBreakParkourBlocks(@NotNull Player player);
}
