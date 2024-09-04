package me.hapyl.eterna.module.reflect;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.module.entity.LimitedVisibility;
import me.hapyl.eterna.module.reflect.npc.RestPosition;
import me.hapyl.eterna.module.util.BukkitUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class NPCRunnable implements Runnable {

    @Override
    public void run() {
        Eterna.getManagers().npc.getRegistered().forEach((id, npc) -> {
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
