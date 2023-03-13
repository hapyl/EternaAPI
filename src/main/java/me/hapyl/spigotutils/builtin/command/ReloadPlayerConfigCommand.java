package me.hapyl.spigotutils.builtin.command;

import me.hapyl.spigotutils.module.annotate.BuiltIn;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import me.hapyl.spigotutils.registry.EternaRegistry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Built in command for reloading {@link me.hapyl.spigotutils.config.PlayerConfig}.
 */
@BuiltIn
public final class ReloadPlayerConfigCommand extends SimplePlayerAdminCommand {

    public ReloadPlayerConfigCommand(String name) {
        super(name);
        setUsage("Allows to load data from player config file.");
    }

    @Override
    protected void execute(Player player, String[] args) {
        final Player target = args.length >= 1 ? Bukkit.getPlayer(args[0]) : player;

        if (target == null) {
            Chat.sendMessage(player, "This player is not online!");
            return;
        }

        EternaRegistry.getConfigManager().getConfig(player).forceLoad(true);
    }
}
