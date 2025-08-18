package me.hapyl.eterna.builtin.manager;

import com.google.common.collect.Maps;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.parkour.*;
import me.hapyl.eterna.module.registry.Key;
import me.hapyl.eterna.module.util.Disposable;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public final class ParkourManager extends EternaManager<Key, Parkour> implements Disposable {
    
    private final ParkourItemStorage parkourItemStorage;
    private final Map<Player, ParkourData> parkourData;
    
    ParkourManager(@Nonnull EternaPlugin eterna) {
        super(eterna);
        
        this.parkourItemStorage = new ParkourItemStorage(this);
        this.parkourData = Maps.newHashMap();
    }
    
    public void start(@Nonnull Player player, @Nonnull Parkour parkour) {
        final ParkourData data = getData(player);
        
        // Already on parkour
        if (data != null) {
            // If same parkour, reset time, do nothing otherwise
            if (data.getParkour().equals(parkour)) {
                data.reset();
                parkour.getFormatter().sendResetTime(player, parkour);
            }
            return;
        }
        
        // Else try to start new parkour instance
        if (parkour instanceof ParkourHandler handler && handler.onStart(player) == ParkourHandler.Response.CANCEL) {
            return;
        }
        
        final ParkourData newData = new ParkourData(player, parkour);
        
        parkourItemStorage.giveItems(player);
        parkourData.put(player, newData);
        
        parkour.getFormatter().sendParkourStarted(player, parkour);
    }
    
    public void finish(@Nonnull Player player) {
        final ParkourData data = getData(player);
        
        if (data == null) {
            return;
        }
        
        final Parkour parkour = data.getParkour();
        data.setFinished();
        
        // The reason we mark as finished and then reset back instead of checking for
        // handler before is to give the completion time to the handler
        if (parkour instanceof ParkourHandler handler && handler.onFinish(player, data) == ParkourHandler.Response.CANCEL) {
            data.resetFinished();
            return;
        }
        
        parkourData.remove(player);
        data.getPlayerInfo().restore();
        
        parkour.getFormatter().sendParkourFinished(data);
    }
    
    public void fail(@Nonnull Player player, @Nonnull FailType type) {
        final ParkourData data = getData(player);
        
        if (data == null) {
            return;
        }
        
        final Parkour parkour = data.getParkour();
        
        if (parkour instanceof ParkourHandler handler && handler.onFail(player, data, type) == ParkourHandler.Response.CANCEL) {
            return;
        }
        
        // Make sure to remove player's data before restoring the info
        parkourData.remove(player);
        
        // Remove our invisibility BEFORE restoring player effects
        data.getPlayerInfo().restore();
        
        parkour.getFormatter().sendParkourFailed(data, type);
    }
    
    public void checkpoint(@Nonnull Player player) {
        final ParkourData data = getData(player);
        
        if (data == null) {
            return;
        }
        
        final Parkour parkour = data.getParkour();
        final ParkourPosition checkpoint = data.getPreviousCheckpoint();
        
        if (checkpoint == null) {
            parkour.getFormatter().sendHaventPassedCheckpoint(data);
        }
        else {
            if (parkour instanceof ParkourHandler handler && handler.onCheckpoint(player, data, checkpoint, ParkourHandler.Type.TELEPORT_TO) == ParkourHandler.Response.CANCEL) {
                return;
            }
            
            player.teleport(checkpoint.getLocationCentered(), Parkour.reversedTeleportCauseForCheckpoint);
            
            data.getStats().increment(ParkourStatistics.Type.CHECKPOINT_TELEPORT, 1);
            parkour.getFormatter().sendCheckpointTeleport(data);
        }
    }
    
    public void reset(@Nonnull Player player) {
        final ParkourData data = getData(player);
        
        if (data == null) {
            return;
        }
        
        start(player, data.getParkour());
        player.teleport(data.getParkour().getStart().getLocationCentered(), Parkour.reversedTeleportCauseForCheckpoint);
    }
    
    public void quit(@Nonnull Player player) {
        final ParkourData data = getData(player);
        
        if (data == null) {
            return;
        }
        
        parkourData.remove(player);
        
        final Parkour parkour = data.getParkour();
        player.teleport(parkour.getQuitLocation(), Parkour.reversedTeleportCauseForCheckpoint);
        
        data.getPlayerInfo().restore();
        
        parkour.getFormatter().sendQuit(data);
    }
    
    public void register(@Nonnull Parkour parkour) {
        managing.put(parkour.getKey(), parkour);
        
        // Create world entities
        parkour.createWorldEntities();
    }
    
    @Nullable
    public Parkour byStartOrFinish(@Nonnull Location target) {
        return managing.values()
                       .stream()
                       .filter(parkour -> parkour.getStart().compare(target) || parkour.getFinish().compare(target))
                       .findFirst()
                       .orElse(null);
    }
    
    @Nullable
    public Parkour byCheckpoint(@Nonnull Location location) {
        for (Parkour parkour : managing.values()) {
            for (ParkourPosition checkpoint : parkour.getCheckpoints()) {
                if (checkpoint.compare(location)) {
                    return parkour;
                }
            }
        }
        
        return null;
    }
    
    @Nullable
    public Parkour byLocation(@Nonnull Location location) {
        final Parkour parkour = byStartOrFinish(location);
        
        return parkour != null ? parkour : byCheckpoint(location);
    }
    
    public boolean isCheckpointOfAnyParkour(@Nonnull Location location) {
        for (Parkour value : managing.values()) {
            if (value.isCheckpoint(location)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isInParkour(@Nonnull Player player) {
        return getData(player) != null;
    }
    
    @Nullable
    public ParkourData getData(@Nonnull Player player) {
        return parkourData.get(player);
    }
    
    @Override
    public void dispose() {
        managing.values().forEach(Parkour::removeWorldEntities);
        parkourData.values().forEach(data -> data.getPlayerInfo().restore());
    }
    
    @Nonnull
    public Map<Player, ParkourData> getParkourData() {
        return parkourData;
    }
    
    public void removePlayerData(@Nonnull Player player) {
        final ParkourData data = parkourData.remove(player);
        
        if (data != null) {
            data.getPlayerInfo().restore();
        }
    }
}
