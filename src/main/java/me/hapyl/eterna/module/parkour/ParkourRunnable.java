package me.hapyl.eterna.module.parkour;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.builtin.manager.ParkourManager;

public class ParkourRunnable implements Runnable {
    @Override
    public void run() {
        final ParkourManager parkourManager = Eterna.getManagers().parkour;
        
        // Tick actionbar every tick
        parkourManager.getParkourData().forEach((player, playerData) -> playerData.getParkour().getFormatter().sendTickActionbar(playerData));
        
        // Tick parkour
        parkourManager.forEach(Parkour::tick);
    }
}
