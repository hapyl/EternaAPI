package me.hapyl.eterna;

import me.hapyl.eterna.builtin.command.EternaCommand;
import me.hapyl.eterna.builtin.command.NoteBlockStudioCommand;
import me.hapyl.eterna.builtin.event.PlayerConfigEvent;
import me.hapyl.eterna.builtin.updater.Updater;
import me.hapyl.eterna.module.command.CommandProcessor;
import me.hapyl.eterna.module.hologram.HologramListener;
import me.hapyl.eterna.module.hologram.HologramRunnable;
import me.hapyl.eterna.module.inventory.ItemBuilderListener;
import me.hapyl.eterna.module.inventory.SignListener;
import me.hapyl.eterna.module.inventory.gui.GUIListener;
import me.hapyl.eterna.module.inventory.item.CustomItemListener;
import me.hapyl.eterna.module.locaiton.TriggerManager;
import me.hapyl.eterna.module.parkour.ParkourListener;
import me.hapyl.eterna.module.parkour.ParkourRunnable;
import me.hapyl.eterna.module.quest.QuestListener;
import me.hapyl.eterna.module.record.ReplayListener;
import me.hapyl.eterna.module.reflect.NPCRunnable;
import me.hapyl.eterna.module.reflect.glow.GlowingProtocolEntitySpawnListener;
import me.hapyl.eterna.module.reflect.glow.GlowingProtocolMetadataListener;
import me.hapyl.eterna.module.reflect.glow.GlowingRunnable;
import me.hapyl.eterna.module.reflect.npc.HumanNPCListener;
import me.hapyl.eterna.module.util.Runnables;
import me.hapyl.eterna.protocol.EternaProtocol;
import me.hapyl.eterna.registry.EternaRegistry;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import javax.annotation.Nonnull;
import java.io.File;

/**
 * Represents EternaAPI plugin.
 */
public class EternaPlugin extends JavaPlugin {

    private static EternaPlugin plugin;

    protected EternaRegistry registry;
    protected Updater updater;
    protected EternaConfig config;
    protected EternaProtocol protocol;

    @Override
    public void onEnable() {
        plugin = Eterna.plugin = this;

        // Check if the server is running paper
        validateUsingPaper();

        // Init protocol
        this.protocol = new EternaProtocol(this);

        // Init registry
        this.registry = new EternaRegistry(this);

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
        manager.registerEvents(new SignListener(), this);
        manager.registerEvents(new HumanNPCListener(), this);
        manager.registerEvents(new GlowingProtocolMetadataListener(), this);
        manager.registerEvents(new GlowingProtocolEntitySpawnListener(), this);

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
        Runnables.runLater(() -> Bukkit.getOnlinePlayers().forEach(player -> registry.configRegistry.getConfig(player).loadAll()), 10L);

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

    /**
     * Gets the {@link EternaPlugin}.
     *
     * @return the plugin.
     */
    @Nonnull
    public static EternaPlugin getPlugin() {
        return plugin;
    }

    private void validateUsingPaper() {
        try {
            Class.forName("io.papermc.paper.ServerBuildInfo"); // this paper class is in root folder, let's hope they won't move it :]
        } catch (Exception ignored) {
            EternaLogger.severe("");
            EternaLogger.severe("** No Paper detected! Disabling...");
            EternaLogger.severe("** EternaAPI does NOT work on Spigot, upgrade to Paper!");
            EternaLogger.severe("** https://papermc.io/downloads/paper");
            EternaLogger.severe("");

            Bukkit.getPluginManager().disablePlugins();
        }
    }

}
