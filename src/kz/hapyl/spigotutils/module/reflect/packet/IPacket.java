package kz.hapyl.spigotutils.module.reflect.packet;

import org.bukkit.entity.Player;

public interface IPacket {

	void sendPacket(Player... viewers);

	void sendPacket();


}
