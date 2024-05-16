package me.hapyl.spigotutils.module.reflect;

import com.google.common.collect.Lists;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.core.Vector3f;
import net.minecraft.core.particles.ParticleParam;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.network.syncher.DataWatcherSerializer;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.IBlockData;
import org.checkerframework.checker.index.qual.SearchIndexBottom;
import org.joml.Quaternionf;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class DataWatcherType<T> {

    public static final DataWatcherType<Byte> BYTE;
    public static final DataWatcherType<Integer> INT;
    public static final DataWatcherType<Float> FLOAT;
    public static final DataWatcherType<String> STR;
    public static final DataWatcherType<Boolean> BOOL;
    public static final DataWatcherType<IChatBaseComponent> CHAT;
    public static final DataWatcherType<ItemStack> ITEM;
    public static final DataWatcherType<IBlockData> BLOCK_DATA;
    public static final DataWatcherType<ParticleParam> PARTICLE;
    public static final DataWatcherType<Vector3f> VECTOR3F;
    public static final DataWatcherType<BlockPosition> BLOCK_POS;
    public static final DataWatcherType<EnumDirection> DIRECTION;
    public static final DataWatcherType<VillagerData> VILLAGER;
    public static final DataWatcherType<EntityPose> ENTITY_POSE;
    public static final DataWatcherType<Quaternionf> QUATERNION;

    private static final List<DataWatcherType<?>> watcherTypes;

    static {
        watcherTypes = Lists.newArrayList();

        BYTE = new DataWatcherType<>(Byte.class, DataWatcherRegistry.a);
        INT = new DataWatcherType<>(Integer.class, DataWatcherRegistry.b);
        FLOAT = new DataWatcherType<>(Float.class, DataWatcherRegistry.d);
        STR = new DataWatcherType<>(String.class, DataWatcherRegistry.e);
        BOOL = new DataWatcherType<>(Boolean.class, DataWatcherRegistry.k);
        CHAT = new DataWatcherType<>(IChatBaseComponent.class, DataWatcherRegistry.f);
        ITEM = new DataWatcherType<>(ItemStack.class, DataWatcherRegistry.h);
        BLOCK_DATA = new DataWatcherType<>(IBlockData.class, DataWatcherRegistry.i);
        PARTICLE = new DataWatcherType<>(ParticleParam.class, DataWatcherRegistry.l);
        VECTOR3F = new DataWatcherType<>(Vector3f.class, DataWatcherRegistry.n);
        BLOCK_POS = new DataWatcherType<>(BlockPosition.class, DataWatcherRegistry.o);
        DIRECTION = new DataWatcherType<>(EnumDirection.class, DataWatcherRegistry.q);
        VILLAGER = new DataWatcherType<>(VillagerData.class, DataWatcherRegistry.u);
        ENTITY_POSE = new DataWatcherType<>(EntityPose.class, DataWatcherRegistry.w);
        QUATERNION = new DataWatcherType<>(Quaternionf.class, DataWatcherRegistry.E);
    }

    public final Class<T> clazz;
    public final DataWatcherSerializer<T> serializer;

    DataWatcherType(Class<T> clazz, DataWatcherSerializer<T> serializer) {
        this.clazz = clazz;
        this.serializer = serializer;

        watcherTypes.add(this);
    }

    @Nonnull
    public Class<?> getClazz() {
        return this.clazz;
    }

    @Nonnull
    public DataWatcherSerializer<T> get() {
        return serializer;
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public DataWatcherSerializer<Object> getRaw() {
        return (DataWatcherSerializer<Object>) serializer;
    }

    @Nonnull
    public static DataWatcherType<?> of(@Nonnull DataWatcherSerializer<?> serializer) {
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
