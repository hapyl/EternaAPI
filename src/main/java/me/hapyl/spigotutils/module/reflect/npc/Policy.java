package me.hapyl.spigotutils.module.reflect.npc;

enum Policy {
    /**
     * This method will only work if the NPC is not spawned.
     */
    BEFORE_SPAWN,

    /**
     * This method will only work if the NPC is spawned.
     */
    AFTER_SPAWN,

    /**
     * This method will work anytime.
     */
    ANYTIME
}
