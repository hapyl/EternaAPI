package me.hapyl.eterna.module.util;

import org.jetbrains.annotations.NotNull;

/**
 * Interface representing an {@link Enum} that supports ordered navigation.
 *
 * @param <E> - The enum type that implements this interface.
 */
public interface OrderedEnum<E extends Enum<E>> {
    
    /**
     * Gets the current {@link Enum} value.
     *
     * @return - The current enum value.
     */
    @NotNull
    E current();
    
    /**
     * Gets the next {@link Enum} value in order, cycling to the first if at the end.
     *
     * @return The next enum value.
     */
    @NotNull
    default E next() {
        final E current = current();
        final Class<E> enumClass = current.getDeclaringClass();
        
        return Enums.getNextValue(enumClass, current);
    }
    
    /**
     * Gets the previous {@link Enum} value in order, cycling to the last if at the beginning.
     *
     * @return The previous enum value.
     */
    @NotNull
    default E previous() {
        final E current = current();
        final Class<E> enumClass = current.getDeclaringClass();
        
        return Enums.getPreviousValue(enumClass, current);
    }
    
}