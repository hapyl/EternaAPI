package me.hapyl.eterna.module.player.sound;

import com.google.common.collect.Lists;
import io.netty.channel.epoll.EpollTcpInfo;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.player.PlayerLib;
import me.hapyl.eterna.module.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Allows creating a 'track' of a sound and play it whenever.
 */
public class SoundQueue {

    protected final LinkedList<SoundQueueSound> sounds;

    public SoundQueue() {
        this.sounds = Lists.newLinkedList();
    }

    /**
     * Appends the given {@link SoundQueueSound} to the end of the list.
     *
     * @param sound - Sound to append.
     */
    public SoundQueue append(@Nonnull SoundQueueSound sound) {
        this.sounds.addLast(sound);
        return this;
    }

    /**
     * Appends the given {@link Sound} with the given pitch and delay to the end of this list.
     *
     * @param sound - Sound to append.
     * @param pitch - Pitch of the sound.
     * @param delay - Delay before playing the sound.
     * @implNote <pre>{@code
     * new SoundQueue()
     *      .append(Sound.ENTITY_GENERIC_DRINK, 0.0f, 1)
     *      .append(Sound.ENTITY_GENERIC_DRINK, 0.5f, 1)
     *      .append(Sound.ENTITY_GENERIC_DRINK, 1.0f, 1);
     * }</pre>
     * Will play the {@code Sound.ENTITY_GENERIC_DRINK} sound with {@code 1.0f} pitch at the third tick.
     */
    public SoundQueue append(@Nonnull Sound sound, float pitch, int delay) {
        return this.append(new SoundQueueSound(sound, pitch, delay));
    }

    static {
        new SoundQueue()
                .appendSameSound(Sound.ENTITY_GENERIC_DRINK, 1.0f, 2, 2, 2);
    }

    /**
     * Appends the given {@link Sound} with the given pitch at the given delays.
     *
     * @param sound  - Sound to append.
     * @param pitch  - Pitch of the sound.
     * @param delays - Delays before playing each sound.
     * @implNote <pre>{@code
     * new SoundQueue()
     *      .appendSameSound(Sound.ENTITY_GENERIC_DRINK, 1.0f, 2, 2, 2);
     * }</pre>
     * Will play the {@code Sound.ENTITY_GENERIC_DRINK} sound at second, fourth and sixth ticks.
     */
    public SoundQueue appendSameSound(@Nonnull Sound sound, float pitch, int... delays) {
        Validate.isTrue(delays.length != 0, "no delays provided");

        for (final int delay : delays) {
            this.append(new SoundQueueSound(sound, pitch, delay));
        }

        return this;
    }

    /**
     * Appends the given {@link Sound} with the given delay at the given pitches.
     *
     * @param sound   - Sound to append.
     * @param delay   - Delay before playing each sound.
     * @param pitches - Pitches of each sound.
     * @implNote <pre>{@code
     * new SoundQueue()
     *      .appendSameSound(Sound.ENTITY_GENERIC_DRINK, 2, 0.5f, 0.75f, 1.0f);
     * }</pre>
     * Will play the {@code Sound.ENTITY_GENERIC_DRINK} sound at second,
     * fourth and sixth ticks with 0.5f, 0.75f and 1.0f pitch respectively.
     */
    public SoundQueue appendSameSound(@Nonnull Sound sound, int delay, float... pitches) {
        Validate.isTrue(pitches.length != 0, "No pitches provided");

        for (float pitch : pitches) {
            append(new SoundQueueSound(sound, pitch, delay));
        }

        return this;
    }

    @Nonnull
    public SoundQueueInstance play(@Nonnull Player player) {
        return play(List.of(player));
    }

    @Nonnull
    public SoundQueueInstance play(@Nonnull Collection<Player> players) {
        return new SoundQueueInstance(this, players);
    }

}
