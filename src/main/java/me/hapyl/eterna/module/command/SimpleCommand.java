package me.hapyl.eterna.module.command;

import com.google.common.collect.Maps;
import me.hapyl.eterna.module.annotate.CaughtExceptions;
import me.hapyl.eterna.module.annotate.ForceLowercase;
import me.hapyl.eterna.module.command.completer.CompleterHandler;
import me.hapyl.eterna.module.command.completer.CompleterSortMethod;
import me.hapyl.eterna.module.util.Cooldown;
import me.hapyl.eterna.module.util.Handle;
import me.hapyl.eterna.module.util.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a {@link Command} builder.
 *
 * <p>Commands made via {@link SimpleCommand} must be registered via {@link CommandProcessor} and they do not require declaring them in {@code plugin.yml}.</p>
 */
public abstract class SimpleCommand implements Handle<Command>, Cooldown {
    
    private final String name;
    private final Map<Integer, CompleterHandler> completerHandlers;
    private final CommandCooldown cooldown;
    
    @Nullable private Permission permission;
    @NotNull private String description;
    @NotNull private String usage;
    @NotNull private String[] aliases;
    
    private boolean allowOnlyOp;
    private boolean allowOnlyPlayer;
    
    private CommandHandle handle;
    private CommandFormatter formatter;
    
    /**
     * Creates a new {@link SimpleCommand}.
     *
     * @param name - The name of the command.
     */
    public SimpleCommand(@NotNull @ForceLowercase String name) {
        this.name = name.toLowerCase();
        this.aliases = new String[] { };
        this.permission = null;
        this.usage = "/" + name;
        this.description = "Default SimpleCommand by EternaAPI.";
        this.allowOnlyPlayer = false;
        this.allowOnlyOp = false;
        this.cooldown = new CommandCooldown();
        this.completerHandlers = Maps.newHashMap();
        this.formatter = CommandFormatter.DEFAULT;
    }
    
    /**
     * Sends the command usage to the given {@link CommandSender} via {@link CommandFormatter}.
     *
     * @param sender - The sender for whom to send the usage.
     */
    public void sendCommandUsage(@NotNull CommandSender sender) {
        formatter.invalidUsage(sender, usage);
    }
    
    /**
     * Gets the {@link CompleterSortMethod} for this command.
     *
     * @return the completer sort method for this command.
     */
    @NotNull
    public CompleterSortMethod completerSortMethod() {
        return CompleterSortMethod.STARTS_WITH;
    }
    
    /**
     * Gets the handle of this command.
     *
     * @return the handle of this command.
     * @throws NullPointerException if the command has not yet been registered.
     */
    @NotNull
    @Override
    public Command getHandle() {
        return Objects.requireNonNull(handle, "Unregistered command! (%s)".formatted(name));
    }
    
    /**
     * Sets the {@link CompleterHandler} for a given {@code index}.
     *
     * @param handler - The handler to set.
     */
    public void setCompleterHandler(@NotNull CompleterHandler handler) {
        this.completerHandlers.put(handler.index(), handler);
    }
    
    /**
     * Sets the tab-completer values for the given {@code index}.
     *
     * @param index  - The index to set the tab-completer values.
     * @param values - The values to set.
     * @throws IllegalArgumentException if varargs are empty.
     */
    public void setCompleterValues(int index, @NotNull String... values) {
        this.completerHandlers.put(index, CompleterHandler.builder(index).values(Arrays.asList(Validate.varargs(values))).build());
    }
    
    /**
     * Gets a {@link CompleterHandler} for the given {@code index}.
     *
     * @param index - The index to retrieve a handler.
     * @return the handler at the given index, or {@code null} if no handler at that {@code index}.
     */
    @Nullable
    public CompleterHandler getCompleterHandler(int index) {
        return completerHandlers.get(index);
    }
    
    /**
     * Gets whether this command is only allowed for {@link Player}.
     *
     * @return {@code true} if this command is only allowed for players; {@code false} otherwise.
     */
    public boolean isAllowOnlyPlayer() {
        return allowOnlyPlayer;
    }
    
    /**
     * Sets whether this command is only allowed for {@link Player}.
     *
     * @param allowOnlyPlayer - {@code true} if the command is restricted to players only, {@code false} otherwise.
     */
    public void setAllowOnlyPlayer(boolean allowOnlyPlayer) {
        this.allowOnlyPlayer = allowOnlyPlayer;
    }
    
