package kz.hapyl.spigotutils.module.reflect.npc;

public enum NPCAnimation {

    SWING_MAIN_HAND(0),
    TAKE_DAMAGE(1),
    LEAVE_BED(2),
    SWING_OFF_HAND(3),
    CRITICAL_EFFECT(4),
    MAGIC_CRITICAL_EFFECT(5);

    private final int pos;

    NPCAnimation(int i) {
        this.pos = i;
    }

    public int getPos() {
        return pos;
    }
}
