package me.hapyl.spigotutils.module.record;

import com.google.common.collect.Sets;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import me.hapyl.spigotutils.module.util.EternaRunnable;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * This class is used to play recorded replay of a player.
 */
public abstract class Replay extends EternaRunnable {

    protected final Record record;
    protected final Set<Player> viewers;

    private HumanNPC npc;
    private ReplayStatus status;
    private int currentFrame;

    public Replay(@Nonnull Record record) {
        super(EternaPlugin.getPlugin());

        this.record = record;
        this.viewers = Sets.newHashSet();
        this.status = null;
    }

    /**
     * Called whenever this {@link Replay} starts.
     */
    public abstract void onStart();

    /**
     * Called whenever this {@link Replay} ends.
     */
    public abstract void onStop();

    /**
     * Called whenever this {@link Replay} is paused/unpause.
     *
     * @param pause - Is now paused.
     */
    public abstract void onPause(boolean pause);

    /**
     * Called every frame.
     *
     * @param data  - Current data.
     * @param frame - Current frame.
     */
    public abstract void onStep(@Nonnull ReplayData data, long frame);

    /**
     * Pauses current replay if it's playing, freezing the {@link HumanNPC} at the current frame.
     * <br>
     * If the {@link Replay} {@link #isPaused()}, it will be resumed. This has no effect if the {@link Replay} {@link #isFinished()}.
     */
    public void pause() {
        if (status == ReplayStatus.FINISHED) {
            return;
        }

        status = (status == ReplayStatus.PAUSED) ? ReplayStatus.PLAYING : ReplayStatus.PAUSED;
        onPause(status == ReplayStatus.PAUSED);
    }

    public boolean isPlaying() {
        return status == ReplayStatus.PLAYING;
    }

    public boolean isPaused() {
        return status == ReplayStatus.PAUSED;
    }

    public boolean isFinished() {
        return status == ReplayStatus.FINISHED;
    }

    @Override
    public void run() {
        if (currentFrame >= record.getFrames()) {
            stop();
            return;
        }

        if (isPaused()) {
            return;
        }

        final ReplayData data = record.getData(currentFrame++);

        data.use(npc);
        onStep(data, currentFrame - 1);
    }

    public boolean hasStarted() {
        return npc != null;
    }

    @CheckForNull
    public HumanNPC getNpc() {
        return npc;
    }

    public void start() {
        start0();
    }

    public void start(@Nonnull Player player) {
        start(List.of(player));
    }

    public void start(@Nonnull Collection<Player> players) {
        viewers.addAll(players);

        start0();
    }

    void start0() {
        if (hasStarted()) {
            return;
        }

        final Player player = record.getPlayer();

        npc = new HumanNPC(record.getDataAtFirstFrame().getLocation(), getNpcName(), player.getName());

        if (viewers.isEmpty()) {
            npc.showAll();
        }
        else {
            viewers.forEach(npc::show);
        }

        status = ReplayStatus.PLAYING;
        onStart();

        runTaskTimer(0, 1);
    }

    /**
     * Stops replay and removes the NPC if it was playing.
     */
    public void stop() {
        if (isCancelled()) {
            return;
        }

        status = ReplayStatus.FINISHED;
        onStop();

        cancel();
        npc.remove();
    }

    @Nullable
    protected String getNpcName() {
        final Player player = record.getPlayer();

        return "[R] " + player.getName();
    }

}
