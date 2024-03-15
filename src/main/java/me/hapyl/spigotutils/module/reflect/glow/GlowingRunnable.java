package me.hapyl.spigotutils.module.reflect.glow;


import me.hapyl.spigotutils.Eterna;
import me.hapyl.spigotutils.EternaLogger;
import me.hapyl.spigotutils.registry.EternaRegistry;

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
