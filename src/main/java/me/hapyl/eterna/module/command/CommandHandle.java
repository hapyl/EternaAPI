package me.hapyl.eterna.module.command;

import com.google.common.collect.Lists;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.command.completer.Completer;
import me.hapyl.eterna.module.command.completer.CompleterHandler;
import me.hapyl.eterna.module.command.completer.CompleterTooltip;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a command handle for {@link SimpleCommand}.
 */
@ApiStatus.Internal
public final class CommandHandle extends Command {
    
    private final SimpleCommand command;
    private final CommandFormatter formatter;
    
    CommandHandle(@NotNull SimpleCommand command) {
        super(command.getName(), command.getDescription(), command.getUsage(), Arrays.asList(command.getAliases()));
        
        this.command = command;
        this.formatter = command.getFormatter();
    }
    
    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        // Check for disable command
        if (command instanceof DisabledCommand) {
            formatter.disabledCommand(sender);
            return true;
        }
        
        // Check for player only command
        if (command.isAllowOnlyPlayer() && !(sender instanceof Player)) {
            formatter.playerOnlyCommand(sender);
            return true;
        }
        
        // Check for permissions
        @Nullable final Permission permission = command.getPermission();
        
        if ((command.isAllowOnlyOp() && !sender.isOp()) || (permission != null && !sender.hasPermission(permission))) {
            formatter.noPermissions(sender);
            return true;
        }
        
        // Check for cooldown
        @NotNull final CommandCooldown cooldown = command.getCommandCooldown();
        
        if (cooldown.hasCooldown() && sender instanceof Player player) {
            if (cooldown.isOnCooldown(player)) {
                formatter.onCooldown(player, cooldown.getCooldown(player));
                return true;
            }
            
            if (!cooldown.canIgnoreCooldown(player)) {
                cooldown.startCooldown(player);
            }
        }
        
        // Attempt to execute the command
        try {
            command.execute(sender, new ArgumentList(args));
        }
        catch (Exception ex) {
            formatter.executionError(sender, ex);
            throw EternaLogger.acknowledgeException(ex);
        }
        
        return true;
    }
    
    @Override
    @NotNull
    @SuppressWarnings("deprecation") // Legacy chat color
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        // Check for player only command
        if (command.isAllowOnlyPlayer() && !(sender instanceof Player)) {
            return List.of();
        }
        
        // Check for permissions
        @Nullable final Permission permission = command.getPermission();
        
        if ((command.isAllowOnlyOp() && !sender.isOp()) || (permission != null && !sender.hasPermission(permission))) {
            return List.of();
        }
        
        final ArgumentList argumentList = new ArgumentList(args);
        final List<String> completerList = Lists.newArrayList(command.tabComplete(sender, argumentList));
        
        // We offset by 1 because the index is arguments length, and we need to target the current argument
        final int argumentIndex = args.length - 1;
        final String lastArgument = args[argumentIndex];
        
        // Apply completer handler
        final CompleterHandler completerHandler = command.getCompleterHandler(argumentIndex);
        final List<String> tooltipList = Lists.newArrayList();
        
        if (completerHandler != null) {
            final List<Completer> completers = completerHandler.handle();
            
            // Append completer values
            completers.forEach(completer -> completerList.addAll(completer.complete(sender, argumentList, argumentIndex)));
            
            // Append tooltip if exists
            final CompleterTooltip tooltip = completerHandler.tooltip();
            
            if (tooltip != null) {
                tooltipList.add(tooltip.apply(argumentList.get(argumentIndex)));
            }
        }
        
        // First, remove completions that aren't applicable
        completerList.removeIf(v -> !command.completerSortMethod().apply(v, lastArgument));
        
        // Then append tooltips at the end because they must be last
        completerList.addAll(tooltipList);
        
        // Then finally colorize all and return
        return completerList.stream()
                            .map(obj -> {
                                // Unfortunately there isn't a native support for completers in the bukkit command implementations, so we still
                                // have to rely on magic chars ¯\_(ツ)_/¯
                                return ChatColor.translateAlternateColorCodes('&', String.valueOf(obj));
                            })
                            .toList();
    }
    
}
