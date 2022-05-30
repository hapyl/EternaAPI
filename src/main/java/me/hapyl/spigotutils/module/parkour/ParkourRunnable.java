package me.hapyl.spigotutils.module.parkour;

import me.hapyl.spigotutils.EternaPlugin;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * This sends actionbar update every 2 ticks.
 */
public class ParkourRunnable implements Runnable {
    @Override
    public void run() {
        final ParkourManager manager = EternaPlugin.getPlugin().getParkourManager();
        final Map<Player, Data> hashMap = manager.getParkourData();

        for (Data data : hashMap.values()) {
            final Parkour parkour = data.getParkour();

            parkour.getFormatter().sendTickActionbar(data);
        }
    }
}
