package me.hapyl.spigotutils.module.reflect.npc;

import com.comphenix.protocol.wrappers.EnumWrappers;
import me.hapyl.spigotutils.module.util.Validate;

public enum ClickType {

    ATTACK, // Left click at entity
    INTERACT, // Right click at entity
    INTERACT_AT; // Also right-click on entity?

    public static ClickType of(String str) {
        return Validate.getEnumValue(ClickType.class, str, INTERACT_AT);
    }

    public static ClickType fromProtocol(EnumWrappers.EntityUseAction action) {
        return switch (action) {
            case ATTACK -> ClickType.ATTACK;
            case INTERACT -> ClickType.INTERACT;
            case INTERACT_AT -> ClickType.INTERACT_AT;
        };
    }

}
