package kz.hapyl.spigotutils.module.reflect.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.util.internal.ConcurrentSet;
import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.reflect.Reflect;
import net.minecraft.network.protocol.Packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.NoSuchElementException;
import java.util.Set;

public final class NettyInjector {

	private static final NettyInjector classInstance = new NettyInjector();

	private final Set<NettyListener> listeners;
	private final String channelName = "EternaInject";

	private NettyInjector() {
		listeners = new ConcurrentSet<>();
	}

	public void addListener(NettyListener listener) {
		listeners.add(listener);
	}

	public void removeListener(NettyListener listener) {
		listeners.remove(listener);
	}

	public void removeInjection(Player player) {
		try {
			final Channel channel = getChannel(player);

			if (channel == null) {
				Chat.sendMessage(player, "&cAn network error occurred! Try rejoining the server if you're seeing this for the first time!");
				return;
			}

			channel.eventLoop().submit(() -> {
				if (channel.pipeline().get(channelName) != null) {
					channel.pipeline().remove(channelName);
				}
				return null;
			});
		}
		catch (NoSuchElementException ignored) {
		}
	}

	public void injectPlayer(Player player) {
		try {
			final ChannelDuplexHandler channel = new ChannelDuplexHandler() {
				@Override
				public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
					super.channelRead(ctx, packet);

					if (!listeners.isEmpty()) {
						for (final NettyListener listener : listeners) {
							listener.readPacket(player, (Packet<?>)packet);
						}
					}

					final Class<?> packetClass = packet.getClass();
					final String packetName = packet.toString();

					/** NPC Controller */
					if (packetName.contains("PacketPlayInUseEntity")) {

					}
				}

/*				@SuppressWarnings("unchecked")
				@Override
				public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {
					final String packetName = packet.toString();
					final Class<?> packetClass = packet.getClass();

					// No returns here!
					if (packetName.contains("PacketPlayOutEntityMetadata")) {
						PacketPlayOutEntityMetadata pa = (PacketPlayOutEntityMetadata) packet;

						final int entityId = (int) FieldUtils.getDeclaredField(packetClass, "a", true).get(pa);
						final List<DataWatcher.Item<?>> items = (List<DataWatcher.Item<?>>) FieldUtils.getDeclaredField(packetClass, "b", true)
								.get(pa);

						if (EntityMetadata.stream.containsKey(entityId)) {
							final EntityMetadata metaData = EntityMetadata.stream.get(entityId);
							if (metaData.isViewer(player)) {
								final DataWatcher.Item<?> element = items.get(0);
								if (element.b() instanceof Byte) {
									System.out.println(3);
									final Byte prevMask = (Byte) element.b();
									FieldUtils.writeDeclaredField(element, "b", metaData.getType().getBitMask(), true);
								}

							}
						}
					}
					ctx.write(packet, promise);
				}*/

			};

			final ChannelPipeline pipe = getPipe(player);
			if (pipe == null) {
				Chat.sendMessage(player, "&cAn network error occurred! Try rejoining the server if you're seeing this for the first time!");
				return;
			}

			if (pipe.get(channelName) != null) {
				return;
			}
			pipe.addBefore("packet_handler", channelName, channel);
		}
		catch (NoSuchElementException e) {
			e.printStackTrace();
			Bukkit.getLogger().warning("Plugin tried to register channel while offline!");
		}
	}

	private ChannelPipeline getPipe(Player player) {
		return getChannel(player).pipeline();
	}

	private Channel getChannel(Player player) {
		return Reflect.getMinecraftPlayer(player).b.a.k;
	}

	public static NettyInjector getInstance() {
		return classInstance;
	}


}
