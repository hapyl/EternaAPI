package me.hapyl.eterna.module.player.song;

import com.google.common.collect.Maps;
import me.hapyl.eterna.EternaHandler;
import me.hapyl.eterna.EternaKey;
import me.hapyl.eterna.EternaLogger;
import me.hapyl.eterna.EternaPlugin;
import me.hapyl.eterna.module.component.Components;
import me.hapyl.eterna.module.util.CollectionUtils;
import me.hapyl.eterna.Runnables;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Represents an internal {@link Song} handler.
 */
public final class SongHandler extends EternaHandler<String, Song> {
    
    /**
     * Defines the wildcard for a random {@link Song}.
     */
    @NotNull
    public static final String WILDCARD_RANDOM_SONG = "?";
    
    /**
     * Defines the file prefix for a {@code nbs} file.
     */
    @NotNull
    public static final String NBS_PREFIX = ".nbs";
    
    static SongHandler handler;
    
    private final File songDirectory;
    private boolean lock;
    
    public SongHandler(@NotNull EternaKey key, @NotNull EternaPlugin eterna) {
        super(key, eterna);
        
        this.songDirectory = new File(eterna.getDataFolder() + "/songs");
        this.reload0();
        
        handler = this;
    }
    
    @NotNull
    public CompletableFuture<Integer> reload0() {
        final CompletableFuture<Integer> future = new CompletableFuture<>();
        
        // Lock the registry
        this.lock = true;
        this.registry.clear();
        
        // Parse files async
        Runnables.async(() -> {
            // Create directory if it doesn't exist
            if (!this.songDirectory.exists()) {
                if (!this.songDirectory.mkdirs()) {
                    EternaLogger.severe("Could not create `%s` directory!".formatted(this.songDirectory.getAbsolutePath()));
                    return;
                }
            }
            
            final File[] songFiles = this.songDirectory.listFiles();
            
            // No songs in the directory skip and notify
            if (songFiles == null || songFiles.length == 0) {
                EternaLogger.info("No songs founds, skipping.");
                
                this.lock = false;
                return;
            }
            
            // Otherwise notify that the parsing started
            EternaLogger.info("Found %s songs, parsing...".formatted(songFiles.length));
            
            for (File file : songFiles) {
                // Skip non-`.nbs` files
                if (!file.getName().endsWith(NBS_PREFIX)) {
                    EternaLogger.warn("`%s` is in '/songs' folder but isn't a '%s' file, skipping!".formatted(file.getName(), NBS_PREFIX));
                    continue;
                }
                
                // Parse
                final SongParser parser = new SongParser(file);
                final Song song = parser.parse();
                
                if (song == null) {
                    EternaLogger.warn("Error parsing `%s`!");
                    continue;
                }
                
                register(song.getFileName(), song);
            }
            
            // Unlocks the registry
            this.lock = false;
            
            // Complete future
            final int parsedSongCount = registry.size();
            future.complete(parsedSongCount);
            
            // Notify success
            EternaLogger.info("Successfully parsed %s songs!".formatted(parsedSongCount));
        });
        
        return future;
    }
    
    @Override
    @NotNull
    protected Map<String, Song> makeNewMap() {
        return Maps.newTreeMap();
    }
    
    /**
     * Gets an <b>immutable</b> {@link List} of all registered {@link Song} files names.
     *
     * @return an <b>immutable</b> list of all registered songs files names.
     */
    @NotNull
    public static List<String> listNames() {
        return List.copyOf(handler.registry.keySet());
    }
    
    /**
     * Gets an <b>immutable</b> {@link List} of all registered {@link Song}.
     *
     * @return an <b>immutable</b> list of all registered songs.
     */
    @NotNull
    public static List<Song> listSongs() {
        return List.copyOf(handler.registry.values());
    }
    
    /**
     * Gets whether any {@link Song} are registered.
     *
     * <p>
     * Note that this may return {@code false} if songs are still being parsed.
     * </p>
     *
     * @return {@code true} if any songs are registered; {@code false} otherwise.
     */
    public static boolean anySongs() {
        return !handler.registry.isEmpty();
    }
    
    /**
     * Gets a {@link Song} by the given {@code name} wrapped in an {@link Optional}.
     *
     * <p>
     * This method first attempts an exact file-name match; if none is found, it falls back to a partial match on both
     * {@link Song#getFileName()} and {@link Song#getName()}.
     * </p>
     *
     * <p>
     * If the wildcard {@link #WILDCARD_RANDOM_SONG} is passed, a random {@link Song} is returned instead.
     * </p>
     *
     * @param name - The name to query by.
     * @return a song wrapped in an optional, or an empty optional if nothing found.
     */
    @NotNull
    public static Optional<Song> byName(@NotNull String name) {
        if (name.equalsIgnoreCase(WILDCARD_RANDOM_SONG)) {
            return randomSong();
        }
        
        String queryName = name.trim();
        
        // Try to query by the exact match
        Song song = handler.registry.get(queryName);
        
        // If no exact match, try checking for similarity
        queryName = queryName.toLowerCase();
        
        if (song == null) {
            for (Song topentialSong : handler.registry.values()) {
                final String songFileName = topentialSong.getFileName().toLowerCase();
                final String songName = Components.toString(topentialSong.getName()).toLowerCase();
                
                if (songFileName.contains(queryName) || songName.contains(queryName)) {
                    song = topentialSong;
                    break;
                }
            }
        }
        
        return Optional.ofNullable(song);
    }
    
    /**
     * Gets a random {@link Song} wrapped in an {@link Optional}.
     *
     * @return a random song wrapped in an optional.
     */
    @NotNull
    public static Optional<Song> randomSong() {
        return handler.registry.isEmpty()
               ? Optional.empty()
               : Optional.of(CollectionUtils.randomElementOrFirst(handler.registry.values()));
    }
    
    /**
     * Gets whether the handler is currently locked, meaning it's still parsing songs.
     *
     * @return {@code true} if the handler is currently locked, meaning it's still parsing songs; {@code false} otherwise.
     */
    public static boolean isLock() {
        return handler.lock;
    }
    
    /**
     * Performs a reload operation by clearing all existing songs and reading from the file.
     *
     * @return a completable future that is completed upon reload finishing with the number of songs reloaded.
     */
    @NotNull
    public static CompletableFuture<Integer> reload() {
        return handler.reload0();
    }
}
