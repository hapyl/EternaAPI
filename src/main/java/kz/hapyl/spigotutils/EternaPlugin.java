package kz.hapyl.spigotutils;

import kz.hapyl.spigotutils.builtin.command.NoteBlockStudioCommand;
import kz.hapyl.spigotutils.builtin.command.QuestCommand;
import kz.hapyl.spigotutils.module.command.CommandProcessor;
import kz.hapyl.spigotutils.module.entity.Rope;
import kz.hapyl.spigotutils.module.hologram.Hologram;
import kz.hapyl.spigotutils.module.hologram.HologramRunnable;
import kz.hapyl.spigotutils.module.inventory.ChestInventoryListener;
import kz.hapyl.spigotutils.module.inventory.ItemBuilderListener;
import kz.hapyl.spigotutils.module.inventory.gui.GUIListener;
import kz.hapyl.spigotutils.module.inventory.item.CustomItemHolder;
import kz.hapyl.spigotutils.module.inventory.item.CustomItemListener;
import kz.hapyl.spigotutils.module.listener.SimpleListener;
import kz.hapyl.spigotutils.module.locaiton.TriggerManager;
import kz.hapyl.spigotutils.module.parkour.Parkour;
import kz.hapyl.spigotutils.module.parkour.ParkourListener;
import kz.hapyl.spigotutils.module.parkour.ParkourManager;
import kz.hapyl.spigotutils.module.parkour.ParkourRunnable;
import kz.hapyl.spigotutils.module.player.song.SongPlayer;
import kz.hapyl.spigotutils.module.quest.QuestListener;
import kz.hapyl.spigotutils.module.reflect.NPCRunnable;
import kz.hapyl.spigotutils.module.reflect.glow.GlowingManager;
import kz.hapyl.spigotutils.module.reflect.glow.GlowingRunnable;
import kz.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import kz.hapyl.spigotutils.module.reflect.protocol.GlowingListener;
import kz.hapyl.spigotutils.module.reflect.protocol.HumanNPCListener;
import kz.hapyl.spigotutils.module.reflect.protocol.SignListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import test.Test;

import java.io.File;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class EternaPlugin extends JavaPlugin {

    private static EternaPlugin plugin;

    private SongPlayer songPlayer;
    private CustomItemHolder itemHolder;
    private GlowingManager glowingManager;
    private ParkourManager parkourManager;

    @Override
    public void onEnable() {
        plugin = this;

        final PluginManager manager = this.getServer().getPluginManager();

        manager.registerEvents(new ItemBuilderListener(), this);
        manager.registerEvents(new CustomItemListener(), this);
        manager.registerEvents(new ChestInventoryListener(), this);
        manager.registerEvents(new GUIListener(), this);
        manager.registerEvents(new TriggerManager(), this);
        manager.registerEvents(new QuestListener(), this);
        manager.registerEvents(new ParkourListener(), this);

        this.songPlayer = new SongPlayer(this);
        this.glowingManager = new GlowingManager(this);
        this.itemHolder = new CustomItemHolder();
        this.parkourManager = new ParkourManager();

        final BukkitScheduler scheduler = Bukkit.getScheduler();

        // Schedule tasks
        scheduler.runTaskTimer(this, new NPCRunnable(), 0, 2);
        scheduler.runTaskTimer(this, new HologramRunnable(), 0, 2);
        scheduler.runTaskTimer(this, new ParkourRunnable(), 0, 2);
        scheduler.runTaskTimer(this, new GlowingRunnable(), 0, 1);

        // Load ProtocolLib listeners
        new SignListener();
        new GlowingListener();
        new HumanNPCListener();

        // Load built-in commands
        final CommandProcessor commandProcessor = new CommandProcessor(this);
        commandProcessor.registerCommand(new QuestCommand("quest"));
        commandProcessor.registerCommand(new NoteBlockStudioCommand("nbs"));

        // Load configuration file
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        // Create songs folder
        try {
            boolean b = new File(this.getDataFolder() + "/songs").mkdir();
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Test().test();

    }

    @Override
    public void onDisable() {
        // reset parkours
        runSafe(() -> {
            parkourManager.getRegisteredParkours().forEach(Parkour::removeWorldEntities);
            parkourManager.restoreAllData();
        }, "parkour manager reset");

        // remove NPCs
        runSafe(HumanNPC::removeAll, "human npc removal");
        // remove holograms
        runSafe(Hologram::removeAll, "hologram removal");
        // remove ropes
        runSafe(Rope::callThisOnDisable, "ropes removal");
    }

    public CustomItemHolder getItemHolder() {
        return itemHolder;
    }

    public SongPlayer getSongPlayer() {
        return songPlayer;
    }

    public ParkourManager getParkourManager() {
        return parkourManager;
    }

    public GlowingManager getGlowingManager() {
        return glowingManager;
    }

    private void runSafe(Runnable runnable, String name) {
        try {
            runnable.run();
        } catch (Error exception) {
            Bukkit.getLogger().severe("Could not run " + name + " in onDisable(), did you /reload your server?");
            Bukkit.getLogger().severe(exception.getMessage());
        }
    }

    public static void runTaskLater(Consumer<BukkitTask> runnable, int later) {
        Bukkit.getScheduler().runTaskLater(plugin, runnable, later);
    }

    public static EternaPlugin getPlugin() {
        return plugin;
    }

    public static void registerEvent(SimpleListener simpleListener) {
        plugin.getServer().getPluginManager().registerEvents(simpleListener, plugin);
    }

    public static Logger logger() {
        return getPlugin().getLogger();
    }

}
