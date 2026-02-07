package me.hapyl.eterna.module.inventory.builder;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a component modifier for {@link ItemBuilder}.
 *
 * @param <E> - The component type.
 */
public interface ComponentModifier<E> {
    
    /**
     * Modifies the given component.
     *
     * @param e - The component to modify.
     */
    void modify(@NotNull E e);
    
}
