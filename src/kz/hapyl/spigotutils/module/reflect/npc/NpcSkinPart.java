package kz.hapyl.spigotutils.module.reflect.npc;

public enum NpcSkinPart {

	CAPE(0x01),
	JACKET(0x02),
	LEFT_ARM(0x04),
	RIGHT_ARM(0x08),
	LEFT_LEG(0x10),
	RIGHT_LEG(0x20),
	HAT(0x40);

	private final byte bitMask;

	NpcSkinPart(int bitMask) {
		this((byte) bitMask);
	}

	NpcSkinPart(byte bitMask) {
		this.bitMask = bitMask;
	}

	public byte getBitMask() {
		return bitMask;
	}
}
