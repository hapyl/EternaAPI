package kz.hapyl.spigotutils.builtin.command;

import kz.hapyl.spigotutils.SpigotUtils;
import kz.hapyl.spigotutils.SpigotUtilsPlugin;
import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.command.SimpleCommand;
import kz.hapyl.spigotutils.module.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class PluginCommand extends SimpleCommand {

	private final PluginManager pluginManager;
	private final List<String>  allPlugins;

	/**
	 * Creates a new simple command
	 *
	 * @param name - Name of the command.
	 */
	public PluginCommand(String name) {
		super(name);
		this.setDescription("Allows to enable, disable or reload plugins without /reload.");
		this.setAllowOnlyPlayer(true);
		this.setAllowOnlyOp(true);
		this.setUsage("plugin (enable/disable/reload) (plugin)");
		pluginManager = Bukkit.getPluginManager();
		allPlugins = this.getAllPlugins();
	}

	@Override
	protected void execute(CommandSender sender, String[] args) {
		// plugin enable (Plugin)
		// plugin disable (Plugin)
		// plugin reload (Plugin)

		if (true) {
			SpigotUtils.sendAPIMessage(sender, "&cThis command is temporary disabled, sorry!");
			return;
		}

		if (args.length == 2) {

			String value = args[0].toLowerCase(Locale.ROOT);
			Plugin plugin = pluginManager.getPlugin(args[1]);

			if (plugin == null) {
				SpigotUtils.sendAPIMessage(sender, "&cCould not find plugin named &e%s&c!", args[1]);
				return;
			}

			final String pluginName = plugin.getName();
			if (plugin instanceof SpigotUtilsPlugin || pluginName.contains("SpigotUtilsPlugin")) {
				SpigotUtils.sendAPIMessage(sender, "&cIt is illegal to manipulate SpigotUtils!");
				return;
			}

			switch (value) {

				case "enable": {
					if (isEnabled(plugin)) {
						SpigotUtils.sendAPIMessage(sender, "&c%s is already enabled!", pluginName);
						return;
					}
					SpigotUtils.sendAPIMessage(sender, "&aAttempting to enable %s...", pluginName);
					enablePlugin(plugin);
					return;
				}

				case "disable": {
					if (!isEnabled(plugin)) {
						SpigotUtils.sendAPIMessage(sender, "&c%s is already disabled!", pluginName);
						return;
					}
					SpigotUtils.sendAPIMessage(sender, "&aAttempting to disable %s...", pluginName);
					disablePlugin(plugin);
					return;
				}

				case "reload": {
					if (!isEnabled(plugin)) {
						SpigotUtils.sendAPIMessage(sender, "&cUnable to reload &e%s &csince it's disabled!", pluginName);
						return;
					}
					SpigotUtils.sendAPIMessage(sender, "&aAttempting to reload %s...", pluginName);
					reloadPlugin(plugin);
					return;
				}

				default: {
					this.sendUsage(sender);
				}

			}

		}
		else {
			this.sendUsage(sender);
		}

	}

	private void sendUsage(CommandSender sender) {
		Chat.sendMessage(sender, "&cInvalid Usage! &e%s", this.getUsage());
	}

	private boolean isEnabled(Plugin plugin) {
		return plugin != null && plugin.isEnabled();
	}

	private void enablePlugin(Plugin plugin) {
		Validate.notNull(plugin, "plugin cannot be null");
		pluginManager.enablePlugin(plugin);
	}

	private void disablePlugin(Plugin plugin) {
		Validate.notNull(plugin, "plugin cannot be null");
		pluginManager.disablePlugin(plugin);
	}

	private void reloadPlugin(Plugin plugin) {
		Validate.notNull(plugin, "plugin cannot be null");
		this.disablePlugin(plugin);
		Bukkit.getScheduler().runTaskLaterAsynchronously(SpigotUtilsPlugin.getPlugin(), () -> {
			this.enablePlugin(plugin);
		}, 20);
	}

	@Override
	protected List<String> tabComplete(CommandSender sender, String[] args) {
		if (args.length == 1) {
			return super.completerSort(Arrays.asList("enable", "disable", "reload"), args);
		}
		if (args.length == 2) {
			return super.completerSort(allPlugins, args);
		}
		return null;
	}

	private List<String> getAllPlugins() {
		final List<String> list = new ArrayList<>();
		for (final Plugin plugin : pluginManager.getPlugins()) {
			list.add(plugin.getName());
		}
		return list;
	}

}
