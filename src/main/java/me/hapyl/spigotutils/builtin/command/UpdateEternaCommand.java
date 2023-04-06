package me.hapyl.spigotutils.builtin.command;

import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.builtin.updater.UpdateResult;
import me.hapyl.spigotutils.builtin.updater.Updater;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.SimpleAdminCommand;
import org.bukkit.command.CommandSender;

public final class UpdateEternaCommand extends SimpleAdminCommand {
    public UpdateEternaCommand(String name) {
        super(name);

        setUsage("/updateEterna");
        setDescription("Checks for updates for Eterna.");
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        final Updater updater = EternaPlugin.getPlugin().getUpdater();
        final UpdateResult result = updater.checkForUpdates();

        if (result == UpdateResult.OUTDATED) {
            updater.broadcastLink();
        }
        else {
            Chat.sendMessage(sender, result.getMessage());
        }
    }

}
