package me.hapyl.eterna.module.npc.appearance;

import me.hapyl.eterna.module.annotate.StrictEnumOrdinal;
import me.hapyl.eterna.module.reflect.EntityDataProvider;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a fox type for {@link AppearanceFox}.
 */
@StrictEnumOrdinal
public enum FoxType implements EntityDataProvider<Integer> {
    
    /**
     * A red fox appearance.
     */
    RED,
    
    /**
     * A white fox appearance.
     */
    WHITE;
    
    @NotNull
    @Override
    public Integer getValue() {
        return this.ordinal();
    }
    
    @NotNull
    @Override
    public EntityDataAccessor<Integer> getAccessor() {
        class Holder {
            private static final EntityDataAccessor<Integer> accessor = EntityDataSerializers.INT.createAccessor(17);
        }
        
        return Holder.accessor;
    }
    
}
