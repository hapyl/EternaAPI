package me.hapyl.eterna.module.player.song;

import me.hapyl.eterna.EternaColors;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.module.component.Formatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

/**
 * Represents a {@link SongFormatter} used for {@link Song}.
 */
public interface SongFormatter extends Formatter {
    
    /**
     * Defines the default {@link SongFormatter}.
     */
    @NotNull
    SongFormatter DEFAULT = new SongFormatter() {
        
        @Override
        public void nowPlaying(@NotNull Player player, @NotNull Song song) {
            EternaLogger.nbs(
                    player,
                    Component.empty()
                             .append(Component.text("Now playing ", EternaColors.GREEN))
                             .append(song.getName().color(EternaColors.GOLD))
                             .append(Component.text(".", EternaColors.GREEN))
            );
        }
        
        @Override
        public void finishedPlaying(@NotNull Player player, @NotNull Song song) {
            EternaLogger.nbs(
                    player,
                    Component.empty()
                             .append(Component.text("Finished playing ", EternaColors.GREEN))
                             .append(song.getName().color(EternaColors.GOLD))
                             .append(Component.text(".", EternaColors.GREEN))
            );
        }
        
        @Override
        public void pausedPlaying(@NotNull Player player, @NotNull Song song, boolean isPaused) {
            EternaLogger.nbs(
                    player,
                    Component.empty()
                             .append(Component.text((isPaused ? "Paused" : "Resumed") + " playing ", EternaColors.GREEN))
                             .append(song.getName().color(EternaColors.GOLD))
                             .append(Component.text(".", EternaColors.GREEN))
            );
        }
        
        @Override
        public void songCannotBeAddedToQueueDuplicate(@NotNull Player player, @NonNull Song song) {
            EternaLogger.nbs(player, Component.text("This song is already in queue!", NamedTextColor.RED));
        }
        
        @Override
        public void songCannotBeRemovedFromQueueNotInQueue(@NotNull Player player, @NotNull Song song) {
            EternaLogger.nbs(player, Component.text("This song is not in queue!", NamedTextColor.RED));
        }
        
        @Override
        public void songAddedToQueue(@NotNull Player player, @NonNull Song song, int queueSize) {
            EternaLogger.nbs(
                    player,
                    Component.empty()
                             .append(Component.text("Added ", EternaColors.GREEN))
                             .append(song.getName().color(EternaColors.AMBER))
                             .append(Component.text(" to queue!", EternaColors.GREEN))
                             .append(queueSize == 0 ? Component.empty() : Component.text(" (%s)".formatted(queueSize), NamedTextColor.DARK_GRAY))
            );
        }
        
        @Override
        public void songRemovedFromQueue(@NotNull Player player, @NotNull Song song) {
            EternaLogger.nbs(
                    player,
                    Component.empty()
                             .append(Component.text("Removed ", EternaColors.GREEN))
                             .append(song.getName().color(EternaColors.AMBER))
                             .append(Component.text(" to queue!", EternaColors.GREEN))
            );
        }
    };
    
    @Override
    @NotNull
    default Component defineFormatter() {
        return Component.text("song");
    }
    
    /**
     * Sends a message for when a {@link Song} has started to play.
     *
     * @param player - The player for whom the song has started to play.
     * @param song   - The song that started to play.
     */
    void nowPlaying(@NotNull Player player, @NotNull Song song);
    
    /**
     * Sends a message for when a {@link Song} has finished playing.
     *
     * @param player - The player for whom the song has finished playing.
     * @param song   - The song that finished playing.
     */
    void finishedPlaying(@NotNull Player player, @NotNull Song song);
    
    /**
     * Sends a message for when a {@link Song} is paused/resumed.
     *
     * @param player   - The player for whom the song was paused/resumed.
     * @param song     - The song that was paused/resumed.
     * @param isPaused - {@code true} if the song is now paused; {@code false} otherwise.
     */
    void pausedPlaying(@NotNull Player player, @NotNull Song song, final boolean isPaused);
    
    /**
     * Sends a message for when a {@link Song} is attempted to be added to a {@link SongQueue} but is already in that queue.
     *
     * @param player - The player for whom the song was attempted to be added to a queue.
     * @param song   - The song that was attempted to be added to a queue.
     */
    void songCannotBeAddedToQueueDuplicate(@NotNull Player player, @NonNull Song song);
    
    /**
     * Sends a message for when a {@link Song} is attempted to be removed from a {@link SongQueue} but is not in that queue.
     *
     * @param player - The player for whom the song was attempted to be removed from a queue.
     * @param song   - The song that was attempted to be removed from a queue.
     */
    void songCannotBeRemovedFromQueueNotInQueue(@NotNull Player player, @NotNull Song song);
    
    /**
     * Sends a message for when a {@link Song} is added to a {@link SongQueue}.
     *
     * @param player    - The player for whom the song was added to a queue.
     * @param song      - The song that was added to a queue.
     * @param queueSize - The current queue size, excluding the song that was just added. (or {@code size - 1})
     */
    void songAddedToQueue(@NotNull Player player, @NonNull Song song, final int queueSize);
    
    /**
     * Sends a message for when a {@link Song} is removed from a {@link SongQueue}.
     *
     * @param player - The player for whom the song was removed from a queue.
     * @param song   - The song that was removed from a queue.
     */
    void songRemovedFromQueue(@NotNull Player player, @NotNull Song song);
}
