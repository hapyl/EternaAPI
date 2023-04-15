package me.hapyl.spigotutils.module.reflect.glow;

import me.hapyl.spigotutils.module.util.CollectionUtils;
import net.minecraft.EnumChatFormat;

public enum GlowingColor {

    BLACK(EnumChatFormat.a),
    DARK_BLUE(EnumChatFormat.b),
    DARK_GREEN(EnumChatFormat.c),
    DARK_AQUA(EnumChatFormat.d),
    DARK_RED(EnumChatFormat.e),
    DARK_PURPLE(EnumChatFormat.f),
    GOLD(EnumChatFormat.g),
    GRAY(EnumChatFormat.h),
    DARK_GRAY(EnumChatFormat.i),
    BLUE(EnumChatFormat.j),
    GREEN(EnumChatFormat.k),
    AQUA(EnumChatFormat.l),
    RED(EnumChatFormat.m),
    LIGHT_PURPLE(EnumChatFormat.n),
    YELLOW(EnumChatFormat.o),
    WHITE(EnumChatFormat.p);

    private final EnumChatFormat nms;

    GlowingColor(EnumChatFormat nms) {
        this.nms = nms;
    }

    public EnumChatFormat getNms() {
        return nms;
    }

    public static GlowingColor random() {
        return CollectionUtils.randomElement(values());
    }
}
