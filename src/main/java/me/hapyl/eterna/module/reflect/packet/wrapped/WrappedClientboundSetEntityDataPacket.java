package me.hapyl.eterna.module.reflect.packet.wrapped;

import me.hapyl.eterna.module.reflect.Reflect;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a wrapped {@link ServerboundPlayerInputPacket}.
 *
 * <p>
 * This packet is sent whenever a {@link Player} receives a metadata update of an {@link Entity}.
 * </p>
 */
public class WrappedClientboundSetEntityDataPacket extends WrappedPacket<ClientboundSetEntityDataPacket> {
    
    WrappedClientboundSetEntityDataPacket(@NotNull ClientboundSetEntityDataPacket packet) {
        super(packet);
    }
    
    /**
     * Gets the id of the {@link Entity} this metadata packet belongs to.
     *
     * @return the id of the {@link Entity} this metadata packet belongs to.
     */
    public int getEntityId() {
        return packet.id();
    }
    
    /**
     * Gets a {@link WrappedEntityDataList} containing the entity data values.
     *
     * @return an entity data values list.
     */
    @NotNull
    public WrappedEntityDataList getWrappedEntityDataList() {
        return new WrappedEntityDataList(packet.packedItems());
    }
    
    /**
     * Represents a {@link List} of {@link WrappedEntityDataValue}.
     *
     * <p>
     * The values are <b>not</b> backed by the raw {@link Packet} values.
     * </p>
     */
    public static class WrappedEntityDataList extends ArrayList<WrappedEntityDataValue> {
        
        WrappedEntityDataList(@NotNull List<SynchedEntityData.DataValue<?>> original) {
            for (SynchedEntityData.DataValue<?> dataValue : original) {
                add(new WrappedEntityDataValue(dataValue));
            }
        }
        
        /**
         * Gets an <b>immutable</b> {@link List} containing the vanilla {@code data values}.
         *
         * @return an <b>immutable</b> list containing the vanilla {@code data values}.
         */
        @NotNull
        public List<SynchedEntityData.DataValue<?>> asVanilla() {
            return stream()
                    .<SynchedEntityData.DataValue<?>>map(WrappedEntityDataValue::getAsDataValue)
                    .toList();
        }
    }
    
    /**
     * Represents a {@link WrappedEntityDataValue}.
     *
     * <p>
     * The value is <b>not</b> backed by the original {@link Packet}.
     * </p>
     */
    public static class WrappedEntityDataValue {
        
        // There is really no point of using generics here,
        // since we deal with raw objects, you'll just have
        // to check the value yourself.
        
        private final int id;
        private final EntityDataSerializer<?> serializer;
        private Object value;
        
        WrappedEntityDataValue(@NotNull SynchedEntityData.DataValue<?> dataValue) {
            this.id = dataValue.id();
            this.serializer = dataValue.serializer();
            this.value = dataValue.value();
        }
        
        /**
         * Gets the id (index) of this value.
         *
         * @return the id (index) of this value.
         */
        public int getId() {
            return id;
        }
        
        /**
         * Gets the {@link EntityDataSerializer} (type) of this value.
         *
         * @return the serializer (type) of this value.
         */
        @NotNull
        public EntityDataSerializer<?> getSerializer() {
            return serializer;
        }
        
        /**
         * Gets the raw {@link Object} value.
         *
         * @return the raw object value.
         */
        @NotNull
        public Object getValue() {
            return value;
        }
        
        /**
         * Sets the raw {@link Object} value.
         *
         * @param value - The value to set.
         */
        public void setValue(@Nullable Object value) {
            this.value = value;
        }
        
        /**
         * Gets the value as the given type, or {@code null} if types don't match.
         *
         * @param clazz - The expected class type.
         * @return the value or {@code null} if types don't match.
         */
        @Nullable
        public <T> T getValueAs(@NotNull Class<T> clazz) {
            return Reflect.castIfInstance(value, clazz);
        }
        
        /**
         * Gets a {@link SynchedEntityData.DataValue} with the values of this {@link WrappedEntityDataValue}.
         *
         * @return a raw data watcher object.
         * @throws ClassCastException if the type and serialized do not match.
         */
        @NotNull
        @SuppressWarnings("unchecked")
        public SynchedEntityData.DataValue<?> getAsDataValue() {
            return new SynchedEntityData.DataValue<>(id, (EntityDataSerializer<Object>) serializer, value);
        }
        
        @Override
        public String toString() {
            return getClass().getSimpleName() + "{id: %s, serializer:%s, value: %s}".formatted(id, serializer, value);
        }
    }
    
}
