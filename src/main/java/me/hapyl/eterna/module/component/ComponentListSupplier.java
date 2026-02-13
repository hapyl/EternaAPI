package me.hapyl.eterna.module.component;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Supplies a {@link ComponentList} for the given {@link Player}.
 */
public interface ComponentListSupplier {
    
    /**
     * Supplies the {@link ComponentList} for the given {@link Player}.
     *
     * @param player - The player to supply the components for.
     */
    @NotNull
    ComponentList supply(@NotNull Player player);
    
}