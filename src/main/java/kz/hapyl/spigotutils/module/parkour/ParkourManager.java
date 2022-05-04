package kz.hapyl.spigotutils.module.parkour;

import com.google.common.collect.Maps;
import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.event.parkour.*;
import kz.hapyl.spigotutils.module.player.EffectType;
import kz.hapyl.spigotutils.module.player.PlayerLib;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

public class ParkourManager {

    public final Map<Position, Parkour> byPosition = Maps.newHashMap();

    public final ParkourItemStorage parkourItemStorage = new ParkourItemStorage(this);
    public final String PARKOUR_MESSAGE_FORMAT = "&6&lPARKOUR! &7";

    private final Map<Player, Data> parkourData;

    public ParkourManager() {
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

                if (!parkour.isSilent()) {
                    sendParkourMessage(player, "Reset time for %s!", parkour.getName());
                    PlayerLib.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f);
                }
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

            if (!parkour.isSilent()) {
                sendParkourMessage(player, "Started %s!", parkour.getName());
                PlayerLib.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f);
            }
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

        if (!data.getParkour().isSilent()) {
            sendParkourMessage(
                    player,
                    "Finished &a%s&7 in &b%ss&7!",
                    data.getParkour().getName(),
                    data.getTimePassedFormatted()
            );
        }
    }

    public void failParkour(Player player, FailType type) {
        final Data data = getData(player);
        if (data == null) {
            return;
        }

        if (tryEventCheckCancel(new ParkourFailEvent(player, data, type))) {
            return;
        }

        PlayerLib.removeEffect(player, EffectType.INVISIBILITY);
        data.getPlayerInfo().restore();

        if (!data.getParkour().isSilent()) {
            sendParkourMessage(player, "&cParkour failed, &l%s&c!", type.getReason());
            PlayerLib.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f);
        }

        parkourData.remove(player);
    }

    public void teleportToCheckpoint(Player player) {
        final Data data = getData(player);
        if (data == null) {
            return;
        }

        final boolean silent = data.getParkour().isSilent();
        final Position checkpoint = data.getPreviousCheckpoint();
        if (checkpoint == null) {
            if (!silent) {
                sendParkourMessage(player, "You haven't passed any checkpoints yet!");
                PlayerLib.Sounds.ENDERMAN_TELEPORT.play(player, 0.0f);
            }
        }
        else {
            if (tryEventCheckCancel(new ParkourCheckpointEvent(
                    player,
                    data,
                    checkpoint,
                    ParkourCheckpointEvent.Type.TELEPORT_TO
            ))) {
                return;
            }
            player.teleport(checkpoint.toLocationCentered(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
            data.getStats().increment(Stats.Type.CHECKPOINT_TELEPORT, 1);
            if (!silent) {
                PlayerLib.Sounds.ENDERMAN_TELEPORT.play(player, 1.25f);
            }
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

        if (!parkour.isSilent()) {
            sendParkourMessage(player, "Quit %s!", parkour.getName());
        }
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
        parkourData.values().forEach(data -> data.getPlayerInfo().restore());
    }

    public Map<Player, Data> getParkourData() {
        return parkourData;
    }

    @Nullable
    public Data getData(Player player) {
        return parkourData.get(player);
    }

    public void sendParkourMessage(Player player, String message, Object... objects) {
        Chat.sendMessage(player, PARKOUR_MESSAGE_FORMAT + message, objects);
    }

    public void broadcastParkourMessage(String message, Object... objects) {
        Chat.broadcast(PARKOUR_MESSAGE_FORMAT + message, objects);
    }

}
