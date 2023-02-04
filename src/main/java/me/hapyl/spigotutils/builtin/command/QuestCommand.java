package me.hapyl.spigotutils.builtin.command;

import me.hapyl.spigotutils.Rule;
import me.hapyl.spigotutils.builtin.gui.QuestJournal;
import me.hapyl.spigotutils.module.annotate.BuiltIn;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.SimpleCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Built in command for {@link QuestJournal}.
 */
@BuiltIn
public final class QuestCommand extends SimpleCommand {

    public QuestCommand(String str) {
        super(str);
        this.setAllowOnlyPlayer(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        final Player player = (Player) sender;

        if (Rule.ALLOW_QUEST_JOURNAL.isFalse()) {
            Chat.sendMessage(player, "&cThis server does not allow Quest Journal!");
            return;
        }

        new QuestJournal(player);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return super.tabComplete(sender, args);
    }

}