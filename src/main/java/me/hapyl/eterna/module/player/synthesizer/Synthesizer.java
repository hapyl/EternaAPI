package me.hapyl.eterna.module.player.synthesizer;

import com.google.common.collect.Lists;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.inventory.item.Event;
import me.hapyl.eterna.module.math.Numbers;
import me.hapyl.eterna.module.math.nn.IntInt;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Represents a synthesizer, or a note block builder that allows
 * to create songs using a string.
 */
public class Synthesizer {

    /**
     * Represents a character that is an empty note that will be skipped.
     */
    public static final Character EMPTY_NOTE = '-';

    private final List<Track> tracks;
    private long bpt;

    /**
     * Creates a new {@link Synthesizer}.
     */
    public Synthesizer() {
        this.tracks = Lists.newArrayList();
        this.bpt = 1L;
    }

    /**
     * Plays this {@link Synthesizer} to the given players at the given location.
     *
     * @param players  - Listeners.
     * @param location - Location.
     */
    public void play(@Nonnull Collection<Player> players, @Nonnull Location location) {
        tracks.forEach(Track::mapTrack);

        final IntInt maxFrame = new IntInt();
        final List<Map<Long, Tune>> tunes = Lists.newArrayList();

        for (Track track : tracks) {
            final Map<Long, Tune> mapped = track.getMappedTrack();
            tunes.add(mapped);

            final int i = mapped.keySet().stream().max(Long::compareTo).orElse(0L).intValue();
            if (maxFrame.get() == 0 || i > maxFrame.get()) {
                maxFrame.set(i);
            }
        }

        onStartPlaying(players);

        new BukkitRunnable() {
            private long currentFrame;

            @Override
            public void run() {
                if (currentFrame > maxFrame.get()) {
                    onStopPlaying(players);
                    cancel();
                    return;
                }

                for (Map<Long, Tune> map : tunes) {
                    final Tune tune = map.get(currentFrame);
                    if (tune == null) {
                        continue;
                    }

                    for (Player player : players) {
                        tune.play(player, location);
                    }
                }

                currentFrame += bpt;
            }
        }.runTaskTimer(EternaPlugin.getPlugin(), 0L, bpt);
    }

    /**
     * Plays this {@link Synthesizer} to the given players at their current location.
     *
     * @param players - Listeners.
     */
    public void play(@Nonnull Collection<Player> players) {
        players.forEach(this::play);
    }

    /**
     * Plays this {@link Synthesizer} to the given player at the given location.
     *
     * @param player   - Listener.
     * @param location - Location.
     */
    public void play(@Nonnull Player player, @Nonnull Location location) {
        play(Collections.singleton(player), location);
    }

    /**
     * Plays this {@link Synthesizer} to the given player at their locating.
     *
     * @param player - Listener.
     */
    public void play(@Nonnull Player player) {
        play(player, player.getLocation());
    }

    /**
     * Called once before the {@link Synthesizer} stars playing.
     *
     * @param players - Collection of listeners.
     */
    @Event
    public void onStartPlaying(@Nonnull Collection<Player> players) {
    }

    /**
     * Called once after the {@link Synthesizer} stopped playing.
     *
     * @param players - Collection of listeners.
     */
    @Event
    public void onStopPlaying(@Nonnull Collection<Player> players) {
    }

    /**
     * Gets this {@link Synthesizer}'s {@link Track} list.
     *
     * @return this synthesizer's track list.
     */
    @Nonnull
    public List<Track> getTracks() {
        return tracks;
    }

    /**
     * Gets the BPT (beats per tick) for this {@link Synthesizer}.
     *
     * @return the BPT for this synthesizer.
     */
    public long getBPT() {
        return bpt;
    }

    /**
     * Sets the BPT (beats per tick) for this {@link Synthesizer}.
     *
     * @param bpt - new bpt.
     */
    public Synthesizer setBPT(long bpt) {
        this.bpt = Numbers.clamp(bpt, 0, 1000);
        return this;
    }

    /**
     * Adds a track for {@link Synthesizer}.
     *
     * @param str - String.
     * @return a newly added track.
     */
    public Track addTrack(@Nonnull String str) {
        final Track track = new Track(this, str);
        tracks.add(track);
        return track;
    }

    /**
     * Creates a new {@link Synthesizer} and adds a new {@link Track}.
     *
     * @param track - Track.
     * @return a new {@link Track}.
     */
    @Nonnull
    public static Track singleTrack(@Nonnull String track) {
        return new Synthesizer().addTrack(track);
    }
}
