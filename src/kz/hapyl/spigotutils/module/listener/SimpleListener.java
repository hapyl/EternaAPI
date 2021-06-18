package kz.hapyl.spigotutils.module.listener;

import kz.hapyl.spigotutils.SpigotUtilsPlugin;
import org.bukkit.event.Listener;

public abstract class SimpleListener implements Listener {

	public SimpleListener(SimpleListener clazz) {
		SpigotUtilsPlugin.registerEvent(clazz);
	}

}
