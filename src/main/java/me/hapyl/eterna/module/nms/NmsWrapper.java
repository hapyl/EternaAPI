package me.hapyl.eterna.module.nms;

import me.hapyl.eterna.module.reflect.PacketFactory;

import javax.annotation.Nonnull;

/**
 * Represents an object which structure may be wrapped into a Nms variant, mostly used in {@link PacketFactory}.
 *
 * @param <N> - The Nms type.
 */
public interface NmsWrapper<N> {
    
    /**
     * Wraps this object into Nms variant.
     *
     * @return this object as Nms.
     */
    @Nonnull
    N wrapToNms();
    
}
