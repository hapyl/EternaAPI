package me.hapyl.eterna.module.reflect.packet.wrapped;

import me.hapyl.eterna.module.reflect.Reflect;
import net.minecraft.network.protocol.Packet;

import javax.annotation.Nonnull;

/**
 * {@link WrappedPacket} is a collection of <b>statically</b> wrapped packets, with namespaces representing Mojang mappings.
 * <br>
 * {@link WrappedPacket}s are <b>not</b> backed up by the raw {@link Packet}, therefore, writing should be done into the <I>raw</I> packet!
 */
public class WrappedPacket<P> {

    protected final P packet;

    public WrappedPacket(P packet) {
        this.packet = packet;
    }

    @Nonnull
    public P getPacket() {
        return packet;
    }

    protected <T> T readField(@Nonnull String fieldName, Class<T> clazz) {
        return Reflect.getDeclaredFieldValue(packet, fieldName, clazz);
    }

    protected <T> void writeField(@Nonnull String fieldName, T value) {
        Reflect.setDeclaredFieldValue(packet, fieldName, value);
    }

}
