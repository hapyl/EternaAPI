package kz.hapyl.spigotutils.module.reflect.netty.builtin;

import kz.hapyl.spigotutils.SpigotUtilsPlugin;
import kz.hapyl.spigotutils.module.reflect.netty.NettyListener;
import kz.hapyl.spigotutils.module.reflect.npc.ClickType;
import kz.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

public class NPCListener implements NettyListener {
	@Override
	public void readPacket(Player player, Packet<?> packet) {
		if (!(packet instanceof PacketPlayInUseEntity)) {
			return;
		}

		try {

			final int entityId = (int)FieldUtils.getDeclaredField(packet.getClass(), "a", true).get(packet);
			final Object action = FieldUtils.getDeclaredField(packet.getClass(), "b", true).get(packet);

			final ClickType clickType = ClickType.of(MethodUtils.invokeMethod(action, "a", null).toString());
			final EquipmentSlot clickHand = clickType == ClickType.ATTACK ?
					EquipmentSlot.HAND :
					FieldUtils.getDeclaredField(action.getClass(), "a", true)
							.get(action)
							.toString()
							.equalsIgnoreCase("OFF_HAND") ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND;


			Bukkit.getScheduler().runTask(SpigotUtilsPlugin.getPlugin(), () -> {
				if (clickHand == EquipmentSlot.OFF_HAND || clickType == ClickType.INTERACT_AT) {
					return;
				}

				final HumanNPC human = HumanNPC.byId.get(entityId);

				if (human == null) {
					return;
				}

				human.onClick(player, human, clickType);
				human.onClickAuto(player);

			});


		}
		catch (Exception e) {
			e.printStackTrace();
			Bukkit.getLogger().warning("An error trying to inject into NPC channel!");
		}
	}

	@Override
	public void writePacket(Player player, Packet<?> packet) {

	}
}
