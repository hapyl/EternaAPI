package me.hapyl.spigotutils.module.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.completer.Checker;
import me.hapyl.spigotutils.module.command.completer.Checker2;
import me.hapyl.spigotutils.module.command.completer.CompleterHandler;
import me.hapyl.spigotutils.module.player.PlayerLib;
import me.hapyl.spigotutils.module.util.BukkitUtils;
import me.hapyl.spigotutils.module.util.TypeConverter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

/**
 * Creates a SimpleCommand.
 *
 * @author hapyl
 */
public abstract class SimpleCommand {

    private final String name;
    private final Map<Integer, List<String>> completerValues;
    private final Map<Integer, CompleterHandler> completerHandlers;
    private String permission;
    private String description;
    private String usage;
    private String[] aliases;
    private boolean allowOnlyOp;
    private boolean allowOnlyPlayer;
    private int cooldownTick;
    private CommandCooldown cooldown;
    @Nullable private ArgumentProcessor argumentProcessor;

    private SimpleCommand() {
        throw new NullPointerException();
    }

    /**
     * Creates a new simple command
     *
     * @param name - Name of the command.
     */
    public SimpleCommand(@Nonnull String name) {
        this.name = name.toLowerCase();
        this.aliases = new String[] {};
        this.permission = "";
        this.usage = "/" + name;
        this.description = "Made using SimpleCommand by EternaAPI.";
        this.allowOnlyPlayer = false;
        this.allowOnlyOp = false;
        this.cooldownTick = 0;
        this.cooldown = null;
        this.completerValues = Maps.newHashMap();
        this.completerHandlers = Maps.newHashMap();
    }

    /**
     * Adds a new completer handler.
     *
     * @param handler - Handler to add.
     */
    public void addCompleterHandler(CompleterHandler handler) {
        this.completerHandlers.put(handler.getIndex(), handler);
    }

    /**
     * Adds a new completer handler.
     *
     * @param index   - Index of the argument.
     * @param checker - Checker to use.
     */
    public void addCompleterHandler(int index, Checker checker) {
        this.completerHandlers.put(index, CompleterHandler.of(index).custom(checker));
    }

    /**
     * Adds a new completer handler.
     *
     * @param index   - Index of the argument.
     * @param checker - Checker to use.
     */
    public void addCompleterHandler(int index, Checker2 checker) {
        this.completerHandlers.put(index, CompleterHandler.of(index).custom(checker));
    }

    /**
     * Adds a new completer handler.
     *
     * @param index     - Index of the argument.
     * @param ifValid   - Value to arg if valid.
     * @param ifInvalid - Value to arg if invalid.
     */
    public void addCompleterHandler(int index, String ifValid, String ifInvalid) {
        this.completerHandlers.put(index, CompleterHandler.of(index).ifValidValue(ifValid).ifInvalidValue(ifInvalid));
    }

    /**
     * Adds a value to index of tab completer.
     * Note that completer values will be automatically sorted
     * using {@link SimpleCommand#completerSort(List, String[])} AFTER
     * {@link SimpleCommand#tabComplete(CommandSender, String[])} is called.
     *
     * @param index - Index. <b>Starts at 1 for first argument (args[0])</b>
     * @param value - Value to add. Will be forced to lower case.
     */
    public void addCompleterValue(int index, String value) {
        addCompleterValues(index, value);
    }

    /**
     * Adds values to index of tab completer.
     *
     * @param index  - Index. <b>Starts at 1 for first argument (args[0])</b>
     * @param values - Values to add. Will be forced to lower case.
     */
    public void addCompleterValues(int index, String... values) {
        index = Math.max(1, index);
        final List<String> list = getCompleterValues(index);
        for (String value : values) {
            list.add(value.toLowerCase());
        }
        completerValues.put(index, list);
    }

    /**
     * Adds values to index of tab completer.
     *
     * @param index  - Index of the argument.
     * @param values - Values to add.
     * @param <T>    - Type of the values.
     */
    public <T> void addCompleterValues(int index, @Nonnull Collection<T> values) {
        addCompleterValues(index, values, str -> {
            if (str instanceof Enum) {
                return ((Enum<?>) str).name();
            }

            return str.toString();
        });
    }

    /**
     * Adds values to index of tab completer.
     *
     * @param index    - Index of the argument.
     * @param values   - Values to add.
     * @param toString - Function to convert values to string.
     * @param <T>      - Type of the values.
     */
    public <T> void addCompleterValues(int index, @Nonnull Collection<T> values, @Nonnull Function<T, String> toString) {
        final String[] strings = new String[values.size()];

        int i = 0;
        for (T value : values) {
            strings[i++] = toString.apply(value);
        }

        addCompleterValues(index, strings);
    }

