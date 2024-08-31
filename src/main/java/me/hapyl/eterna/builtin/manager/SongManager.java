package me.hapyl.eterna.builtin.manager;

import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.player.song.Parser;
import me.hapyl.eterna.module.player.song.Song;
import me.hapyl.eterna.module.util.CollectionUtils;
import me.hapyl.eterna.module.util.NonnullConsumer;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class SongManager extends EternaManager<String, Song> {

    private final int itemsPerPage = 30;
    private boolean lock;

    SongManager(@Nonnull EternaPlugin eterna) {
        super(eterna);

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
        managing.clear();

        // Make sure to load async
        new BukkitRunnable() {
            @Override
            public void run() {
                final File directory = new File(eterna.getDataFolder() + "/songs");

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                final File[] songs = directory.listFiles();

                if (songs == null || songs.length == 0) {
                    lock = false;
                    eterna.getLogger().info("No songs founds, skipping.");
                    return;
                }

                eterna.getLogger().info("Found %s songs, parsing...".formatted(songs.length));

                for (File file : songs) {
                    if (!file.getName().endsWith(".nbs")) {
                        eterna.getLogger().warning("%s is in '/songs' folder but isn't a 'nbs' file, skipping.".formatted(file.getName()));
                        continue;
                    }

                    // Parse
                    final Song song = Parser.parse(file);

                    if (song != null) {
                        register(song.getDevName(), song);
                    }

                }

                eterna.getLogger().info("Successfully parsed %s songs.".formatted(managing.size()));
                andThen.accept(managing.size());
                lock = false;
            }
        }.runTaskAsynchronously(eterna);
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
        Song song = managing.get(name);

        // If not exact match, try checking for similar names
        if (song == null) {
            for (String otherName : managing.keySet()) {
                if (otherName.toLowerCase().contains(name.toLowerCase())) {
                    return managing.get(otherName);
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
    @Nonnull
    public List<String> listNames() {
        return new ArrayList<>(managing.keySet());
    }

    @Nullable
    public Song randomSong() {
        if (managing.isEmpty()) {
            return null;
        }

        return CollectionUtils.randomElement(managing.values(), null);
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
        page = Math.clamp(page, 0, maxPage());
        return listNames(page * itemsPerPage - itemsPerPage, page * itemsPerPage);
    }

    /**
     * Returns true if there are any songs; false otherwise.
     *
     * @return true if there are any songs; false otherwise.
     */
    public boolean anySongs() {
        return !managing.isEmpty();
    }

    /**
     * Returns max page.
     *
     * @return max page.
     */
    public int maxPage() {
        return managing.size() / itemsPerPage + 1;
    }

    public boolean isEmpty() {
        return managing.isEmpty();
    }
}
