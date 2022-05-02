package kz.hapyl.spigotutils.module.reflect.netty;

import com.google.common.collect.Sets;
import org.bukkit.entity.Player;

import java.util.Set;

public final class NettyInjector {

	private static final NettyInjector classInstance = new NettyInjector();

	private final Set<NettyListener> listeners;
	private final String channelName = "EternaInject";

	private NettyInjector() {
		listeners = Sets.newConcurrentHashSet();
	}

	public void addListener(NettyListener listener) {
		listeners.add(listener);
	}

	public void removeListener(NettyListener listener) {
		listeners.remove(listener);
	}

	public void injectPlayer(Player player) {
//		try {
//			final ChannelDuplexHandler channel = new ChannelDuplexHandler() {
//				@Override
//				public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
//					super.channelRead(ctx, packet);
//					if (!listeners.isEmpty()) {
//						for (final NettyListener listener : listeners) {
//							listener.readPacket(player, (Packet<?>)packet);
//						}
//					}
//				}
//
//				@Override
//				public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {
//					super.write(ctx, packet, promise);
//					if (!listeners.isEmpty()) {
//						for (final NettyListener listener : listeners) {
//							listener.writePacket(player, (Packet<?>)packet);
//						}
//					}
//				}
//			};
//
//			final ChannelPipeline pipe = getPipe(player);
//			if (pipe == null) {
//				Chat.sendMessage(player, "&cAn network error occurred! Try rejoining the server if you're seeing this for the first time!");
//				return;
//			}
//
//			if (pipe.get(channelName) != null) {
//				return;
//			}
//
//			pipe.addBefore("packet_handler", channelName, channel);
//
//		}
//		catch (NoSuchElementException e) {
//			e.printStackTrace();
//			Bukkit.getLogger().warning("Plugin tried to register channel while offline!");
//		}
	}

	public void removeInjection(Player player) {
//		try {
//			final Channel channel = getChannel(player);
//
//			if (channel == null) {
//				Chat.sendMessage(player, "&cAn network error occurred! Try rejoining the server if you're seeing this for the first time!");
//				return;
//			}
//
//			channel.eventLoop().submit(() -> {
//				if (channel.pipeline().get(channelName) != null) {
//					channel.pipeline().remove(channelName);
//				}
//				return null;
//			});
//		}
//		catch (NoSuchElementException ignored) {
//		}
	}

//	private ChannelPipeline getPipe(Player player) {
//		return getChannel(player).pipeline();
//	}
//
//	private Channel getChannel(Player player) {
//		return Reflect.getMinecraftPlayer(player).b.a.k;
//	}
//
//	public static NettyInjector getInstance() {
//		return classInstance;
//	}


}
