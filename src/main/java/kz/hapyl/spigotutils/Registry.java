package kz.hapyl.spigotutils;

import kz.hapyl.spigotutils.module.command.SimpleCommand;
import kz.hapyl.spigotutils.module.inventory.item.CustomItem;
import org.bukkit.plugin.java.JavaPlugin;

public interface Registry {

	void registerCommand(SimpleCommand command);

	void registerCommand(JavaPlugin plugin, SimpleCommand command);

	void registerItem(CustomItem item);

}