    /**
     * Adds values to index of tab completer.
     *
     * @param index - Index of the argument.
     * @param array - Array of values.
     * @param <T>   - Type of the values.
     */
    public <T extends Enum<T>> void addCompleterValues(int index, Enum<T>[] array) {
        for (Enum<T> tEnum : array) {
            addCompleterValue(index, tEnum.name());
        }
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
     * Sets if command can only be executed by a player.
     *
     * @param flag - boolean flag
     */
    public void setAllowOnlyPlayer(boolean flag) {
        this.allowOnlyPlayer = flag;
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
     * Sets cooldown of command in ticks.
     *
     * @param cooldownTick - cooldown.
     */
    public void setCooldownTick(int cooldownTick) {
        this.cooldownTick = cooldownTick;
        this.cooldown = new CommandCooldown(this);
    }

    /**
     * Returns argument processor for this command if any present.
     *
     * @return argument processor for this command if any present.
     */
    @Nullable
    @Deprecated
    public ArgumentProcessor getArgumentProcessor() {
        return argumentProcessor;
    }

    public void testArgumentProcessorIfExists(CommandSender sender, String[] args) {
        if (argumentProcessor != null) {
            argumentProcessor.checkArgumentAndExecute(sender, args);
        }
    }

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
     * Sets a description of the command.
     *
     * @param info - new description
     */
    public void setDescription(String info) {
        this.description = info;
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
     * Sets a command permission
     *
     * @param permission - new permission
     */
    public void setPermission(String permission) {
        this.permission = permission;
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
     * Sets a command aliases.
     *
     * @param aliases - new aliases
     */
    public void setAliases(String... aliases) {
        this.aliases = new String[aliases.length];

        for (int i = 0; i < aliases.length; i++) {
            this.aliases[i] = aliases[i].toLowerCase();
        }
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
     * Sets if this command is only allowed to be run by sever operators.
     *
     * @param bool - New value.
     */
    public void setAllowOnlyOp(boolean bool) {
        this.allowOnlyOp = bool;
    }

    /**
     * Returns usage of this command, which is '/(CommandName)' by default.
     *
     * @return usage of this command, which is '/(CommandName)' by default.
     */
    public String getUsage() {
        return usage;
    }

    // Some useful utilities for ya.

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

    public final Command createCommand() {
        final SimpleCommand cmd = this;

        return new Command(cmd.getName(), cmd.getDescription(), cmd.getUsage(), Arrays.asList(cmd.getAliases())) {
            @Override
            public boolean execute(@Nonnull CommandSender sender, @Nonnull String label, @Nonnull String[] args) {
                if (cmd instanceof DisabledCommand) {
                    Chat.sendMessage(sender, "&cThis command is currently disabled!");
                    return true;
                }

                if (cmd.isOnlyForPlayers() && !(sender instanceof Player)) {
                    Chat.sendMessage(sender, "&cYou must be a player to use perform this command!");
                    return true;
                }

                // permission check
                if ((cmd.isAllowOnlyOp() && !sender.isOp()) || (cmd.hasPermission() && !sender.hasPermission(cmd.getPermission()))) {
                    Chat.sendMessage(sender, "&4No permissions.");
                    return true;
                }

                // cooldown check
                if (cmd.hasCooldown() && sender instanceof final Player playerSender) {
                    final CommandCooldown cooldown = cmd.getCooldown();
                    if (cooldown.hasCooldown(playerSender)) {
                        Chat.sendMessage(
                                playerSender,
                                "&cThis command is on cooldown for %ss!",
                                BukkitUtils.roundTick((int) (cooldown.getTimeLeft(playerSender) / 50L))
                        );
                        PlayerLib.playSound(playerSender, Sound.ENTITY_ENDERMAN_TELEPORT, 0.0f);
                        return true;
                    }
                    if (!cooldown.canIgnoreCooldown(playerSender)) {
                        cooldown.startCooldown(playerSender);
                    }
                }

                cmd.execute(sender, args);

                //  test argument processor
                cmd.testArgumentProcessorIfExists(sender, args);

                return true;
            }

            // Register Tab Completer
            @Override
            @Nonnull
            public List<String> tabComplete(@Nonnull CommandSender sender, @Nonnull String alias, @Nonnull String[] args) throws IllegalArgumentException {
                if (cmd.isOnlyForPlayers() && !(sender instanceof Player)) {
                    return defaultCompleter();
                }

                final List<String> strings = colorizeList(preTabComplete(sender, args));
                final List<String> tabComplete = cmd.tabComplete(sender, args);

                if (tabComplete != null) {
                    strings.addAll(tabComplete);
                }

                if (cmd.hasCompleterValues(args.length)) {
                    strings.addAll(cmd.completerSort(cmd.getCompleterValues(args.length), args));
                }

                strings.addAll(colorizeList(postTabComplete(sender, args)));

                if (sender instanceof Player player) {
                    completerHandler(player, args.length, args, strings);
                }

                return strings.isEmpty() ? defaultCompleter() : strings;
            }
        };
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
    @Nullable
    protected List<String> tabComplete(CommandSender sender, String[] args) {
        return Lists.newArrayList();
    }

    /**
     * Pre-tab complete, called before {@link SimpleCommand#tabComplete(CommandSender, String[])}.
     *
     * @param sender - Who send the command, you can safely case sender to a player if setAllowOnlyPlayer(boolean flag) is used
     * @param args   - Arguments of the command
     * @return New list of completes.
     */
    @Nonnull
    protected List<String> preTabComplete(CommandSender sender, String[] args) {
        return Lists.newArrayList();
    }

    /**
     * Post-tab complete, called after {@link SimpleCommand#tabComplete(CommandSender, String[])}.
     *
     * @param sender - Who send the command, you can safely case sender to a player if setAllowOnlyPlayer(boolean flag) is used
     * @param args   - Arguments of the command
     * @return New list of completes.
     */
    @Nonnull
    protected List<String> postTabComplete(CommandSender sender, String[] args) {
        return Lists.newArrayList();
    }

    protected void tryArgumentProcessor() {
        if (this.argumentProcessor == null && ArgumentProcessor.countMethods(this) > 0) {
            this.argumentProcessor = new ArgumentProcessor(this);
        }
    }

    /**
     * Sort input lists so there are only strings that can finish the string you are typing.
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

    // end of utils

    protected List<String> completerSort2(List<String> list, String[] args, boolean forceLowerCase) {
        return Chat.tabCompleterSort0(list, args, forceLowerCase, false);
    }

    protected List<String> completerSort2(List<String> list, String[] args) {
        return completerSort2(list, args, true);
    }

    protected <E> List<String> completerSort2(E[] array, String[] args) {
        return completerSort2(arrayToList(array), args);
    }

    /**
     * Returns true if argument of provided index is present and is equals to string.
     *
     * @param args   - Array of strings to check.
     * @param index  - Index of argument to match.
     * @param string - String to match.
     * @return true if the argument of provided index is present and is equals to string.
     */
    protected boolean matchArgs(String[] args, int index, String string) {
        return index < args.length && args[index].equalsIgnoreCase(string);
    }

    @Nonnull
    protected TypeConverter getArgument(String[] args, int index) {
        return TypeConverter.from(index >= args.length ? "" : args[index]);
    }

    /**
     * Converts a set of strings to list of strings.
     *
     * @param set - Set to convert.
     * @return List of strings with set values.
     */
    protected List<String> setToList(Set<String> set) {
        return new ArrayList<>(set);
    }

    /**
     * Converts an array to a list of strings.
     *
     * @param array - Array to convert.
     * @return List of strings with array values.
     */
    protected <T> List<String> arrayToList(T[] array) {
        return Chat.arrayToList(array);
    }

    /**
     * Sends <code>Invalid Usage!</code> message with the correct usage of this command
     * to the sender.
     *
     * @param sender - Receiver actually.
     */
    protected void sendInvalidUsageMessage(CommandSender sender) {
        Chat.sendMessage(sender, "&cInvalid Usage! &e%s.", this.usage);
    }

    // end of utilities

    private <E> List<String> eToString(Collection<E> list) {
        List<String> str = new ArrayList<>();
        for (final E e : list) {
            str.add(e.toString());
        }
        return str;
    }

    private void completerHandler(Player player, int index, String[] array, List<String> list) {
        final CompleterHandler handler = completerHandlers.get(index);
        if (handler == null) {
            return;
        }

        handler.handle(player, array, list);
    }

    private boolean hasPermission() {
        return permission != null && !permission.isEmpty();
    }

    private static List<String> colorizeList(List<String> list) {
        final List<String> newList = new ArrayList<>();

        for (final String s : list) {
            newList.add(Chat.format(s));
        }

        return newList;
    }

    private static List<String> defaultCompleter() {
        final List<String> list = new ArrayList<>();
        for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            list.add(onlinePlayer.getName());
        }
        return list;
    }

}
