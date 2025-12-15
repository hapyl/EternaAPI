package me.hapyl.eterna.module.npc.appearance;

import me.hapyl.eterna.module.annotate.StrictEnumOrdinal;
import me.hapyl.eterna.module.reflect.MetadataProvider;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;

import javax.annotation.Nonnull;

/**
 * Represents a color of a {@link AppearanceSheep} wool.
 */
@StrictEnumOrdinal
public enum SheepColor implements MetadataProvider<Byte> {
    
    /**
     * White wool.
     */
    WHITE,
    
    /**
     * Orange wool.
     */
    ORANGE,
    
    /**
     * Magenta wool.
     */
    MAGENTA,
    
    /**
     * Light blue wool.
     */
    LIGHT_BLUE,
    
    /**
     * Yellow wool.
     */
    YELLOW,
    
    /**
     * Lime wool.
     */
    LIME,
    
    /**
     * Pink wool.
     */
    PINK,
    
    /**
     * Gray wool.
     */
    GRAY,
    
    /**
     * Light gray wool.
     */
    LIGHT_GRAY,
    
    /**
     * Cyan wool.
     */
    CYAN,
    
    /**
     * Purple wool.
     */
    PURPLE,
    
    /**
     * Blue wool.
     */
    BLUE,
    
    /**
     * Brown wool.
     */
    BROWN,
    
    /**
     * Green wool.
     */
    GREEN,
    
    /**
     * Red wool.
     */
    RED,
    
    /**
     * Black wool.
     */
    BLACK;
    
    @Nonnull
    @Override
    public Byte getAccessorValue() {
        return (byte) this.ordinal();
    }
    
    @Nonnull
    @Override
    public EntityDataAccessor<Byte> getAccessor() {
        class Holder {
            private static final EntityDataAccessor<Byte> accessor = EntityDataSerializers.BYTE.createAccessor(17);
        }
        
        return Holder.accessor;
    }
    
}
