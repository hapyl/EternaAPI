package me.hapyl.eterna.builtin.command;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.module.command.SimplePlayerCommand;
import me.hapyl.eterna.module.player.dialog.DialogInstance;
import org.bukkit.entity.Player;

public final class SelectDialogOptionCommand extends SimplePlayerCommand {
    public SelectDialogOptionCommand(String name) {
        super(name);

        setDescription("Allows selection a dialog option by pressing on it in chat.");
    }

    @Override
    protected void execute(Player player, String[] args) {
        final DialogInstance dialog = Eterna.getManagers().dialog.get(player);

        if (dialog != null) {
            dialog.trySelectOption(getArgument(args, 0).toInt());
        }
    }
}
