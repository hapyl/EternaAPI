package kz.hapyl.spigotutils;

import kz.hapyl.spigotutils.config.PlayerConfigManager;
import kz.hapyl.spigotutils.module.entity.RopeRegistry;
import kz.hapyl.spigotutils.module.hologram.HologramRegistry;
import kz.hapyl.spigotutils.module.inventory.item.CustomItemHolder;
import kz.hapyl.spigotutils.module.parkour.ParkourManager;
import kz.hapyl.spigotutils.module.player.song.SongPlayer;
import kz.hapyl.spigotutils.module.reflect.glow.GlowingManager;
import kz.hapyl.spigotutils.module.reflect.npc.HumanNPCRegistry;
import org.bukkit.Bukkit;

public final class EternaRegistry {

    private static boolean init;

    private final EternaPlugin plugin;

    // registries (should probably refactor for consistency)
    public final SongPlayer songPlayer;
    public final CustomItemHolder itemHolder;
    public final GlowingManager glowingManager;
    public final ParkourManager parkourManager;
    public final PlayerConfigManager configManager;
    public final HumanNPCRegistry npcRegistry;
    public final HologramRegistry hologramRegistry;
    public final RopeRegistry ropeRegistry;

    public EternaRegistry(EternaPlugin plugin) {
        if (init) {
            throw new IllegalStateException("registry already created!");
        }

        this.plugin = plugin;

        // register registries
        songPlayer = new SongPlayer(plugin);
        glowingManager = new GlowingManager(plugin);
        itemHolder = new CustomItemHolder(plugin);
        parkourManager = new ParkourManager(plugin);
        configManager = new PlayerConfigManager(plugin);
        npcRegistry = new HumanNPCRegistry(plugin);
        hologramRegistry = new HologramRegistry(plugin);
        ropeRegistry = new RopeRegistry(plugin);

        init = true;
    }

    public static RopeRegistry getRopeRegistry() {
        return current().ropeRegistry;
    }

    public static HologramRegistry getHologramRegistry() {
        return current().hologramRegistry;
    }

    public static HumanNPCRegistry getNpcRegistry() {
        return current().npcRegistry;
    }

    public static PlayerConfigManager getConfigManager() {
        return current().configManager;
    }

    public static SongPlayer getSongPlayer() {
        return current().songPlayer;
    }

    public static CustomItemHolder getItemHolder() {
        return current().itemHolder;
    }

    public static GlowingManager getGlowingManager() {
        return current().glowingManager;
    }

    public static ParkourManager getParkourManager() {
        return current().parkourManager;
    }

    //
    public static EternaRegistry current() {
        return EternaPlugin.getPlugin().getRegistry();
    }

    public EternaPlugin getPlugin() {
        return plugin;
    }

    public void onDisable() {
        runSafe(configManager::saveAllData, "config save");
        runSafe(parkourManager::restoreAllData, "parkour save");
        runSafe(npcRegistry::removeAll, "npc removal");
        runSafe(hologramRegistry::removeAll, "hologram removal");
        runSafe(ropeRegistry::removeAll, "rope removal");

        init = false;
    }

    private void runSafe(Runnable runnable, String name) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            Bukkit.getLogger().severe("Unable to run '%s'! Did you /reload your server? {%s}".formatted(name, throwable.getMessage()));
        }
    }
}
