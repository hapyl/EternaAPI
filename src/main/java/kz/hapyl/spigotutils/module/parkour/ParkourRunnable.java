package kz.hapyl.spigotutils.module.parkour;

import kz.hapyl.spigotutils.SpigotUtilsPlugin;
import kz.hapyl.spigotutils.module.chat.Chat;
import org.bukkit.entity.Player;

import java.util.Map;

public class ParkourRunnable implements Runnable {
    @Override
    public void run() {
        final ParkourManager manager = SpigotUtilsPlugin.getPlugin().getParkourManager();
        final Map<Player, Data> hashMap = manager.getParkourData();

        for (Data data : hashMap.values()) {
            final Parkour parkour = data.getParkour();
            if (parkour.isSilent()) {
                continue;
            }

            Chat.sendActionbar(data.get(), "&a&l%s: &b%ss", parkour.getName(), data.getTimePassedFormatted());
        }
    }
}
