package me.hapyl.spigotutils.module.player.song;


import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class SongStorage {

	protected static final Map<String, Song> byName = new HashMap<>();

	@Nullable
	public static Song getSong(String name) {
		return byName.getOrDefault(name.toLowerCase(Locale.ROOT), null);
	}

	public static boolean alreadyParsed(String name) {
		return getSong(name) != null;
	}

	public static void addSong(String devName, Song song) {
		byName.put(devName.toLowerCase(Locale.ROOT), song);
	}

	public static Map<String, Song> getByName() {
		return byName;
	}

	public static Set<String> getNames() {
		return byName.keySet();
	}
}
