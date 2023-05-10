package me.hapyl.spigotutils.module.player.song;

import me.hapyl.spigotutils.module.math.Numbers;
import me.hapyl.spigotutils.module.util.CollectionUtils;
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

    private final int ITEMS_PER_PAGE = 30;
    private boolean lock;

    public SongRegistry(JavaPlugin plugin) {
        super(plugin);

        reload(NonnullConsumer.empty());
    }

    /**
     * Returns true if registry is locked; false otherwise.
     * If the registry is locked, songs cannot be retrieved or added
     * outside the registry.
     *
     * @return true if the registry is locked; false otherwise.
     */
    public boolean isLock() {
        return lock;
    }

    /**
     * Clears all the cached songs and reloads them from the disk.
     * This is done asynchronously.
     *
     * @param andThen - Consumer of how many songs was loaded to execute <b>after</b> the load.
     */
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

    /**
     * Returns a song by its name.
     * This checks both for exact match and for containing name.
     * Use '?' as input to get a random song.
     *
     * @param name - Song to get.
     * @return a song by its name or null if none found.
     */
    @Nullable
    public Song byName(@Nonnull String name) {
        if (name.equalsIgnoreCase("?")) {
            return randomSong();
        }

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

    @Nullable
    public Song randomSong() {
        if (registry.isEmpty()) {
            return null;
        }

        return CollectionUtils.randomElement(registry.values(), null);
    }

    /**
     * Returns a sublist of song names from start to end
     *
     * @param start - Start. Cannot be negative.
     * @param end   - End. Cannot be > names.size().
     * @return a sublist from start to end of song names.
     */
    public List<String> listNames(int start, int end) {
        final List<String> names = listNames();
        return names.subList(Math.max(0, start), Math.min(names.size(), end));
    }

    /**
     * Returns a sublist of song names based on a page.
     *
     * @param page - Page. Starts at 1.
     * @return a sublist of song names based on a page.
     */
    public List<String> listNames(int page) {
        page = Numbers.clamp(page, 0, maxPage());
        return listNames(page * ITEMS_PER_PAGE - ITEMS_PER_PAGE, page * ITEMS_PER_PAGE);
    }

    /**
     * Returns true if there are any songs; false otherwise.
     *
     * @return true if there are any songs; false otherwise.
     */
    public boolean anySongs() {
        return !registry.isEmpty();
    }

    /**
     * Returns max page.
     *
     * @return max page.
     */
    public int maxPage() {
        return registry.size() / ITEMS_PER_PAGE + 1;
    }
}
