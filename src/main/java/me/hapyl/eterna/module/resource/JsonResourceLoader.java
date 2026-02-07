package me.hapyl.eterna.module.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import me.hapyl.eterna.module.util.BukkitUtils;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a resource loader for {@link JsonElement}.
 */
public class JsonResourceLoader extends AbstractResourceLoader {
    
    private static final Gson gson;
    
    static {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }
    
    private JsonElement json;
    
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
     * Gets the root {@link JsonElement}.
     *
     * @return the root json element.
     * @throws NullPointerException if the JSON is unloaded, which should never happen.
     */
    @NotNull
    public JsonElement getJson() {
        return Objects.requireNonNull(json, "Json unloaded!");
    }
    
    /**
     * Gets a {@link JsonElement} by the given key from the {@code root} {@link JsonElement}.
     *
     * @param key - The key retrieve the element.
     * @return the JSON element, or {@link JsonNull} if no elements at the given {@code key}.
     */
    @NotNull
    public JsonElement get(@NotNull String key) {
        final JsonElement element = json.getAsJsonObject().get(key);
        
        return element != null ? element : JsonNull.INSTANCE;
    }
    
    @NotNull
    @Override
    public CompletableFuture<Void> reload() {
        final CompletableFuture<Void> future = super.reload();
        
        try {
            json = gson.fromJson(Files.newBufferedReader(file.toPath()), JsonElement.class);
            future.complete(null);
        }
        catch (Exception ex) {
            future.completeExceptionally(ex);
        }
        
        return future;
    }
    
    @NotNull
    @Override
    public CompletableFuture<Void> saveToFile() {
        final CompletableFuture<Void> future = super.saveToFile();
        
        try (Writer writer = Files.newBufferedWriter(file.toPath())) {
            gson.toJson(json, writer);
            future.complete(null);
        }
        catch (IOException ex) {
            future.completeExceptionally(ex);
        }
        
        return future;
    }
}
