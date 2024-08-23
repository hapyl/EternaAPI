package me.hapyl.eterna.module.player.sound;

import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.player.PlayerLib;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

public class SoundQueueInstance extends BukkitRunnable {

    private final CachedSoundQueue cached;
    private final Collection<Player> listeners;

    private int frame = 0;

    SoundQueueInstance(SoundQueue queue, Collection<Player> listeners) {
        this.cached = new CachedSoundQueue(queue.sounds);
        this.listeners = listeners;

        runTaskTimer(EternaPlugin.getPlugin(), 0, 1);
    }

    public void stop() {
        cancel();
    }

    @Override
    public void run() {
        if (frame++ >= cached.getMaxFrame()) {
            cancel();
            return;
        }

        for (final SoundQueueSound sound : cached.get(frame)) {
            listeners.forEach(player -> PlayerLib.playSound(player, sound.getSound(), sound.getPitch()));
        }
    }
}
