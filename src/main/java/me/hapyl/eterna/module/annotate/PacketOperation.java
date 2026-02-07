package me.hapyl.eterna.module.annotate;

import me.hapyl.eterna.module.reflect.packet.PacketFactory;
import net.minecraft.network.protocol.Packet;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated method handles logic via {@link Packet}, usually meaning
 * the changes will only be visible to the provided player via parameters.
 *
 * @see PacketFactory
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface PacketOperation {
}
