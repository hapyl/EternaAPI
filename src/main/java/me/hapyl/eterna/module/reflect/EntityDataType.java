package me.hapyl.eterna.module.reflect;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.npc.villager.VillagerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Quaternionfc;
import org.joml.Vector3fc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Represents a {@link SynchedEntityData} (previously {@code DataWatcher}) wrapper.
 *
 * @param <T> - The data watcher type.
 */
@ApiStatus.Obsolete
public class EntityDataType<T> {
    
    public static final EntityDataType<Byte> BYTE;
    public static final EntityDataType<Integer> INT;
    public static final EntityDataType<Float> FLOAT;
    public static final EntityDataType<String> STR;
    public static final EntityDataType<Boolean> BOOL;
    public static final EntityDataType<Component> CHAT;
    public static final EntityDataType<ItemStack> ITEM;
    public static final EntityDataType<BlockState> BLOCK_DATA;
    public static final EntityDataType<ParticleOptions> PARTICLE;
    public static final EntityDataType<Vector3fc> VECTOR3F;
    public static final EntityDataType<BlockPos> BLOCK_POS;
    public static final EntityDataType<Direction> DIRECTION;
    public static final EntityDataType<VillagerData> VILLAGER;
    public static final EntityDataType<Pose> ENTITY_POSE;
    public static final EntityDataType<Quaternionfc> QUATERNION;
    
    private static final List<EntityDataType<?>> watcherTypes;
    
    static {
        watcherTypes = Lists.newArrayList();
        
        BYTE = new EntityDataType<>(Byte.class, EntityDataSerializers.BYTE);
        INT = new EntityDataType<>(Integer.class, EntityDataSerializers.INT);
        FLOAT = new EntityDataType<>(Float.class, EntityDataSerializers.FLOAT);
        STR = new EntityDataType<>(String.class, EntityDataSerializers.STRING);
        BOOL = new EntityDataType<>(Boolean.class, EntityDataSerializers.BOOLEAN);
        CHAT = new EntityDataType<>(Component.class, EntityDataSerializers.COMPONENT);
        ITEM = new EntityDataType<>(ItemStack.class, EntityDataSerializers.ITEM_STACK);
        BLOCK_DATA = new EntityDataType<>(BlockState.class, EntityDataSerializers.BLOCK_STATE);
        PARTICLE = new EntityDataType<>(ParticleOptions.class, EntityDataSerializers.PARTICLE);
        VECTOR3F = new EntityDataType<>(Vector3fc.class, EntityDataSerializers.VECTOR3);
        BLOCK_POS = new EntityDataType<>(BlockPos.class, EntityDataSerializers.BLOCK_POS);
        DIRECTION = new EntityDataType<>(Direction.class, EntityDataSerializers.DIRECTION);
        VILLAGER = new EntityDataType<>(VillagerData.class, EntityDataSerializers.VILLAGER_DATA);
        ENTITY_POSE = new EntityDataType<>(Pose.class, EntityDataSerializers.POSE);
        QUATERNION = new EntityDataType<>(Quaternionfc.class, EntityDataSerializers.QUATERNION);
    }
    
    public final Class<T> clazz;
    public final EntityDataSerializer<T> serializer;
    
    EntityDataType(Class<T> clazz, EntityDataSerializer<T> serializer) {
        this.clazz = clazz;
        this.serializer = serializer;
        
        watcherTypes.add(this);
    }
    
    @Nonnull
    public Class<?> getClazz() {
        return this.clazz;
    }
    
    /**
     * Creates an {@link EntityDataAccessor} with the specified id.
     *
     * @param id - The id of the accessor.
     * @return an {@link EntityDataAccessor}.
     */
    @Nonnull
    public EntityDataAccessor<T> createAccessor(int id) {
        return serializer.createAccessor(id);
    }
    
    @Override
    public String toString() {
        return clazz.getSimpleName();
    }
    
}
