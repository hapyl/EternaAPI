package me.hapyl.eterna.module.reflect.packet.wrapped;

import me.hapyl.eterna.module.reflect.Reflect;
import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a base {@link WrappedPacket}, which wraps a raw {@link Packet} into a readable and documented wrapper.
 *
 *
 * <p>
 * Note that wrapped packets are <b>immutable</b> and do not support mutations; any changes must be made to a raw {@link Packet} object.
 * </p>
 */
public class WrappedPacket<P> {
    
    protected final P packet;
    
    WrappedPacket(@NotNull P packet) {
        this.packet = packet;
    }
    
    /**
     * Gets the raw {@link Packet}.
     *
     * @return the raw packet.
     */
    @NotNull
    public P getPacket() {
        return packet;
    }
    
    /**
     * A helper method for reading raw {@link Packet} fields.
     *
     * <p>
     * Note that this method is <b>sensitive</b> to both {@code fieldName} and the class type. If either
     * don't match the actual field, an {@link IllegalArgumentException} is thrown!
     * </p>
     *
     * @param fieldName - The field name.
     * @param clazz     - The field type.
     * @param <T>       - The field type.
     * @return the field value.
     */
    @NotNull
    public <T> T readField(@NotNull String fieldName, @NotNull Class<T> clazz) throws IllegalArgumentException {
        return Reflect.readFieldValue(packet, fieldName, clazz).orElseThrow(
                () -> new IllegalArgumentException("Cannot find field `%s` (%s) in `%s`!".formatted(fieldName, clazz.getSimpleName(), packet.getClass().getSimpleName()))
        );
    }
    
    /**
     * A helper method for writing a field value.
     *
     * @param fieldName - The field name.
     * @param value     - The field type.
     * @param <T>       - The field type.
     */
    public <T> void writeField(@NotNull String fieldName, @NotNull T value) {
        Reflect.writeFieldValue(packet, fieldName, value);
    }
    
}
