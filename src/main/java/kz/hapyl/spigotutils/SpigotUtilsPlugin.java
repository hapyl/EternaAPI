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
import kz.hapyl.spigotutils.module.parkour.*;
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
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import test.Commands;

import java.io.File;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class SpigotUtilsPlugin extends JavaPlugin implements Listener {

    private static SpigotUtilsPlugin plugin;

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
        manager.registerEvents(this, this);

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
            new File(this.getDataFolder() + "/songs").mkdir();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // FIXME: 005. 05/05/2022 - remove this before publish
        //        test();

    }

    private void test() {
        Commands.createCommands();

        final World world = Bukkit.getWorlds().get(0);
        final Parkour parkour = new Parkour(
                "Lobby Parkour",
                new Position(world, 9, 65, -5, -45.0f, 0.0f),
                new Position(world, -2, 65, 8)
        );

        parkour.addCheckpoint(world, 9, 67, 5, 35.0f, 0.0f);
        parkour.addCheckpoint(world, 1, 67, 11, 55.0f, 0.0f);
        parkour.addCheckpoint(world, -4, 68, 8, -90.0f, 65.0f);

        parkourManager.registerParkour(parkour);
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

    @Override
    public void onDisable() {
        // reset parkours
        parkourManager.getRegisteredParkours().forEach(Parkour::removeWorldEntities);
        parkourManager.restoreAllData();

        // remove NPCs
        this.runSafe(HumanNPC::removeAll, "human npc removal");
        // remove holograms
        this.runSafe(Hologram::removeAll, "hologram removal");
        // remove ropes
        this.runSafe(Rope::callThisOnDisable, "ropes removal");
    }

    private void runSafe(Runnable runnable, String name) {
        try {
            runnable.run();
        } catch (Exception ignored0) {
            Bukkit.getLogger().warning("Could not run " + name + " in onDisable(), did you /reload your server?");
        }
    }

    public static void runTaskLater(Consumer<BukkitTask> runnable, int later) {
        Bukkit.getScheduler().runTaskLater(plugin, runnable, later);
    }

    public GlowingManager getGlowingManager() {
        return glowingManager;
    }

    public static SpigotUtilsPlugin getPlugin() {
        return plugin;
    }

    public static void registerEvent(SimpleListener simpleListener) {
        plugin.getServer().getPluginManager().registerEvents(simpleListener, plugin);
    }

    public static Logger logger() {
        return getPlugin().getLogger();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleJoinEvent(PlayerJoinEvent ev) {
        //		NettyInjector.getInstance().injectPlayer(ev.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleQuitEvent(PlayerQuitEvent ev) {
        //        NettyInjector.getInstance().removeInjection(ev.getPlayer());
    }

}
