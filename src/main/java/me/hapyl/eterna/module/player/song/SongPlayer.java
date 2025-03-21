package me.hapyl.eterna.module.player.song;

import com.google.common.collect.Sets;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.chat.Chat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Represents a player that can play {@see Song} to players.
 */
public class SongPlayer {
    
    public static final SongPlayer DEFAULT_PLAYER;
    public static final String PREFIX = "&b&lNBS&b> &7";
    
    static {
        DEFAULT_PLAYER = new SongPlayer(EternaPlugin.getPlugin()) {
            @Override
            public boolean isGlobal() {
                return true; // Force global
            }
            
            @Nonnull
            @Override
            public Collection<Player> getListeners() {
                return Sets.newHashSet(Bukkit.getOnlinePlayers());
            }
        };
    }
    
    private final JavaPlugin plugin;
    private final SongQueue queue;
    private final Collection<Player> listeners;
    
    private SongInstance currentInstance;
    private float volume;
    
    public SongPlayer(@Nonnull JavaPlugin plugin) {
        this.plugin = plugin;
        this.queue = new SongPlayerQueue();
        this.listeners = Sets.newHashSet();
        this.volume = SongNote.DEFAULT_VOLUME;
    }
    
    /**
     * Gets the current volume of the player.
     *
     * @return the current volume of the player.
     */
    public float volume() {
        return volume;
    }
    
    /**
     * Sets the current volume of the player.
     * <p>If there is currently an instance playing, its volume will be changed.</p>
     *
     * @param volume - The new volume.
     */
    public void volume(float volume) {
        this.volume = Math.clamp(volume, 0, 1);
        
        if (currentInstance != null) {
            currentInstance.volume(volume);
        }
    }
    
    /**
     * Gets the owning plugin of the player.
     *
     * @return the owning plugin of the player.
     */
    @Nonnull
    public JavaPlugin getOwningPlugin() {
        return plugin;
    }
    
    /**
     * Gets the song queue of the player.
     *
     * @return the song queue of the player.
     */
    @Nonnull
    public SongQueue getQueue() {
        return queue;
    }
    
    /**
     * Adds the given player as to listeners.
     *
     * @param player - The player to add.
     */
    public void addListener(@Nonnull Player player) {
        this.listeners.add(player);
    }
    
    /**
     * Removes the given player from listeners.
     *
     * @param player - The player to remove.
     */
    public void removeListener(Player player) {
        this.listeners.remove(player);
    }
    
    /**
     * Returns {@code true} if the given player is a listener.
     *
     * @param player - The player to check.
     * @return {@code true} if the given player is a listener.
     */
    public boolean isListener(@Nonnull Player player) {
        return this.listeners.isEmpty() || this.listeners.contains(player);
    }
    
    /**
     * Clears all the listeners, making the playback global for each online player.
     */
    public void setGlobal() {
        this.listeners.clear();
    }
    
    /**
     * Returns {@code true} if playback is global for each online player.
     *
     * @return {@code true} if playback is global for each online player.
     */
    public boolean isGlobal() {
        return this.listeners.isEmpty();
    }
    
    /**
     * Gets the listeners of the player.
     *
     * @return the listeners.
     */
    @Nonnull
    public Collection<Player> getListeners() {
        return listeners;
    }
    
    /**
     * Gets the current song instance or {@code null} if there is none.
     *
     * @return the current song instance or {@code null} if there is none.
     */
    @Nullable
    public SongInstance currentInstance() {
        return currentInstance;
    }
    
    /**
     * Gets the current song or {@code null} if there is none.
     *
     * @return the current song or {@code null} if there is none.
     */
    @Nullable
    public Song currentSong() {
        return currentInstance != null ? currentInstance.song() : null;
    }
    
    /**
     * Pauses or resumes the current instance if it exists.
     */
    public void pausePlaying() {
        if (this.currentInstance == null) {
            return;
        }
        
        this.currentInstance.pause();
    }
    
