package kz.hapyl.spigotutils.module.hologram;


import net.minecraft.network.protocol.Packet;

public class HologramPackets {

	private final Packet<?> createPacket;
	private final Packet<?> destroyPacket;
	private final Packet<?> metadataPacket;

	public HologramPackets(Packet<?> createPacket, Packet<?> destroyPacket, Packet<?> metadataPacket) {
		this.createPacket = createPacket;
		this.destroyPacket = destroyPacket;
		this.metadataPacket = metadataPacket;
	}

	public Packet<?> getCreatePacket() {
		return createPacket;
	}

	public Packet<?> getDestroyPacket() {
		return destroyPacket;
	}

	public Packet<?> getMetadataPacket() {
		return metadataPacket;
	}
}