    /**
     * Gets whether this command is only allowed for operators.
     *
     * @return {@code true} if this command is only allowed for operators, {@code false} otherwise.
     */
    public boolean isAllowOnlyOp() {
        return allowOnlyOp;
    }
    
    /**
     * Sets whether this command is only allowed for operators.
     *
     * @param bool - {@code true} if the command is restricted to operators only, {@code false} otherwise.
     */
    public void setAllowOnlyOp(boolean bool) {
        this.allowOnlyOp = bool;
    }
    
    /**
     * Gets the command cooldown.
     *
     * @return the command cooldown, or {@code 0} if unset.
     */
    @Override
    public int getCooldown() {
        return this.cooldown.getCooldown();
    }
    
    /**
     * Sets the command cooldown, in ticks.
     *
     * @param cooldown - The cooldown to set.
     */
    @Override
    public void setCooldown(int cooldown) {
        this.cooldown.setCooldown(cooldown);
    }
    
    /**
     * Gets the {@link CommandCooldown}.
     *
     * @return the command cooldown.
     */
    @NotNull
    public CommandCooldown getCommandCooldown() {
        return this.cooldown;
    }
    
    /**
     * Gets the description of this command.
     *
     * @return the description of this command.
     */
    @NotNull
    public String getDescription() {
        return this.description;
    }
    
    /**
     * Sets the description of this command.
     *
     * @param description - The description to set.
     */
    public void setDescription(@NotNull String description) {
        this.description = description;
    }
    
    /**
     * Gets the name of this command.
     *
     * @return the name of this command.
     */
    @NotNull
    public String getName() {
        return name;
    }
    
    /**
     * Gets the {@link Permission} for this command.
     * <p>Players within the permission cannot execute the command and are shown an error message.</p>
     *
     * @return the permission for this command, or {@code null} if unset.
     */
    @Nullable
    public Permission getPermission() {
        return permission;
    }
    
    /**
     * Sets the {@link Permission} for this command.
     * <p>Players within the permission cannot execute the command and are shown an error message.</p>
     *
     * @param permission - The permission to set.
     */
    public void setPermission(@Nullable Permission permission) {
        this.permission = permission;
    }
    
    /**
     * Gets a copy of command aliases.
     *
     * @return a copy of command aliases.
     */
    @NotNull
    public String[] getAliases() {
        return Arrays.copyOf(aliases, aliases.length);
    }
    
    /**
     * Sets the command aliases.
     *
     * @param aliases - The aliases to set.
     * @throws IllegalArgumentException if varargs are empty.
     */
    public void setAliases(@NotNull String... aliases) {
        this.aliases = Arrays.stream(Validate.varargs(aliases))
                             .map(String::toLowerCase)
                             .toArray(String[]::new);
    }
    
    /**
     * Gets the command usage.
     *
     * @return the command usage.
     */
    @NotNull
    public String getUsage() {
        return usage;
    }
    
    /**
     * Sets the command usage.
     *
     * @param usage - The usage to set.
     */
    public void setUsage(@NotNull String usage) {
        this.usage = usage;
    }
    
    /**
     * Gets the {@link CommandFormatter}.
     *
     * @return the command formatter.
     */
    @NotNull
    public CommandFormatter getFormatter() {
        return formatter;
    }
    
    /**
     * Sets the {@link CommandFormatter}.
     *
     * @param formatter - The formatter to set.
     */
    public void setFormatter(@NotNull CommandFormatter formatter) {
        this.formatter = formatter;
    }
    
    /**
     * Attempts to execute this command.
     *
     * @param sender - The command sender.
     * @param args   - The command arguments.
     */
    @CaughtExceptions
    protected abstract void execute(@NotNull CommandSender sender, @NotNull ArgumentList args);
    
    /**
     * Gets a static list of tab-completers.
     *
     * @param sender - The command sender.
     * @param args   - The command arguments.
     * @return a list of tab-completers.
     * @see CompleterHandler
     */
    @NotNull
    protected List<String> tabComplete(@NotNull CommandSender sender, @NotNull ArgumentList args) {
        return List.of();
    }
    
    /**
     * Creates the command handle.
     *
     * @return the command handle.
     */
    @NotNull
    @ApiStatus.Internal
    final Command createCommand() {
        if (this.handle == null) {
            this.handle = new CommandHandle(this);
        }
        else {
            throw new IllegalStateException("Command already registered! (%s)".formatted(name));
        }
        
        return this.handle;
    }
    
}
