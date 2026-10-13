package me.hapyl.eterna.module.component;

import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an interface that creates a hoverable context.
 */
public interface Hoverable {
    
    /**
     * Creates a hover event.
     *
     * @return a new hover event.
     */
    @NotNull HoverEvent<?> createHoverEvent();
    
}
