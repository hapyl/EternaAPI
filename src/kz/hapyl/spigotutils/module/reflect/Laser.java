package kz.hapyl.spigotutils.module.reflect;

import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.EntitySquid;
import net.minecraft.world.entity.monster.EntityGuardian;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Laser {

	private final Location start;
	private final Location end;

	private EntityGuardian guardian;
	private EntitySquid squid;

	public Laser(Location start, Location end) {
		this.start = start;
		this.end = end;
	}

	public void spawn(Player... viewers) {
		viewers = a(viewers);

		// NSM
		this.guardian = new EntityGuardian(EntityTypes.K, Reflect.getMinecraftWorld(this.start.getWorld()));
		this.guardian.setLocation(this.start.getX(), this.start.getY(), this.start.getZ(), this.start.getYaw(), this.start.getPitch());

		this.squid = new EntitySquid(EntityTypes.aJ, Reflect.getMinecraftWorld(this.end.getWorld()));
		this.squid.setLocation(this.end.getX(), this.end.getY(), this.end.getZ(), this.end.getYaw(), this.end.getPitch());

		new ReflectPacket(new PacketPlayOutSpawnEntityLiving(this.guardian)).sendPackets(viewers);
		new ReflectPacket(new PacketPlayOutSpawnEntityLiving(this.squid)).sendPackets(viewers);

		Reflect.setDataWatcherValue(squid, DataWatcherType.BYTE, 0, (byte)0x20, viewers);
		Reflect.setDataWatcherValue(guardian, DataWatcherType.BYTE, 0, (byte)0x20, viewers);
		Reflect.setDataWatcherValue(guardian, DataWatcherType.INT, 17, squid.getId(), viewers);

	}

	private Player[] a(Player... b) {
		if (b == null || b.length == 0) {
			return Bukkit.getOnlinePlayers().toArray(new Player[]{});
		}
		return b;
	}

	public void remove(Player... viewers) {
		if (this.guardian == null || this.squid == null) {
			return;
		}

		viewers = a(viewers);
		Reflect.destroyEntity(this.guardian, viewers);
		Reflect.destroyEntity(this.squid, viewers);

	}

	public void move(Location start, Location end, Player... v1) {
		v1 = a(v1);
		Reflect.setEntityLocation(this.guardian, start);
		Reflect.setEntityLocation(this.squid, end);
		Reflect.updateEntityLocation(this.guardian, v1);
		Reflect.updateEntityLocation(this.squid, v1);
	}

}
