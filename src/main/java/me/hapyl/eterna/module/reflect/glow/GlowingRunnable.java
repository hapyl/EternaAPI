package me.hapyl.eterna.module.reflect.glow;


import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.EternaLogger;

public class GlowingRunnable implements Runnable {
    @Override
    public void run() {
        try {
            Eterna.getRegistry().glowingRegistry.tickAll();
        } catch (Exception ex) {
            EternaLogger.exception(ex);
        }
    }
}
