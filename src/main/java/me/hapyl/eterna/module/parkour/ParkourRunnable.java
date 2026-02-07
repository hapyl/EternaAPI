package me.hapyl.eterna.module.parkour;

import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaKeyed;
import org.jetbrains.annotations.NotNull;

public sealed abstract class ParkourRunnable extends EternaKeyed implements Runnable {
    
    public ParkourRunnable(@NotNull EternaKey key) {
        super(key);
    }
    
    public static final class Actionbar extends ParkourRunnable {
        public Actionbar(@NotNull EternaKey key) {
            super(key);
        }
        
        @Override
        public void run() {
            ParkourHandler.handler.forEachPlayerData(playerData -> playerData.getParkour().getFormatter().actionbar(playerData));
        }
    }
    
    public static final class Hologram extends ParkourRunnable {
        public Hologram(@NotNull EternaKey key) {
            super(key);
        }
        
        @Override
        public void run() {
            ParkourHandler.handler.forEach(Parkour::tick);
        }
    }
}
