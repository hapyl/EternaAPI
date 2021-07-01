package kz.hapyl.spigotutils.module.error;

import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.chat.LazyClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public final class Exceptions {

	public static void throwRuntimeError(String message) {
		throw new RuntimeException(message);
	}

	public static void runSafe(Runnable runnable) {
		try {
			runnable.run();
		}
		catch (Exception ignored) {
		}
	}

	public static void handleAny(Runnable runnable) {
		handleAny(null, runnable);
	}

	public static void handleAny(CommandSender executor, Runnable runnable) {
		try {
			runnable.run();
		}
		catch (Exception e) {
			if (executor != null) {
				executor.spigot()
						.sendMessage(new ComponentBuilder(Chat.format("&cAn error occurred, please report this! &e(\"%s\")", e.getMessage())).event(LazyClickEvent.COPY_TO_CLIPBOARD
								.of(Arrays.toString(e.getStackTrace()))).create());
			}
			e.printStackTrace();
		}
	}


}
