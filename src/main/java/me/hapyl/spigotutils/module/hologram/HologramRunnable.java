package me.hapyl.spigotutils.module.hologram;

import me.hapyl.spigotutils.Eterna;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.entity.LimitedVisibility;
import org.bukkit.entity.Player;

public class HologramRunnable implements Runnable {

    @Override
    public void run() {
        // Check for visibility
        Eterna.getRegistry().hologramRegistry.getHolograms().forEach(hologram -> {
            for (Player player : hologram.getShowingTo()) {
                LimitedVisibility.check(player, hologram);
            }
        });
    }

}
