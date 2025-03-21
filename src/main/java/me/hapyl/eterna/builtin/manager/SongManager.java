package me.hapyl.eterna.builtin.manager;

import com.google.common.collect.Lists;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.player.song.Parser;
import me.hapyl.eterna.module.player.song.Song;
import me.hapyl.eterna.module.util.CollectionUtils;
import me.hapyl.eterna.module.util.Runnables;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class SongManager extends EternaManager<String, Song> {
    
    private static final int ITEMS_PER_PAGE = 9; // 9 since chat fits 10 lines - 1 for info
    private static final String WILDCARD_RANDOM_SONG = "?";
    
    private boolean lock;
    
    SongManager(@Nonnull EternaPlugin eterna) {
        super(eterna);
        
        reload();
    }
    
    /**
     * Returns {@code true} if the registry is currently locked due to song parsing, {@code false} otherwise.
     *
     * @return {@code true} if the registry is currently locked due to song parsing, {@code false} otherwise.
     */
    public boolean isLock() {
        return lock;
    }
    
    /**
     * Reloads the songs from disk.
     * <br>
     * This will lock the registry until all songs are loaded and parsed.
     *
     * @return A completable future with a {@code Integer} representing the number of reloaded songs.
     */
    @Nonnull
    public CompletableFuture<Integer> reload() {
        lock = true;
        managing.clear();
        
        final CompletableFuture<Integer> future = new CompletableFuture<>();
        
        Runnables.runAsync(() -> {
            final File directory = new File(eterna.getDataFolder() + "/songs");
            
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            final File[] songs = directory.listFiles();
            
            if (songs == null || songs.length == 0) {
                lock = false;
                EternaLogger.info("No songs founds, skipping.");
                return;
            }
            
            EternaLogger.info("Found %s songs, parsing...".formatted(songs.length));
            
            for (File file : songs) {
                if (!file.getName().endsWith(".nbs")) {
                    EternaLogger.warn("%s is in '/songs' folder but isn't a 'nbs' file, skipping.".formatted(file.getName()));
                    continue;
                }
                
                // Parse
                final Song song = Parser.parse(file);
                
                if (song != null) {
                    register(song.fileName(), song);
                }
                
            }
            
            final int parsedSongCount = managing.size();
            
            future.complete(parsedSongCount);
            lock = false;
            
            EternaLogger.info("Successfully parsed %s songs!".formatted(parsedSongCount));
        });
        
        return future;
    }
    
    /**
     * Gets a {@link Song} by its file name.
     * <br>
     * This method first looks for an exact match of the name before trying to find a similar name.
     *
     * @param name - The file name of the song. Supports {@link #WILDCARD_RANDOM_SONG} as a random song wildcard, returning a random parsed song.
     * @return a song by its file name or {@code null} if no song was found by that name.
     */
    @Nullable
    public Song byName(@Nonnull String name) {
        if (name.equalsIgnoreCase(WILDCARD_RANDOM_SONG)) {
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
     * Gets a {@link List} of all parsed {@link Song}s.
     *
     * @return a {@link List} of all parsed {@link Song}s.
     */
    @Nonnull
    public List<String> listNames() {
        return Lists.newArrayList(managing.keySet());
    }
    
    /**
     * Gets a random {@link Song}, or {@code null} if there are no songs.
     *
     * @return a random {@link Song}, or {@code null} if there are no songs.
     */
    @Nullable
    public Song randomSong() {
        return CollectionUtils.randomElement(managing.values());
    }
    
    /**
     * Returns a sublist of names from the list of all available names, starting from the specified start index (inclusive)
     * and ending at the specified end index (exclusive).
     *
     * @param start - the index to start the sublist (inclusive)
     * @param end   - the index to end the sublist (exclusive)
     * @return a {@link List} of names between the specified start and end indices
     */
    @Nonnull
    public List<String> listNames(int start, int end) {
        final List<String> names = listNames();
        return names.subList(Math.max(0, start), Math.min(names.size(), end));
    }
    
    /**
     * Returns a list of names for the specified page.
     * <p>
     * The page is clamped between {@code 0} and the maximum available page.
     * <p>
     * The page size is defined by the constant {@link #ITEMS_PER_PAGE}.
     * This method calculates the appropriate start and end indices based on the requested page.
     *
     * @param page the page number (starting from 0)
     * @return a {@link List} of names for the requested page
     */
    @Nonnull
    public List<String> listNames(int page) {
        page = Math.clamp(page, 0, maxPage());
        
        return listNames(page * ITEMS_PER_PAGE - ITEMS_PER_PAGE, page * ITEMS_PER_PAGE);
    }
    
    /**
     * Returns {@code true} if there are any parsed songs, {@code false} otherwise.
     *
     * @return {@code true} if there are any parsed songs, {@code false} otherwise.
     */
    public boolean anySongs() {
        return !managing.isEmpty();
    }
    
    /**
     * Returns {@code true} if there are no songs parsed, {@code false} otherwise.
     *
     * @return {@code true} if there are no songs parsed, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return managing.isEmpty();
    }
    
    /**
     * Returns the maximum page number based on the total number of items
     * divided by the number of items per page.
     *
     * @return the maximum page number.
     */
    public int maxPage() {
        return managing.size() / ITEMS_PER_PAGE + 1;
    }
}
