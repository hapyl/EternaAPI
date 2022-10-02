package me.hapyl.spigotutils.module.player.synthesizer;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.inventory.item.Event;
import me.hapyl.spigotutils.module.math.Numbers;
import me.hapyl.spigotutils.module.math.nn.IntInt;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Synthesizer {

    public static final Character EMPTY_NOTE = '-';

    private final List<Track> tracks;
    private long bpt;

    public Synthesizer() {
        this.tracks = Lists.newArrayList();
        this.bpt = 1L;
    }

    /**
     * Plays tracks to players.
     */
    public void play(Player player, Player... other) {
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

        final Set<Player> players = combinePlayers(player, other);
        onStartPlaying(players);

        new BukkitRunnable() {
            private long currentFrame;

            @Override
            public void run() {
                if (currentFrame > maxFrame.get()) {
                    onStopPlaying(players);
                    this.cancel();
                    return;
                }

                for (Map<Long, Tune> map : tunes) {
                    final Tune tune = map.get(currentFrame);
                    if (tune == null) {
                        continue;
                    }

                    tune.play(player, other);
                }

                currentFrame += bpt;
            }
        }.runTaskTimer(EternaPlugin.getPlugin(), 0L, bpt);
    }

    @Event
    public void onStartPlaying(Set<Player> players) {
    }

    @Event
    public void onStopPlaying(Set<Player> players) {
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

    private Set<Player> combinePlayers(Player player, Player... other) {
        final Set<Player> set = Sets.newHashSet();
        set.add(player);
        set.addAll(Arrays.asList(other));
        return set;
    }
}
