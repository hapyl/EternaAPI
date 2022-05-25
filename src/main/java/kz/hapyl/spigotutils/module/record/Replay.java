package kz.hapyl.spigotutils.module.record;

import kz.hapyl.spigotutils.EternaPlugin;
import kz.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import kz.hapyl.spigotutils.module.util.ContainsViewers;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * This class is used to play recorded replay of a player.
 */
public class Replay extends ContainsViewers<Player> {

    private final Record replay;

    private HumanNPC npc;

    private boolean statusPlaying;
    private boolean statusPaused;
    private boolean global;

    public Replay(Record replay) {
        this.replay = replay;
    }

    /**
     * Makes the NPC global for players at the time of
     * calling {@link this#start()}.
     *
     * @param global - global.
     */
    public void setGlobalReplay(boolean global) {
        this.global = global;
    }

    /**
     * Pauses current replay if it's playing.
     *
     * - NPC will be frozen at the last frame
     * is replay is paused.
     *
     * - Replay will have no effect if it's not playing,
     * but the NPC will not move after calling {@link this#start()}.
     */
    public void pause() {
        statusPaused = !statusPaused;
    }

    /**
     * Returns true if replay is currently paused, false otherwise.
     *
     * @return true if replay is currently paused, false otherwise.
     */
    public boolean isPaused() {
        return statusPaused;
    }

    /**
     * Returns true is replay is currently playing, false otherwise.
     *
     * @return true is replay is currently playing, false otherwise.
     */
    public boolean isPlaying() {
        return statusPlaying;
    }

    /**
     * Stops replay and removes the NPC if it was playing.
     */
    public void stop() {
        statusPlaying = false;
    }

    /**
     * Stars replay.
     *
     * Summons NPC and iterates through frames.
     */
    public void start() {
        final Player player = replay.getPlayer();

        if (getViewers().isEmpty()) {
            setGlobalReplay(true);
        }

        if (npc != null) {
            npc.remove();
            npc = null;
        }

        statusPlaying = true;
        npc = new HumanNPC(replay.getDataAtFirstFrame().getLocation(), "[R] " + player.getName(), player.getName());

        // show npc
        if (global) {
            npc.showAll();
        }
        else {
            forEachViewer(npc::show);
        }

        new BukkitRunnable() {
            private long frame = 0;

            @Override
            public void run() {
                if (!statusPlaying || frame >= replay.getFrames()) {
                    if (npc != null) {
                        npc.remove();
                        npc = null;
                    }
                    statusPlaying = false;
                    this.cancel();
                    return;
                }

                if (isPaused()) {
                    return;
                }

                replay.getData(frame).applyToNpc(npc);
                frame++;
            }
        }.runTaskTimer(EternaPlugin.getPlugin(), 0L, 1L);
    }


}
