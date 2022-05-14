package kz.hapyl.spigotutils.module.listener;

import kz.hapyl.spigotutils.EternaPlugin;
import org.bukkit.event.Listener;

public abstract class SimpleListener implements Listener {

	public SimpleListener(SimpleListener clazz) {
        EternaPlugin.registerEvent(clazz);
	}

}
