package me.hapyl.spigotutils.registry;

import me.hapyl.spigotutils.EternaPlugin;
import me.hapyl.spigotutils.config.PlayerConfigManager;
import me.hapyl.spigotutils.module.entity.RopeRegistry;
import me.hapyl.spigotutils.module.hologram.HologramRegistry;
import me.hapyl.spigotutils.module.inventory.item.CustomItemRegistry;
import me.hapyl.spigotutils.module.parkour.ParkourManager;
import me.hapyl.spigotutils.module.player.song.SongPlayer;
import me.hapyl.spigotutils.module.player.song.SongRegistry;
import me.hapyl.spigotutils.module.reflect.glow.GlowingRegistry;
import me.hapyl.spigotutils.module.reflect.npc.HumanNPCRegistry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Registry of registries. 😕
 */
public final class EternaRegistry {

    private static boolean init;

    private final EternaPlugin plugin;

    public final SongPlayer songPlayer; // fixme -> this is not even a registry, why is this here

    public final CustomItemRegistry itemHolder;
    public final GlowingRegistry glowingManager;
    public final ParkourManager parkourManager;
    public final PlayerConfigManager configManager;
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
        glowingManager = new GlowingRegistry(plugin);
        itemHolder = new CustomItemRegistry(plugin);
        parkourManager = new ParkourManager(plugin);
        configManager = new PlayerConfigManager(plugin);
        npcRegistry = new HumanNPCRegistry(plugin);
        hologramRegistry = new HologramRegistry(plugin);
        ropeRegistry = new RopeRegistry(plugin);
        songRegistry = new SongRegistry(plugin);

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

    public static SongPlayer getNewSongPlayer(JavaPlugin plugin) {
        return new SongPlayer(plugin);
    }

    public static CustomItemRegistry getItemHolder() {
        return current().itemHolder;
    }

    public static GlowingRegistry getGlowingManager() {
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
        runSafe(configManager::saveAllData, "Config Save (Some data did NOT save!)");
        runSafe(parkourManager::restoreAllData, "Parkour Save (Some entities might be alive.)");
        runSafe(npcRegistry::removeAll, "NPC Removal (Some NPC's might be alive.)");
        runSafe(hologramRegistry::removeAll, "Hologram Removal (Some Armor Stands might be visible.)");
        //runSafe(ropeRegistry::removeAll, "Rope Removal (Some bats might be alive.)");

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
