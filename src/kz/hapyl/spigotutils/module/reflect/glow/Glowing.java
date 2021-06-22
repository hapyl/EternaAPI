package kz.hapyl.spigotutils.module.reflect.glow;

import com.google.common.collect.Sets;
import kz.hapyl.spigotutils.module.reflect.Reflect;
import kz.hapyl.spigotutils.module.reflect.ReflectPacket;
import kz.hapyl.spigotutils.module.reflect.Ticking;
import kz.hapyl.spigotutils.module.util.Validate;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.syncher.DataWatcher;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Set;

public class Glowing implements Ticking {

	protected final static Set<Glowing> glowing = Sets.newConcurrentHashSet();

	private final net.minecraft.world.entity.Entity netEntity;
	private final Entity entity;
	private final Set<Player> viewers;
	private ChatColor color;
	private int duration;
	private boolean everyoneIsListener;

	public Glowing(Entity entity, ChatColor color, int duration) {
		this.entity = entity;
		this.netEntity = Reflect.getMinecraftEntity(entity);
		this.duration = duration;
		this.everyoneIsListener = false;
		this.viewers = Sets.newHashSet();
		this.setColor(color);
		glowing.add(this);
	}

	public Glowing(Entity entity, int duration) {
		this(entity, ChatColor.WHITE, duration);
	}

	public void setColor(ChatColor color) {
		Validate.isTrue(color.isColor(), "color must be color, not formatter!");
		this.color = color;
	}

	@Override
	@SuppressWarnings("all")
	public void tick() {
		if (everyoneIsListener) {
			this.viewers.addAll(Bukkit.getOnlinePlayers());
		}
		final boolean last = duration-- <= 0;
		if (last || entity.isDead()) {
			glowing.remove(this);
		}

		try {

			// This is maybe not the best way of doing this
			final DataWatcher dataWatcher = netEntity.getDataWatcher();
			final DataWatcher fakeWatcher = new DataWatcher(netEntity);

			final Int2ObjectMap<DataWatcher.Item<?>> values = (Int2ObjectMap<DataWatcher.Item<?>>)FieldUtils.getDeclaredField(DataWatcher.class, "f", true)
					.get(dataWatcher);
			final Int2ObjectMap<DataWatcher.Item<?>> fakeValues = new Int2ObjectOpenHashMap<>();

			values.forEach((pos, object) -> {
				fakeValues.put(pos, object.d());
			});

			// 0 is always a byte value as far as protocol says so we should be fine casting
			final DataWatcher.Item<Byte> item = (DataWatcher.Item<Byte>)fakeValues.get(0);
			final byte initBitMask = (byte)item.b();
			final byte bitMask = 0x40;

			item.a((byte)(!last ? initBitMask | bitMask : initBitMask & ~bitMask));

			// Update Color
			for (final Player player : this.viewers) {
				if (last) {
					deleteTeam(player);
				}
				else {
					createTeam(player);
				}
			}

			FieldUtils.writeDeclaredField(fakeWatcher, "f", fakeValues, true);
			new ReflectPacket(new PacketPlayOutEntityMetadata(entity.getEntityId(), fakeWatcher, true)).sendPackets(this.viewers);

		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void deleteTeam(Player player) {
		final Team team = player.getScoreboard().getTeam("ETERNA_API_GC");
		if (team != null) {
			team.unregister();
		}
	}

	private void createTeam(Player player) {
		final Team team = getTeamOrCreate(player);
		team.setColor(this.color);
		team.addEntry(this.entity.getUniqueId().toString());
	}

	private Team getTeamOrCreate(Player player) {
		final Scoreboard scoreboard = player.getScoreboard();
		Team team = scoreboard.getTeam("ETERNA_API_GC");
		if (team == null) {
			team = scoreboard.registerNewTeam("ETERNA_API_GC");
			team.setColor(this.color);
		}
		return team;
	}

	private void __debug(Object o) {
		Bukkit.broadcastMessage(o.toString());
	}

	public boolean isEveryoneIsListener() {
		return everyoneIsListener;
	}

	public void setEveryoneIsListener(boolean everyoneIsListener) {
		this.everyoneIsListener = everyoneIsListener;
	}

	public void addViewer(Player player) {
		this.viewers.add(player);
	}

	public void removeViewer(Player player) {
		this.viewers.remove(player);
	}

	public boolean isViewer(Player player) {
		return this.viewers.contains(player);
	}
}
