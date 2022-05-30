package me.hapyl.spigotutils.module.hologram;

import me.hapyl.spigotutils.EternaRegistry;

import java.util.Objects;

public class HologramRunnable implements Runnable {

    @Override
    public void run() {
        EternaRegistry.getHologramRegistry().getHolograms().forEach(hologram -> {
            if (hologram.getRemoveWhenFarAway() > 0 && !hologram.isPersistent()) {
                hologram.getShowingTo().forEach((player, status) -> {
                    if (!Objects.equals(player.getLocation().getWorld(), hologram.getLocation().getWorld())) {
                        return;
                    }
                    if (player.getLocation().distance(hologram.getLocation()) >= hologram.getRemoveWhenFarAway()) {
                        if (hologram.isShowingTo(player)) {
                            hologram.hide(true, player);
                        }
                    }
                    else {
                        if (!hologram.isShowingTo(player)) {
                            hologram.show(player);
                        }
                    }
                });
            }
        });
    }

}
