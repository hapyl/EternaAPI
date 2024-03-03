package me.hapyl.spigotutils;

import me.hapyl.spigotutils.builtin.command.EternaCommand;
import me.hapyl.spigotutils.builtin.command.NoteBlockStudioCommand;
import me.hapyl.spigotutils.builtin.event.PlayerConfigEvent;
import me.hapyl.spigotutils.builtin.updater.Updater;
import me.hapyl.spigotutils.module.command.CommandProcessor;
import me.hapyl.spigotutils.module.hologram.HologramListener;
import me.hapyl.spigotutils.module.hologram.HologramRunnable;
import me.hapyl.spigotutils.module.inventory.ItemBuilderListener;
import me.hapyl.spigotutils.module.inventory.gui.GUIListener;
import me.hapyl.spigotutils.module.inventory.item.CustomItemListener;
import me.hapyl.spigotutils.module.inventory.item.CustomItemRegistry;
import me.hapyl.spigotutils.module.locaiton.TriggerManager;
import me.hapyl.spigotutils.module.parkour.ParkourListener;
import me.hapyl.spigotutils.module.parkour.ParkourManager;
import me.hapyl.spigotutils.module.parkour.ParkourRunnable;
import me.hapyl.spigotutils.module.player.song.SongPlayer;
import me.hapyl.spigotutils.module.quest.QuestListener;
import me.hapyl.spigotutils.module.record.ReplayListener;
import me.hapyl.spigotutils.module.reflect.NPCRunnable;
import me.hapyl.spigotutils.module.reflect.glow.GlowingProtocolEntitySpawn;
import me.hapyl.spigotutils.module.reflect.glow.GlowingProtocolMetadata;
import me.hapyl.spigotutils.module.reflect.glow.GlowingRegistry;
import me.hapyl.spigotutils.module.reflect.glow.GlowingRunnable;
import me.hapyl.spigotutils.module.reflect.protocol.HumanNPCListener;
import me.hapyl.spigotutils.module.reflect.protocol.SignListener;
import me.hapyl.spigotutils.module.util.Runnables;
import me.hapyl.spigotutils.registry.EternaRegistry;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.logging.Logger;

/**
 * Represents EternaAPI plugin.
 */
public class EternaPlugin extends JavaPlugin {

    private static EternaPlugin plugin;

    private EternaRegistry registry;
    private Updater updater;
    private EternaConfig config;

    @Override
    public void onEnable() {
        plugin = this;

        final PluginManager manager = this.getServer().getPluginManager();

        manager.registerEvents(new ItemBuilderListener(), this);
        manager.registerEvents(new CustomItemListener(), this);
        manager.registerEvents(new GUIListener(), this);
        manager.registerEvents(new TriggerManager(), this);
        manager.registerEvents(new QuestListener(), this);
        manager.registerEvents(new ParkourListener(), this);
        manager.registerEvents(new PlayerConfigEvent(), this);
        manager.registerEvents(new ReplayListener(), this);
        manager.registerEvents(new HologramListener(), this);

        // Init registry
        this.registry = new EternaRegistry(this);

        final BukkitScheduler scheduler = Bukkit.getScheduler();

        // Load config
        final FileConfiguration config = getConfig();

        config.options().copyDefaults(true);
        saveConfig();

        this.config = new EternaConfig(this);

        // Schedule tasks
        scheduler.runTaskTimer(this, new NPCRunnable(), 0, config.getInt("tick-time.npc", 2));
        scheduler.runTaskTimer(this, new HologramRunnable(), 0, config.getInt("tick-time.hologram", 2));
        scheduler.runTaskTimer(this, new ParkourRunnable(), 0, config.getInt("tick-time.parkour", 2));
        scheduler.runTaskTimer(this, new GlowingRunnable(), 0, config.getInt("tick-time.glowing", 1));

        // Load ProtocolLib listeners
        new SignListener();
        new GlowingProtocolMetadata();
        new GlowingProtocolEntitySpawn();
        new HumanNPCListener();

        // Load built-in commands
        final CommandProcessor commandProcessor = new CommandProcessor(this);
        commandProcessor.registerCommand(new EternaCommand("eterna"));
        commandProcessor.registerCommand(new NoteBlockStudioCommand("nbs"));

        // Create songs folder
        try {
            new File(this.getDataFolder() + "/songs").mkdir();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Load PlayerConfig for online players
        Runnables.runLater(() -> Bukkit.getOnlinePlayers().forEach(player -> registry.configManager.getConfig(player).loadAll()), 10L);

        // Load dependencies
        EternaAPI.loadAll();

        // Check for updates
        this.updater = new Updater();

        if (this.config.isTrue(EternaConfigValue.CHECK_FOR_UPDATES)) {
            Runnables.runLaterAsync(() -> this.updater.checkForUpdatesAndGiveLink(), 20L);
        }
    }

    @Override
    public void onDisable() {
        registry.onDisable();
    }

    public EternaRegistry getRegistry() {
        return registry;
    }

    public CustomItemRegistry getItemHolder() {
        return registry.itemHolder;
    }

    public SongPlayer getSongPlayer() {
        return registry.songPlayer;
    }

    public ParkourManager getParkourManager() {
        return registry.parkourManager;
    }

    public GlowingRegistry getGlowingManager() {
        return registry.glowingManager;
    }

    public Updater getUpdater() {
        return updater;
    }

    /**
     * Gets the {@link EternaConfig}.
     *
     * @return the api config.
     */
    @Nonnull
    public static EternaConfig config() {
        return plugin.config;
    }

    /**
     * Gets the {@link EternaPlugin}.
     *
     * @return the plugin.
     */
    @Nonnull
    public static EternaPlugin getPlugin() {
        return plugin;
    }

    /**
     * Gets the {@link Logger}.
     *
     * @return the logger.
     */
    @Nonnull
    public static Logger logger() {
        return getPlugin().getLogger();
    }

}
