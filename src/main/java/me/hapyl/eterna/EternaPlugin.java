package me.hapyl.eterna;

import me.hapyl.eterna.builtin.command.EternaCommand;
import me.hapyl.eterna.builtin.command.NoteBlockStudioCommand;
import me.hapyl.eterna.builtin.command.SelectDialogOptionCommand;
import me.hapyl.eterna.builtin.updater.UpdateResult;
import me.hapyl.eterna.builtin.updater.Updater;
import me.hapyl.eterna.module.command.CommandHandler;
import me.hapyl.eterna.module.command.CommandProcessor;
import me.hapyl.eterna.module.inventory.builder.ItemBuilderHandler;
import me.hapyl.eterna.module.inventory.menu.PlayerMenuHandler;
import me.hapyl.eterna.module.inventory.sign.SignHandler;
import me.hapyl.eterna.module.npc.NpcHandler;
import me.hapyl.eterna.module.npc.NpcRunnable;
import me.hapyl.eterna.module.parkour.ParkourHandler;
import me.hapyl.eterna.module.parkour.ParkourRunnable;
import me.hapyl.eterna.module.player.dialog.DialogHandler;
import me.hapyl.eterna.module.player.quest.QuestHandler;
import me.hapyl.eterna.module.player.song.SongHandler;
import me.hapyl.eterna.module.reflect.glowing.GlowingHandler;
import me.hapyl.eterna.module.reflect.glowing.GlowingProtocolEntitySpawnHandler;
import me.hapyl.eterna.module.reflect.glowing.GlowingProtocolMetadataHandler;
import me.hapyl.eterna.module.reflect.glowing.GlowingRunnable;
import me.hapyl.eterna.protocol.EternaProtocol;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;

/**
 * Represents the Eterna {@link JavaPlugin}.
 */
public /*final*/ class EternaPlugin extends JavaPlugin {
    
    private static EternaPlugin plugin;
    private static EternaKey soleKey;
    
    protected Updater updater;
    protected EternaConfig config;
    protected EternaProtocol protocol;
    
    @Override
    public void onDisable() {
        ParkourHandler.dispose0(soleKey);
    }
    
    @Override
    public void onEnable() {
        plugin = Eterna.plugin = this;
        
        // Create soleKey
        soleKey = EternaKey.createSoleKey();
        
        // Check if the server is running paper
        this.validateUsingPaper();
        
        // Init protocol
        this.protocol = new EternaProtocol(soleKey, this);
        
        // Init handlers
        final PluginManager manager = this.getServer().getPluginManager();
        
        new GlowingHandler(soleKey, this);
        new SongHandler(soleKey, this);
        
        manager.registerEvents(new ItemBuilderHandler(soleKey), this);
        manager.registerEvents(new PlayerMenuHandler(soleKey), this);
        
        manager.registerEvents(new ParkourHandler(soleKey, this), this);
        manager.registerEvents(new DialogHandler(soleKey, this), this);
        manager.registerEvents(new NpcHandler(soleKey, this), this);
        manager.registerEvents(new QuestHandler(soleKey, this), this);
        
        manager.registerEvents(new SignHandler(soleKey), this);
        manager.registerEvents(new GlowingProtocolMetadataHandler(soleKey), this);
        manager.registerEvents(new GlowingProtocolEntitySpawnHandler(soleKey), this);
        manager.registerEvents(new CommandHandler(soleKey), this);
        
        final BukkitScheduler scheduler = Bukkit.getScheduler();
        
        // Load config
        this.config = new EternaConfigImpl(soleKey, this);
        
        // Schedule tasks
        scheduler.runTaskTimer(this, new NpcRunnable(soleKey), 0, config.tickRate().npc());
        scheduler.runTaskTimer(this, new ParkourRunnable.Actionbar(soleKey), 0, config.tickRate().parkour());
        
        // The following tasks have hardcoded periods
        scheduler.runTaskTimer(this, new GlowingRunnable(soleKey), 0, 1);
        scheduler.runTaskTimer(this, new ParkourRunnable.Hologram(soleKey), 0, 20);
        
        // Load built-in commands
        final CommandProcessor commandProcessor = new CommandProcessor(this);
        commandProcessor.registerCommand(new EternaCommand(soleKey, "eterna"));
        commandProcessor.registerCommand(new NoteBlockStudioCommand(soleKey, "nbs"));
        commandProcessor.registerCommand(new SelectDialogOptionCommand(soleKey, "selectdialogoption"));
        
        try {
            new File(this.getDataFolder() + "/songs").mkdir();
        }
        catch (Exception ignored) {
        }
        
        // Check for updates
        this.updater = new Updater(soleKey);
        
        if (this.config.checkForUpdates()) {
            this.updater.checkForUpdates().thenAccept(response -> {
                if (response.result() == UpdateResult.OUTDATED) {
                    final Component downloadUrl = response.downloadUrl();
                    
                    Bukkit.getOnlinePlayers().stream().filter(Player::isOp).forEach(player -> player.sendMessage(downloadUrl));
                    Bukkit.getConsoleSender().sendMessage(downloadUrl);
                }
            });
        }
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
            
            Bukkit.getServer().shutdown();
        }
    }
    
}
