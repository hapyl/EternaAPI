package me.hapyl.spigotutils.module.reflect;

import me.hapyl.spigotutils.registry.EternaRegistry;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class NPCRunnable implements Runnable {

    @Override
    public void run() {
        EternaRegistry.getNpcRegistry().getRegistered().forEach((id, npc) -> {
            if (npc.getFarAwayDist() > 0 && !npc.isPersistent()) {
                npc.getViewers().forEach((player) -> {
                    if (!Objects.equals(player.getLocation().getWorld(), npc.getLocation().getWorld())) {
                        return;
                    }
                    if (player.getLocation().distance(npc.getLocation()) >= npc.getFarAwayDist()) {
                        if (npc.isShowingTo(player)) {
                            npc.hide(true, player);
                        }
                    }
                    else {
                        if (!npc.isShowingTo(player)) {
                            npc.show(player);
                        }
                    }
                });
            }

            if (npc.getLookAtCloseDist() > 0) {
                final int dist = npc.getLookAtCloseDist();
                final Location location = npc.getLocation();
                if (location.getWorld() == null) {
                    return;
                }

                final Set<Entity> entities = new HashSet<>(location.getWorld().getNearbyEntities(location, dist, dist, dist));

                if (entities.isEmpty()) {
                    return;
                }

                Player closest = null;
                double closestDist = dist;

                for (final Entity entity : entities) {
                    if (!(entity instanceof Player tar)) {
                        continue;
                    }
                    if (closest == null) {
                        closest = tar;
                    }
                    else {
                        final double currentDist = tar.getLocation().distance(location);
                        if (currentDist <= closestDist) {
                            closestDist = currentDist;
                            closest = tar;
                        }
                    }
                }

                if (closest != null && closest.isOnline()) {
                    npc.lookAt(closest);
                }
            }
        });

    }

}
