package kz.hapyl.spigotutils.module.reflect.border;

import kz.hapyl.spigotutils.module.reflect.Reflect;
import kz.hapyl.spigotutils.module.reflect.ReflectPacket;
import kz.hapyl.spigotutils.module.util.Holder;
import net.minecraft.network.protocol.game.ClientboundInitializeBorderPacket;
import net.minecraft.world.level.border.WorldBorder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Allows to create per player border. (For red/green effect only usually.)
 */
public class PlayerBorder extends Holder<Player> {

	public PlayerBorder(Player player) {
		super(player);
	}

	public void update(Operation operation, double size) {
		final WorldBorder border = new WorldBorder();
		final Location location = getPlayer().getLocation();

		border.world = Reflect.getMinecraftWorld(location.getWorld()).getMinecraftWorld();
//		border.setCenter(location.getX(), location.getZ());
//		border.setWarningTime(0);

		if (operation == Operation.REMOVE) {
//			border.setSize(Integer.MAX_VALUE);
//			border.setWarningDistance(0);
		}
		else {
//			border.setSize(size);
//			border.setWarningDistance(Integer.MAX_VALUE);
		}

		switch (operation) {
//			case BORDER_RED -> border.transitionSizeBetween(size, size - 1.0d, 20000000L);
//			case BORDER_GREEN -> border.transitionSizeBetween(size - 0.1d, size, 20000000L);
		}

		new ReflectPacket(new ClientboundInitializeBorderPacket(border)).sendPackets(getPlayer());

	}

	public static void reset(Player player) {
		new PlayerBorder(player).update(Operation.REMOVE, 0);
	}

	public static void showRedOutline(Player player) {
		new PlayerBorder(player).update(Operation.BORDER_RED, 1000);
	}

	public Player getPlayer() {
		return this.get();
	}

	public enum Operation {

		REMOVE,
		BORDER_GREEN,
		BORDER_RED

	}

}
