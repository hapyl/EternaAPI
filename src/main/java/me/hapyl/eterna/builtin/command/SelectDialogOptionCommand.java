package me.hapyl.eterna.builtin.command;

import me.hapyl.eterna.EternaColors;
import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.command.ArgumentList;
import me.hapyl.eterna.module.command.SimplePlayerCommand;
import me.hapyl.eterna.module.player.dialog.Dialog;
import me.hapyl.eterna.module.player.dialog.entry.OptionIndex;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class SelectDialogOptionCommand extends SimplePlayerCommand {
    public SelectDialogOptionCommand(@NotNull EternaKey key, String name) {
        super(name);
        
        key.validateKey();
        
        setDescription("Allows selection a dialog option by pressing on it in chat.");
    }
    
    @Override
    protected void execute(@NotNull Player player, @NotNull ArgumentList args) {
        Dialog.getCurrentDialog(player).ifPresentOrElse(
                dialog -> {
                    final OptionIndex optionIndex = OptionIndex.fromInt(args.get(0).toInt());
                    
                    if (optionIndex == null) {
                        EternaLogger.message(player, Component.text("Invalid option!", EternaColors.DARK_RED));
                        return;
                    }
                    
                    if (!dialog.trySelectOption(optionIndex)) {
                        EternaLogger.message(player, Component.text("Nothing to select!", EternaColors.DARK_RED));
                    }
                },
                () -> {
                    EternaLogger.message(player, Component.text("Not currently in dialog!", EternaColors.DARK_RED));
                });
    }
    
}
