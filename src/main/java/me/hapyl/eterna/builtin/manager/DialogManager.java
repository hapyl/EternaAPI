package me.hapyl.eterna.builtin.manager;

import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.player.dialog.DialogInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

import javax.annotation.Nonnull;

public class DialogManager extends EternaManager<Player, DialogInstance> implements Listener {
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
}
