package kz.hapyl.spigotutils.module.reflect;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import kz.hapyl.spigotutils.SpigotUtilsPlugin;
import kz.hapyl.spigotutils.module.chat.Chat;
import kz.hapyl.spigotutils.module.inventory.SignGUI;
import kz.hapyl.spigotutils.module.reflect.npc.ClickType;
import kz.hapyl.spigotutils.module.reflect.npc.HumanNPC;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

import java.util.NoSuchElementException;

public final class NettyInjector {

	private NettyInjector() {
	}

	public void removeInjection(Player player) {
		try {
			Channel channel = getChannel(player);
			if (channel == null) {
				Chat.sendMessage(player,
						"&cAn error occurred whist trying to inject into your netty channel, try rejoining the server if you seeing this for the " +
								"first time!");
				return;
			}
			channel.eventLoop().submit(() -> {
				if (channel.pipeline().get(player.getName()) != null) {
					channel.pipeline().remove(player.getName());
				}
				return null;
			});
		}
		catch (NoSuchElementException e) {
			e.printStackTrace();
			Bukkit.getLogger().warning("Plugin tried to unregister channel while offline!");
		}
	}

	public void injectPlayer(Player player) {
		try {
			ChannelDuplexHandler channel = new ChannelDuplexHandler() {
				@Override
				public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
					// make sure to proceed the packet either way
					super.channelRead(ctx, packet);
					final Class<?> packetClass = packet.getClass();
					final String packetName = packet.toString();

					/** Sign GUI Controller */
					if (packetName.contains("PacketPlayInUpdateSign")) {

						try {
							final String[] lines = (String[])FieldUtils.getDeclaredField(packetClass, "c", true).get(packet);
							final SignGUI gui = SignGUI.saved.get(player);

							if (gui == null) {
								return;
							}

							gui.onResponse(player, lines);
							gui.clearSign();
							SignGUI.saved.remove(player);

						}
						catch (Exception e) {
							e.printStackTrace();
							Bukkit.getLogger().warning("An error trying to get Sign GUI strings.");
						}
						return;
					}

					/** NPC Controller */
					if (packetName.contains("PacketPlayInUseEntity")) {
						final int entityId = (int)FieldUtils.getDeclaredField(packetClass, "a", true).get(packet);
						final Object action = FieldUtils.getDeclaredField(packetClass, "b", true).get(packet);

						final ClickType clickType = ClickType.of(MethodUtils.invokeMethod(action, "a", null).toString());
						final EquipmentSlot clickHand = clickType == ClickType.ATTACK ?
								EquipmentSlot.HAND :
								FieldUtils.getDeclaredField(action.getClass(), "a", true)
										.get(action)
										.toString()
										.equalsIgnoreCase("OFF_HAND") ? EquipmentSlot.OFF_HAND : EquipmentSlot.HAND;

						try {
							Bukkit.getScheduler().runTask(SpigotUtilsPlugin.getPlugin(), () -> {
								if (clickHand == EquipmentSlot.OFF_HAND || clickType == ClickType.INTERACT_AT) {
									return;
								}

								final HumanNPC human = HumanNPC.byId.get(entityId);

								if (human == null) {
									return;
								}

								human.onClick(player, human, clickType);
								human.onClickAuto(player);

							});


						}
						catch (Exception e) {
							e.printStackTrace();
							Bukkit.getLogger().warning("An error trying to inject into NPC channel!");
						}
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

			ChannelPipeline pipe = getPipe(player);
			if (pipe == null) {
				Chat.sendMessage(player,
						"&cAn error occurred whist trying to inject into your netty channel, try rejoining the server if you seeing this for the " +
								"first time!");
				return;
			}
			// don't add it if exists
			if (pipe.get(player.getName()) != null) {
				return;
			}
			pipe.addBefore("packet_handler", player.getName(), channel);
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

	private static final NettyInjector classInstance = new NettyInjector();

	public static NettyInjector getInstance() {
		return classInstance;
	}


}
