package me.hapyl.spigotutils.module.parkour;

import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.event.parkour.ParkourCheckpointEvent;
import me.hapyl.spigotutils.module.util.cd.InternalCooldownStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.*;

public class ParkourListener implements Listener {

    private ParkourManager manager() {
        return EternaPlugin.getPlugin().getParkourManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePlayerJoin(PlayerJoinEvent ev) {
        manager().getRegisteredParkours().forEach(p -> {
            p.showHolograms(ev.getPlayer());
        });
    }

    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent ev) {
        final Player player = ev.getPlayer();
        final Data data = manager().getData(player);

        if (data == null) {
            return;
        }

        manager().getParkourData().remove(player);
        data.getPlayerInfo().restore();
    }

    // Brain
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePlayerInteractEvent(PlayerInteractEvent ev) {
        final Block clickedBlock = ev.getClickedBlock();
        final Action action = ev.getAction();
        final Player player = ev.getPlayer();

        if (clickedBlock == null || action != Action.PHYSICAL) {
            return;
        }

        final Data data = manager().getData(player);
        final Material clickedBlockType = clickedBlock.getType();
        final Location clickedBlockLocation = clickedBlock.getLocation();

        /** Start and Finish */
        if (clickedBlockType == Position.Type.START_OR_FINISH.material()) {
            final Parkour parkour = manager().byStartOrFinish(clickedBlockLocation);

            if (parkour == null || InternalCooldownStorage.PARKOUR_START.isOnCooldown(player)) {
                return;
            }

            InternalCooldownStorage.PARKOUR_START.start(player);
            ev.setUseInteractedBlock(Event.Result.DENY);
            ev.setCancelled(true);

            // Start
            if (parkour.getStart().compare(clickedBlockLocation)) {
                manager().startParkour(player, parkour);
                return;
            }

            // Finish
            if (parkour.getFinish().compare(clickedBlockLocation)) {
                if (data == null) {
                    parkour.getFormatter().sendErrorParkourNotStarted(player, parkour);
                    return;
                }

                // Make sure it's the same parkour lol.
                if (!data.compareParkour(parkour)) {
                    return;
                }

                // Not all checkpoints
                if (!data.getCheckpoints().isEmpty()) {
                    if (data.hasNextCheckpoint()) {
                        manager().teleportToCheckpoint(player);
                    }
                    parkour.getFormatter().sendErrorMissedCheckpointCannotFinish(data);
                    return;
                }

                manager().finishParkour(player);
            }
        }

        /** Checkpoint */
        else if (clickedBlockType == Position.Type.CHECKPOINT.material()) {
            if (data == null || InternalCooldownStorage.PARKOUR_CHECKPOINT.isOnCooldown(player)) {
                return;
            }

            InternalCooldownStorage.PARKOUR_CHECKPOINT.start(player);
            ev.setUseInteractedBlock(Event.Result.DENY);
            ev.setCancelled(true);

            final Position nextCheckpoint = data.getNextCheckpoint();

            if (nextCheckpoint != null) {
                final ParkourCheckpointEvent event = new ParkourCheckpointEvent(
                        player,
                        data,
                        nextCheckpoint,
                        ParkourCheckpointEvent.Type.REACH
                );
                Bukkit.getServer().getPluginManager().callEvent(event);
                if (event.isCancelled()) {
                    return;
                }
            }

            if (nextCheckpoint != null && nextCheckpoint.compare(clickedBlockLocation)) {
                data.nextCheckpoint(true);
            }
            // If any other checkpoint besides that we're standing right now say that we missed a checkpoint.
            else if (data.isFutureCheckpoint(clickedBlockLocation)) {
                data.getParkour().getFormatter().sendErrorMissedCheckpoint(data);
            }
        }
    }

    // Fail detection
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePlayerTeleportEvent(PlayerTeleportEvent ev) {
        if (ev.getCause() != PlayerTeleportEvent.TeleportCause.UNKNOWN) {
            testFail(ev.getPlayer(), FailType.TELEPORT);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePlayerGameModeChangeEvent(PlayerGameModeChangeEvent ev) {
        testFail(ev.getPlayer(), FailType.GAMEMODE_CHANGE);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePlayerToggleFlightEvent(PlayerToggleFlightEvent ev) {
        testFail(ev.getPlayer(), FailType.FLIGHT);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleParkourBlockBreak(BlockBreakEvent ev) {
        final Material type = ev.getBlock().getType();
        if (type == Position.Type.START_OR_FINISH.material() || type == Position.Type.CHECKPOINT.material()) {
            ev.setCancelled(true);
        }
    }

    private void testFail(Player player, FailType type) {
        final Data data = manager().getData(player);

        if (data == null || data.getParkour().isFailAllowed(type)) {
            return;
        }

        manager().failParkour(player, type);
    }

}
