package me.hapyl.eterna.builtin.command;

import me.hapyl.eterna.*;
import me.hapyl.eterna.builtin.gui.QuestJournal;
import me.hapyl.eterna.builtin.updater.UpdateResult;
import me.hapyl.eterna.builtin.updater.Updater;
import me.hapyl.eterna.module.chat.Chat;
import me.hapyl.eterna.module.command.SimpleAdminCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import test.EternaRuntimeTest;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public final class EternaCommand extends SimpleAdminCommand {
    public EternaCommand(String name) {
        super(name);
        
        setDescription("API management administrative command.");
        addCompleterValues(1, "update", "version", "quest", "reload", "test");
    }
    
    @Override
    protected void execute(CommandSender sender, String[] args) {
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
                    EternaLogger.sendMessage(sender, result.getMessage());
                }
            }
            
            case "version" -> {
                final Updater updater = Eterna.getUpdater();
                
                EternaLogger.sendMessage(sender, "&aYour version: " + updater.getPluginVersion());
                
                if (updater.getLastResult() == UpdateResult.OUTDATED) {
                    EternaLogger.sendMessage(sender, "&aLatest version: " + updater.getLatestVersion());
                    EternaLogger.sendMessage(sender, "&aDownload here: &e" + updater.getDownloadUrl());
                }
                else if (updater.getLastResult() == UpdateResult.UP_TO_DATE) {
                    EternaLogger.sendMessage(sender, "&aYou're up to date!");
                }
            }
            
            case "quest" -> {
                if (sender instanceof Player player) {
                    if (Rule.ALLOW_QUEST_JOURNAL.isFalse()) {
                        EternaLogger.sendMessage(player, "&cThis server does not allow Quest Journal!");
                        return;
                    }
                    
                    new QuestJournal(player);
                }
                else {
                    EternaLogger.sendMessage(sender, "&cThis command can only be executed by players.");
                }
            }
            
            case "reload" -> {
                if (args.length < 2) {
                    EternaPlugin.getPlugin().reloadConfig();
                    EternaLogger.sendMessage(sender, "Reloaded config!");
                }
            }
            
            case "test" -> {
                if (!Eterna.getConfig().keepTestCommands()) {
                    EternaLogger.sendMessage(sender, "&cTests are disabled on this server.");
                    return;
                }
                
                final String testName = getArgument(args, 1).toString();
                
                if (testName.isEmpty()) {
                    EternaLogger.sendMessage(sender, "Invalid test.");
                    return;
                }
                
                if (!EternaRuntimeTest.testExists(testName)) {
                    EternaLogger.sendMessage(sender, "&cInvalid test.");
                    return;
                }
                
                if (!(sender instanceof Player player)) {
                    Chat.sendMessage(sender, "&4You must be a player to use this!");
                    return;
                }
                
                EternaRuntimeTest.test(player, testName, Arrays.copyOfRange(args, 2, args.length));
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
