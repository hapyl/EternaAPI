package me.hapyl.eterna.module.reflect.glowing;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.builtin.manager.GlowingManager;

public class GlowingRunnable implements Runnable {
    @Override
    public void run() {
        final GlowingManager manager = Eterna.getManagers().glowing;
        
        manager.emptyOrphans(); // Wild name
        manager.forEach(Glowing::tick);
    }
}
