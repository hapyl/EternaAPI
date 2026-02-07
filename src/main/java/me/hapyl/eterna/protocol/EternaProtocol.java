package me.hapyl.eterna.protocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import me.hapyl.eterna.*;
import me.hapyl.eterna.module.reflect.Reflect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Represents an internal protocol.
 */
@ApiStatus.Internal
public final class EternaProtocol extends EternaKeyed implements Listener {
    
    public static final String PROTOCOL_NAME;
    public static final String INTERCEPTOR_NAME;
    public static final String ADD_BEFORE_NAME;
    
    static {
        PROTOCOL_NAME = "ProtocolLib";
        INTERCEPTOR_NAME = "EternaAPIInject";
        ADD_BEFORE_NAME = "packet_handler";
    }
    
    private final EternaPlugin plugin;
    
    public EternaProtocol(@NotNull EternaKey key, @NotNull EternaPlugin plugin) {
        super(key);
        
        this.plugin = plugin;
        
        // Inject online players
        Runnables.later(() -> Bukkit.getOnlinePlayers().forEach(this::inject), 10);
        
        // Register events
        Bukkit.getPluginManager().registerEvents(this, plugin);
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
    
    public void inject(@NotNull Player player) {
        final PacketInterceptor packetInterceptor = new PacketInterceptor(player);
        final Channel channel = Reflect.getPlayerConnection(player).connection.channel;
        final ChannelPipeline pipeline = channel.pipeline();
        
        try {
            pipeline.addBefore(ADD_BEFORE_NAME, INTERCEPTOR_NAME, packetInterceptor);
        }
        catch (Exception e) {
            throw EternaLogger.acknowledgeException(e);
        }
    }
    
    @SuppressWarnings("all")
    public void uninject(@NotNull Player player) {
        final Channel channel = Reflect.getPlayerConnection(player).connection.channel;
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
