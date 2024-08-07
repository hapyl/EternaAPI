package me.hapyl.eterna.module.reflect.npc;

public enum NPCAnimation {

    /**
     * Swings main hand (right hand).
     */
    SWING_MAIN_HAND(0),
    /**
     * Glows character red with alpha.
     */
    TAKE_DAMAGE(1),
    /**
     * Idk
     */
    LEAVE_BED(2),
    /**
     * Swings off hand (left hand).
     */
    SWING_OFF_HAND(3),
    /**
     * Idk
     */
    CRITICAL_EFFECT(4),
    /**
     * Idk
     */
    MAGIC_CRITICAL_EFFECT(5);

    private final int pos;

    NPCAnimation(int i) {
        this.pos = i;
    }

    public int getPos() {
        return pos;
    }
}
