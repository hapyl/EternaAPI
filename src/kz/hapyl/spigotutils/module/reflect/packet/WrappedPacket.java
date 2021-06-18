package kz.hapyl.spigotutils.module.reflect.packet;

import kz.hapyl.spigotutils.module.annotate.Super;
import kz.hapyl.spigotutils.module.reflect.Reflect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class WrappedPacket implements IPacket {

	private final Object  packet;

	protected WrappedPacket(Object packet) {
		this.packet = packet;
		this.validatePacket();
	}

	public static WrappedPacket empty() {
		return new WrappedPacket("null");
	}

	@Super
	@Override
	public void sendPacket(Player... viewers) {
		this.validatePacket();
		for (final Player viewer : viewers) {
			Reflect.sendPacket(viewer, this.packet);
		}
	}

	@Override
	public void sendPacket() {
		sendPacket(Bukkit.getOnlinePlayers().toArray(new Player[]{}));
	}

	private void validatePacket() {
		if (this.packet == null || !this.packet.getClass().getSimpleName().contains("Packet")) {
			throw new IllegalArgumentException(String.format("Provided object is not a packet! (%s)",
					this.packet == null ? "NULL_PACKET" : this.packet.getClass().getSimpleName()));
		}
	}

}