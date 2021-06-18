package kz.hapyl.spigotutils.module.reflect;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * This class allows to apply glowing for entities for certain players. Glowing colors not yet implemented!
 */
public class Glowing {

	// FIXME: 018. 02/18/2021 - fix this

	public static final Map<Glowing, Integer> glowingFor = new HashMap<>();

	private       Entity      entity;
	private final Set<Player> viewers = new HashSet<>();

	public Glowing() {
	}

	/**
	 * Creates a glowing object.
	 *
	 * @param entity          - Entity that will glow.
	 * @param glowingDuration - Glowing duration in ticks.
	 * @param players         - Players who will see the glowing.
	 */
	public Glowing(Entity entity, int glowingDuration, Player... players) {
		this.entity = entity;
		this.viewers.addAll(Arrays.asList(players));
		glowingFor.put(this, glowingDuration);
	}

	public void reset() {
		glowingFor.clear();
	}

	protected static Map<Glowing, Integer> a() {
		return glowingFor;
	}

	protected Entity getEntity() {
		return entity;
	}

	protected Set<Player> getViewers() {
		return viewers;
	}

	protected void glow(boolean glow) {
		try {

			final Object netEntity = Reflect.getNetEntity(this.entity);
			final Object dataWatcher = netEntity.getClass().getMethod("getDataWatcher").invoke(netEntity);
			final Object newDataWatcher = Reflect.getNetClass("DataWatcher").getConstructor(Reflect.getNetClass("Entity")).newInstance(netEntity);

			final Int2ObjectMap<Object> entries =
					(Int2ObjectMap<Object>) FieldUtils.readDeclaredField(dataWatcher, "entries", true);
			final Int2ObjectMap<Object> copyEntries = new Int2ObjectOpenHashMap<>();

			entries.forEach((integer, o) -> {
				try {
					copyEntries.put((int) integer, o.getClass().getMethod("d").invoke(o));
				}
				catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
					e.printStackTrace();
				}
			});

			final Object item = entries.get(0);
			byte initBitMask = (byte) item.getClass().getMethod("b").invoke(item);
			byte bitMask = (byte) 6;

			item.getClass().getMethod("a", Object.class)
					.invoke(item, (byte) (glow ? initBitMask | 1 << bitMask : initBitMask & ~(1 << bitMask)));

			FieldUtils.writeDeclaredField(newDataWatcher, "entries", copyEntries, true);

			for (Player p : this.viewers) {
				Reflect.sendPacket(p, Reflect.getNetClass("PacketPlayOutEntityMetadata")
						.getConstructor(int.class, Reflect.getNetClass("DataWatcher"), boolean.class)
						.newInstance(this.entity.getEntityId(), dataWatcher, true));
			}

		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void repatchTeam(Entity entity, GlowingColor color, boolean createOrAdd) {

		// TODO: 005. 03/05/2021 - Use player.getScoreboard() thing

		if (true) throw new NotImplementedException();

		try {

			Object packetScoreTeam = Reflect.getNetClass("PacketPlayOutScoreboardTeam").newInstance();

			FieldUtils.writeDeclaredField(packetScoreTeam, "i", 0, true);
			FieldUtils.writeDeclaredField(packetScoreTeam, "i", 3, true);
			FieldUtils.writeDeclaredField(packetScoreTeam, "a", color.getNSMColor(), true);
			FieldUtils.writeDeclaredField(packetScoreTeam, "e", false, true);
			FieldUtils.writeDeclaredField(packetScoreTeam, "f", false, true);

			if (createOrAdd) {
				FieldUtils.writeDeclaredField(packetScoreTeam, "g", color.getNSMColor());
			}

		}
		catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public enum GlowingColor {

		BLACK('0'),
		DARK_BLUE('1'),
		DARK_GREEN('2'),
		DARK_AQUA('3'),
		DARK_RED('4'),
		DARK_PURPLE('5'),
		GOLD('6'),
		GRAY('7'),
		DARK_GRAY('8'),
		BLUE('9'),
		GREEN('a'),
		AQUA('b'),
		RED('c'),
		LIGHT_PURPLE('d'),
		YELLOW('e'),
		WHITE('f'),
		NULL('_');

		private final char c;

		GlowingColor(char c) {
			this.c = c;

		}

		public Object getNSMColor() {
			try {
				final Class<?> clazz = Reflect.getNetClass("EnumChatFormat");
				return FieldUtils.getDeclaredField(clazz, this.name()).get(clazz);
			}
			catch (IllegalAccessException e) {
				e.printStackTrace();
				return null;
			}
		}

	}

} // end of glowing class