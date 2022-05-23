package kz.hapyl.spigotutils.module.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import kz.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Creates a SimpleCommand.
 *
 * @author hapyl
 */
public abstract class SimpleCommand {

    private SimpleCommand() {
        throw new NullPointerException();
    }

    private final String name;
    private final Map<Integer, List<String>> completerValues;

    private String permission;
    private String description;
    private String usage;

    private String[] aliases;

    private boolean allowOnlyOp;
    private boolean allowOnlyPlayer;

    private int cooldownTick;

    private CommandCooldown cooldown;

    /**
     * Creates a new simple command
     *
     * @param name - Name of the command.
     */
    public SimpleCommand(@Nonnull String name) {
        this.name = name;
        this.aliases = new String[] {};
        this.permission = "";
        this.usage = "/" + name;
        this.description = "Made using SimpleCommand by EternaAPI.";
        this.allowOnlyPlayer = false;
        this.allowOnlyOp = false;
        this.cooldownTick = 0;
        this.cooldown = null;
        this.completerValues = Maps.newHashMap();
    }

    /**
     * Adds a value to index of tab completer.
     * Note that completer values will be automatically sorted
     * using {@link this#completerSort(List, String[])} AFTER
     * {@link this#tabComplete(CommandSender, String[])} is called.
     *
     * @param index - Index. (Length of arguments).
     * @param value - Value to add. Will be forced to lower case.
     */
    public void addCompleterValue(int index, String value) {
        final List<String> values = getCompleterValues(index);
        values.add(value.toLowerCase());
        completerValues.put(index, values);
    }

    /**
     * Returns a list of strings of tab completer if present or empty list is not.
     *
     * @param index - Index. (Length of arguments)
     * @return a list of strings of tab completer if present or empty list is not.
     */
    public List<String> getCompleterValues(int index) {
        return completerValues.computeIfAbsent(index, (s) -> Lists.newArrayList());
    }

    /**
     * Returns true if completer values are present on index.
     *
     * @param index - Index or arguments length.
     * @return true if completer values are present on index.
     */
    public boolean hasCompleterValues(int index) {
        return completerValues.get(index) != null;
    }

    /**
     * Returns mapped index-value completer values.
     *
     * @return mapped index-value completer values.
     */
    protected Map<Integer, List<String>> getCompleterValues() {
        return completerValues;
    }

    /**
     * Executes the command
     *
     * @param sender - Who send the command, you can safely case sender to a player if setAllowOnlyPlayer(boolean flag) is used
     * @param args   - Arguments of the command
     */
    protected abstract void execute(CommandSender sender, String[] args);

    /**
     * Tab-Completes the command
     *
     * @param sender - Who send the command, you can safely case sender to a player if setAllowOnlyPlayer(boolean flag) is used
     * @param args   - Arguments of the command
     * @return Sorted list with valid arguments
     */
    protected List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    /**
     * Sets if command can only be executed by a player.
     *
     * @param flag - boolean flag
     */
    public void setAllowOnlyPlayer(boolean flag) {
        this.allowOnlyPlayer = flag;
    }

    /**
     * Sets cooldown of command in ticks.
     *
     * @param cooldownTick - cooldown.
     */
    public void setCooldownTick(int cooldownTick) {
        this.cooldownTick = cooldownTick;
        this.cooldown = new CommandCooldown(this);
    }

    /**
     * If command has cooldown.
     *
     * @return true if command has cooldown, else otherwise.
     */
    public boolean hasCooldown() {
        return this.cooldownTick > 0 && this.cooldown != null;
    }

    public CommandCooldown getCooldown() {
        return cooldown;
    }

    /**
     * Returns cooldown in ticks or 0 if no cooldown.
     *
     * @return cooldown in ticks or 0 if no cooldown.
     */
    public int getCooldownTick() {
        return cooldownTick;
    }

    /**
     * Sets command usage.
     *
     * @param usage - New usage, slash will be removed.
     */
    public void setUsage(String usage) {
        if (usage.startsWith("/")) {
            usage = usage.replace("/", "");
        }
        this.usage = "/" + usage;
    }

