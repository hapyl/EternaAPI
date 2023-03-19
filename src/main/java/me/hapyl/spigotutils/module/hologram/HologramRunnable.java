package me.hapyl.spigotutils.module.hologram;

import me.hapyl.spigotutils.registry.EternaRegistry;

import java.util.Objects;

public class HologramRunnable implements Runnable {

    private void run0() {
        EternaRegistry.getHologramRegistry().getHolograms().forEach(hologram -> {
            if (hologram.getRemoveWhenFarAway() == 0 || hologram.isPersistent()) {
                return;
            }

            hologram.getShowingTo().forEach((player, status) -> {
                if (!Objects.equals(player.getLocation().getWorld(), hologram.getLocation().getWorld())) {
                    return;
                }

                final double distance = player.getLocation().distance(hologram.getLocation());

                if (distance >= hologram.getRemoveWhenFarAway()) {
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
        });
    }

    @Override
    public void run() {
        run0();
    }

}
