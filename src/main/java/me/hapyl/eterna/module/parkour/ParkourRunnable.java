package me.hapyl.eterna.module.parkour;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.builtin.manager.ParkourManager;

public abstract class ParkourRunnable implements Runnable {
    
    protected final ParkourManager parkourManager;
    
    public ParkourRunnable() {
        this.parkourManager = Eterna.getManagers().parkour;
    }
    
    public static class Actionbar extends ParkourRunnable {
        @Override
        public void run() {
            parkourManager.getParkourData().forEach((player, playerData) -> playerData.getParkour().getFormatter().sendTickActionbar(playerData));
        }
    }
    
    public static class Hologram extends ParkourRunnable {
        @Override
        public void run() {
            parkourManager.forEach(Parkour::tickHolograms);
        }
    }
}
