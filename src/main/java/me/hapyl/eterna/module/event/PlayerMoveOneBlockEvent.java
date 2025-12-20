package me.hapyl.eterna.module.event;

import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaLock;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents an event called when a player has moved a single block,
 * regardless if it's on foot, in a vehicle or on an elytra.
 * <p>This is considered a 'heavy' event, since it keeps track of player and vehicle movements.</p>
 */
public class PlayerMoveOneBlockEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private boolean cancel;

    public PlayerMoveOneBlockEvent(@Nonnull Player player) {
        super(player);
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Nonnull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @ApiStatus.Internal
    public static final class Handler extends EternaLock implements Listener {

        private static final Map<Player, Data> PLAYER_DATA = new ConcurrentHashMap<>();
        
        public Handler(@Nullable EternaKey key) {
            super(key);
        }
        
        @EventHandler(ignoreCancelled = true)
        public void handlePlayerMoveEvent(PlayerMoveEvent ev) {
            final Player player = ev.getPlayer();
            final Location from = ev.getFrom();
            final Location to = ev.getTo();

            increment(player, from, to);
        }

        @EventHandler(ignoreCancelled = true)
        public void handleVehicleMoveEvent(VehicleMoveEvent ev) {
            final Vehicle vehicle = ev.getVehicle();
            final List<Entity> passengers = vehicle.getPassengers();

            // We actually only care about minecarts, because for some
            // reason they don't call move event ¯\_(ツ)_/¯
            if (!(vehicle instanceof Minecart)) {
                return;
            }

            if (passengers.isEmpty()) {
                return;
            }

            for (Entity passenger : passengers) {
                if (!(passenger instanceof Player player)) {
                    continue;
                }

                increment(player, ev.getFrom(), ev.getTo());
            }
        }

        @EventHandler
        public void handlePlayerQuitEvent(PlayerQuitEvent ev) {
            PLAYER_DATA.remove(ev.getPlayer());
        }

        private static void increment(Player player, Location from, Location to) {
            if (from.distanceSquared(to) == 0.0d) { // ignore mouse movement
                return;
            }

            final Data data = PLAYER_DATA.computeIfAbsent(player, t -> new Data(from));

            if (data.increment(from, to) < 1.0d) {
                return;
            }

            // If the event is cancelled, teleport to the initial
            // location and reset x, y, and z
            if (!new PlayerMoveOneBlockEvent(player).callEvent()) {
                final Location playerLocation = player.getLocation();
                final Location location = data.initialLocation;

                // Mutating the initial location is fine
                location.setYaw(playerLocation.getYaw());
                location.setPitch(playerLocation.getPitch());

                player.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
                
                data.movedX = 0.0d;
                data.movedY = 0.0d;
                data.movedZ = 0.0d;
                return;
            }

            PLAYER_DATA.remove(player);
        }
    }

    private static class Data {
        Location initialLocation;
        double movedX;
        double movedY;
        double movedZ;

        Data(Location location) {
            this.initialLocation = location;
        }

        double increment(Location from, Location to) {
            final double fromX = from.getX();
            final double toX = to.getX();

            final double fromY = from.getY();
            final double toY = to.getY();

            final double fromZ = from.getZ();
            final double toZ = to.getZ();

            movedX += Math.max(fromX, toX) - Math.min(fromX, toX);
            movedY += Math.max(fromY, toY) - Math.min(fromY, toY);
            movedZ += Math.max(fromZ, toZ) - Math.min(fromZ, toZ);

            return movedX + movedY + movedZ;
        }

    }
}
