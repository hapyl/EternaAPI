package me.hapyl.spigotutils.module.reflect.npc;

import net.minecraft.network.protocol.game.ClientboundAnimatePacket;

public enum NPCAnimation {

    /**
     * Swings main hand (right hand).
     */
    SWING_MAIN_HAND(ClientboundAnimatePacket.SWING_MAIN_HAND),
    /**
     * Glows character red with alpha.
     */
    TAKE_DAMAGE(1),
    /**
     * Idk
     */
    LEAVE_BED(ClientboundAnimatePacket.WAKE_UP),
    /**
     * Swings off hand (left hand).
     */
    SWING_OFF_HAND(ClientboundAnimatePacket.SWING_OFF_HAND),
    /**
     * Idk
     */
    CRITICAL_EFFECT(ClientboundAnimatePacket.CRITICAL_HIT),
    /**
     * Idk
     */
    MAGIC_CRITICAL_EFFECT(ClientboundAnimatePacket.MAGIC_CRITICAL_HIT);

    private final int pos;

    NPCAnimation(int i) {
        this.pos = i;
    }

    public int getPos() {
        return pos;
    }
}
