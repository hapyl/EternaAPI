package me.hapyl.spigotutils.module.player.song;

import me.hapyl.spigotutils.module.chat.Chat;
import me.hapyl.spigotutils.module.util.Holder;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a player that can play {@see Song} to players.
 */
public class SongPlayer extends Holder<JavaPlugin> {

    private final SongQueue queue;
    private final String prefix = "&b&lNBS> &7";
    protected Song currentSong;
    private boolean playing;
    private boolean pause;
    private long tick = 0L;
    private BukkitTask task;
    private Set<Player> listeners;

    public SongPlayer(JavaPlugin plugin) {
        super(plugin);
        this.listeners = new HashSet<>();
        this.queue = new SongPlayerQueue();
    }

    /**
     * Returns the plugin who owns this player.
     *
     * @return the plugin who owns this player.
     */
    public JavaPlugin getOwningPlugin() {
        return get();
    }

    /**
     * Returns current song queue.
     *
     * @return current song queue.
     */
    public SongQueue getQueue() {
        return queue;
    }

    /**
     * Adds a listener to this player.
     *
     * @param player - Listener.
     */
    public void addListener(Player player) {
        if (this.listeners == null) {
            return; // global
        }
        this.listeners.add(player);
    }

    /**
     * Removes a listener from this player.
     *
     * @param player - Listener.
     */
    public void removeListener(Player player) {
        if (this.listeners == null) {
            return; // global
        }
        this.listeners.remove(player);
    }

    /**
     * Returns true if the player is paused.
     *
     * @return true if the player is paused.
     */
    public boolean isPaused() {
        return pause;
    }

    /**
     * Returns true if player is listener for this player.
     *
     * @param player - Player to check.
     * @return true if player is listener for this player.
     */
    public boolean isListener(Player player) {
        return this.listeners == null || this.listeners.contains(player);
    }

    /**
     * Returns true if this player is global.
     */
    public void everyoneIsListener() {
        this.listeners = null;
    }

    /**
     * Pauses or unpauses the player.
     */
    public void pausePlaying() {
        this.pause = !this.pause;
        sendMessage("%s Playing &l" + this.currentSong.getName(), this.pause ? "&ePaused" : "&aUnpause");
    }

    /**
     * Stops the player.
     */
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

    /**
     * Returns true if player is playing a song.
     *
     * @return true if player is playing a song.
     */
    public boolean isPlaying() {
        return playing;
    }

    /**
     * Returns the currently playing song, or null if none is playing.
     *
     * @return the currently playing song, or null if none is playing.
     */
    @Nullable
    public Song getCurrentSong() {
        return currentSong;
    }

    /**
     * Stops the current song and sets the current song to the given song.
     *
     * @param song - Song to set.
     */
    public void setCurrentSong(Song song) {
        this.stopPlaying();
        this.currentSong = song;
    }

    /**
     * Returns true if the player has a song.
     *
     * @return true if the player has a song.
     */
    public boolean hasSong() {
        return currentSong != null;
    }

    /**
     * Stars the playing of a song.
     */
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

    /**
     * Returns current frame (tick) of a song.
     *
     * @return current frame (tick) of a song.
     */
    public long getCurrentFrame() {
        return tick;
    }

    /**
     * Returns max frames (length) of a song.
     *
     * @return max frames (length) of a song.
     */
    public long getMaxFrame() {
        return this.currentSong == null ? 1 : this.currentSong.getLength();
    }

    /**
     * Returns listeners for this song. Or all online players is global.
     *
     * @return listeners for this song. Or all online players is global.
     */
    private Collection<? extends Player> getListeners() {
        return this.listeners == null ? Bukkit.getOnlinePlayers() : this.listeners;
    }

    // shortcuts
    public void sendMessage(CommandSender player, String msg, Object... dot) {
        Chat.sendMessage(player, prefix + msg, dot);
    }

    public void sendMessage(CommandSender player, BaseComponent[] components) {
        player.spigot().sendMessage(new ComponentBuilder(Chat.format(prefix)).append(components).create());
    }

    public void sendMessage(String msg, Object... dot) {
        for (final Player player : getListeners()) {
            Chat.sendMessage(player, prefix + msg, dot);
        }
    }
}
