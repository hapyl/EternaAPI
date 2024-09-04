package me.hapyl.eterna.module.parkour;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.builtin.manager.ParkourManager;
import org.bukkit.entity.Player;

import java.util.Map;

public class ParkourRunnable implements Runnable {
    @Override
    public void run() {
        final ParkourManager manager = Eterna.getManagers().parkour;
        final Map<Player, Data> hashMap = manager.getParkourData();

        for (Data data : hashMap.values()) {
            final Parkour parkour = data.getParkour();

            parkour.getFormatter().sendTickActionbar(data);
        }
    }
}
