package me.hapyl.eterna.module.reflect.packet.wrapped;

import com.google.common.collect.Lists;
import me.hapyl.eterna.module.annotate.RawUsage;
import me.hapyl.eterna.module.reflect.Reflect;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a wrapped {@link ClientboundSetEntityDataPacket}.
 */
public class WrappedSetEntityDataPacket extends WrappedPacket<ClientboundSetEntityDataPacket> {
    public WrappedSetEntityDataPacket(ClientboundSetEntityDataPacket packet) {
        super(packet);
    }
    
    /**
     * Gets the Id of the entity this metadata packet belongs to.
     *
     * @return the Id of the entity this metadata packet belongs to.
     */
    public int getEntityId() {
        return packet.id();
    }
    
    /**
     * Gets the metadata raw items of this metadata packet.
     * <br>
     * It is recommended to use {@link #getWrappedDataWatcherValueList()} to read/write values.
     * <br>
     * <b>These values are backed up by the actual packet.</b>
     *
     * @return the raw items of this packet.
     */
    @Nonnull
    @RawUsage(useInstead = "getWrappedDataWatcherValueList()")
    public List<SynchedEntityData.DataValue<?>> getPackedItems() {
        return packet.packedItems();
    }
    
    /**
     * Gets a copy {@link WrappedDataWatcherValueList} of the values.
     *
     * @return a copy of the values as a readable/writable list.
     */
    @Nonnull
    public WrappedDataWatcherValueList getWrappedDataWatcherValueList() {
        return new WrappedDataWatcherValueList(getPackedItems());
    }
    
    /**
     * Represents a {@link List} of {@link WrappedEntityDataValue}.
     * <br>
     * Changing the values in this {@link List} will not affect values in the origin packet nor wrapped packet, but they can be converted for further use in a {@link ClientboundSetEntityDataPacket}
     * packet using {@link #getAsDataWatcherObjectList()}.
     */
    public static class WrappedDataWatcherValueList extends ArrayList<WrappedEntityDataValue> {
        public WrappedDataWatcherValueList(List<SynchedEntityData.DataValue<?>> list) {
            for (SynchedEntityData.DataValue<?> dataValue : list) {
                add(new WrappedEntityDataValue(dataValue));
            }
        }
        
        /**
         * Gets a {@link List} of raw {@link SynchedEntityData.DataValue} with the updated values.
         * <br>
         * This method will use the current value of the entire list, but will not update the origin values in the {@link Packet}.
         *
         * @return a list of raw data watcher values with the updated values.
         */
        @Nonnull
        public List<SynchedEntityData.DataValue<?>> getAsDataWatcherObjectList() {
            List<SynchedEntityData.DataValue<?>> list = Lists.newArrayList();
            
            for (WrappedEntityDataValue value : this) {
                list.add(value.getAsDataValue());
            }
            
            return list;
        }
    }
    
    /**
     * Represents a {@link WrappedEntityDataValue}.
     * <br>
     * The values are <b><i>not</i></b> backed by the {@link SynchedEntityData} nor the origin {@link Packet}.
     */
    public static class WrappedEntityDataValue {
        // There is really no point of using generics here,
        // since we deal with raw objects, you'll just have
        // to check the value yourself.
        
        private final int id;
        private final EntityDataSerializer<?> serializer;
        private Object value;
        
        public WrappedEntityDataValue(@Nonnull SynchedEntityData.DataValue<?> dataValue) {
            this.id = dataValue.id();
            this.serializer = dataValue.serializer();
            this.value = dataValue.value();
        }
        
        /**
         * Gets the Id (index) of this value.
         *
         * @return the Id (index) of this value.
         */
        public int getId() {
            return id;
        }
        
        /**
         * Gets the serializer (type) of this value.
         *
         * @return the serializer (type) of this value.
         */
        @Nonnull
        public EntityDataSerializer<?> getSerializer() {
            return serializer;
        }
        
        /**
         * Gets the value.
         *
         * @return the value.
         */
        @Nonnull
        public Object getValue() {
            return value;
        }
        
        /**
         * Sets the value.
         * <br>
         * This method will <b>not</b> throw exceptions if types mismatch, but {@link #getAsDataValue()} will!
         *
         * @param value - Value to set.
         */
        public void setValue(@Nullable Object value) {
            this.value = value;
        }
        
        /**
         * Gets the value as the given type, or <code>null</code> if types don't match.
         *
         * @param clazz - Value class.
         * @return the value or <code>null</code>.
         */
        @Nullable
        public <T> T getValueAs(@Nonnull Class<T> clazz) {
            return Reflect.castIfInstance(value, clazz);
        }
        
        /**
         * Sets the value if the types match, does nothing otherwise.
         *
         * @param value - Value to set.
         */
        public <T> void setValueAs(@Nullable T value) {
            if (this.value.getClass().isInstance(value)) {
                this.value = value;
            }
        }
        
        /**
         * Gets a {@link SynchedEntityData.DataValue} with the values of this {@link WrappedEntityDataValue}.
         *
         * @return a raw data watcher object.
         * @throws ClassCastException if the type and serialized do not match.
         */
        @Nonnull
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
