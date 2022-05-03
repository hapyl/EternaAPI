package kz.hapyl.spigotutils.module.reflect.glow;

import kz.hapyl.spigotutils.SpigotUtilsPlugin;

public class GlowingRunnable implements Runnable {
    @Override
    public void run() {
        try {
            SpigotUtilsPlugin.getPlugin().getGlowingManager().tickAll();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
