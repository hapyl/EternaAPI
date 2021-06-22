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
import kz.hapyl.spigotutils.module.listener.SimpleListener;
import kz.hapyl.spigotutils.module.locaiton.TriggerManager;
import kz.hapyl.spigotutils.module.player.song.SongPlayer;
import kz.hapyl.spigotutils.module.quest.QuestListener;
import kz.hapyl.spigotutils.module.reflect.NPCRunnable;
import kz.hapyl.spigotutils.module.reflect.NettyInjector;
import kz.hapyl.spigotutils.module.reflect.glow.GlowingRunnable;
import kz.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.function.Consumer;

public class SpigotUtilsPlugin extends JavaPlugin implements Listener {

	private static SpigotUtilsPlugin plugin;

	private SongPlayer songPlayer;

	@Override
	public void onEnable() {
		plugin = this;

		final PluginManager manager = this.getServer().getPluginManager();

		manager.registerEvents(new ItemBuilderListener(), this);
		manager.registerEvents(new ChestInventoryListener(), this);
		manager.registerEvents(new GUIListener(), this);
		manager.registerEvents(new TriggerManager(), this);
		manager.registerEvents(new QuestListener(), this);
		manager.registerEvents(this, this);

		this.songPlayer = new SongPlayer(this);
		final BukkitScheduler scheduler = Bukkit.getScheduler();

		scheduler.runTaskTimer(this, new NPCRunnable(), 0, 2);
		scheduler.runTaskTimer(this, new HologramRunnable(), 0, 2);
		scheduler.runTaskTimer(this, new GlowingRunnable(), 0, 1);

		// reassign pipe
		Bukkit.getOnlinePlayers().forEach(player -> NettyInjector.getInstance().injectPlayer(player));

		// assign built in commands
		final CommandProcessor commandProcessor = new CommandProcessor(this);
		commandProcessor.registerCommand(new QuestCommand("quest"));
		commandProcessor.registerCommand(new NoteBlockStudioCommand("nbs"));

		// Config
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();

		try {
			final boolean created = new File(this.getDataFolder() + "/songs").mkdir();
		}
		catch (Exception e) {
			e.printStackTrace();
		}


	}

	public SongPlayer getSongPlayer() {
		return songPlayer;
	}

	@Override
	public void onDisable() {
		// remove pipes
		this.runSafe(() -> Bukkit.getOnlinePlayers().forEach(player -> NettyInjector.getInstance().removeInjection(player)));
		// remove NPCs
		//this.runSafe(NPC::removeAll);
		this.runSafe(HumanNPC::removeAll);
		// remove holograms
		this.runSafe(Hologram::removeAll);
		// remove ropes
		this.runSafe(Rope::callThisOnDisable);
	}

	private void runSafe(Runnable runnable) {
		try {
			runnable.run();
		}
		catch (Exception ignored0) {
		}
	}

	public static void runTaskLater(Consumer<BukkitTask> runnable, int later) {
		Bukkit.getScheduler().runTaskLater(plugin, runnable, later);
	}

	public static SpigotUtilsPlugin getPlugin() {
		return plugin;
	}

	public static void registerEvent(SimpleListener simpleListener) {
		plugin.getServer().getPluginManager().registerEvents(simpleListener, plugin);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void handleJoinEvent(PlayerJoinEvent ev) {
		NettyInjector.getInstance().injectPlayer(ev.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void handleQuitEvent(PlayerQuitEvent ev) {
		NettyInjector.getInstance().removeInjection(ev.getPlayer());
	}

}
