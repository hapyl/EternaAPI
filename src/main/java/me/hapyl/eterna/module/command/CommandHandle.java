package me.hapyl.eterna.module.command;

import com.google.common.collect.Lists;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class CommandHandle extends Command {
    
    private final SimpleCommand command;
    private final CommandFormatter formatter;
    
    protected CommandHandle(SimpleCommand command) {
        super(command.getName(), command.getDescription(), command.getUsage(), Arrays.asList(command.getAliases()));
        
        this.command = command;
        this.formatter = command.getFormatter();
    }
    
    @Override
    public final boolean execute(@Nonnull CommandSender sender, @Nonnull String commandLabel, @Nonnull String[] args) {
        // Disabled command check
        if (command instanceof DisabledCommand) {
            formatter.sendDisabledCommand(sender);
            return true;
        }
        
        // Player command check
        if (command.isOnlyForPlayers() && !(sender instanceof Player)) {
            formatter.sendPlayerOnlyCommand(sender);
            return true;
        }
        
        // Permissions check
        if ((command.isAllowOnlyOp() && !sender.isOp()) || (command.hasPermission() && !sender.hasPermission(command.getPermission()))) {
            formatter.sendNoPermissions(sender);
            return true;
        }
        
        // Cooldown check
        if (command.hasCooldown() && sender instanceof final Player playerSender) {
            final CommandCooldown cooldown = command.getCooldown();
            if (cooldown.hasCooldown(playerSender)) {
                formatter.sendOnCooldown(playerSender, (int) (cooldown.getTimeLeft(playerSender) / 50));
                return true;
            }
            if (!cooldown.canIgnoreCooldown(playerSender)) {
                cooldown.startCooldown(playerSender);
            }
        }
        
        try {
            command.execute(sender, args);
        }
        catch (Exception e) {
            Chat.sendMessage(sender, "&4Error executing command! &c%s".formatted(e.getMessage()));
            throw EternaLogger.exception(e);
        }
        
        return true;
    }
    
    @Override
    @Nonnull
    public List<String> tabComplete(@Nonnull CommandSender sender, @Nonnull String alias, @Nonnull String[] args) throws IllegalArgumentException {
        if (command.isOnlyForPlayers() && !(sender instanceof Player)) {
            return defaultCompleter(sender);
        }
        
        final List<String> strings = colorizeList(command.preTabComplete(sender, args));
        final List<String> tabComplete = command.tabComplete(sender, args);
        
        if (tabComplete != null) {
            strings.addAll(tabComplete);
        }
        
        if (command.hasCompleterValues(args.length)) {
            strings.addAll(command.completerSort(command.getCompleterValues(args.length), args));
        }
        
        strings.addAll(colorizeList(command.postTabComplete(sender, args)));
        
        if (sender instanceof Player player) {
            command.completerHandler(player, args.length, args, strings);
        }
        
        return strings.isEmpty() ? defaultCompleter(sender) : strings;
    }
    
    private static List<String> colorizeList(List<String> list) {
        final List<String> newList = Lists.newArrayList();
        
        for (final String s : list) {
            newList.add(Chat.format(s));
        }
        
        return newList;
    }
    
    private static List<String> defaultCompleter(CommandSender sender) {
        final List<String> list = Lists.newArrayList();
        
        for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (sender instanceof Player player && !player.canSee(onlinePlayer)) {
                continue;
            }
            
            list.add(onlinePlayer.getName());
        }
        
        return list;
    }
}
