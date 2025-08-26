package me.hapyl.eterna.module.hologram;

import me.hapyl.eterna.module.util.list.StringList;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Supplies the lines of a hologram for a specific {@link Player}.
 */
public interface LineSupplier {
    
    /**
     * Provides the lines to be shown to the given {@link Player}.
     *
     * @param player - The player the hologram is being displayed to.
     * @return the lines to display.
     */
    @Nonnull
    StringList supply(@Nonnull Player player);
    
}
