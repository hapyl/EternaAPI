package kz.hapyl.spigotutils.module.reflect.netty;

import net.minecraft.network.protocol.Packet;
import org.bukkit.entity.Player;

public interface NettyListener {

	void readPacket(Player player, Packet<?> packet);

	void writePacket(Player player, Packet<?> packet);

}
