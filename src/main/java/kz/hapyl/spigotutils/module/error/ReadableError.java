package kz.hapyl.spigotutils.module.error;

import kz.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class ReadableError extends RuntimeException {

	public ReadableError(String readable, Object... format) {
		super(String.format(readable, format));
		final ConsoleCommandSender sender = Bukkit.getConsoleSender();
		Chat.broadcastOp("&4[Error] &cAn error occurred in EternaAPI, check the console!");
		Chat.sendMessage(sender, "");
		Chat.sendMessage(sender, "&4&lAn Error Occurred in EternaAPI!");
		Chat.sendMessage(sender, "&c" + this.getMessage());
		Chat.sendMessage(sender, "");
	}

}
