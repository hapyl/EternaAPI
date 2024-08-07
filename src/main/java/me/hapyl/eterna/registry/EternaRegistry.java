package me.hapyl.eterna.registry;

import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.config.PlayerConfigRegistry;
import me.hapyl.eterna.module.entity.RopeRegistry;
import me.hapyl.eterna.module.hologram.HologramRegistry;
import me.hapyl.eterna.module.inventory.item.CustomItemRegistry;
import me.hapyl.eterna.module.parkour.ParkourRegistry;
import me.hapyl.eterna.module.player.song.SongPlayer;
import me.hapyl.eterna.module.player.song.SongRegistry;
import me.hapyl.eterna.module.reflect.glow.GlowingRegistry;
import me.hapyl.eterna.module.reflect.npc.HumanNPCRegistry;

import javax.annotation.Nonnull;

/**
 * Registry of registries. 😕
 */
public final class EternaRegistry {

    private static boolean init;

    private final EternaPlugin plugin;

    /**
     * Do not use this, I will move it later.
     */
    public final SongPlayer songPlayer;

    public final CustomItemRegistry itemRegistry;
    public final GlowingRegistry glowingRegistry;
    public final ParkourRegistry parkourRegistry;
    public final PlayerConfigRegistry configRegistry;
    public final HumanNPCRegistry npcRegistry;
    public final HologramRegistry hologramRegistry;
    public final RopeRegistry ropeRegistry;
    public final SongRegistry songRegistry;

    public EternaRegistry(EternaPlugin plugin) {
        if (init) {
            throw new IllegalStateException("registry already created!");
        }

        this.plugin = plugin;

        // register registries
        songPlayer = new SongPlayer(plugin);
        glowingRegistry = new GlowingRegistry(plugin);
        itemRegistry = new CustomItemRegistry(plugin);
        parkourRegistry = new ParkourRegistry(plugin);
        configRegistry = new PlayerConfigRegistry(plugin);
        npcRegistry = new HumanNPCRegistry(plugin);
        hologramRegistry = new HologramRegistry(plugin);
        ropeRegistry = new RopeRegistry(plugin);
        songRegistry = new SongRegistry(plugin);

        init = true;
    }

    @Nonnull
    public EternaPlugin getPlugin() {
        return plugin;
    }

    public void onDisable() {
        runSafe(configRegistry::saveAllData, "Save Config");
        runSafe(parkourRegistry::restoreAllData, "Restore Parkour");
        runSafe(npcRegistry::removeAll, "Remove NPCs");
        runSafe(hologramRegistry::removeAll, "Remove Holograms");
        //runSafe(ropeRegistry::removeAll, "Rope Removal (Some bats might be alive.)");

        init = false;
    }

    private void runSafe(Runnable runnable, String name) {
        try {
            runnable.run();
        } catch (Exception exception) {
            EternaLogger.severe("Cannot %s! Did you /reload your server? (%s)".formatted(name, exception.getClass().getSimpleName()));
            EternaLogger.exception(exception);
        }
    }

}
