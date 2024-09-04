package me.hapyl.eterna.builtin.manager;

import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.player.dialog.Dialog;
import me.hapyl.eterna.module.player.dialog.DialogInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public final class DialogManager extends EternaManager<Player, DialogInstance> implements Listener {
    DialogManager(@Nonnull EternaPlugin eterna) {
        super(eterna);
    }

    @EventHandler
    public void handlePlayerItemHeldEvent(PlayerItemHeldEvent ev) {
        final Player player = ev.getPlayer();
        final int newSlot = ev.getNewSlot();

        final DialogInstance dialog = get(player);

        if (dialog == null) {
            return;
        }

        // Options start from 1
        // Slots start from 0
        dialog.trySelectOption(newSlot + 1);
    }

    /**
     * Stops the {@link DialogInstance} for the given {@link Player} if the instance's dialog matches the given {@link Dialog}.
     *
     * @param player - Player to stop the dialog for.
     * @param dialog - Dialog to stop.
     * @return {@code true} if the dialog was stopped, {@code false} otherwise.
     */
    public boolean stopDialog(@Nonnull Player player, @Nonnull Dialog dialog) {
        return stopDialogIf(player, _dialog -> _dialog == dialog);
    }

    /**
     * Stops the {@link DialogInstance} for the given {@link Player}
     * if the given {@link Predicate} {@link Predicate#test(Object)} returns {@code true}.
     *
     * @param player    - Player to stop the dialog for.
     * @param predicate - Predicate to test against a dialog.
     * @return {@code true} if the dialog was stopped, {@code false} otherwise.
     */
    public boolean stopDialogIf(@Nonnull Player player, @Nonnull Predicate<Dialog> predicate) {
        final DialogInstance instance = get(player);

        if (instance != null && predicate.test(instance.getDialog())) {
            instance.cancel();
            return true;
        }

        return false;
    }

    /**
     * Stops all {@link DialogInstance} if the given {@link Predicate} {@link Predicate#test(Object)} returns {@code true}.
     *
     * @param predicate - Predicate to test against a dialog.
     */
    public void stopDialogIf(@Nonnull Predicate<Dialog> predicate) {
        managing.values().removeIf(instance -> {
            final Dialog dialog = instance.getDialog();

            if (predicate.test(dialog)) {
                instance.cancel0(); // Don't throw coe
                return true;
            }

            return false;
        });
    }
}
