package me.hapyl.spigotutils.registry;

import me.hapyl.spigotutils.EternaLogger;
import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.config.PlayerConfigRegistry;
import me.hapyl.spigotutils.module.entity.RopeRegistry;
import me.hapyl.spigotutils.module.hologram.HologramRegistry;
import me.hapyl.spigotutils.module.inventory.item.CustomItemRegistry;
import me.hapyl.spigotutils.module.parkour.ParkourRegistry;
import me.hapyl.spigotutils.module.player.song.SongPlayer;
import me.hapyl.spigotutils.module.player.song.SongRegistry;
import me.hapyl.spigotutils.module.reflect.glow.GlowingRegistry;
import me.hapyl.spigotutils.module.reflect.npc.HumanNPCRegistry;

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
