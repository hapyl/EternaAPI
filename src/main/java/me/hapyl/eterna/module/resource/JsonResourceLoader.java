package me.hapyl.eterna.module.resource;

import com.google.gson.*;
import me.hapyl.eterna.module.util.BukkitUtils;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a resource loader for {@link JsonObject}.
 *
 * <p>
 *
 * </p>
 * <p><b>Note that the underlying JSON on must be structured as an {@link JsonObject}, not a {@link JsonArray}!</b></p>
 */
public class JsonResourceLoader extends AbstractResourceLoader {
    
    private static final Gson GSON;
    
    static {
        GSON = new GsonBuilder().setPrettyPrinting().create();
    }
    
    private JsonObject root;
    
    /**
     * Creates a new {@link JsonResourceLoader}.
     *
     * @param plugin       - The plugin resource belongs to.
     * @param absolutePath - The absolute path of the resource.
     * @throws IllegalArgumentException if the resource doesn't exist in plugins {@code jar} file.
     */
    public JsonResourceLoader(@NotNull Plugin plugin, @NotNull String absolutePath) {
        super(plugin, BukkitUtils.unsureFileExtension(absolutePath, ".json"));
    }
    
    /**
     * Gets the root {@link JsonObject}.
     *
     * @return the root json element.
     * @throws NullPointerException if the JSON is unloaded, which should never happen.
     */
    @NotNull
    public JsonObject getJson() {
        return Objects.requireNonNull(root, "Json unloaded!");
    }
    
    /**
     * Gets a {@link JsonElement} by the given key from the {@code root} {@link JsonObject}.
     *
     * @param key - The key retrieve the element.
     * @return the JSON element, or {@link JsonNull} if no elements at the given {@code key}.
     */
    @NotNull
    public JsonElement get(@NotNull String key) {
        final JsonElement element = root.get(key);
        
        return element != null ? element : JsonNull.INSTANCE;
    }
    
    /**
     * Sets the given {@link JsonElement} at the given key in the {@code root} {@link JsonObject}.
     *
     * @param key     - The key to set the element.
     * @param element - The element to set.
     */
    public void set(@NotNull String key, @NotNull JsonElement element) {
        this.root.add(key, element);
    }
    
    /**
     * Removes the value at the given key in the {@code root} {@link JsonObject}.
     *
     * @param key - The key where to remove the value.
     */
    public void remove(@NotNull String key) {
        this.root.remove(key);
    }
    
    @NotNull
    @Override
    public CompletableFuture<Void> reload() {
        final CompletableFuture<Void> future = super.reload();
        
        try {
            final JsonElement rootElement = GSON.fromJson(Files.newBufferedReader(file.toPath()), JsonElement.class);
            
            if (!(rootElement instanceof JsonObject rootObject)) {
                throw new IllegalArgumentException("Json root must be JsonObject, not %s!".formatted(rootElement.getClass().getSimpleName()));
            }
            
            this.root = rootObject;
            future.complete(null);
        }
        catch (Exception ex) {
            future.completeExceptionally(ex);
            throw new RuntimeException(ex);
        }
        
        return future;
    }
    
    @NotNull
    @Override
    public CompletableFuture<Void> saveToFile() {
        final CompletableFuture<Void> future = super.saveToFile();
        
        try (Writer writer = Files.newBufferedWriter(file.toPath())) {
            GSON.toJson(root, writer);
            future.complete(null);
        }
        catch (IOException ex) {
            future.completeExceptionally(ex);
        }
        
        return future;
    }
}
