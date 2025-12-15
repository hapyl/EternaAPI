package me.hapyl.eterna.module.npc.appearance;

import me.hapyl.eterna.module.reflect.MetadataProvider;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;

import javax.annotation.Nonnull;

/**
 * Represents a behaviour {@link AppearanceFox} may have, which may include multiple at any given time.
 */
public enum FoxBehaviour implements MetadataProvider<Byte> {
    
    /**
     * The fox is sitting.
     */
    SITTING((byte) 0x01),
    
    /**
     * The fox is crunching.
     */
    CROUCHING((byte) 0x04),
    
    /**
     * The fox is interested in something. (Tilted head)
     */
    INTERESTED((byte) 0x08),
    
    /**
     * The fox is pouncing.
     */
    POUNCING((byte) 0x10),
    
    /**
     * The fox is sleeping.
     */
    SLEEPING((byte) 0x20),
    
    /**
     * The fox is faceplated.
     * <p>Note that you need to position the fox properly.</p>
     */
    FACEPLANTED((byte) 0x40),
    
    /**
     * The fox is defending.
     */
    DEFENDING((byte) 0x80);
    
    private final byte bit;
    
    FoxBehaviour(byte bit) {
        this.bit = bit;
    }
    
    @Nonnull
    @Override
    public Byte getAccessorValue() {
        return bit;
    }
    
    @Nonnull
    @Override
    public EntityDataAccessor<Byte> getAccessor() {
        class Holder {
            private static final EntityDataAccessor<Byte> accessor = EntityDataSerializers.BYTE.createAccessor(18);
        }
        
        return Holder.accessor;
    }
}
