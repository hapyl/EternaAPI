package kz.hapyl.spigotutils.module.player.song;

import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.util.Holder;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SongPlayer extends Holder<JavaPlugin> {

    protected Song currentSong;
    private boolean playing;
    private boolean pause;
    private long tick = 0L;
    private BukkitTask task;
    private Set<Player> listeners;

    private final SongQueue queue;

    public SongPlayer(JavaPlugin plugin) {
        super(plugin);
        this.listeners = new HashSet<>();
        this.queue = new SongPlayerQueue();
    }

    public JavaPlugin getOwningPlugin() {
        return get();
    }

    public SongQueue getQueue() {
        return queue;
    }

    public void addListener(Player player) {
        this.listeners.add(player);
    }

    public void removeListener(Player player) {
        this.listeners.remove(player);
    }

    public boolean isPaused() {
        return pause;
    }

    public boolean isListener(Player player) {
        return this.listeners == null || this.listeners.contains(player);
    }

    public void everyoneIsListener() {
        this.listeners = null;
    }

    public void pausePlaying() {
        this.pause = !this.pause;
        sendMessage("%s Playing &l" + this.currentSong.getName(), this.pause ? "&ePaused" : "&aUnpause");
    }

    public void stopPlaying() {
        if (currentSong != null) {
            sendMessage("&aFinished Playing &l" + this.currentSong.getName());
            this.currentSong = null;
        }
        if (task != null) {
            task.cancel();
        }
        this.playing = false;
    }

    public void setCurrentSong(Song song) {
        this.stopPlaying();
        this.currentSong = song;
    }

    public boolean isPlaying() {
        return playing;
    }

    @Nullable
    public Song getCurrentSong() {
        return currentSong;
    }

    public boolean hasSong() {
        return currentSong != null;
    }

    public void startPlaying() {
        if (playing) {
            stopPlaying();
        }

        sendMessage("&aNow Playing &l" + this.currentSong.getName());
        if (!currentSong.isOkOctave()) {
            sendMessage("&eSome notes in this song aren't between F#0 and F#2, it might sound off!");
        }
        this.playing = true;
        this.tick = 0;
        this.task = new BukkitRunnable() {

            @Override
            public void run() {
                if (pause) {
                    return;
                }

                if (tick++ >= currentSong.getLength()) {
                    stopPlaying();
                    queue.playNext();
                    return;
                }

                final List<SongNote> notes = currentSong.getNotes(tick);

                if (notes != null) {
                    for (final SongNote note : notes) {
                        note.play(getListeners());
                    }
                }

            }
        }.runTaskTimer(getOwningPlugin(), 0, this.currentSong.getTempo());
    }

    public long getCurrentFrame() {
        return tick;
    }

    public long getMaxFrame() {
        return this.currentSong == null ? 1 : this.currentSong.getLength();
    }

    private Collection<? extends Player> getListeners() {
        return this.listeners == null ? Bukkit.getOnlinePlayers() : this.listeners;
    }

    private final String prefix = "&b&lNBS> &7";

    public void sendMessage(Player player, String msg, Object... dot) {
        Chat.sendMessage(player, prefix + msg, dot);
    }

    public void sendMessage(Player player, BaseComponent[] components) {
        player.spigot().sendMessage(new ComponentBuilder(Chat.format(prefix)).append(components).create());
    }

    public void sendMessage(String msg, Object... dot) {
        for (final Player player : getListeners()) {
            Chat.sendMessage(player, prefix + msg, dot);
        }
    }
}
