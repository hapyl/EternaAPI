package me.hapyl.spigotutils.module.reflect;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nonnull;
import java.util.List;

public class DataWatcherType<T> {

    public static final DataWatcherType<Byte> BYTE;
    public static final DataWatcherType<Integer> INT;
    public static final DataWatcherType<Float> FLOAT;
    public static final DataWatcherType<String> STR;
    public static final DataWatcherType<Boolean> BOOL;
    public static final DataWatcherType<Component> CHAT;
    public static final DataWatcherType<ItemStack> ITEM;
    public static final DataWatcherType<BlockState> BLOCK_DATA;
    public static final DataWatcherType<ParticleOptions> PARTICLE;
    public static final DataWatcherType<Vector3f> VECTOR3F;
    public static final DataWatcherType<BlockPos> BLOCK_POS;
    public static final DataWatcherType<Direction> DIRECTION;
    public static final DataWatcherType<VillagerData> VILLAGER;
    public static final DataWatcherType<Pose> ENTITY_POSE;
    public static final DataWatcherType<Quaternionf> QUATERNION;

    private static final List<DataWatcherType<?>> watcherTypes;

    static {
        watcherTypes = Lists.newArrayList();

        BYTE = new DataWatcherType<>(Byte.class, EntityDataSerializers.BYTE);
        INT = new DataWatcherType<>(Integer.class, EntityDataSerializers.INT);
        FLOAT = new DataWatcherType<>(Float.class, EntityDataSerializers.FLOAT);
        STR = new DataWatcherType<>(String.class, EntityDataSerializers.STRING);
        BOOL = new DataWatcherType<>(Boolean.class, EntityDataSerializers.BOOLEAN);
        CHAT = new DataWatcherType<>(Component.class, EntityDataSerializers.COMPONENT);
        ITEM = new DataWatcherType<>(ItemStack.class, EntityDataSerializers.ITEM_STACK);
        BLOCK_DATA = new DataWatcherType<>(BlockState.class, EntityDataSerializers.BLOCK_STATE);
        PARTICLE = new DataWatcherType<>(ParticleOptions.class, EntityDataSerializers.PARTICLE);
        VECTOR3F = new DataWatcherType<>(Vector3f.class, EntityDataSerializers.VECTOR3);
        BLOCK_POS = new DataWatcherType<>(BlockPos.class, EntityDataSerializers.BLOCK_POS);
        DIRECTION = new DataWatcherType<>(Direction.class, EntityDataSerializers.DIRECTION);
        VILLAGER = new DataWatcherType<>(VillagerData.class, EntityDataSerializers.VILLAGER_DATA);
        ENTITY_POSE = new DataWatcherType<>(Pose.class, EntityDataSerializers.POSE);
        QUATERNION = new DataWatcherType<>(Quaternionf.class, EntityDataSerializers.QUATERNION);
    }

    public final Class<T> clazz;
    public final EntityDataSerializer<T> serializer;

    DataWatcherType(Class<T> clazz, EntityDataSerializer<T> serializer) {
        this.clazz = clazz;
        this.serializer = serializer;

        watcherTypes.add(this);
    }

    @Nonnull
    public Class<?> getClazz() {
        return this.clazz;
    }

    @Nonnull
    public EntityDataSerializer<T> get() {
        return serializer;
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    @Deprecated
    public EntityDataSerializer<Object> getRaw() {
        return (EntityDataSerializer<Object>) serializer;
    }

    @Nonnull
    public static DataWatcherType<?> of(@Nonnull EntityDataSerializer<?> serializer) {
        for (DataWatcherType<?> type : watcherTypes) {
            if (type.serializer.equals(serializer)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unsupported serializer: " + serializer);
    }

    @Override
    public String toString() {
        return clazz.getSimpleName();
    }
}
