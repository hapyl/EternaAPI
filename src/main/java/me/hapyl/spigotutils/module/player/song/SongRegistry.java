package me.hapyl.spigotutils.module.player.song;

import me.hapyl.spigotutils.module.util.NonnullConsumer;
import me.hapyl.spigotutils.registry.Registry;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SongRegistry extends Registry<String, Song> {

    private boolean lock;

    public SongRegistry(JavaPlugin plugin) {
        super(plugin);

        reload(NonnullConsumer.empty());
    }

    public boolean isLock() {
        return lock;
    }

    public void reload(@Nonnull NonnullConsumer<Integer> andThen) {
        lock = true;
        registry.clear();

        new BukkitRunnable() {
            @Override
            public void run() {
                final File directory = new File(getPlugin().getDataFolder() + "/songs");

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                final File[] songs = directory.listFiles();

                if (songs == null) {
                    lock = false;
                    logger.info("No songs founds, skipping.");
                    return;
                }

                logger.info("Found %s songs, parsing...".formatted(songs.length));

                for (File file : songs) {
                    if (!file.getName().endsWith(".nbs")) {
                        logger.warning("%s is in '/songs' folder but isn't a 'nbs' file, skipping.".formatted(file.getName()));
                        continue;
                    }

                    // Parse
                    final Song song = Parser.parse(file);

                    if (song != null) {
                        register(song.getDevName(), song);
                    }

                }

                logger.info("Successfully parsed %s songs.".formatted(registry.size()));

                andThen.accept(registry.size());
                lock = false;
            }
        }.runTaskAsynchronously(getPlugin());
    }

    @Nullable
    public Song byName(String name) {
        name = name.trim();
        Song song = byKey(name);

        // If not exact match, try checking for similar names
        if (song == null) {
            for (String otherName : registry.keySet()) {
                if (otherName.toLowerCase().contains(name.toLowerCase())) {
                    return byKey(otherName);
                }
            }
        }

        return song;
    }

    /**
     * Returns copy of song names.
     *
     * @return copy of song names.
     */
    public List<String> listNames() {
        return new ArrayList<>(registry.keySet());
    }

    // TODO (hapyl): 004, May 4, 2023: Add pages for songs, it's impossible to get through if there is a lot of them
    public List<String> listName(int start, int end) {
        return listNames().subList(start, end);
    }

    public boolean anySongs() {
        return !lock && !registry.isEmpty();
    }
}
