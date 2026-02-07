package me.hapyl.eterna.module.reflect;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a provider for {@link SynchedEntityData}.
 *
 * @param <T> - The serializer type.
 */
public interface EntityDataProvider<T> {
    
    /**
     * Gets the value for the {@link EntityDataAccessor}.
     *
     * @return the value.
     */
    @NotNull
    T getValue();
    
    /**
     * Gets the {@link EntityDataAccessor}.
     *
     * @return the entity data accessor.
     */
    @NotNull
    EntityDataAccessor<T> getAccessor();
    
}
