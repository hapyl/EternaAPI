package me.hapyl.spigotutils.module.locaiton;

import me.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TriggerManager implements Listener {

    private static final Map<Integer, Trigger> byId = new HashMap<>();
    protected static final Map<Player, Set<Trigger>> inTrigger = new HashMap<>();

    private static int freeId = 0;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePlayerMove(PlayerMoveEvent ev) {
        if (byId.isEmpty()) {
            return;
        }

        final Player player = ev.getPlayer();
        final Location from = ev.getFrom();
        final Location to = ev.getTo();

        if (to == null) {
            return;
        }

        // check for mouse movement
        if (from.getX() == to.getX() && from.getY() == to.getY() && from.getZ() == to.getZ()) {
            return;
        }

        final Set<Trigger> playerTriggers = getPlayerTriggers(player);
        // handle trigger leave
        if (!playerTriggers.isEmpty()) {
            playerTriggers.iterator().forEachRemaining(trigger -> {
                if (!trigger.getCuboid().isIn(player)) {
                    trigger.broadcastLeaveIfEnabled(player);
                    trigger.onTriggerLeave(player);
                    removePlayerTrigger(player, trigger);
                }
            });
        }

        final Set<Trigger> byLocation = getByLocation(to);

        // handle trigger join
        if (!byLocation.isEmpty()) {
            for (final Trigger trigger : byLocation) {
                if (playerTriggers.contains(trigger)) {
                    continue;
                }
                if (!trigger.hasEnteredBefore(player)) {
                    trigger.onTriggerEnterOnce(player);
                    trigger.setPlayedBefore(player, true);
                }
                trigger.broadcastEnterIfEnabled(player);
                trigger.onTriggerEnter(player);
                addPlayerTrigger(player, trigger);
            }
        }

    }

    private void addPlayerTrigger(Player player, Trigger trigger) {
        final Set<Trigger> set = getPlayerTriggers(player);
        set.add(trigger);
        inTrigger.put(player, set);
    }

    private void removePlayerTrigger(Player player, Trigger trigger) {
        final Set<Trigger> set = getPlayerTriggers(player);
        set.remove(trigger);
        inTrigger.put(player, set);
    }

    public Set<Trigger> getPlayerTriggers(Player player) {
        return inTrigger.getOrDefault(player, new HashSet<>());
    }

    public static void registerTrigger(Trigger trigger) {
        if (byId.containsValue(trigger)) {
            throw new IllegalArgumentException(String.format("Trigger %s is already registered with Id %s!",
                                                             trigger.getName(), trigger.getId()
            ));
        }
        trigger.setId(freeId++);
        byId.put(trigger.getId(), trigger);
    }

    @Nullable
    public Trigger getById(int id) {
        return byId.getOrDefault(id, null);
    }

    @Nullable
    public Trigger getByName(String name) {
        Validate.isTrue(!name.isEmpty(), "name cannot be empty");
        for (final Trigger value : byId.values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        return null;
    }

    public Set<Trigger> getByLocation(Location location) {
        final Set<Trigger> triggers = new HashSet<>();
        for (final Trigger value : byId.values()) {
            if (value.getCuboid().isIn(location)) {
                triggers.add(value);
            }
        }
        return triggers;
    }


}
