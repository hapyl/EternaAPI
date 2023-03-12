package me.hapyl.spigotutils.module.reflect.npc;

public enum SkinPart {

    CAPE((byte) 0x01),
    JACKET((byte) 0x02),
    LEFT_SLEEVE((byte) 0x04),
    RIGHT_SLEEVE((byte) 0x08),
    LEFT_LEG((byte) 0x10),
    RIGHT_LEG((byte) 0x20),
    HAT((byte) 0x40);

    private final byte mask;

    SkinPart(byte mask) {
        this.mask = mask;
    }

    public static byte mask(SkinPart... parts) {
        byte mask = 0;
        for (SkinPart part : parts) {
            mask |= part.mask;
        }
        return mask;
    }

}
