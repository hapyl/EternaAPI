package me.hapyl.eterna.module.npc.appearance;

import me.hapyl.eterna.module.reflect.EntityDataProvider;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link AppearanceMannequin} skin part.
 */
public enum SkinPart implements EntityDataProvider<Byte> {
    
    /**
     * Whether the cape is visible.
     */
    CAPE((byte) 0x01),
    
    /**
     * Whether the jacket is visible.
     */
    JACKET((byte) 0x02),
    
    /**
     * Whether the left sleeve is visible.
     */
    LEFT_SLEEVE((byte) 0x04),
    
    /**
     * Whether the right sleeve is visible.
     */
    RIGHT_SLEEVE((byte) 0x08),
    
    /**
     * Whether the left leg is visible.
     */
    LEFT_LEG((byte) 0x10),
    
    /**
     * Whether the right leg is visible.
     */
    RIGHT_LEG((byte) 0x20),
    
    /**
     * Whether the hat is visible.
     */
    HAT((byte) 0x40);
    
    private final byte bit;
    
    SkinPart(byte bit) {
        this.bit = bit;
    }
    
    @NotNull
    @Override
    public Byte getValue() {
        return bit;
    }
    
    @NotNull
    @Override
    public EntityDataAccessor<Byte> getAccessor() {
        class Holder {
            private static final EntityDataAccessor<Byte> accessor = EntityDataSerializers.BYTE.createAccessor(16);
        }
        
        return Holder.accessor;
    }
}
