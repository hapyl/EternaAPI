package me.hapyl.eterna.builtin.event;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.builtin.updater.UpdateResult;
import me.hapyl.eterna.builtin.updater.Updater;
import me.hapyl.eterna.module.annotate.BuiltIn;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Event handler for {@link me.hapyl.eterna.config.PlayerConfig}.
 */
@BuiltIn
public final class PlayerConfigEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePlayerJoinEvent(PlayerJoinEvent ev) {
        final Player player = ev.getPlayer();

        Eterna.getRegistry().configRegistry.getConfig(player).loadAll();

        // Operator checks
        if (!player.isOp()) {
            return;
        }

        final Updater updater = Eterna.getUpdater();
        final UpdateResult lastUpdateResult = updater.getLastResult();

        if (lastUpdateResult == UpdateResult.OUTDATED) {
            updater.sendLink(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePlayerQuitEvent(PlayerQuitEvent ev) {
        Eterna.getRegistry().configRegistry.getConfig(ev.getPlayer()).saveAll();
    }

}
