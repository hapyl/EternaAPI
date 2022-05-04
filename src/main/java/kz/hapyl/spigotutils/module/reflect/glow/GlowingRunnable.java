package kz.hapyl.spigotutils.module.reflect.glow;


import kz.hapyl.spigotutils.EternaPlugin;

public class GlowingRunnable implements Runnable {
    @Override
    public void run() {
        try {
            EternaPlugin.getPlugin().getGlowingManager().tickAll();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
