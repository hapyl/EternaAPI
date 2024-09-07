package me.hapyl.eterna.builtin.manager;

import com.google.common.collect.Maps;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.parkour.*;
import me.hapyl.eterna.module.player.PlayerLib;
import me.hapyl.eterna.module.util.Disposable;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public final class ParkourManager extends EternaManager<ParkourPosition, Parkour> implements Disposable {

    private final ParkourItemStorage parkourItemStorage;
    private final Map<Player, Data> parkourData;

    ParkourManager(@Nonnull EternaPlugin eterna) {
        super(eterna);

        this.parkourItemStorage = new ParkourItemStorage(this);
        this.parkourData = Maps.newHashMap();
    }

    public void start(@Nonnull Player player, @Nonnull Parkour parkour) {
        // Already doing parkour
        if (isInParkour(player)) {
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

            PlayerLib.addEffect(player, PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1);
            parkourData.put(player, newData);

            parkour.getFormatter().sendParkourStarted(player, parkour);
        }
    }

    public void finish(@Nonnull Player player) {
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

    public void fail(@Nonnull Player player, @Nonnull FailType type) {
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

        PlayerLib.removeEffect(player, PotionEffectType.INVISIBILITY);

        parkour.getFormatter().sendParkourFailed(data, type);
    }

    public void checkpoint(@Nonnull Player player) {
        final Data data = getData(player);

        if (data == null) {
            return;
        }

        final Parkour parkour = data.getParkour();
        final ParkourPosition checkpoint = data.getPreviousCheckpoint();

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

    public void reset(@Nonnull Player player) {
        final Data data = getData(player);

        if (data == null) {
            return;
        }

        start(player, data.getParkour());
        player.teleport(
                data.getParkour().getStart().toLocationCentered(),
                PlayerTeleportEvent.TeleportCause.UNKNOWN /* unknown to not fail parkour because of teleport */
        );
    }

    public void quit(@Nonnull Player player) {
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

    public void register(@Nonnull Parkour parkour) {
        managing.put(parkour.getStart(), parkour);
        reload();
    }

    public void unregister(@Nonnull Parkour parkour) {
        managing.remove(parkour.getStart());
        reload();
    }

    @Nullable
    public Parkour byStartOrFinish(@Nonnull Location target) {
        for (Parkour parkour : managing.values()) {
            if (parkour.getStart().compare(target) || parkour.getFinish().compare(target)) {
                return parkour;
            }
        }
        return null;
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

    public void reload() {
        unload();
        load();
    }

    public void load() {
        managing.forEach((location, parkour) -> {
            parkour.spawnWorldEntities();
        });
    }

    public void unload() {
        managing.forEach((location, parkour) -> {
            parkour.removeWorldEntities();
        });
    }

    public boolean isInParkour(@Nonnull Player player) {
        return getData(player) != null;
    }

    @Nullable
    public Data getData(@Nonnull Player player) {
        return parkourData.get(player);
    }

    @Override
    public void dispose() {
        managing.values().forEach(Parkour::removeWorldEntities);
        parkourData.values().forEach(data -> data.getPlayerInfo().restore());
    }

    @Nonnull
    public Map<Player, Data> getParkourData() {
        return parkourData;
    }
}
