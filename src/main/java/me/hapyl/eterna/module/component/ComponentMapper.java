package me.hapyl.eterna.module.component;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

/**
 * A functional interface that maps a {@link Component} to another {@link Component}.
 */
@FunctionalInterface
public interface ComponentMapper {
    
    /**
     * Maps the given {@link Component}.
     *
     * @param component - The component to map.
     * @return the mapped component.
     */
    @NotNull
    Component map(@NotNull Component component);
    
    /**
     * Gets a {@link ComponentMapper} that always returns its input argument.
     *
     * @return Gets a component mapper  that always returns its input argument.
     */
    @NotNull
    static ComponentMapper self() {
        return _component -> _component;
    }
    
}
