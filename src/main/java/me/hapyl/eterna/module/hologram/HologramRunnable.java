package me.hapyl.eterna.module.hologram;

import me.hapyl.eterna.Eterna;
import me.hapyl.eterna.module.entity.LimitedVisibility;
import org.bukkit.entity.Player;

public class HologramRunnable implements Runnable {

    @Override
    public void run() {
        // Check for visibility
        Eterna.getManagers().hologram.getHolograms().forEach(hologram -> {
            for (Player player : hologram.getShowingTo()) {
                LimitedVisibility.check(player, hologram);
            }
        });
    }

}
