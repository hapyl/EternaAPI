package me.hapyl.eterna.module.reflect.glow;

public interface GlowingListener {

    /**
     * Called once whenever glowing starts ticking.
     */
    void onGlowingStart();

    /**
     * Called once whenever glowing stopped.
     * No matter if manually or duration ended.
     */
    void onGlowingStop();

    /**
     * Called every tick glowing is... well... ticking.
     */
    void onGlowingTick();

}
