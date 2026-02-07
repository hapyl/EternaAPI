package me.hapyl.eterna.module.resource;

import me.hapyl.eterna.module.annotate.SupportsNestedKey;
import me.hapyl.eterna.module.util.Reloadable;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.io.File;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a resource loader, allowing easier control over config files.
 *
 * <p>Note that the target file <b>must</b> exist at the exact same path in the {@code jar} file!</p>
 *
 * @see YamlResourceLoader
 * @see JsonResourceLoader
 */
public abstract class AbstractResourceLoader implements Reloadable {
    
    protected final Plugin plugin;
    protected final File file;
    
    private final String absolutePath;
    
    /**
     * Creates a new {@link AbstractResourceLoader}.
     *
     * @param plugin       - The plugin resource belongs to.
     * @param absolutePath - The absolute path of the resource.
     * @throws IllegalArgumentException if the resource doesn't exist in plugins {@code jar} file.
     */
    public AbstractResourceLoader(@NotNull Plugin plugin, @NotNull @SupportsNestedKey String absolutePath) {
        this.plugin = plugin;
        this.file = plugin.getDataFolder().toPath().resolve(absolutePath).toFile();
        this.absolutePath = absolutePath;
        
        // Reload the file to instantiate the fields
        this.reload();
    }
    
    /**
     * Gets the {@link Plugin} resource belongs to.
     *
     * @return the plugin resource belongs to.
     */
    @NotNull
    public Plugin getPlugin() {
        return plugin;
    }
    
    /**
     * Gets the {@link File} associated with the resource.
     *
     * @return the file associated with the resource.
     */
    @NotNull
    public File getFile() {
        return file;
    }
    
    /**
     * Attempts to reload the resource from disk.
     *
     * <p>
     * If the file was deleted from disk, it will be recreated with the default values.
     * </p>
     *
     * @return a future that completes when the reload is finished, or exceptionally if the reload fails.
     */
    @NotNull
    @Override
    @OverridingMethodsMustInvokeSuper
    public CompletableFuture<Void> reload() {
        // Ensure the file exists, or create it if it doesn't
        if (!this.file.exists()) {
            this.plugin.saveResource(absolutePath, false);
        }
        
        return new CompletableFuture<>();
    }
    
    /**
     * Attempts to save the resource to disk.
     *
     * @return a future that completes when the save is finished, or exceptionally if the save fails.
     */
    @NotNull
    @OverridingMethodsMustInvokeSuper
    public CompletableFuture<Void> saveToFile() {
        return new CompletableFuture<>();
    }
    
}
