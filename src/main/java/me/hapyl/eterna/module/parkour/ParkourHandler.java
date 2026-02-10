package me.hapyl.eterna.module.parkour;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.google.common.collect.Maps;
import me.hapyl.eterna.EternaHandler;
import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.player.PlayerLib;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.util.Disposable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

@ApiStatus.Internal
public final class ParkourHandler extends EternaHandler<Key, Parkour> implements Listener, Disposable {
    
    static ParkourHandler handler;
    
    private final Key cooldownStart = Key.ofString("eterna_parkour_start");
    private final Key cooldownCheckpoint = Key.ofString("eterna_parkour_checkpoint");
    
    private final ParkourItemStorage parkourItemStorage;
    private final Map<Player, ParkourPlayerData> playerData;
    
    public ParkourHandler(@NotNull EternaKey key, @NotNull EternaPlugin plugin) {
        super(key, plugin);
        
        this.parkourItemStorage = new ParkourItemStorage(this);
        this.playerData = Maps.newHashMap();
        
        handler = this;
    }
    
    public void start(@NotNull Player player, @NotNull Parkour parkour) {
        final ParkourPlayerData data = playerData.get(player);
        
        // If on already on parkour, do nothing
        if (data != null) {
            // Unless on the same parkour, then reset time
            if (data.getParkour().equals(parkour)) {
                data.reset();
                parkour.getFormatter().resetTime(data);
            }
            
            return;
        }
        
        // Else try to start new parkour instance
        if (parkour instanceof ParkourListener listener && listener.onStart(player) == ParkourListener.Response.CANCEL) {
            return;
        }
        
        final ParkourPlayerData newData = new ParkourPlayerData(player, parkour);
        
        this.parkourItemStorage.giveItems(player);
        this.playerData.put(player, newData);
        
        parkour.getFormatter().parkourStarted(newData);
    }
    
    // *-* Operations *-* //
    
    public void quit(@NotNull Player player) {
        final ParkourPlayerData data = playerData.get(player);
        
        if (data == null) {
            return;
        }
        
        playerData.remove(player);
        
        final Parkour parkour = data.getParkour();
        player.teleport(parkour.getQuitLocation(), Parkour.RESERVED_TELEPORT_CAUSE);
        
        data.restorePlayer();
        
        parkour.getFormatter().quit(data);
    }
    
    public void reset(@NotNull Player player) {
        final ParkourPlayerData data = playerData.get(player);
        
        if (data != null) {
            player.teleport(data.getParkour().getStart().getLocationCentered(), Parkour.RESERVED_TELEPORT_CAUSE);
        }
    }
    
    public void finish(@NotNull Player player) {
        final ParkourPlayerData data = playerData.get(player);
        
        if (data == null) {
            return;
        }
        
        final Parkour parkour = data.getParkour();
        
        if (parkour instanceof ParkourListener listener && listener.onFinish(player, data) == ParkourListener.Response.CANCEL) {
            return;
        }
        
        playerData.remove(player);
        
        data.markFinished();
        data.restorePlayer();
        
        parkour.getFormatter().parkourFinished(data);
    }
    
    public void fail(@NotNull Player player, @NotNull FailType type) {
        final ParkourPlayerData data = playerData.get(player);
        
        if (data == null) {
            return;
        }
        
        final Parkour parkour = data.getParkour();
        
        if (parkour instanceof ParkourListener listener && listener.onFail(player, data, type) == ParkourListener.Response.CANCEL) {
            return;
        }
        
        playerData.remove(player);
        
        data.restorePlayer();
        
        parkour.getFormatter().parkourFailed(data, type);
    }
    
