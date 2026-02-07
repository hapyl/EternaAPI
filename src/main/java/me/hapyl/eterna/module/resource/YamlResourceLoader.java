package me.hapyl.eterna.module.resource;

import me.hapyl.eterna.module.annotate.SupportsNestedKey;
import me.hapyl.eterna.module.util.BukkitUtils;
import me.hapyl.eterna.module.util.TypeConverter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a resource loader for {@link YamlConfiguration}.
 */
public class YamlResourceLoader extends AbstractResourceLoader {
    
    private YamlConfiguration yaml;
    
    /**
     * Creates a new {@link YamlResourceLoader}.
     *
     * @param plugin       - The plugin resource belongs to.
     * @param absolutePath - The absolute path of the resource.
     * @throws IllegalArgumentException if the resource doesn't exist in plugins {@code jar} file.
     */
    public YamlResourceLoader(@NotNull Plugin plugin, @NotNull String absolutePath) {
        super(plugin, BukkitUtils.unsureFileExtension(absolutePath, ".yml", ".yaml"));
    }
    
    /**
     * Gets the {@link YamlConfiguration}.
     *
     * @return the YAML configuration.
     * @throws NullPointerException if the YAML is unloaded, which should never happen.
     */
    @NotNull
    public YamlConfiguration getYaml() {
        return Objects.requireNonNull(yaml, "Yaml unloaded!");
    }
    
    /**
     * Gets a {@link TypeConverter} at the given key.
     *
     * @param key - The key to retrieve an object.
     * @return a type converter at the given key.
     */
    @NotNull
    public TypeConverter get(@NotNull @SupportsNestedKey String key) {
        return TypeConverter.fromNullable(yaml.get(key));
    }
    
    /**
     * Sets the value at the given key.
     *
     * @param key   - The key to set the value to.
     * @param value - The value to set, or {@code null} to remove.
     * @param <T>   - The type of the file.
     */
    public <T> void set(@NotNull @SupportsNestedKey String key, @Nullable T value) {
        this.yaml.set(key, value);
    }
    
    @NotNull
    @Override
    public CompletableFuture<Void> reload() {
        final CompletableFuture<Void> future = super.reload();
        
        try {
            this.yaml = new YamlConfiguration();
            this.yaml.load(file);
            
            future.complete(null);
        }
        catch (Exception ex) {
            future.completeExceptionally(ex);
        }
        
        return future;
    }
    
    @Override
    @NotNull
    public CompletableFuture<Void> saveToFile() {
        final CompletableFuture<Void> future = super.saveToFile();
        
        try {
            this.yaml.save(file);
            
            future.complete(null);
        }
        catch (IOException ex) {
            future.completeExceptionally(ex);
        }
        
        return future;
    }
}
