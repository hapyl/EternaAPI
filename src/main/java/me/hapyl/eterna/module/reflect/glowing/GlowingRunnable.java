package me.hapyl.eterna.module.reflect.glowing;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaLock;
import me.hapyl.eterna.builtin.manager.GlowingManager;

import javax.annotation.Nonnull;

public class GlowingRunnable extends EternaLock implements Runnable {
    public GlowingRunnable(@Nonnull EternaKey key) {
        super(key);
    }
    
    @Override
    public void run() {
        final GlowingManager manager = Eterna.getManagers().glowing;
        
        manager.emptyOrphans(); // Wild name
        manager.forEach(Glowing::tick);
    }
}
