package kz.hapyl.spigotutils.module.reflect;

public enum MetadataType {

	GLOWING((byte) 0x40);

	private final byte bitMask;

	MetadataType(byte bitMask) {
		this.bitMask = bitMask;
	}

	public byte getBitMask() {
		return bitMask;
	}
}
