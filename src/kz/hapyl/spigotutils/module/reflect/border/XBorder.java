package kz.hapyl.spigotutils.module.reflect.border;

import net.minecraft.world.level.border.WorldBorder;
import org.bukkit.entity.Player;

public class XBorder {
	private final WorldBorder border;
	private final Player player;
	private int size;
	private int warning;

	public XBorder(Player player) {
		// PacketPlayOutWorldBorder Where?
		this.player = player;
		this.border = new WorldBorder();
		this.size = 1000;
		this.warning = 0;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}

	public void setWarning(int warning) {
		this.warning = warning;
	}

	public int getWarning() {
		return warning;
	}

	public void update() {
	}

}
