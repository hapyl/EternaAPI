package me.hapyl.eterna.module.hologram;

import me.hapyl.eterna.module.component.ComponentList;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Supplies a {@link ComponentList} for use in holograms.
 */
public interface ComponentSupplier {
    
    /**
     * Supplies the {@link ComponentList} for the given {@link Player}.
     *
     * @param player - The player to supply the components for.
     */
    @Nonnull
    ComponentList supply(@Nonnull Player player);
    
}