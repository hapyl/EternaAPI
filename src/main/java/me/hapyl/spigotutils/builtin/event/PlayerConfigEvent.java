package me.hapyl.spigotutils.builtin.event;

import me.hapyl.spigotutils.registry.EternaRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConfigEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePlayerJoinEvent(PlayerJoinEvent ev) {
        EternaRegistry.getConfigManager().getConfig(ev.getPlayer()).loadAll();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePlayerQuitEvent(PlayerQuitEvent ev) {
        EternaRegistry.getConfigManager().getConfig(ev.getPlayer()).saveAll();
    }

}
