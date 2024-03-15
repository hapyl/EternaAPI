package me.hapyl.spigotutils.builtin.command;

import me.hapyl.spigotutils.*;
import me.hapyl.spigotutils.builtin.gui.QuestJournal;
import me.hapyl.spigotutils.builtin.updater.UpdateResult;
import me.hapyl.spigotutils.builtin.updater.Updater;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.SimpleAdminCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import test.EternaRuntimeTest;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public final class EternaCommand extends SimpleAdminCommand {
    public EternaCommand(String name) {
        super(name);

        setDescription("Eterna management command.");
        addCompleterValues(1, "update", "version", "quest", "reload", "test", "testsq");
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        // eterna (update)
        // eterna (quest) --player_only
        // eterna reload (config, addons)
        // eterna test (testName)
        // eterna testall

        if (args.length == 0) {
            sendInvalidUsageMessage(sender);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "update" -> {
                final Updater updater = Eterna.getUpdater();
                final UpdateResult result = updater.checkForUpdates();

                if (result == UpdateResult.OUTDATED) {
                    updater.broadcastLink();
                }
                else {
                    Chat.sendMessage(sender, result.getMessage());
                }
            }

            case "version" -> {
                final Updater updater = Eterna.getUpdater();

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
                // eterna reload [value]
                if (args.length < 2) {
                    EternaPlugin.getPlugin().reloadConfig();
                    EternaLogger.sendMessage(sender, "Reloaded config!");
                    return;
                }

                switch (args[1].toLowerCase()) {
                    case "config" -> {
                        final Player target = args.length >= 3 ? Bukkit.getPlayer(args[2]) : sender instanceof Player player ? player : null;

                        if (target == null) {
                            EternaLogger.sendMessage(sender, "&c%s is not online!", args[2]);
                            return;
                        }

                        Eterna.getRegistry().configRegistry.getConfig(target).forceLoad(true);
                        EternaLogger.sendMessage(sender, "&aReloaded %s player config!", target.getName());
                    }
                }
            }

            case "test" -> {
                if (!Eterna.getConfig().isTrue(EternaConfigValue.KEEP_TESTS)) {
                    Chat.sendMessage(sender, "&cTests are disabled on this server.");
                    return;
                }

                final String testName = getArgument(args, 1).toString();

                if (testName.isEmpty()) {
                    Chat.sendMessage(sender, "Invalid test.");
                    return;
                }

                if (!(sender instanceof Player player)) {
                    Chat.sendMessage(sender, "&4You must be a player to use this!");
                    return;
                }

                EternaRuntimeTest.test(player, testName, Arrays.copyOfRange(args, 2, args.length));
            }

            case "testsq" -> {
                if (!Eterna.getConfig().isTrue(EternaConfigValue.KEEP_TESTS)) {
                    Chat.sendMessage(sender, "&cTests are disabled on this server.");
                    return;
                }

                if (!(sender instanceof Player player)) {
                    Chat.sendMessage(sender, "&4You must be a player to use this!");
                    return;
                }

                EternaRuntimeTest.nextOrNewTestSq(player);
            }

            default -> {
                Chat.sendMessage(sender, "&cUnknown argument. " + getUsage());
            }
        }
    }

    @Nullable
    @Override
    protected List<String> tabComplete(CommandSender sender, String[] args) {
        if (matchArgs(args, "test")) {
            return completerSort(EternaRuntimeTest.listTests(), args, false);
        }

        return null;
    }
}
