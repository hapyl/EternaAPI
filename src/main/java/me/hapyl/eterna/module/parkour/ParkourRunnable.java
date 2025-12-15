package me.hapyl.eterna.module.parkour;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaLock;
import me.hapyl.eterna.builtin.manager.ParkourManager;

import javax.annotation.Nonnull;

public abstract class ParkourRunnable extends EternaLock implements Runnable {
    
    protected final ParkourManager parkourManager;
    
    public ParkourRunnable(@Nonnull EternaKey key) {
        super(key);
        
        this.parkourManager = Eterna.getManagers().parkour;
    }
    
    public static class Actionbar extends ParkourRunnable {
        public Actionbar(@Nonnull EternaKey key) {
            super(key);
        }
        
        @Override
        public void run() {
            parkourManager.getParkourData().forEach((player, playerData) -> playerData.getParkour().getFormatter().sendTickActionbar(playerData));
        }
    }
    
    public static class Hologram extends ParkourRunnable {
        public Hologram(@Nonnull EternaKey key) {
            super(key);
        }
        
        @Override
        public void run() {
            parkourManager.forEach(Parkour::tickHolograms);
        }
    }
}
