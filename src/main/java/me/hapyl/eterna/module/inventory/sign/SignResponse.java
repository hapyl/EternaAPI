package me.hapyl.eterna.module.inventory.sign;

import me.hapyl.eterna.module.annotate.Synchronized;
import me.hapyl.eterna.module.util.CollectionUtils;
import me.hapyl.eterna.module.util.TypeConverter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a response for {@link SignInput}, containing the modified {@code lines}.
 */
public final class SignResponse {
    
    private final Player player;
    private final String[] lines;
    
    @ApiStatus.Internal
    SignResponse(@NotNull Player player, @NotNull String[] lines) {
        this.player = player;
        this.lines = lines;
    }
    
    /**
     * Gets the responding {@link Player}.
     *
     * @return the responding player.
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Gets an element wrapped in {@link TypeConverter} at the given {@code index}
     *
     * @param index - The index to retrieve the element at.
     * @return the element at the given {@code index} wrapped in type converter.
     */
    @NotNull
    public TypeConverter get(int index) {
        return TypeConverter.fromNullable(CollectionUtils.get(lines, index).orElse(null));
    }
    
    /**
     * Gets the {@link String} representation of this {@link SignResponse}, consisting of text
     * from the first line, up to the {@link SignInput#DASHED_LINE}.
     *
     * @return the string representation of this sign response.
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        
        for (int i = 0; i < lines.length; i++) {
            final String line = lines[i];
            
            // Stop at the separator
            if (line.equals(SignInput.DASHED_LINE)) {
                break;
            }
            
            // Ignore blank lines
            if (line.isEmpty()) {
                continue;
            }
            
            // Put spaces between lines
            if (i != 0) {
                builder.append(" ");
            }
            
            builder.append(line.trim());
        }
        
        return builder.toString();
    }
    
    /**
     * A helper utility method that synchronized the given {@link Runnable} to be run at the next server tick.
     *
     * <p>
     * Since the response is handled asynchronously, in order to use it in a synchronized context, you must wrap is using this method.
     * </p>
     *
     * @param plugin   - The plugin delegate.
     * @param runnable - The runnable to run.
     */
    @Synchronized
    public void synchronize(@NotNull Plugin plugin, @NotNull Runnable runnable) {
        Bukkit.getScheduler().runTask(plugin, runnable);
    }
    
}
