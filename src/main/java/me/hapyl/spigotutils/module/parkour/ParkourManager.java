package me.hapyl.spigotutils.module.parkour;

import com.google.common.collect.Maps;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.event.parkour.ParkourEvent;
import me.hapyl.spigotutils.module.player.EffectType;
import me.hapyl.spigotutils.module.player.PlayerLib;
import me.hapyl.spigotutils.registry.Registry;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

public class ParkourManager extends Registry<Position, Parkour> {

    public final ParkourItemStorage parkourItemStorage = new ParkourItemStorage(this);

    private final Map<Player, Data> parkourData;

    public ParkourManager(EternaPlugin plugin) {
        super(plugin);
        this.parkourData = Maps.newHashMap();
    }

    public void startParkour(Player player, Parkour parkour) {
        // Already doing parkour
        if (isParkouring(player)) {
            final Data data = getData(player);
            if (data == null) {
                return;
            }

            // Reset time if same parkour
            if (data.getParkour().equals(parkour)) {
                data.resetTime();
                data.resetCheckpoints();

                parkour.getFormatter().sendResetTime(player, parkour);
            }
        }
        else {
            // Else start a new instance
            final Data newData = new Data(player, parkour);

            if (parkour instanceof ParkourHandler handler && handler.onStart(player, newData) == ParkourHandler.Response.CANCEL) {
                return;
            }

            player.setGameMode(GameMode.ADVENTURE);
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }
            player.getInventory().clear();

            parkourItemStorage.giveItems(player);

            PlayerLib.addEffect(player, EffectType.INVISIBILITY, Integer.MAX_VALUE, 1);
            parkourData.put(player, newData);

            parkour.getFormatter().sendParkourStarted(player, parkour);
        }

    }

    public void finishParkour(Player player) {
        final Data data = getData(player);
        if (data == null) {
            return;
        }

        final Parkour parkour = data.getParkour();
        data.setFinished();

        if (parkour instanceof ParkourHandler handler && handler.onFinish(player, data) == ParkourHandler.Response.CANCEL) {
            data.resetFinished();
            return;
        }

        parkourData.remove(player);

        data.getPlayerInfo().restore();
        //player.teleport(data.getParkour().getQuitLocation());

        parkour.getFormatter().sendParkourFinished(data);
    }

    public void failParkour(Player player, FailType type) {
        final Data data = getData(player);
        if (data == null) {
            return;
        }

        final Parkour parkour = data.getParkour();

        if (parkour instanceof ParkourHandler handler && handler.onFail(player, data, type) == ParkourHandler.Response.CANCEL) {
            return;
        }

        // make sure to remove player's data before restoring the info
        parkourData.remove(player);
        data.getPlayerInfo().restore();

        PlayerLib.removeEffect(player, EffectType.INVISIBILITY);

        parkour.getFormatter().sendParkourFailed(data, type);
    }

    public void teleportToCheckpoint(Player player) {
        final Data data = getData(player);
        if (data == null) {
            return;
        }

        final Parkour parkour = data.getParkour();
        final Position checkpoint = data.getPreviousCheckpoint();

        if (checkpoint == null) {
            parkour.getFormatter().sendHaventPassedCheckpoint(data);
        }
        else {
            if (parkour instanceof ParkourHandler handler &&
                    handler.onCheckpoint(player, data, checkpoint, ParkourHandler.Type.TELEPORT_TO) == ParkourHandler.Response.CANCEL) {
                return;
            }

            player.teleport(checkpoint.toLocationCentered(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
            data.getStats().increment(Stats.Type.CHECKPOINT_TELEPORT, 1);
            parkour.getFormatter().sendCheckpointTeleport(data);
        }
    }

    public void resetParkour(Player player) {
        final Data data = getData(player);
        if (data == null) {
            return;
        }

        startParkour(player, data.getParkour());
        player.teleport(
                data.getParkour().getStart().toLocationCentered(),
                PlayerTeleportEvent.TeleportCause.UNKNOWN /* unknown to not fail parkour because of teleport */
        );
    }

    private boolean handleStart(Data data) {
        if (data.getParkour() instanceof ParkourHandler handler) {
            return handler.onStart(data.get(), data) == ParkourHandler.Response.CANCEL;
        }

        return false;
    }

    @Deprecated
    private boolean tryEventCheckCancel(ParkourEvent event) {
        Bukkit.getServer().getPluginManager().callEvent(event);
        return event.isCancelled();
    }

    public void quitParkour(Player player) {
        final Data data = getData(player);
        if (data == null) {
            return;
        }

        parkourData.remove(player);

        final Parkour parkour = data.getParkour();
        player.teleport(parkour.getQuitLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        data.getPlayerInfo().restore();

        parkour.getFormatter().sendQuit(data);
    }

    public Collection<Parkour> getRegisteredParkours() {
        return registry.values();
    }

    public void registerParkour(Parkour parkour) {
        registry.put(parkour.getStart(), parkour);
        reloadParkours();
    }

    public void unregisterParkour(Parkour parkour) {
        registry.remove(parkour.getStart());
        reloadParkours();
    }

    public Parkour registry(Position target) {
        for (Position position : registry.keySet()) {
            if (position.compare(target)) {
                return registry.get(position);
            }
        }
        return null;
    }

    @Nullable
    public Parkour byStartOrFinish(Location target) {
        for (Parkour parkour : registry.values()) {
            if (parkour.getStart().compare(target) || parkour.getFinish().compare(target)) {
                return parkour;
            }
        }
        return null;
    }

    public boolean isCheckpointOfAnyParkour(Location location) {
        for (Parkour value : registry.values()) {
            if (value.isCheckpoint(location)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public Parkour registry(Location target) {
        for (Position position : registry.keySet()) {
            if (position.compare(target)) {
                return registry.get(position);
            }
        }
        return null;
    }

    public void reloadParkours() {
        unloadParkours();
        loadParkours();
    }

    public void loadParkours() {
        registry.forEach((loc, park) -> park.spawnWorldEntities());
    }

    public void unloadParkours() {
        registry.forEach((loc, park) -> park.removeWorldEntities());
    }

    public boolean isParkouring(Player player) {
        return getData(player) != null;
    }

    public void restoreAllData() {
        getRegisteredParkours().forEach(Parkour::removeWorldEntities);
        parkourData.values().forEach(data -> data.getPlayerInfo().restore());
    }

    public Map<Player, Data> getParkourData() {
        return parkourData;
    }

    @Nullable
    public Data getData(Player player) {
        return parkourData.get(player);
    }
}
