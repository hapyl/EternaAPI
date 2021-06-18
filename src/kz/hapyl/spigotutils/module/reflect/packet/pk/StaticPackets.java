package kz.hapyl.spigotutils.module.reflect.packet.pk;

import kz.hapyl.spigotutils.module.reflect.Reflect;
import kz.hapyl.spigotutils.module.reflect.packet.PacketWrapper;
import kz.hapyl.spigotutils.module.reflect.packet.WrappedPacket;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;

/**
 * Reflection Alert
 */
public final class StaticPackets {

	public static WrappedPacket PacketPlayOutEntityDestroy(int id) {
		return new PacketWrapper("PacketPlayOutEntityDestroy", int[].class).newInstance(PacketWrapper.Util.reflectArray(int.class, id));
	}

	public static WrappedPacket PacketPlayOutPlayerInfo(PlayerInfoAction info, Player player) {

		final Class<?> enumInfo = nmsClass("PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
		final Object craftPlayer = nmsPlayer(player);

		// create reflect array
		final Object array = Array.newInstance(craftPlayer.getClass(), 1);
		Array.set(array, 0, craftPlayer);

		final Object enumValue = Reflect.lazyFieldValue(enumInfo, info.name(), null);
		return new PacketWrapper("PacketPlayOutPlayerInfo", enumInfo, array.getClass()).newInstance(enumValue, array);

	}

	public static WrappedPacket PacketPlayOutSpawnEntityLiving(LivingEntity entity) {
		return new PacketWrapper("PacketPlayOutSpawnEntityLiving", nmsClass("EntityLiving")).newInstance(Reflect.getNetEntity(entity));
	}

	private static Class<?> nmsClass(String nmsClass) {
		return Reflect.getNetClass(nmsClass);
	}

	private static Object nmsPlayer(Player player) {
		return Reflect.getCraftPlayer(player);
	}

	private static Constructor<?> nmsConstructor(String nsmClass, Class<?> params) {
		return Reflect.getNetConstructor(nsmClass, params);
	}

}
