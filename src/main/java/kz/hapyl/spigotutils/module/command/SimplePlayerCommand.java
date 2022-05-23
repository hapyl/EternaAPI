package kz.hapyl.spigotutils.module.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class SimplePlayerCommand extends SimpleCommand {
    /**
     * Creates a new simple command
     *
     * @param name - Name of the command.
     */
    public SimplePlayerCommand(String name) {
        super(name);
        this.setAllowOnlyPlayer(true);
    }

    protected abstract void execute(Player player, String[] args);

    @Override
    protected final void execute(CommandSender sender, String[] args) {
        execute((Player) sender, args);
    }
}
