package kz.hapyl.spigotutils;

import kz.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SpigotUtils {

	private final static double pluginVersion = 1.63d;

	private final JavaPlugin plugin;

	public static void hookIntoAPI(JavaPlugin init) {
		new SpigotUtils(init);
	}

	public SpigotUtils(JavaPlugin init) {
		this(init, false);
	}

	private boolean isDepends(JavaPlugin plugin) {
		final PluginDescriptionFile description = plugin.getDescription();
		final String pluginName = SpigotUtilsPlugin.getPlugin().getName();
		return description.getDepend().contains(pluginName) || description.getSoftDepend().contains(pluginName);
	}

	public SpigotUtils(JavaPlugin init, boolean broadcastMessageOnlyToConsole) {

		if (pluginVersion > 1.6d) {
			if (init == null) {
				throw new EternaException("Could not load EternaAPI since provided plugin is null!");
			}

			if (!isDepends(init)) {
				throw new EternaException
						(String.format("Could not load EternaAPI for %s since it's doesn't depend nor soft-depends the API!", init.getName()));
			}
		}

		this.plugin = init;
		final String formattedMessage = String.format("%s implements EternaAPI v%s.", plugin.getName(), pluginVersion);

		new BukkitRunnable() {
			@Override
			public void run() {

				// broadcast to console
				Bukkit.getConsoleSender().sendMessage(formattedMessage);

				if (broadcastMessageOnlyToConsole) {
					return;
				}

				// broadcast to admins
				broadcastAPIMessage(formattedMessage);
			}
		}.runTaskLater(plugin, 20);
	}

	public JavaPlugin getPlugin() {
		return plugin;
	}

	private static final String PREFIX = "&b&lEternaAPI&b> &a";

	private static void broadcastAPIMessage(String string) {
		Chat.broadcastOp(PREFIX + string);
	}

	public static void sendAPIMessage(CommandSender receiver, String string, Object... replacements) {
		Chat.sendMessage(receiver, PREFIX + string, replacements);
	}

}
