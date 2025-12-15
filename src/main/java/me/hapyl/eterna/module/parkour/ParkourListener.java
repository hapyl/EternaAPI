package me.hapyl.eterna.module.parkour;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaLock;
import me.hapyl.eterna.builtin.manager.ParkourManager;
import me.hapyl.eterna.module.inventory.ItemBuilder;
import me.hapyl.eterna.module.registry.Key;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;

@ApiStatus.Internal
public final class ParkourListener extends EternaLock implements Listener {
    
    private final ItemStack cooldownStart = ItemBuilder.createDummyCooldownItem(Key.ofString("eterna_parkour_start"));
    private final ItemStack cooldownCheckpoint = ItemBuilder.createDummyCooldownItem(Key.ofString("eterna_parkour_checkpoint"));
    
    public ParkourListener(@Nonnull EternaKey key) {
        super(key);
    }
    
    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent ev) {
        final Player player = ev.getPlayer();
        
        // Create holograms
        getParkourManager().forEach(parkour -> parkour.showHolograms(player));
    }
    
    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent ev) {
        final Player player = ev.getPlayer();
        final ParkourManager manager = getParkourManager();
        final ParkourData data = manager.getData(player);
        
        // If we're in parkour, reset the data and return player items
        if (data != null) {
            manager.removePlayerData(player);
        }
        
        // Delete holograms
        manager.forEach(parkour -> parkour.hideHolograms(player));
    }
    
    @EventHandler
    public void handlePlayerInteractEvent(PlayerInteractEvent ev) {
        final Block clickedBlock = ev.getClickedBlock();
        final Action action = ev.getAction();
        final Player player = ev.getPlayer();
        
        if (clickedBlock == null || action != Action.PHYSICAL) {
            return;
        }
        
        final ParkourManager parkourManager = getParkourManager();
        
        final Material clickedBlockType = clickedBlock.getType();
        final Location clickedBlockLocation = clickedBlock.getLocation();
        
        final ParkourData playerData = parkourManager.getData(player);
        
        // Check for start/finish block
        if (clickedBlockType == Parkour.blockStartFinish) {
            final Parkour parkour = parkourManager.byStartOrFinish(clickedBlockLocation);
            
            // Not a parkour, just a normal pressure plate
            if (parkour == null) {
                return;
            }
            
            // Always cancel the click event
            ev.setCancelled(true);
            
            final ParkourPosition start = parkour.getStart();
            final ParkourPosition finish = parkour.getFinish();
            
            // Start/Finish has shared cooldown
            if (player.hasCooldown(cooldownStart)) {
                return;
            }
            
            player.setCooldown(cooldownStart, 30);
            
            // Parkour start
            if (start.compare(clickedBlockLocation)) {
                parkourManager.start(player, parkour);
            }
            else if (finish.compare(clickedBlockLocation)) {
                // If we stepped at finish block but data is null, means we didn't start the parkour
                if (playerData == null) {
                    parkour.getFormatter().sendErrorParkourNotStarted(player, parkour);
                    return;
                }
                
                // Make sure it's still the same parkour
                if (!playerData.getParkour().equals(parkour)) {
                    return;
                }
                
                // Make sure we passed all checkpoints
                if (!playerData.getCheckpoints().isEmpty()) {
                    parkour.getFormatter().sendErrorMissedCheckpointCannotFinish(playerData);
                    return;
                }
                
                // Otherwise finish
                parkourManager.finish(player);
            }
        }
        else if (clickedBlockType == Parkour.blockCheckpoint) {
            // If clicked block is part of ANY checkpoint, cancel the event
            if (parkourManager.isCheckpointOfAnyParkour(clickedBlockLocation)) {
                ev.setCancelled(true);
            }
            
            // Check for whether we're doing parkour or cooldown
            if (playerData == null || player.hasCooldown(cooldownCheckpoint)) {
                return;
            }
            
            player.setCooldown(cooldownCheckpoint, 20);
            
            // Check for handler
            final ParkourPosition nextCheckpoint = playerData.getNextCheckpoint();
            final Parkour parkour = playerData.getParkour();
            
            // All checkpoints passed, probably other parkour checkpoint
            if (nextCheckpoint == null) {
                return;
            }
            
            // If the checkpoint location is indeed the location that we're standing on, pass it
            if (nextCheckpoint.compare(clickedBlockLocation)) {
                playerData.nextCheckpoint();
            }
            // If it's not the next checkpoint, and there IS one checkpoint at the location, we're missing a checkpoint
            else if (playerData.getCheckpoints().stream().anyMatch(position -> position.compare(clickedBlockLocation))) {
                parkour.getFormatter().sendErrorMissedCheckpoint(playerData);
            }
        }
    }
    
    @EventHandler
    public void handleParkourBlockBreak(BlockBreakEvent ev) {
        final Player player = ev.getPlayer();
        final Block block = ev.getBlock();
        final Location location = block.getLocation();
        
        // Prevent breaking start/finish/checkpoint pressure plates
        final Parkour parkour = getParkourManager().byLocation(location);
        
        if (parkour != null) {
            ev.setCancelled(true);
            parkour.getFormatter().sendErrorCannotBreakParkourBlocks(player);
        }
    }
    
    
    @EventHandler
    public void handleJumpStatistic(PlayerJumpEvent ev) {
        final Player player = ev.getPlayer();
        final ParkourData data = getParkourManager().getData(player);
        
        if (data != null) {
            data.getStats().increment(ParkourStatistics.Type.JUMP, 1);
        }
    }
    
    // +-+ Fail Detections +-+
    
    @EventHandler
    public void handlePlayerTeleportEvent(PlayerTeleportEvent ev) {
        if (ev.getCause() != Parkour.reversedTeleportCauseForCheckpoint) {
            tryFailParkour(ev.getPlayer(), FailType.TELEPORT);
        }
    }
    
    @EventHandler
    public void handlePlayerGameModeChangeEvent(PlayerGameModeChangeEvent ev) {
        tryFailParkour(ev.getPlayer(), FailType.GAME_MODE_CHANGE);
    }
    
    @EventHandler
    public void handlePlayerToggleFlightEvent(PlayerToggleFlightEvent ev) {
        tryFailParkour(ev.getPlayer(), FailType.FLIGHT);
    }
    
    @EventHandler()
    public void handleParkourEffect(EntityPotionEffectEvent ev) {
        final Entity entity = ev.getEntity();
        
        if (!(entity instanceof Player player) || ev.getNewEffect() == null) {
            return;
        }
        
        tryFailParkour(player, FailType.EFFECT_CHANGE);
    }
    
    @Nonnull
    private ParkourManager getParkourManager() {
        return Eterna.getManagers().parkour;
    }
    
    private void tryFailParkour(Player player, FailType type) {
        final ParkourData data = getParkourManager().getData(player);
        
        if (data == null || data.getParkour().isFailAllowed(type)) {
            return;
        }
        
        getParkourManager().fail(player, type);
    }
    
}
