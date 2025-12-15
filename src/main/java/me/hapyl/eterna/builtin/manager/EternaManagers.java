package me.hapyl.eterna.builtin.manager;

import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaLock;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.util.Disposable;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;

@ApiStatus.Internal
public final class EternaManagers extends EternaLock implements Disposable {

    public final ParkourManager parkour;
    public final RopeManager rope;
    public final SongManager song;
    public final GlowingManager glowing;
    public final DialogManager dialog;
    public final QuestManager quest;
    public final NpcManager npc;

    private final EternaPlugin plugin;

    public EternaManagers(@Nonnull EternaKey key, @Nonnull EternaPlugin eterna) {
        super(key);
        
        this.plugin = eterna;

        this.parkour = new ParkourManager(eterna);
        this.rope = new RopeManager(eterna);
        this.song = new SongManager(eterna);
        this.glowing = new GlowingManager(eterna);
        this.dialog = new DialogManager(eterna);
        this.quest = new QuestManager(eterna);
        this.npc = new NpcManager(eterna);
    }

    @Nonnull
    public EternaPlugin getPlugin() {
        return plugin;
    }

    @Override
    public void dispose() {
        runSafe(parkour::dispose, "restore parkour");
        runSafe(npc::dispose, "remove NPCs");
    }

    private void runSafe(Runnable runnable, String name) {
        try {
            runnable.run();
        } catch (Exception exception) {
            EternaLogger.severe("Cannot %s! Did you /reload your server? (%s)".formatted(name, exception.getClass().getSimpleName()));
        }
    }

}
