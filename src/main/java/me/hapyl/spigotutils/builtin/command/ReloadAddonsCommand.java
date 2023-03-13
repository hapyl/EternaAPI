package me.hapyl.spigotutils.builtin.command;

import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.addons.AddonRegistry;
import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.command.SimplePlayerAdminCommand;
import org.bukkit.entity.Player;

public class ReloadAddonsCommand extends SimplePlayerAdminCommand {

    public ReloadAddonsCommand(String name) {
        super(name);
        setDescription("Reloads addons.");
    }

    @Override
    protected void execute(Player player, String[] args) {
        final AddonRegistry registry = EternaPlugin.getPlugin().getRegistry().addonRegistry;
        registry.unloadAll();
        registry.loadAll();

        Chat.sendMessage(player, "&aReloaded!");
    }
}
