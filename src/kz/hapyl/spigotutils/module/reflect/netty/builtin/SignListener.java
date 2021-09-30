package kz.hapyl.spigotutils.module.reflect.netty.builtin;

import kz.hapyl.spigotutils.module.inventory.SignGUI;
import kz.hapyl.spigotutils.module.reflect.netty.NettyListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayInUpdateSign;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SignListener implements NettyListener {

	@Override
	public void readPacket(Player player, Packet<?> packet) {
		if (packet instanceof PacketPlayInUpdateSign sign) {
			try {
				final String[] lines = (String[])FieldUtils.getDeclaredField(sign.getClass(), "c", true).get(packet);
				final SignGUI gui = SignGUI.saved.get(player);

				if (gui == null) {
					return;
				}

				gui.onResponse(player, lines);
				gui.clearSign();
				SignGUI.saved.remove(player);

			}
			catch (Exception e) {
				e.printStackTrace();
				Bukkit.getLogger().warning("An error trying to get Sign GUI strings.");
			}
		}
	}

	@Override
	public void writePacket(Player player, Packet<?> packet) {

	}
}
