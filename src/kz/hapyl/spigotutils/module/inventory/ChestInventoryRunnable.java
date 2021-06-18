package kz.hapyl.spigotutils.module.inventory;

import org.bukkit.event.inventory.ClickType;

import java.util.HashMap;
import java.util.Map;

public class ChestInventoryRunnable {

	private Runnable                 run;
	private final Map<ClickType, Runnable> perAction = new HashMap<>();

	public ChestInventoryRunnable(Runnable run, ClickType... types) {
		if (types != null && types.length > 0) {
			for (ClickType type : types) {
				perAction.put(type, run);
			}
		}
		else {
			this.run = run;
		}
	}

	public void addClick(Runnable run, ClickType... types) {
		if (types.length > 0) {
			for (ClickType type : types) {
				perAction.put(type, run);
			}
		}
	}

	public Runnable getRunnable(ClickType click) {
		if (this.run != null) {
			return this.run;
		}
		else {
			return perAction.getOrDefault(click, null);
		}
	}

} //runnable