    public void checkpoint(@NotNull Player player) {
        final ParkourPlayerData data = playerData.get(player);
        
        if (data == null) {
            return;
        }
        
        final Parkour parkour = data.getParkour();
        final ParkourPosition checkpoint = data.getLastCheckpoint();
        
        if (checkpoint == null) {
            parkour.getFormatter().cannotTeleportToCheckpointHaveNotPassedAny(data);
        }
        else {
            if (parkour instanceof ParkourListener listener && listener.onCheckpoint(player, data, checkpoint, ParkourListener.Type.TELEPORT_TO) == ParkourListener.Response.CANCEL) {
                return;
            }
            
            player.teleport(checkpoint.getLocationCentered(), Parkour.RESERVED_TELEPORT_CAUSE);
            
            data.getParkourStatistics().incrementStatistic(ParkourStatistics.Type.CHECKPOINT_TELEPORT, 1);
            parkour.getFormatter().checkpointTeleport(data);
        }
    }
    
    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent ev) {
        final Player player = ev.getPlayer();
        
        // Create holograms
        forEach(parkour -> parkour.showHolograms(player));
    }
    
    // *-* Listeners *-* //
    
    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent ev) {
        final Player player = ev.getPlayer();
        final ParkourPlayerData data = playerData.remove(player);
        
        // If we're current in parkour, restore player
        if (data != null) {
            data.restorePlayer();
        }
        
        // Delete holograms
        forEach(parkour -> parkour.hideHolograms(player));
    }
    
    @EventHandler
    public void handlePlayerInteractEvent(PlayerInteractEvent ev) {
        final Block clickedBlock = ev.getClickedBlock();
        final Action action = ev.getAction();
        final Player player = ev.getPlayer();
        
        if (clickedBlock == null || action != Action.PHYSICAL) {
            return;
        }
        
        final Material clickedBlockType = clickedBlock.getType();
        final Location clickedBlockLocation = clickedBlock.getLocation();
        
        final ParkourPlayerData data = playerData.get(player);
        
        // Check for start/finish block
        if (clickedBlockType == Parkour.MATERIAL_START_AND_FINISH) {
            final Parkour parkour = getParkourByStartOrFinish(clickedBlockLocation);
            
            // Not a parkour, just a normal pressure plate
            if (parkour == null) {
                return;
            }
            
            // Always cancel the click event
            ev.setCancelled(true);
            
            final ParkourPosition start = parkour.getStart();
            final ParkourPosition finish = parkour.getFinish();
            
            // Start/Finish has shared cooldown
            if (PlayerLib.isOnCooldown(player, cooldownStart)) {
                return;
            }
            
            player.setCooldown(cooldownStart.asNamespacedKey(), 30);
            
            // Parkour start
            if (start.compare(clickedBlockLocation)) {
                this.start(player, parkour);
            }
            // Parkour finish
            else if (finish.compare(clickedBlockLocation)) {
                // If we stepped at finish block but data is null, means we didn't start the parkour
                if (data == null) {
                    parkour.getFormatter().cannotFinishParkourNotStarted(player, parkour);
                    return;
                }
                
                // Make sure it's still the same parkour
                if (!data.getParkour().equals(parkour)) {
                    return;
                }
                
                // Make sure we passed all checkpoints
                if (!data.checkpoints().isEmpty()) {
                    parkour.getFormatter().cannotFinishParkourMissedCheckpoint(data);
                    return;
                }
                
                // Otherwise finish
                this.finish(player);
            }
        }
        else if (clickedBlockType == Parkour.MATERIAL_CHECKPOINT) {
            // If clicked block is part of ANY checkpoint, cancel the event
            if (getParkourByCheckpoint(clickedBlockLocation) != null) {
                ev.setCancelled(true);
            }
            
            // Check for whether we're doing parkour or cooldown
            if (data == null || PlayerLib.isOnCooldown(player, cooldownCheckpoint)) {
                return;
            }
            
            player.setCooldown(cooldownCheckpoint.asNamespacedKey(), 20);
            
            // Check for handler
            final ParkourPosition nextCheckpoint = data.getNextCheckpoint();
            final Parkour parkour = data.getParkour();
            
            // All checkpoints passed, probably other parkour checkpoint
            if (nextCheckpoint == null) {
                return;
            }
            
            // If the checkpoint location is indeed the location that we're standing on, pass it
            final ParkourFormatter formatter = parkour.getFormatter();
            
            if (nextCheckpoint.compare(clickedBlockLocation)) {
                if (data.nextCheckpoint()) {
                    formatter.checkpointPassed(data);
                }
            }
            // If it's not the next checkpoint, and there IS one checkpoint at the location, we're missing a checkpoint
            else if (data.checkpoints().stream().anyMatch(position -> position.compare(clickedBlockLocation))) {
                formatter.cannotSetCheckpointMissingPreviousCheckpoint(data);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleBlockBreakEvent(BlockBreakEvent ev) {
        final Player player = ev.getPlayer();
        final Block block = ev.getBlock();
        
        doPreventParkourBlockBreak(player, block, () -> ev.setCancelled(true));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleEntityExplodeEvent(EntityExplodeEvent ev) {
        final Iterator<Block> iterator = ev.blockList().iterator();
        
        while (iterator.hasNext()) {
            final Block block = iterator.next();
            
            doPreventParkourBlockBreak(null, block, iterator::remove);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleBlockExplodeEvent(BlockExplodeEvent ev) {
        final Iterator<Block> iterator = ev.blockList().iterator();
        
        while (iterator.hasNext()) {
            final Block block = iterator.next();
            
            doPreventParkourBlockBreak(null, block, iterator::remove);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleBlockPhysicsEvent(BlockPhysicsEvent ev) {
        doPreventParkourBlockBreak(null, ev.getBlock().getRelative(BlockFace.UP), () -> ev.setCancelled(true));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleBlockPistonExtendEvent(BlockPistonExtendEvent ev) {
        // For optimization’s sake, we manually check here for a block
        for (Block block : ev.getBlocks()) {
            final Material blockType = block.getType();
            
            if (!isParkourMaterial(blockType)) {
                continue;
            }
            
            final Parkour parkour = getParkourByStartOrFinishOrCheckpoint(block.getLocation());
            
            if (parkour != null) {
                ev.setCancelled(true);
                
                // Break here, one parkour block is enough
                break;
            }
        }
    }
    
    @EventHandler
    public void handleJumpStatistic(PlayerJumpEvent ev) {
        final Player player = ev.getPlayer();
        final ParkourPlayerData data = playerData.get(player);
        
        if (data != null) {
            data.getParkourStatistics().incrementStatistic(ParkourStatistics.Type.JUMP, 1);
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void handlePlayerTeleportEvent(PlayerTeleportEvent ev) {
        if (ev.getCause() != Parkour.RESERVED_TELEPORT_CAUSE) {
            tryFailParkour(ev.getPlayer(), FailType.TELEPORT);
        }
    }
    
    // *-* Fail Detections *-* //
    
    @EventHandler
    public void handlePlayerGameModeChangeEvent(PlayerGameModeChangeEvent ev) {
        tryFailParkour(ev.getPlayer(), FailType.GAME_MODE_CHANGE);
    }
    
    @EventHandler
    public void handlePlayerToggleFlightEvent(PlayerToggleFlightEvent ev) {
        tryFailParkour(ev.getPlayer(), FailType.FLIGHT);
    }
    
    @EventHandler
    public void handleEntityPotionEffectEvent(EntityPotionEffectEvent ev) {
        final Entity entity = ev.getEntity();
        
        if (!(entity instanceof Player player) || ev.getNewEffect() == null) {
            return;
        }
        
        tryFailParkour(player, FailType.EFFECT_CHANGE);
    }
    
    @Override
    public void dispose() {
        registry.values().forEach(Parkour::removeWorldEntities);
        playerData.values().forEach(ParkourPlayerData::restorePlayer);
    }
    
    @ApiStatus.Internal
    void forEachPlayerData(@NotNull Consumer<ParkourPlayerData> consumer) {
        this.playerData.values().forEach(consumer);
    }
    
    private boolean isParkourMaterial(@NotNull Material material) {
        return material == Parkour.MATERIAL_CHECKPOINT || material == Parkour.MATERIAL_START_AND_FINISH;
    }
    
    private void doPreventParkourBlockBreak(@Nullable Player player, @NotNull Block block, @NotNull Runnable runnable) {
        final Location location = block.getLocation();
        
        // If non-parkour block, don't care
        final Material blockType = block.getType();
        
        if (!isParkourMaterial(blockType)) {
            return;
        }
        
        final Parkour parkour = getParkourByStartOrFinishOrCheckpoint(location);
        
        if (parkour != null) {
            runnable.run();
            
            if (player != null) {
                parkour.getFormatter().cannotBreakParkourBlocks(player);
            }
        }
    }
    
    @Nullable
    private Parkour getParkourByCheckpoint(@NotNull Location location) {
        return registry.values()
                       .stream()
                       .filter(parkour -> parkour.checkpoints.stream().anyMatch(position -> position.compare(location)))
                       .findFirst()
                       .orElse(null);
    }
    
    @Nullable
    private Parkour getParkourByStartOrFinish(@NotNull Location location) {
        return registry.values()
                       .stream()
                       .filter(parkour -> parkour.getStart().compare(location) || parkour.getFinish().compare(location))
                       .findFirst()
                       .orElse(null);
    }
    
    @Nullable
    private Parkour getParkourByStartOrFinishOrCheckpoint(@NotNull Location location) {
        final Parkour parkour = getParkourByStartOrFinish(location);
        
        return parkour != null ? parkour : getParkourByCheckpoint(location);
    }
    
    private void tryFailParkour(@NotNull Player player, @NotNull FailType type) {
        final ParkourPlayerData data = playerData.get(player);
        
        if (data == null || data.getParkour().isFailAllowed(type)) {
            return;
        }
        
        fail(player, type);
    }
    
    public static void dispose0(@NotNull EternaKey key) {
        EternaHandler.tryCatch(() -> {
            key.validateKey();
            handler.dispose();
        });
    }
    
}
