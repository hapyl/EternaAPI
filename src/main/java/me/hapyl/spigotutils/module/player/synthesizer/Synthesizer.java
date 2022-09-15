package me.hapyl.spigotutils.module.player.synthesizer;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.math.Numbers;
import me.hapyl.spigotutils.module.math.nn.IntInt;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Synthesizer {

    public static final Character EMPTY_NOTE = '-';

    static {
        final Synthesizer synthesizer = Synthesizer
                .singleTrack("a-a-a--DB--DB")
                .plingWhere('a', 2.0f)
                .plingWhere('D', 1.0f)
                .plingWhere('B', 0.5f)
                .toSynthesizer()
                .setBPT(2);
    }

    private final List<Track> tracks;
    private final Set<Player> listeners;
    private long bpt;
    private State state;

    public Synthesizer() {
        this.tracks = Lists.newArrayList();
        this.listeners = Sets.newHashSet();
        this.state = State.STANDBY;
        this.bpt = 1L;
    }

    public void play() {
        if (state == State.PLAYING) {
            return;
        }

        state = State.PLAYING;
        tracks.forEach(Track::mapTrack);

        final IntInt maxFrame = new IntInt();
        final List<Map<Long, Tune>> tunes = Lists.newArrayList();

        for (Track track : tracks) {
            final Map<Long, Tune> mapped = track.getMappedTrack();
            final int size = mapped.size();
            tunes.add(mapped);

            if (maxFrame.get() == 0 || size > maxFrame.get()) {
                maxFrame.set(size);
            }
        }

        new BukkitRunnable() {
            private long currentFrame;

            @Override
            public void run() {
                if (state == State.PAUSED) {
                    return;
                }

                if (state == State.STANDBY || (currentFrame > maxFrame.get())) {
                    state = State.STANDBY;
                    this.cancel();
                    return;
                }

                for (Map<Long, Tune> map : tunes) {
                    final Tune tune = map.get(currentFrame);
                    if (tune == null) {
                        continue;
                    }

                    tune.play(listeners);
                }

                currentFrame++;
            }
        }.runTaskTimer(EternaPlugin.getPlugin(), 0L, bpt);
    }

    /**
     * Plays tracks to a certain player.
     * <b>Note that this is done with a separate runnable and cannot be paused.<b/>
     *
     * @param player - Player to play to.
     */
    public void play(Player player) {
        tracks.forEach(Track::mapTrack);

        final IntInt maxFrame = new IntInt();
        final List<Map<Long, Tune>> tunes = Lists.newArrayList();

        for (Track track : tracks) {
            final Map<Long, Tune> mapped = track.getMappedTrack();
            final int size = mapped.size();
            tunes.add(mapped);

            if (maxFrame.get() == 0 || size > maxFrame.get()) {
                maxFrame.set(size);
            }
        }

        new BukkitRunnable() {
            private long currentFrame;

            @Override
            public void run() {
                if (currentFrame > maxFrame.get()) {
                    this.cancel();
                    return;
                }

                for (Map<Long, Tune> map : tunes) {
                    final Tune tune = map.get(currentFrame);
                    if (tune == null) {
                        continue;
                    }

                    tune.play(player);
                }

                currentFrame++;
            }
        }.runTaskTimer(EternaPlugin.getPlugin(), 0L, bpt);
    }

    public void stop() {
        state = State.STANDBY;
    }

    public void pause() {
        if (state != State.PLAYING) {
            return;
        }
        state = State.PAUSED;
    }

    public void unpause() {
        if (state != State.PAUSED) {
            return;
        }
        state = State.PLAYING;
    }

    public State getState() {
        return state;
    }

    public Set<Player> getListeners() {
        return listeners;
    }

    public boolean isListener(Player player) {
        return listeners.contains(player);
    }

    public Synthesizer addListener(Player player) {
        listeners.add(player);
        return this;
    }

    public Synthesizer setBPT(long bpt) {
        this.bpt = Numbers.clamp(bpt, 0, 1000);
        return this;
    }

    public long getBPT() {
        return bpt;
    }

    public Track addTrack(String str) {
        final Track track = new Track(this, str);
        tracks.add(track);
        return track;
    }

    // static members
    public static Track singleTrack(String track) {
        return new Synthesizer().addTrack(track);
    }
}
