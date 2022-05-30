package me.hapyl.spigotutils.builtin.command;

import me.hapyl.spigotutils.builtin.gui.QuestJournal;
import me.hapyl.spigotutils.module.command.SimpleCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class QuestCommand extends SimpleCommand {

	public QuestCommand(String str) {
		super(str);
		this.setAllowOnlyPlayer(true);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		final Player player = (Player)sender;
		if (args.length == 0) {
			new QuestJournal(player);
		}
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		return super.tabComplete(sender, args);
	}

}