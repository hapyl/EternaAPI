package me.hapyl.eterna.module.reflect.glow;


import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.util.Ticking;

public class GlowingRunnable implements Runnable {
    @Override
    public void run() {
        try {
            Eterna.getManagers().glowing.forEach((k, v) -> v.tick());
        } catch (Exception ex) {
            EternaLogger.exception(ex);
        }
    }
}
