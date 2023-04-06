package me.hapyl.spigotutils.builtin.command;

import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.Rule;
import me.hapyl.spigotutils.builtin.gui.QuestJournal;
import me.hapyl.spigotutils.builtin.updater.UpdateResult;
import me.hapyl.spigotutils.builtin.updater.Updater;
import me.hapyl.spigotutils.module.addons.AddonRegistry;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.SimpleAdminCommand;
import me.hapyl.spigotutils.registry.EternaRegistry;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class EternaCommand extends SimpleAdminCommand {
    public EternaCommand(String name) {
        super(name);

        setDescription("Eterna management command.");
        addCompleterValues(1, "update", "version", "quest", "reload");
        addCompleterValues(2, "config", "addons");
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        // eterna (update)
        // eterna (quest) --player_only
        // eterna reload (config, addons)

        if (args.length == 0) {
            sendInvalidUsageMessage(sender);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "update" -> {
                final Updater updater = EternaPlugin.getPlugin().getUpdater();
                final UpdateResult result = updater.checkForUpdates();

                if (result == UpdateResult.OUTDATED) {
                    updater.broadcastLink();
                }
                else {
                    Chat.sendMessage(sender, result.getMessage());
                }
            }

            case "version" -> {
                final Updater updater = EternaPlugin.getPlugin().getUpdater();

                Chat.sendMessage(sender, "&aYour version: " + updater.getPluginVersion());

                if (updater.getLastResult() == UpdateResult.OUTDATED) {
                    Chat.sendMessage(sender, "&aLatest version: " + updater.getLatestVersion());
                    Chat.sendMessage(sender, "&aDownload here: &e" + updater.getDownloadUrl());
                }
                else if (updater.getLastResult() == UpdateResult.UP_TO_DATE) {
                    Chat.sendMessage(sender, "&aYou're up to date!");
                }
            }

            case "quest" -> {
                if (sender instanceof Player player) {
                    if (Rule.ALLOW_QUEST_JOURNAL.isFalse()) {
                        Chat.sendMessage(player, "&cThis server does not allow Quest Journal!");
                        return;
                    }

                    new QuestJournal(player);
                }
                else {
                    Chat.sendMessage(sender, "&cThis command can only be executed by players.");
                }
            }

            case "reload" -> {
                // eterna reload (value)
                if (args.length < 2) {
                    Chat.sendMessage(sender, "&4Invalid usage! &c/eterna update (config, addons) [config?String]");
                    return;
                }

                switch (args[1].toLowerCase()) {
                    case "config" -> {
                        final Player target = args.length >= 3 ? Bukkit.getPlayer(args[2]) : sender instanceof Player player ? player : null;

                        if (target == null) {
                            Chat.sendMessage(sender, "&c%s is not online!", args[2]);
                            return;
                        }

                        EternaRegistry.getConfigManager().getConfig(target).forceLoad(true);
                        Chat.sendMessage(sender, "&aReloaded %s player config!", target.getName());
                    }

                    case "addons" -> {
                        final AddonRegistry registry = EternaPlugin.getPlugin().getRegistry().addonRegistry;
                        registry.unloadAll();
                        registry.loadAll();

                        Chat.sendMessage(sender, "&aReloaded addons!");
                    }
                }
            }

            default -> {
                Chat.sendMessage(sender, "&cUnknown argument. " + getUsage());
            }
        }
    }

}
