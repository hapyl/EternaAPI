package kz.hapyl.spigotutils.module.entity;

import kz.hapyl.spigotutils.module.reflect.Reflect;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
import net.minecraft.world.entity.EntityLiving;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

public final class PacketEntity {

	private final PacketEntityType type;
	private final Set<Player>      viewers;

	// Entity properties
	private Location location;
	private boolean  visible;
	private boolean  glowing;

	private Object entity;

	private Object packetShow;
	private Object packetHide;
	private Object headPacket;

	public PacketEntity(PacketEntityType type) {
		this.type = type;
		this.viewers = new HashSet<>();
		this.visible = true;
		this.glowing = false;
	}

	private boolean isExists() {
		return this.entity != null;
	}

	public void create(Location location) {

		this.validateDoesNotExists();
		this.location = location;

		final Constructor<?> constructor = Reflect.getNetConstructor(this.type.getNmsPath(), Reflect.getNetClass("EntityTypes"),
				Reflect.getNetClass("World"));

		final Object field = Reflect.getFieldValue(Reflect.getNetField("EntityTypes", this.type.getNsmEntityTypes()), null);

		this.entity = Reflect.newInstance(constructor, field, Reflect.getNetWorld(location.getWorld()));
		this.setLocation(location);

		// FIXME: 028. 05/28/2021 - create packets here

	}

	public void destroy() {
		if (!isExists()) {
			return;
		}
	}

	public void show(Player... viewers) {

		this.validateExists();
		this.validateViewers(viewers);

		final PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving((EntityLiving) this.entity);
		for (final Player player : viewers) {
			Reflect.sendPacket(player, packet);
		}
	}

	public void hide(Player... viewers) {

		this.validateExists();
		this.validateViewers(viewers);

		final PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(this.getEntityId());
		for (final Player player : viewers) {
			Reflect.sendPacket(player, packet);
		}
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.teleport(location);
	}

	public void teleport(Location location) {
		EntityLiving l = ((EntityLiving) this.entity);
//		((EntityLiving) this.entity).teleportAndSync(location.getX(), location.getY(), location.getZ());
//		l.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
//		l.teleportAndSync(location.getX(), location.getY(), location.getZ());

		l.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		l.setFlag((byte) 0x40, true);

		// Fix head rotation
		this.headPacket = new PacketPlayOutEntityHeadRotation(l, (byte) (location.getYaw() * 256 / 360));

	}

	public int getEntityId() {
		try {
			assert false;
			return (int) Reflect.invokeMethod(Reflect.getNetMethod("EntityLiving", "getId"), this.entity);
		}
		catch (Exception e) {
			return -1;
		}
	}

	private void updateMetadata() {
		// this is the hard one...
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isGlowing() {
		return glowing;
	}

	public void setGlowing(boolean glowing) {
		this.glowing = glowing;
	}

	private void validateDoesNotExists() {
		if (isExists()) {
			throw new IllegalStateException("Entity already created!");
		}
	}

	private void validateExists() {
		if (!isExists()) {
			throw new IllegalStateException("You must first '.create(Location)' entity to use this!");
		}
	}

	private void validateViewers(Player[] viewers) {
		if (viewers == null || viewers.length == 0) {
			throw new IllegalArgumentException("There must be at least one viewer!");
		}
	}

}
