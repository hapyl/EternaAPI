package kz.hapyl.spigotutils.module.reflect;

import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

/**
 * This class allows to create per player border, useful for 'warning red screen' effect.
 */
public class Border {

	private Object border;
	private final World world;
	private int size, warning;

	/**
	 * Creates a border object.
	 *
	 * @param world - CraftWorld. Usage:
	 *              <p>
	 *              this.Border border = new this.Border(WORLD); border.setSize(1000).setWarningDistance(1000); border.applyChanges(PlAYER...)
	 *              <p>
	 *              /OR/ new this.Border(WORLD).setSize(1000).setWarningDistance(1000).applyChanges(PLAYER...);
	 */
	public Border(World world) {

		this.world = world;
		this.size = 1000;
		this.warning = 0;
		this.init();
	}

	/**
	 * Sets the size of the border.
	 *
	 * @param size - Size.
	 */
	public Border setSize(int size) {
		this.size = size;
		return this;
	}

	/**
	 * Sets the distance for the warning.
	 *
	 * @param distance - Distance.
	 */
	public Border setWarningDistance(int distance) {
		this.warning = distance;
		return this;
	}

	public static void reset(Player player) {
		player.getWorld().getWorldBorder().reset();
	}

	/**
	 * Applies all the changes via sending packets the viewers.
	 *
	 * @param viewers - Players that will see the effect.
	 */
	public void applyChanges(Player... viewers) {

		if (viewers.length == 0) {
			throw new IllegalArgumentException("Viewers must have at least 1 player!");
		}

		try {
			for (Player player : viewers) {
				// change size
				MethodUtils.invokeMethod(this.border, "setSize", this.size);
				throwPacket(BorderInfo.SET_SIZE, player, this.border);
				// set center to players location
				final Location location = player.getLocation();
				MethodUtils.invokeMethod(this.border, "setCenter", new Object[]{location.getX(), location.getY()});
				throwPacket(BorderInfo.SET_CENTER, player, this.border);
				// change warning
				MethodUtils.invokeMethod(this.border, "setWarningDistance", this.warning);
				throwPacket(BorderInfo.SET_WARNING_BLOCKS, player, this.border);
			}
		}
		catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public void setSize(int newSize, long time, Player... viewers) {
		try {
			this.border.getClass().getMethod("setSize", double.class, long.class).invoke(this.border, newSize, time);
			for (Player viewer : viewers) {
				throwPacket(BorderInfo.SET_SIZE, viewer, this.border);
			}
		}
		catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	public void transitionSizeBetween(double start, double finish, long time, Player... viewers) {

		if (viewers.length == 0) {
			throw new IllegalArgumentException("Viewers must have at least 1 player!");
		}

		try {
			this.border.getClass()
					.getMethod("transitionSizeBetween", double.class, double.class, long.class)
					.invoke(this.border, start, finish, time);
			for (Player player : viewers) {
				throwPacket(BorderInfo.SET_SIZE, player, border);
			}
		}
		catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private void init() {
		try {
			this.border = Reflect.getNetClass("WorldBorder").getConstructor().newInstance();
			FieldUtils.writeDeclaredField(this.border, "world", Reflect.getNetWorld(this.world));
			MethodUtils.invokeMethod(this.border, "setDamageAmount", 0);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void throwPacket(BorderInfo info, Player player, Object worldBorder) {
		try {
			final Class<?> enumWorld = Reflect.getNetClass("PacketPlayOutWorldBorder$EnumWorldBorderAction");
			final Object packet = Reflect.getNetClass("PacketPlayOutWorldBorder")
					.getConstructor(Reflect.getNetClass("WorldBorder"), enumWorld)
					.newInstance(worldBorder, enumWorld.getField(info.getStringName()).get(enumWorld));
			Reflect.sendPacket(player, packet);
		}
		catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e) {
			e.printStackTrace();
		}

	}

	private enum BorderInfo {

		SET_SIZE,
		SET_CENTER,
		SET_WARNING_BLOCKS;

		BorderInfo() {
		}

		private String getStringName() {
			return this.name();
		}

	}

} // end of border class
