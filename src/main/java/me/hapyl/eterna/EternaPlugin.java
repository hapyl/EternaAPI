package me.hapyl.eterna;

import me.hapyl.eterna.builtin.command.EternaCommand;
import me.hapyl.eterna.builtin.command.NoteBlockStudioCommand;
import me.hapyl.eterna.builtin.command.SelectDialogOptionCommand;
import me.hapyl.eterna.builtin.manager.EternaManagers;
import me.hapyl.eterna.builtin.updater.Updater;
import me.hapyl.eterna.module.command.CommandProcessor;
import me.hapyl.eterna.module.event.PlayerMoveOneBlockEvent;
import me.hapyl.eterna.module.inventory.ItemBuilderListener;
import me.hapyl.eterna.module.inventory.SignListener;
import me.hapyl.eterna.module.inventory.gui.GUIListener;
import me.hapyl.eterna.module.npc.NpcRunnable;
import me.hapyl.eterna.module.parkour.ParkourListener;
import me.hapyl.eterna.module.parkour.ParkourRunnable;
import me.hapyl.eterna.module.reflect.glowing.GlowingProtocolEntitySpawnListener;
import me.hapyl.eterna.module.reflect.glowing.GlowingProtocolMetadataListener;
import me.hapyl.eterna.module.reflect.glowing.GlowingRunnable;
import me.hapyl.eterna.module.npc.NpcListener;
import me.hapyl.eterna.module.util.Runnables;
import me.hapyl.eterna.protocol.EternaProtocol;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import javax.annotation.Nonnull;
import java.io.File;

/**
 * Represents EternaAPI plugin.
 */
public /*final*/ class EternaPlugin extends JavaPlugin {
    
    private static EternaPlugin plugin;
    
    protected EternaManagers managers;
    protected Updater updater;
    protected EternaConfig config;
    protected EternaProtocol protocol;
    
    @Override
    public void onEnable() {
        plugin = Eterna.plugin = this;
        
        // Create soleKey
        final EternaKey soleKey = EternaKey.createSoleKey();
        
        // Check if the server is running paper
        validateUsingPaper();
        
        // Init protocol
        this.protocol = new EternaProtocol(soleKey, this);
        
        // Init managers
        this.managers = new EternaManagers(soleKey, this);
        
        final PluginManager manager = this.getServer().getPluginManager();
        
        manager.registerEvents(new ItemBuilderListener(soleKey), this);
        manager.registerEvents(new GUIListener(soleKey), this);
        manager.registerEvents(new ParkourListener(soleKey), this);
        manager.registerEvents(new SignListener(soleKey), this);
        manager.registerEvents(new NpcListener(soleKey), this);
        manager.registerEvents(new GlowingProtocolMetadataListener(soleKey), this);
        manager.registerEvents(new GlowingProtocolEntitySpawnListener(soleKey), this);
        manager.registerEvents(new PlayerMoveOneBlockEvent.Handler(soleKey), this);
        
        final BukkitScheduler scheduler = Bukkit.getScheduler();
        
        // Load config
        getConfig().options().copyDefaults(true);
        saveConfig();
        
        this.config = new EternaConfigImpl(soleKey, this);
        
        // Schedule tasks
        scheduler.runTaskTimer(this, new NpcRunnable(soleKey), 0, config.tickRate().npc());
        scheduler.runTaskTimer(this, new ParkourRunnable.Actionbar(soleKey), 0, config.tickRate().parkour());
        
        // The following tasks have hardcoded periods
        scheduler.runTaskTimer(this, new GlowingRunnable(soleKey), 0, 1);
        scheduler.runTaskTimer(this, new ParkourRunnable.Hologram(soleKey), 0, 20);
        
        // Load built-in commands
        final CommandProcessor commandProcessor = new CommandProcessor(this);
        commandProcessor.registerCommand(new EternaCommand("eterna"));
        commandProcessor.registerCommand(new NoteBlockStudioCommand("nbs"));
        commandProcessor.registerCommand(new SelectDialogOptionCommand("selectdialogoption"));
        
        try {
            new File(this.getDataFolder() + "/songs").mkdir();
        }
        catch (Exception ignored) {
        }
        
        // Load dependencies
        EternaAPI.loadAll();
        
        // Check for updates
        this.updater = new Updater(soleKey);
        
        if (this.config.checkForUpdates()) {
            Runnables.runLaterAsync(() -> this.updater.checkForUpdatesAndGiveLink(), 20L);
        }
    }
    
    @Override
    public void onDisable() {
        managers.dispose();
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
        }
        catch (Exception ignored) {
            EternaLogger.severe("");
            EternaLogger.severe("** No Paper detected! Disabling...");
            EternaLogger.severe("** EternaAPI does NOT work on Spigot, upgrade to Paper!");
            EternaLogger.severe("** https://papermc.io/downloads/paper");
            EternaLogger.severe("");
            
            Bukkit.getPluginManager().disablePlugins();
        }
    }
    
}
