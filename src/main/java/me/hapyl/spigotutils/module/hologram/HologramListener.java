package me.hapyl.spigotutils.module.hologram;

import me.hapyl.spigotutils.registry.EternaRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class HologramListener implements Listener {

    @EventHandler()
    public void handlePlayerJoinEvent(PlayerJoinEvent ev) {
        final Player player = ev.getPlayer();

        for (Hologram hologram : EternaRegistry.getHologramRegistry().getHolograms()) {
            if (!(hologram instanceof GlobalHologram) || hologram.isShowingTo(player) || !hologram.isCreated()) {
                continue;
            }

            hologram.show(player);
        }
    }

}