    /**
     * Sets if this command is only allowed to be run by sever operators.
     *
     * @param bool - New value.
     */
    public void setAllowOnlyOp(boolean bool) {
        this.allowOnlyOp = bool;
    }

    /**
     * Sets a description of the command.
     *
     * @param info - new description
     */
    public void setDescription(String info) {
        this.description = info;
    }

    /**
     * Sets a command aliases.
     *
     * @param aliases - new aliases
     */
    public void setAliases(String... aliases) {
        this.aliases = aliases;
    }

    /**
     * Sets a command permission
     *
     * @param permission - new permission
     */
    public void setPermission(String permission) {
        this.permission = permission;
    }

    // Some useful utilities for ya.

    /**
     * Sorts input list so there are only strings that can finish the string you are typing.
     *
     * @param list           - List to sort
     * @param args           - Command args
     * @param forceLowerCase - Forces input and args to be in lower case
     */
    protected List<String> completerSort(List<String> list, String[] args, boolean forceLowerCase) {
        return Chat.tabCompleterSort(list, args, forceLowerCase);
    }

    // see above
    protected List<String> completerSort(List<String> list, String[] args) {
        return completerSort(list, args, true);
    }

    // see above
    protected <E> List<String> completerSort(E[] array, String[] args) {
        return completerSort(arrayToList(array), args);
    }

    // see above
    protected <E> List<String> completerSort(Collection<E> list, String[] args) {
        return Chat.tabCompleterSort(eToString(list), args);
    }

    /**
     * Returns true if argument of provided index is present and is equals to string.
     *
     * @param args   - Array of strings to check.
     * @param index  - Index of argument to match.
     * @param string - String to match.
     * @return true if argument of provided index is present and is equals to string.
     */
    protected boolean matchArgs(String[] args, int index, String string) {
        return index < args.length && args[index].equalsIgnoreCase(string);
    }

    /**
     * Converts set of strings to list of strings.
     *
     * @param set - Set to convert.
     * @return List of strings with set values.
     */
    protected List<String> setToList(Set<String> set) {
        return new ArrayList<>(set);
    }

    /**
     * Converts array to list of strings.
     *
     * @param array - Array to convert.
     * @return List of strings with array values.
     */
    protected <T> List<String> arrayToList(T[] array) {
        return Chat.arrayToList(array);
    }

    /**
     * Sends 'Invalid Usage!' message with correct usage of this command
     * to the sender.
     *
     * @param sender - Receiver actually.
     */
    protected void sendInvalidUsageMessage(CommandSender sender) {
        Chat.sendMessage(sender, "&cInvalid Usage! &e%s.", this.usage);
    }

    private <E> List<String> eToString(Collection<E> list) {
        List<String> str = new ArrayList<>();
        for (final E e : list) {
            str.add(e.toString());
        }
        return str;
    }

    // end of utils

    /**
     * Returns true if this command can only be called by players, false otherwise.
     *
     * @return true if this command can only be called by players, false otherwise.
     */
    public boolean isOnlyForPlayers() {
        return allowOnlyPlayer;
    }

    /**
     * Returns description of this command if present, 'Made using SimpleCommand by EternaAPI.' otherwise.
     *
     * @return Returns description of this command if present, 'Made using SimpleCommand by EternaAPI.' otherwise.
     */
    @Nonnull
    public String getDescription() {
        return description;
    }

    /**
     * Returns name of this command, aka the actual command.
     *
     * @return name of this command.
     */
    @Nonnull
    public String getName() {
        return name;
    }

    /**
     * Returns permission for this command.
     *
     * @return permission for this command.
     */
    @Nonnull
    public String getPermission() {
        return permission;
    }

    /**
     * Returns aliases of this command.
     *
     * @return aliases of this command.
     */
    @Nonnull
    public String[] getAliases() {
        return aliases;
    }

    /**
     * Returns true if this command can only be used by sever operators, false otherwise.
     *
     * @return true if this command can only be used by sever operators, false otherwise.
     */
    public boolean isAllowOnlyOp() {
        return allowOnlyOp;
    }

    /**
     * Returns usage of this command, which is '/(CommandName)' by default.
     *
     * @return usage of this command, which is '/(CommandName)' by default.
     */
    public String getUsage() {
        return usage;
    }

}
