package me.hapyl.spigotutils.module.reflect;

import me.hapyl.spigotutils.Eterna;
import me.hapyl.spigotutils.EternaLogger;
import me.hapyl.spigotutils.module.entity.LimitedVisibility;
import me.hapyl.spigotutils.module.reflect.npc.RestPosition;
import me.hapyl.spigotutils.module.util.BukkitUtils;
import me.hapyl.spigotutils.registry.EternaRegistry;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class NPCRunnable implements Runnable {

    @Override
    public void run() {
        Eterna.getRegistry().npcRegistry.getRegistered().forEach((id, npc) -> {
            // Visibility
            for (Player player : npc.getViewers()) {
                LimitedVisibility.check(player, npc);
            }

            // Look at the closest player
            if (npc.getLookAtCloseDist() > 0) {
                final int dist = npc.getLookAtCloseDist();
                final Location location = npc.getLocation();

                if (location.getWorld() == null) {
                    return;
                }

                final Player nearest = BukkitUtils.getNearestEntity(location, dist, dist, dist, Player.class);

                if (nearest != null && nearest.isOnline()) {
                    npc.lookAt(nearest);
                }
                else {
                    final RestPosition restPosition = npc.getRestPosition();
                    if (restPosition != null) {
                        final float yaw = location.getYaw();
                        final float pitch = location.getPitch();

                        if (yaw == restPosition.yaw() && pitch == restPosition.pitch()) {
                            return;
                        }

                        npc.setHeadRotation(restPosition.yaw(), restPosition.pitch());
                    }
                }
            }
        });

    }

}
