package me.hapyl.eterna.module.npc.appearance;

import me.hapyl.eterna.module.annotate.DefaultEnumValue;
import me.hapyl.eterna.module.annotate.StrictEnumOrdinal;
import me.hapyl.eterna.module.reflect.MetadataProvider;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;

import javax.annotation.Nonnull;

/**
 * Represents a fox type for {@link AppearanceFox}.
 */
@StrictEnumOrdinal
public enum FoxType implements MetadataProvider<Integer> {
    
    /**
     * A red fox appearance.
     */
    @DefaultEnumValue
    RED,
    
    /**
     * A white fox appearance.
     */
    WHITE;
    
    @Nonnull
    @Override
    public Integer getAccessorValue() {
        return this.ordinal();
    }
    
    @Nonnull
    @Override
    public EntityDataAccessor<Integer> getAccessor() {
        class Holder {
            private static final EntityDataAccessor<Integer> accessor = EntityDataSerializers.INT.createAccessor(17);
        }
        
        return Holder.accessor;
    }
    
}
