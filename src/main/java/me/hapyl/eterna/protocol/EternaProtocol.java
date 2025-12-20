package me.hapyl.eterna.protocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import me.hapyl.eterna.*;
import me.hapyl.eterna.module.player.PlayerLib;
import me.hapyl.eterna.module.reflect.Reflect;
import me.hapyl.eterna.module.util.Runnables;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.NoSuchElementException;

@ApiStatus.Internal
public final class EternaProtocol extends EternaLock implements Listener {
    
    public static final String PROTOCOL_NAME;
    public static final String INTERCEPTOR_NAME;
    public static final String ADD_BEFORE_NAME;
    
    static {
        PROTOCOL_NAME = "ProtocolLib";
        INTERCEPTOR_NAME = "EternaAPIInject";
        ADD_BEFORE_NAME = "packet_handler";
    }
    
    private final EternaPlugin plugin;
    
    public EternaProtocol(@Nonnull EternaKey key, @Nonnull EternaPlugin plugin) {
        super(key);
        
        this.plugin = plugin;
        
        // Inject online players
        Runnables.runLater(() -> Bukkit.getOnlinePlayers().forEach(this::inject), 10);
        
        // Register events
        Bukkit.getPluginManager().registerEvents(this, plugin);
        
        // Check for ProtocolLib
        Runnables.runLater(this::checkProtocolLibAndNotifyIfNoPluginsUseIt, 20);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void handlePlayerLoginEvent(PlayerJoinEvent ev) {
        final Player player = ev.getPlayer();
        
        inject(player);
    }
    
    @EventHandler()
    public void handlePlayerQuitEvent(PlayerQuitEvent ev) {
        final Player player = ev.getPlayer();
        
        uninject(player);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void handlePluginDisableEvent(PluginDisableEvent ev) {
        if (!this.plugin.equals(ev.getPlugin())) {
            return;
        }
        
        close();
    }
    
    public void inject(@Nonnull Player player) {
        final PacketInterceptor packetInterceptor = new PacketInterceptor(player);
        final Channel channel = Reflect.getNettyChannel(player);
        final ChannelPipeline pipeline = channel.pipeline();
        
        try {
            pipeline.addBefore(ADD_BEFORE_NAME, INTERCEPTOR_NAME, packetInterceptor);
        }
        catch (Exception e) {
            throw EternaLogger.acknowledgeException(e);
        }
    }
    
    @SuppressWarnings("all")
    public void uninject(@Nonnull Player player) {
        final Channel channel = Reflect.getNettyChannel(player);
        final ChannelPipeline pipeline = channel.pipeline();
        
        // Don't use Auto-Closable bruh...
        channel.eventLoop().execute(() -> {
            try {
                pipeline.remove(INTERCEPTOR_NAME);
            }
            catch (NoSuchElementException e) {
                // Don't care
            }
        });
    }
    
    // Since EternaAPI no longer needs ProtocolLib, this is a reminder to remove it, unless needed by other plugins
    public void checkProtocolLibAndNotifyIfNoPluginsUseIt() {
        final PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        
        final Plugin[] plugins = pluginManager.getPlugins();
        boolean hasProtocol = false;
        
        for (Plugin plugin : plugins) {
            if (plugin.getName().contains(PROTOCOL_NAME)) {
                hasProtocol = true;
                break;
            }
        }
        
        if (!hasProtocol) {
            return;
        }
        
        // Check if any plugins depend or soft-depend on in
        for (Plugin plugin : plugins) {
            final PluginDescriptionFile description = plugin.getDescription();
            
            if (isDependOnProtocol(description.getDepend()) || isDependOnProtocol(description.getSoftDepend())) {
                return;
            }
        }
        
        // Notify
        EternaLogger.broadcastMessageOP("&eIt looks like you're using &6ProtocolLib&e!");
        EternaLogger.broadcastMessageOP("&6EternaAPI &e&nno&e &nlonger&e requires &6ProtocolLib&e.");
        EternaLogger.broadcastMessageOP("&eNo other installed plugins require &6ProtocolLib&e, you can safely delete it.");
        
        Eterna.getOnlineOperators().forEach(player -> PlayerLib.playSound(player, Sound.ENTITY_CAT_AMBIENT, 0.0f));
    }
    
    /**
     * Uninject all the players.
     * <br>
     * Called upon server shutdown.
     */
    protected void close() {
        Bukkit.getOnlinePlayers().forEach(this::uninject);
    }
    
    private boolean isDependOnProtocol(List<String> dependList) {
        for (String depend : dependList) {
            if (depend.contains(PROTOCOL_NAME)) {
                return true;
            }
        }
        
        return false;
    }
    
}
