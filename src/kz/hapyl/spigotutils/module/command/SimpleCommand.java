package kz.hapyl.spigotutils.module.command;

import kz.hapyl.spigotutils.module.annotate.NOTNULL;
import kz.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class SimpleCommand {

	private SimpleCommand() {
		throw new NullPointerException();
	}

	private final String name;

	private String permission;
	private String description;
	private String usage;

	private String[] aliases;

	private ArgumentProcessor post;

	private boolean tabCompleteArgumentProcessor;
	private boolean allowOnlyOp;
	private boolean allowOnlyPlayer;

	private int cooldownTick;

	private CommandCooldown cooldown;

	/**
	 * Creates a new simple command
	 *
	 * @param name - Name of the command.
	 */
	public SimpleCommand(@NOTNULL String name) {
		this.name = name;
		this.aliases = new String[]{};
		this.permission = "";
		this.usage = "/" + name;
		this.description = "Made using SimpleCommand using EternaAPI.";
		this.allowOnlyPlayer = false;
		this.allowOnlyOp = false;
		this.cooldownTick = 0;
		this.cooldown = null;
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

	protected int smartTabComplete(CommandSender sender, String[] args) {
		return -1;
	}

	/**
	 * Sets if command can only be executed by a player.
	 *
	 * @param flag - boolean flag
	 */
	public void setAllowOnlyPlayer(boolean flag) {
		this.allowOnlyPlayer = flag;
	}

	public void setCooldownTick(int cooldownTick) {
		this.cooldownTick = cooldownTick;
		this.cooldown = new CommandCooldown(this);
	}

	public boolean hasCooldown() {
		return this.cooldownTick > 0 && this.cooldown != null;
	}

	public CommandCooldown getCooldown() {
		return cooldown;
	}

	public int getCooldownTick() {
		return cooldownTick;
	}

	public ArgumentProcessor newArgumentProcessor() {
		this.post = new ArgumentProcessor(this);
		return this.post;
	}

	public void setUsage(String usageWithoutSlash) {
		if (usageWithoutSlash.startsWith("/")) {
			usageWithoutSlash = usageWithoutSlash.replace("/", "");
		}
		this.usage = "/" + usageWithoutSlash;
	}

	public void tabCompleteArgumentProcessor(boolean bool) {
		this.tabCompleteArgumentProcessor = bool;
	}

	public void setAllowOnlyOp(boolean bool) {
		this.allowOnlyOp = bool;
	}

	public ArgumentProcessor getPost() {
		return post;
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

	protected boolean matchArgs(String[] args, int index, String string) {
		return index < args.length && args[index].equalsIgnoreCase(string);
	}

	protected List<String> completerSort(List<String> list, String[] args) {
		return completerSort(list, args, true);
	}

	protected List<String> setToList(Set<String> set) {
		return new ArrayList<>(set);
	}

	protected <T> List<String> arrayToList(T[] array) {
		return Chat.arrayToList(array);
	}

	protected void sendInvalidUsageMessage(CommandSender sender) {
		Chat.sendMessage(sender, "&cInvalid Usage! &e%s.", this.usage);
	}

	// end of utils

	public boolean isOnlyForPlayers() {
		return allowOnlyPlayer;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public String getPermission() {
		return permission;
	}

	public String[] getAliases() {
		return aliases;
	}

	public boolean isAllowOnlyOp() {
		return allowOnlyOp;
	}

	public boolean isTabCompleteArgumentProcessor() {
		return tabCompleteArgumentProcessor;
	}

	public String getUsage() {
		return usage;
	}

}