    /**
     * Forcefully stops the current instance if it exists.
     */
    public void stopPlaying() {
        if (this.currentInstance == null) {
            return;
        }
        
        this.currentInstance.cancel();
        this.currentInstance = null;
    }
    
    /**
     * Returns {@code true} if there is an instance and its currently {@link SongInstance#isPlaying()}.
     *
     * @return {@code true} if there is an instance and its currently {@link SongInstance#isPlaying()}.
     */
    public boolean isPlaying() {
        return this.currentInstance != null && this.currentInstance.isPlaying();
    }
    
    /**
     * Returns {@code ture} if there is an instance.
     *
     * @return {@code ture} if there is an instance.
     */
    public boolean hasSong() {
        return this.currentInstance != null;
    }
    
    /**
     * Starts playing the given {@link Song}.
     * <p>This will cancel currently playing song if there is one.</p>
     *
     * @param song - The song to play.
     */
    public void startPlaying(@Nonnull Song song) {
        stopPlaying();
        
        final SongInstance instance = song.newInstance(plugin);
        instance.volume(volume);
        instance.callback(makeCallback(song));
        
        this.currentInstance = instance;
        this.currentInstance.play(getListeners());
    }
    
    /**
     * Sends the NBS message to the given player.
     *
     * @param player  - The player.
     * @param message - The message.
     */
    public void message(@Nonnull CommandSender player, @Nonnull String message) {
        Chat.sendMessage(player, PREFIX + message);
    }
    
    /**
     * Sends the NBS message to the given player.
     *
     * @param player  - The player.
     * @param message - The message.
     */
    public void message(@Nonnull CommandSender player, @Nonnull TextComponent.Builder message) {
        player.sendMessage(Component.text(Chat.format(PREFIX)).append(message));
    }
    
    /**
     * Sends the NBS message to the all listeners.
     *
     * @param message - The message.
     */
    public void message(@Nonnull String message) {
        getListeners().forEach(player -> this.message(player, message));
    }
    
    /**
     * Sends the NBS message to the all listeners.
     *
     * @param message - The message.
     */
    public void message(@Nonnull TextComponent.Builder message) {
        getListeners().forEach(player -> this.message(player, message));
    }
    
    private SongCallback makeCallback(Song song) {
        return new SongCallback() {
            @Override
            public void onStartPlaying() {
                message("&aNow playing &6%s&a.".formatted(song.getName()));
                
                // Display warnings
                if (!song.isPerfect()) {
                    final TextComponent.Builder builder = Component.text()
                                                                   .append(Component.text(" ❗ ", NamedTextColor.GOLD));
                    
                    if (!song.isOkOctave()) {
                        builder.append(
                                Component.text("Illegal Notes", NamedTextColor.YELLOW, TextDecoration.BOLD, TextDecoration.UNDERLINED)
                                         .hoverEvent(HoverEvent.showText(Component.text(
                                                 "Some notes aren't within Minecraft limit of F#0 and F#2 so it might sound off!",
                                                 NamedTextColor.GOLD
                                         )))
                        );
                        
                        builder.append(Component.text("  ")); // Don't retain the format
                    }
                    
                    if (!song.isOkTempo()) {
                        builder.append(
                                Component.text("Illegal Tempo", NamedTextColor.YELLOW, TextDecoration.BOLD, TextDecoration.UNDERLINED)
                                         .hoverEvent(HoverEvent.showText(Component.text(
                                                 "The tempo of the song isn't divisible by 20 so it might sound faster due to tick limitations!",
                                                 NamedTextColor.GOLD
                                         )))
                        );
                    }
                    
                    message(builder);
                }
            }
            
            @Override
            public void onFinishedPlaying() {
                stopPlaying();
                queue.playNext();
                
                message("&aFinished playing &6%s&a.".formatted(song.getName()));
            }
            
            @Override
            public void onPause(boolean pause) {
                message("%s&a playing &6%s&a.".formatted(pause ? "&ePaused" : "&aResumed", song.getName()));
            }
        };
    }
    
}
