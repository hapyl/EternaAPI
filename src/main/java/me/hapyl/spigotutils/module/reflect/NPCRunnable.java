package me.hapyl.spigotutils.module.reflect;

import me.hapyl.spigotutils.module.util.BukkitUtils;
import me.hapyl.spigotutils.registry.EternaRegistry;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class NPCRunnable implements Runnable {

    @Override
    public void run() {
        EternaRegistry.getNpcRegistry().getRegistered().forEach((id, npc) -> {
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
            }
        });

    }

}
