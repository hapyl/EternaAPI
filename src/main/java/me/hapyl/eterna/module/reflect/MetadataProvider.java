package me.hapyl.eterna.module.reflect;

import net.minecraft.network.syncher.EntityDataAccessor;

import javax.annotation.Nonnull;

public interface MetadataProvider<T> {
    
    @Nonnull
    T getAccessorValue();
    
    @Nonnull
    EntityDataAccessor<T> getAccessor();
    
}
