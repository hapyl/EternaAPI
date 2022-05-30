package me.hapyl.spigotutils.module.reflect.glow;


import me.hapyl.spigotutils.EternaPlugin;

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
