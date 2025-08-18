package me.hapyl.eterna.builtin.manager;

import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.util.Disposable;

import javax.annotation.Nonnull;

public final class EternaManagers implements Disposable {

    public final ParkourManager parkour;
    public final HumanNPCManager npc;
    public final RopeManager rope;
    public final SongManager song;
    public final GlowingManager glowing;
    public final DialogManager dialog;
    public final QuestManager quest;

    private final EternaPlugin plugin;

    public EternaManagers(EternaPlugin eterna) {
        this.plugin = eterna;

        this.parkour = new ParkourManager(eterna);
        this.npc = new HumanNPCManager(eterna);
        this.rope = new RopeManager(eterna);
        this.song = new SongManager(eterna);
        this.glowing = new GlowingManager(eterna);
        this.dialog = new DialogManager(eterna);
        this.quest = new QuestManager(eterna);
    }

    @Nonnull
    public EternaPlugin getPlugin() {
        return plugin;
    }

    @Override
    public void dispose() {
        runSafe(parkour::dispose, "Restore Parkour");
        runSafe(npc::dispose, "Remove NPCs");
    }

    private void runSafe(Runnable runnable, String name) {
        try {
            runnable.run();
        } catch (Exception exception) {
            EternaLogger.severe("Cannot %s! Did you /reload your server? (%s)".formatted(name, exception.getClass().getSimpleName()));
            
            throw EternaLogger.exception(exception);
        }
    }

}
