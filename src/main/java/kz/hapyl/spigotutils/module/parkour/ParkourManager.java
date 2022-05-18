package kz.hapyl.spigotutils.module.parkour;

import com.google.common.collect.Maps;
import kz.hapyl.spigotutils.EternaPlugin;
import kz.hapyl.spigotutils.HashRegistry;
import kz.hapyl.spigotutils.module.event.parkour.*;
import kz.hapyl.spigotutils.module.player.EffectType;
import kz.hapyl.spigotutils.module.player.PlayerLib;
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

public class ParkourManager extends HashRegistry<Position, Parkour> {

    public final Map<Position, Parkour> byPosition = Maps.newHashMap();

    public final ParkourItemStorage parkourItemStorage = new ParkourItemStorage(this);

    private final Map<Player, Data> parkourData;

    public ParkourManager(EternaPlugin plugin) {
        super(plugin);
        this.parkourData = Maps.newHashMap();
    }

    @Override
    public void register(Position position, Parkour parkour) {
        byPosition.put(position, parkour);
    }

    @Override
    public void unregister(Position position, Parkour parkour) {
        byPosition.remove(position);
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

            if (tryEventCheckCancel(new ParkourStartEvent(player, newData))) {
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

        if (tryEventCheckCancel(new ParkourFinishEvent(player, data))) {
            return;
        }

        data.setFinished();
        parkourData.remove(player);

        data.getPlayerInfo().restore();
        player.teleport(data.getParkour().getQuitLocation());

        data.getParkour().getFormatter().sendParkourFinished(data);
    }

    public void failParkour(Player player, FailType type) {
        final Data data = getData(player);
        if (data == null) {
            return;
        }

        if (tryEventCheckCancel(new ParkourFailEvent(player, data, type))) {
            return;
        }

        // make sure to remove player's data before restoring the info
        parkourData.remove(player);
        data.getPlayerInfo().restore();

        PlayerLib.removeEffect(player, EffectType.INVISIBILITY);

        data.getParkour().getFormatter().sendParkourFailed(data, type);
    }

    public void teleportToCheckpoint(Player player) {
        final Data data = getData(player);
        if (data == null) {
            return;
        }

        final Position checkpoint = data.getPreviousCheckpoint();
        if (checkpoint == null) {
            data.getParkour().getFormatter().sendHaventPassedCheckpoint(data);
        }
        else {
            if (tryEventCheckCancel(new ParkourCheckpointEvent(player, data, checkpoint, ParkourCheckpointEvent.Type.TELEPORT_TO))) {
                return;
            }
            player.teleport(checkpoint.toLocationCentered(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
            data.getStats().increment(Stats.Type.CHECKPOINT_TELEPORT, 1);
            data.getParkour().getFormatter().sendCheckpointTeleport(data);
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
        return byPosition.values();
    }

    public void registerParkour(Parkour parkour) {
        byPosition.put(parkour.getStart(), parkour);
        reloadParkours();
    }

    public void unregisterParkour(Parkour parkour) {
        byPosition.remove(parkour.getStart());
        reloadParkours();
    }

    public Parkour byPosition(Position target) {
        for (Position position : byPosition.keySet()) {
            if (position.compare(target)) {
                return byPosition.get(position);
            }
        }
        return null;
    }

    @Nullable
    public Parkour byStartOrFinish(Location target) {
        for (Parkour parkour : byPosition.values()) {
            if (parkour.getStart().compare(target) || parkour.getFinish().compare(target)) {
                return parkour;
            }
        }
        return null;
    }

    @Nullable
    public Parkour byPosition(Location target) {
        for (Position position : byPosition.keySet()) {
            if (position.compare(target)) {
                return byPosition.get(position);
            }
        }
        return null;
    }

    public void reloadParkours() {
        unloadParkours();
        loadParkours();
    }

    public void loadParkours() {
        byPosition.forEach((loc, park) -> park.spawnWorldEntities());
    }

    public void unloadParkours() {
        byPosition.forEach((loc, park) -> park.removeWorldEntities());
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
