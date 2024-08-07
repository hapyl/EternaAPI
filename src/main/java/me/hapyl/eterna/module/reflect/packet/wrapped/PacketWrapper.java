package me.hapyl.eterna.module.reflect.packet.wrapped;

import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

public class PacketWrapper<T extends PacketListener, P extends Packet<T>, W extends WrappedPacket<P>> {

    private final Class<P> clazz;
    private final Function<P, W> function;

    PacketWrapper(Class<P> clazz, Function<P, W> function) {
        this.clazz = clazz;
        this.function = function;
    }

    @Nonnull
    public Class<P> getClazz() {
        return clazz;
    }

    /**
     * Wraps the given {@link Packet} if the type matches.
     *
     * @param packet - Packet to warp.
     * @return the wrapped packet or null.
     */
    @Nullable
    public W wrap(@Nonnull Packet<?> packet) {
        if (clazz.isInstance(packet)) {
            return function.apply(clazz.cast(packet));
        }

        return null;
    }

    /**
     * Wraps the given {@link Packet} if the type matches, throws an exception otherwise.
     *
     * @param packet - Packet to wrap.
     * @return the wrapped packet.
     * @throws IllegalArgumentException if the packet type does not match.
     */
    @Nonnull
    public W wrapOrThrow(@Nonnull Packet<?> packet) {
        final W wrapped = wrap(packet);

        if (wrapped != null) {
            return wrapped;
        }

        throw new IllegalArgumentException("Packet %s cannot be wrapped unto Wrapped%s!".formatted(
                packet.getClass().getSimpleName(),
                clazz.getSimpleName()
        ));
    }

}
